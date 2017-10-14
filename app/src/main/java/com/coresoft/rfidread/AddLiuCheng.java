package com.coresoft.rfidread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.coresoft.base.Common;
import com.coresoft.base.IntentDef;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDReportFile;
import com.coresoft.sql.MySQLBase;
import com.coresoft.utils.FileUtils;
import com.coresoft.utils.HttpUpload;
import com.coresoft.utils.HttpUtil;
import com.coresoft.utils.nlog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import static com.coresoft.base.IntentDef.HttpState.UPLOAD_ERROR;
import static com.coresoft.base.IntentDef.HttpState.UPLOAD_START;
import static com.coresoft.base.IntentDef.HttpState.UPLOAD_SUCCESS;
import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.INTENT_COMM_CMD_SQL_RFID_INSERT_FILE;
import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.INTENT_COMM_CMD_SQL_RFID_INSERT_LIUCHENG;
import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.INTENT_COMM_CMD_SQL_RFID_LIUCHENG;
import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.INTENT_COMM_CMD_SQL_RFID_UPLOAD_FILE;
import static com.coresoft.base.IntentDef.INTENT_UPLOAD_ADDR;
import static com.coresoft.base.IntentDef.getInnerSDCardPath;
import static org.apache.http.impl.cookie.DateUtils.GMT;

/**
 * Created by Tangxl on 2017/6/11.
 */

public class AddLiuCheng extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private final static int HANDLE_UPLOAD_SUCCESS = 0xF1F1;
    private final static int HANDLE_UPLOAD_ERROR = 0xF1F2;
    private final static int HANDLE_UPLOAD_START = 0xF1F3;
    private final static int FILE_SELECT_CODE = 0xF1;
    private final static int SNAP_SELECT_CODE = 0xF2;

    private ListView mListView;
    private RFIDLiuCheng mRFIDLiuCheng ;
    private ArrayAdapter<String> mAdapter;
    private EditText mEditText;
    private Button mButton;
    private Button mButtonSnap;
    private Button mButtonUpload;
    private RFIDReportFile mRFIDReportFile;
    private ImageView mImageView;
    private Boolean mSnapImage = false;
    private ProgressDialog mProgressDialog = null;
    private Handler mHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liucheng_add);
        mListView = (ListView)findViewById(R.id.liucheng_add);
        mListView.setOnItemClickListener(this);
        mButton  = (Button)findViewById(R.id.liucheng_ok);
        mButton.setOnClickListener(this);
        mButtonSnap= (Button)findViewById(R.id.liucheng_snap);
        mButtonSnap.setOnClickListener(this);
        mButtonUpload = (Button)findViewById(R.id.liucheng_upload);
        mButtonUpload.setOnClickListener(this);
        mImageView = (ImageView)findViewById(R.id.liucheng_snapview);
        loaddefault();
        updateList();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){

                    case HANDLE_UPLOAD_START:
                        ShowWaitDialog();
                        break;

                    case HANDLE_UPLOAD_SUCCESS:
                        HideWaitDialog();
                        Toast.makeText(getApplicationContext(), R.string.toast_hit_upload_success, Toast.LENGTH_SHORT).show();
                        break;

                    case HANDLE_UPLOAD_ERROR:
                        HideWaitDialog();
                        Toast.makeText(getApplicationContext(), R.string.toast_hit_upload_error, Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void loaddefault()
    {
        mRFIDLiuCheng = new RFIDLiuCheng();
        mRFIDLiuCheng.cLCname = getIntent().getStringExtra(IntentDef.ACTIVITY_MESSAGE_LIUCHENGNAME);
        mRFIDLiuCheng.cRFID = getIntent().getStringExtra(IntentDef.ACTIVITY_MESSAGE_RFID);
        mRFIDLiuCheng.cShengchanID = getIntent().getStringExtra(IntentDef.ACTIVITY_MESSAGE_SHENGCHANGID);
        mRFIDLiuCheng.cFenkuaihao = getIntent().getStringExtra(IntentDef.ACTIVITY_MESSAGE_FENGKUAIHAO);
        mRFIDLiuCheng.cLCpreson = "管理员";
        mRFIDLiuCheng.cLCtext = "无";
        mRFIDLiuCheng.cLCreslut = getResources().getString(R.string.liucheng_hege);
        mRFIDLiuCheng.cLCchuli = getResources().getString(R.string.liucheng_null);
        mRFIDLiuCheng.cliucheng_UUID = java.util.UUID.randomUUID().toString();
        mRFIDLiuCheng.cLCtime =  new Timestamp(System.currentTimeMillis());//获取当前时间

        mRFIDReportFile = new RFIDReportFile();
        mRFIDReportFile.c_Report_UUID = "";
        mRFIDReportFile.cliucheng_UUID = mRFIDLiuCheng.cliucheng_UUID;
        mRFIDReportFile.cfilehouzui = ".jpg";
        mRFIDReportFile.c_UUID =java.util.UUID.randomUUID().toString();
        mRFIDReportFile.SP_pid = Integer.valueOf(mRFIDLiuCheng.cShengchanID);
    }

    private void updateList()
    {
        ArrayList<String> mString = new ArrayList<String>();
        mString.add(0,getResources().getString(R.string.liucheng_name) +": "+mRFIDLiuCheng.cLCname);
        mString.add(1,getResources().getString(R.string.nfc_read_chipid) +": "+mRFIDLiuCheng.cRFID);
        mString.add(2,getResources().getString(R.string.nfc_read_huanhao) +": "+mRFIDLiuCheng.cShengchanID);
        mString.add(3,getResources().getString(R.string.nfc_read_fenkuaihao) +": "+mRFIDLiuCheng.cFenkuaihao);
        mString.add(4,getResources().getString(R.string.liucheng_caozuoren) +": "+mRFIDLiuCheng.cLCpreson);
        mString.add(5,getResources().getString(R.string.liucheng_yanshoujielun) +": "+mRFIDLiuCheng.cLCreslut);
        mString.add(6,getResources().getString(R.string.liucheng_chulifangshi) +": "+mRFIDLiuCheng.cLCchuli);
        if (mRFIDLiuCheng.cLCtext.length() > 20) {
            String cLCtext = String.copyValueOf(mRFIDLiuCheng.cLCtext.toCharArray(),0,20)+ "...";
            mString.add(7, getResources().getString(R.string.liucheng_wenzishuoming) + ": " + cLCtext);
        }else{
            mString.add(7, getResources().getString(R.string.liucheng_wenzishuoming) + ": " + mRFIDLiuCheng.cLCtext);
        }
        mString.add(7,getResources().getString(R.string.liucheng_querenshijian) +": "+mRFIDLiuCheng.cLCtime.toString());
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int ID[] =
        {
                R.string.liucheng_name,
                R.string.nfc_read_chipid,
                R.string.nfc_read_huanhao,
                R.string.nfc_read_fenkuaihao,
                R.string.liucheng_caozuoren,
                R.string.liucheng_yanshoujielun,
                R.string.liucheng_chulifangshi,
                R.string.liucheng_querenshijian,
                R.string.liucheng_wenzishuoming,
        };

        switch(ID[position])
        {
            case R.string.liucheng_yanshoujielun:
                choose_yanshou();
                break;

            case R.string.liucheng_chulifangshi:
                choose_chulifangshi();
                break;

            case R.string.liucheng_wenzishuoming:
                choose_wenzishuru();
                break;
        }
    }

    public void choose_yanshou()
    {
        String[] msg = new String[2];
        msg[0] = getResources().getString(R.string.liucheng_hege);
        msg[1] = getResources().getString(R.string.liucheng_buhege);
        int index = 0;
        if(msg[1].equals(mRFIDLiuCheng.cLCreslut)){
            index = 1;
        }
        new  AlertDialog.Builder(this)
        .setTitle(getResources().getString(R.string.liucheng_yanshoujielun))
        .setIcon(android.R.drawable.ic_dialog_info)
        .setSingleChoiceItems(msg,  index,
            new  DialogInterface.OnClickListener() {
                public void  onClick(DialogInterface dialog,  int  which) {
                    switch(which)
                    {
                        case 0:
                            mRFIDLiuCheng.cLCreslut = getResources().getString(R.string.liucheng_hege);
                            break;

                        case 1:
                            mRFIDLiuCheng.cLCreslut = getResources().getString(R.string.liucheng_buhege);
                            break;
                    }
                    updateList();
                    dialog.dismiss();
                }
            }
        ).show();
    }

    public void choose_chulifangshi()
    {
        String[] msg = new String[2];
        msg[0] = getResources().getString(R.string.liucheng_null);
        msg[1] = getResources().getString(R.string.liucheng_tuihui);
        int index = 0;
        if(msg[1].equals(mRFIDLiuCheng.cLCchuli)){
            index = 1;
        }
        new  AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.liucheng_yanshoujielun))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(msg,  index,
                        new  DialogInterface.OnClickListener() {
                            public void  onClick(DialogInterface dialog,  int  which) {
                                switch(which)
                                {
                                    case 0:
                                        mRFIDLiuCheng.cLCchuli = getResources().getString(R.string.liucheng_null);
                                        break;

                                    case 1:
                                        mRFIDLiuCheng.cLCchuli = getResources().getString(R.string.liucheng_tuihui);
                                        break;
                                }
                                updateList();
                                dialog.dismiss();
                            }
                        }
                ).show();
    }

    public void choose_wenzishuru()
    {
        mEditText = new EditText(this);
        mEditText.setHeight(500);
        mEditText.setGravity(Gravity.TOP);
        mEditText.setSingleLine(false);
        mEditText.setHorizontallyScrolling(false);
        mEditText.setText(mRFIDLiuCheng.cLCtext);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        new  AlertDialog.Builder(this)
        .setTitle("请输入" )
        .setIcon(android.R.drawable.ic_dialog_info)
        .setView(mEditText)
        .setPositiveButton("确定" , new  DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRFIDLiuCheng.cLCtext = mEditText.getText().toString();
                updateList();
            }
        })
        .setNegativeButton("取消" ,  null )
        .show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.liucheng_ok:
                SendResult();
                break;

            case R.id.liucheng_snap:
                startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), SNAP_SELECT_CODE);
                break;

            case R.id.liucheng_upload:
                showFileChooser();
                //Toast.makeText(this,R.string.toast_hit_liucheng_snap,Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SNAP_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                mImageView.setImageBitmap(bm);
                mSnapImage = true;
                Long longTime = System.currentTimeMillis();
                mRFIDReportFile.ReportDate = new Timestamp(longTime);
                nlog.Info("Save Bmp =============== ["+mRFIDReportFile.cliucheng_UUID+"]");
                File myCaptureFile = new File(getInnerSDCardPath()+"/"+IntentDef.DEFAULT_DIR+"/"+mRFIDReportFile.cliucheng_UUID+".jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                    Toast.makeText(this,R.string.toast_hit_save_success,Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else if (requestCode == FILE_SELECT_CODE){
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String path = FileUtils.getPath(this, uri);
                Long longTime = System.currentTimeMillis();
                mRFIDReportFile.ReportDate = new Timestamp(longTime);
                nlog.Info("File Select ==================["+path+"]"+"cliucheng_UUID ["+mRFIDReportFile.cliucheng_UUID+"]");
                Upload(path);
            }
        }
    }

    public void SendResult()
    {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDLiuCheng);
        Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
        intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_INSERT_LIUCHENG);
        intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
        intent.putExtras(mBundle);
        sendBroadcast(intent);

        if(mSnapImage){
            Bundle mBundleImage = new Bundle();
            mBundleImage.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDReportFile);
            Intent intentImage = new Intent(IntentDef.MODULE_RESPONSION);
            intentImage.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_INSERT_FILE);
            intentImage.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
            intentImage.putExtras(mBundleImage);
            sendBroadcast(intentImage);
        }
        finish();
    }

    public void ShowWaitDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMessage(getText(R.string.toast_hit_upload_wait));
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void HideWaitDialog()
    {
        mProgressDialog.hide();
    }

    public void Upload(String path){
        nlog.Info("UploadFile=========================Path ["+path+"]");
        HttpUtil.UploadFile(INTENT_UPLOAD_ADDR, path, new IntentDef.OnHttpReportListener(){
            @Override
            public void OnHttpDataReport(int Oper, long param1, long param2) {

                switch(Oper){
                    case UPLOAD_ERROR:
                        nlog.Info("UPLOAD_ERROR");
                        mHandler.sendEmptyMessage(HANDLE_UPLOAD_ERROR);
                        break;

                    case UPLOAD_START:
                        nlog.Info("UPLOAD_START");
                        mHandler.sendEmptyMessage(HANDLE_UPLOAD_START);
                        break;

                    case UPLOAD_SUCCESS:
                        nlog.Info("UPLOAD_SUCCESS");
                        Bundle mBundleImage = new Bundle();
                        mBundleImage.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDReportFile);
                        Intent intentImage = new Intent(IntentDef.MODULE_RESPONSION);
                        intentImage.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_UPLOAD_FILE);
                        intentImage.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                        intentImage.putExtras(mBundleImage);
                        sendBroadcast(intentImage);
                        mHandler.sendEmptyMessage(HANDLE_UPLOAD_SUCCESS);
                        break;
                }
            }
        });
    }
}
