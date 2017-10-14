package com.coresoft.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.coresoft.base.IntentDef;
import com.coresoft.rfidread.R;

import java.io.File;

/**
 * Created by Tangxl on 2017/5/31.
 */

public class Upgrade {

    private final int MSG_HIT = 0xA1A1;
    private final int MSG_PROC = 0xA1A2;

    private Context mContext = null;
    private Boolean mUpdataState = false;
    private ProgressDialog mProgress = null;
    private Handler mMsgHandler = null;

    public Upgrade(Context context){
        mContext = context;
        mMsgHandler  = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message arg0) {
                // TODO Auto-generated method stub
                switch(arg0.what){
                    case MSG_HIT:
                        Toast.makeText(mContext.getApplicationContext(), arg0.arg1, Toast.LENGTH_SHORT).show();
                        break;

                    case MSG_PROC:
                        Upgrade_Func(arg0.arg1);
                        break;
                }

                return false;
            }
        });
    }

    public void Start(){
        if (false == mUpdataState)
        {
            mUpdataState = true;
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info;
            try {
                info = pm.getPackageInfo(mContext.getPackageName(), 0);
                nlog.Info("Check Version ["+info.versionName+"]");
                if(info.versionName.equals("1.00.001")){
                    new DownloadThread().start();
                }
                else
                {
                    Msg_Hit_Func(R.string.toast_hit_new_version);
                    mUpdataState = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void Upgrade_Func(int proc)
    {
        if(null == mProgress){
            mProgress = new ProgressDialog(mContext);
            mProgress.setTitle(R.string.downloading);
            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setCancelable(false);
            mProgress.setMax(100);
            mProgress.setProgress(0);
            mProgress.show();
        }else{
            mProgress.setProgress((int)proc);
        }
    }

    private void Msg_Hit_Func(int res)
    {
        Message msg = new Message();
        msg.what = MSG_HIT;
        msg.arg1 = res;
        mMsgHandler.sendMessage(msg);
    }

    private void Msg_Upgrade_Func(int Proc)
    {
        Message msg = new Message();
        msg.what = MSG_PROC;
        msg.arg1 = Proc;
        mMsgHandler.sendMessage(msg);
    }

    public class DownloadThread extends Thread
    {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            final String FileName = "RFIDRead_V1.00.002.apk";
            File file1 = new File(IntentDef.getInnerSDCardPath()+"/"+FileName);
            if (file1 != null && file1.length() > 0 && file1.exists() && file1.isFile()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(IntentDef.getInnerSDCardPath()+"/"+FileName)), "application/vnd.android.package-archive");
                mContext.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            else
            {
                String Download = IntentDef.getInnerSDCardPath()+"/";
                nlog.Info("Download Path ["+Download+"]");
                try {
                    new FTPUtil().downloadSingleFile("/RFIDRead/"+FileName, Download, FileName, new FTPUtil.DownLoadProgressListener(){
                        @Override
                        public void onDownLoadProgress(String currentStep,
                                                       long downProcess, File file) {
                            // TODO Auto-generated method stub
                            nlog.Info("onDownLoadProgress==============================["+currentStep+"]");
                            if(currentStep.equals(IntentDef.FTP_STATE.FTP_DOWN_SUCCESS)){
                                nlog.Info("Download =============== Succecc ");
                                mProgress.cancel();
                                mProgress = null;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                String FilePath = IntentDef.getInnerSDCardPath()+"/"+FileName;
                                File file1 = new File(FilePath);
                                if (file1 != null && file1.length() > 0 && file1.exists() && file1.isFile()) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(new File(FilePath)), "application/vnd.android.package-archive");
                                    mContext.startActivity(intent);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            } else if(currentStep.equals(IntentDef.FTP_STATE.FTP_DOWN_LOADING)){
                                nlog.Info("Download ============ Proc ["+downProcess+"]");
                                Msg_Upgrade_Func((int)downProcess);
                            } else if(currentStep.equals(IntentDef.FTP_STATE.FTP_DISCONNECT_SUCCESS))
                            {
                            }else if(currentStep.equals(IntentDef.FTP_STATE.FTP_CONNECT_SUCCESSS))
                            {

                            }
                            else
                            {
                                Msg_Hit_Func(R.string.toast_hit_new_version);
                            }
                        }

                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    nlog.Info("FTP error ===============================");
                    e.printStackTrace();
                }
            }
            mUpdataState = false;
        }
    }
}
