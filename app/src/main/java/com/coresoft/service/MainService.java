package com.coresoft.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.coresoft.base.IntentDef;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDReportFile;
import com.coresoft.utils.nlog;

import java.sql.Date;
import java.sql.Timestamp;

public class MainService extends Service {

    private static final String tag = "MainService";
    private MainBinder mMainBinder= null;
    public ServiceProcessor mServiceProcessor = null;

    @Override
    public IBinder onBind(Intent intent) {
        return mMainBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        nlog.Info("create MainService....");
        if	(mMainBinder==null)
            mMainBinder = new MainBinder(this.getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        nlog.Info("destory mainService");
        mServiceProcessor.ServiceProcessorStop();
        mServiceProcessor = null;
        mMainBinder = null;
        super.onDestroy();
    }

    public class MainBinder extends Binder{
        public MainBinder(Context context){
            if(mServiceProcessor == null){
                mServiceProcessor = new ServiceProcessor(context);
            }
        }

        public MainService getMainService(){
            return MainService.this;
        }
    }

    public boolean getSqlState(){
        return mServiceProcessor.getSqlState();
    }

    public void SqlConnect(){
        mServiceProcessor.SqlConnect();
    }

    public void GetRFIDSegmentPartFromNet(String card)
    {
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDSegmentPartFromNet(card);
        }
    }

    public void GetRFIDSegmentFormNet(String ID){
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDSegmentFormNet(ID);
        }
    }

    public void GetRFIDReport(String Type, String chuanhao,String cManufacture)
    {
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDReport(Type,chuanhao,cManufacture);
        }
    }

    public void GetRFIDLiucheng(String ChipId,String cManufacture)
    {
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDLiucheng(ChipId,cManufacture);
        }
    }

    public void InsertLiucheng(RFIDLiuCheng liucheng){
        if(mServiceProcessor != null){
            mServiceProcessor.InsertLiucheng(liucheng);
        }
    }

    public void InsertFile(RFIDReportFile Image){
        if(mServiceProcessor != null){
            mServiceProcessor.InsertFile(Image);
        }
    }

    public void GetRFIDYaoWenDu(String type, String number, Timestamp mDateIn , Timestamp mDateOut){
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDYaoWenDu(type,number,mDateIn, mDateOut);
        }
    }

    public void GetRFIDShuiYanChi(String type, String number, Timestamp mDateIn , Timestamp mDateOut){
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDShuiYanChi(type, number, mDateIn, mDateOut);
        }
    }

    public void GetRFIDPeiHeBi(String ID, Timestamp mDateIn){
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDPeiHeBi(ID, mDateIn);
        }
    }

    public void GetRFIDLiuchengFile(String liucheng_id){
        if(mServiceProcessor != null){
            mServiceProcessor.GetRFIDLiuchengFile(liucheng_id);
        }
    }

    public void GetAllRFIDSegmentPartFromNet()
    {
        if(mServiceProcessor != null){
            mServiceProcessor.GetAllRFIDSegmentPartFromNet();
        }
    }

    public void GetAllRFIDSegmentFormNet(){
        if(mServiceProcessor != null){
            mServiceProcessor.GetAllRFIDSegmentFormNet();
        }
    }

    public void UploadFile(RFIDReportFile Image){
        if(mServiceProcessor != null){
            mServiceProcessor.UploadFile(Image);
        }
    }
}
