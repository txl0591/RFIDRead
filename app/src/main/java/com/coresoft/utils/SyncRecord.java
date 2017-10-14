package com.coresoft.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.coresoft.base.IntentDef;
import com.coresoft.client.MainClient;

import java.io.Serializable;

/**
 * Created by Tangxl on 2017/9/26.
 */

public class SyncRecord implements IntentDef.OnCommDataReportListener {

    private Context mContext = null;
    private boolean SyncThreadRun = false;
    private MainClient mMainClient = null;
    private Handler mHandler = null;
    private boolean RecvData = false;

    public SyncRecord(Context context, MainClient Client){
        mContext = context;
        mMainClient = Client;
        mMainClient.setmDataReportListener(this);

    }

    public void StartSyncRecord(){
        SyncThreadRun = true;
        new SyncThread().start();
    }

    @Override
    public void OnResponsionReport(int Cmd, Bundle Data, int DataLen) {
        Message msg = new Message();
        msg.what = Cmd;
        if(DataLen > 0){
            Serializable mSerializable = Data.getSerializable(IntentDef.INTENT_COMM_DATA);
            if(mSerializable != null){
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA,mSerializable);
                msg.setData(mBundle);
            }
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void OnDistributeReport(int Cmd, Bundle Data, int DataLen) {

    }

    class SyncThread extends Thread{
        @Override
        public void run() {
            super.run();
            if(SyncThreadRun){
                mMainClient.GetAllRFIDSegmentPartFromNet();
            }
        }
    }
}
