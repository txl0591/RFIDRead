package com.coresoft.rfidread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.coresoft.base.Common;
import com.coresoft.base.IntentDef;
import com.coresoft.utils.HttpUtil;
import com.coresoft.utils.nlog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.coresoft.base.IntentDef.getInnerSDCardPath;
import static com.coresoft.base.IntentDef.HttpState.*;

/**
 * Created by Tangxl on 2017/6/10.
 */

public class DisplayActivity extends Activity implements OnPageChangeListener, OnLoadCompleteListener,IntentDef.OnHttpReportListener {

    private PDFView pdfView;
    private ImageView imageView;
    private Uri uri;
    private Integer pageNumber = 0;
    private String pdfFileName;
    private ProgressDialog mProgressDialog = null;
    private Handler mHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfView.setVisibility(View.INVISIBLE);
        imageView = (ImageView)findViewById(R.id.imgView);
        imageView.setVisibility(View.INVISIBLE);
        String FileUrl = getIntent().getStringExtra(IntentDef.ACTIVITY_MESSAGE_URL);
        String FileName = getIntent().getStringExtra(IntentDef.ACTIVITY_MESSAGE_FILENAME);
        nlog.Info("FileUrl ["+FileUrl+"] ");
        nlog.Info("FileName ["+FileName+"]");

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                long mMax = msg.getData().getLong("MAX");
                long mNow = msg.getData().getLong("NOW");
                switch(msg.what)
                {
                    case DOWNLOAD_START:
                        ShowDailog(mMax,mNow);
                        break;
                    case DOWNLOAD_ING:
                        ShowDailog(mMax,mNow);
                        break;
                    case  DOWNLOAD_SUCCESS:
                        if(mProgressDialog != null){
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                        display(getInnerSDCardPath()+"/"+IntentDef.DEFAULT_DIR+"/"+pdfFileName);
                        break;
                    case  DOWNLOAD_ERROR:
                        if(mProgressDialog != null){
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                        break;

                    case  UPLOAD_START:
                        break;
                    case  UPLOAD_ING:
                        break;
                    case  UPLOAD_SUCCESS:
                        if(mProgressDialog != null){
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                        break;
                    case  UPLOAD_ERROR:
                        if(mProgressDialog != null){
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                        break;

                    default:
                        break;
                }
            }
        };
        pdfFileName = FileName;
        HttpUtil.DownloadFile(FileUrl,getInnerSDCardPath()+"/"+IntentDef.DEFAULT_DIR+"/", FileName, this);
     }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;
        pdfView.fromAsset(pdfFileName)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName(uri);
        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void displayPDFFromFile(String FileFull) {
        File mFile = new File(FileFull);
        pdfView.fromFile(mFile)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void displayImageFromFile(String FileFull){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(FileFull, options);
        imageView.setImageBitmap(bm);
    }

    private void display(String FileFull){
        String fileType = pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1, pdfFileName.length()).toLowerCase();
        nlog.Info("display ===================["+fileType+"]");
        if(fileType.equals("pdf") || fileType.equals("PDF"))
        {
            imageView.setVisibility(View.INVISIBLE);
            pdfView.setVisibility(View.VISIBLE);
            displayPDFFromFile(FileFull);
        }else if(fileType.equals("bmp") ||  fileType.equals("png") || fileType.equals("jpg") || fileType.equals("jpeg")){
            imageView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.INVISIBLE);
            displayImageFromFile(FileFull);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        nlog.Info("title = " + meta.getTitle());
        nlog.Info("author = " + meta.getAuthor());
        nlog.Info("subject = " + meta.getSubject());
        nlog.Info("keywords = " + meta.getKeywords());
        nlog.Info("creator = " + meta.getCreator());
        nlog.Info("producer = " + meta.getProducer());
        nlog.Info("creationDate = " + meta.getCreationDate());
        nlog.Info("modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void OnHttpDataReport(int Oper, long param1, long param2) {
        Message msg = new Message();
        msg.what = Oper;
        Bundle bundleData = new Bundle();
        bundleData.putLong("MAX",param1);
        bundleData.putLong("NOW",param2);
        msg.setData(bundleData);
        mHandler.sendMessage(msg);
    }

    public void ShowDailog(long Max, long proc)
    {
        if(null == mProgressDialog){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIcon(android.R.drawable.ic_dialog_alert);
            mProgressDialog.setMessage(getText(R.string.toast_hit_load_wait));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        mProgressDialog.setProgress((int)proc);
    }

}
