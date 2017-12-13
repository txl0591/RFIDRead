package com.coresoft.client;

import android.content.Context;

import com.coresoft.base.IntentDef;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDReportFile;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Tangxl on 2017/5/29.
 */

public class MainClient extends BaseClient {

    public Context mContext = null;

    public MainClient(Context context) {
        super(context);
        mContext = context;
        String RecvAction[] = {IntentDef.MODULE_DISTRIBUTE,IntentDef.MODULE_RESPONSION};
        startReceiver(context,RecvAction);
        StartIPC(context,IntentDef.SERVICE_NAME_MAIN);
    }

    public void MainClientStop(){
        stopReceiver(mContext, IntentDef.MODULE_DISTRIBUTE);
        StopIPC(mContext, IntentDef.SERVICE_NAME_MAIN);
    }

    public void SqlConnect(){
        mMainService.SqlConnect();
    }

    public boolean getSqlState(){
        return mMainService.getSqlState();
    }

    public void GetRFIDSegmentPartFromNet(String card){
        mMainService.GetRFIDSegmentPartFromNet(card);
    }

    public void GetRFIDSegmentFormNet(String ID){
        mMainService.GetRFIDSegmentFormNet(ID);
    }

    public void GetRFIDReport(String Type, String chuanhao,String cManufacture) {mMainService.GetRFIDReport(Type,chuanhao,cManufacture);}

    public void GetRFIDLiucheng(String ChipId,String cManufacture)
    {
        mMainService.GetRFIDLiucheng(ChipId,cManufacture);
    }

    public void InsertLiucheng(RFIDLiuCheng liucheng){ mMainService.InsertLiucheng(liucheng);   }

    public void GetRFIDYaoWenDu(String type, String number, Timestamp mDateIn , Timestamp mDateOut){
        mMainService.GetRFIDYaoWenDu(type, number, mDateIn, mDateOut);
    }

    public void GetRFIDShuiYanChi(String type, String number, Timestamp mDateIn , Timestamp mDateOut){
        mMainService.GetRFIDShuiYanChi(type, number, mDateIn, mDateOut);
    }

    public void InsertFile(RFIDReportFile Image){
        mMainService.InsertFile(Image);
    }

    public void GetRFIDPeiHeBi(String ID, Timestamp mDateIn){
        mMainService.GetRFIDPeiHeBi(ID, mDateIn);
    }

    public void GetRFIDLiuchengFile(String liucheng_id){
        mMainService.GetRFIDLiuchengFile(liucheng_id);
    }

    public void GetAllRFIDSegmentPartFromNet(){
        mMainService.GetAllRFIDSegmentPartFromNet();
    }

    public void GetAllRFIDSegmentFormNet(){
        mMainService.GetAllRFIDSegmentFormNet();
    }

    public void UploadFile(RFIDReportFile Image){
        mMainService.UploadFile(Image);
    }
}
