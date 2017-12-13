package com.coresoft.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.coresoft.base.Common;
import com.coresoft.base.IntentDef;
import com.coresoft.base.RFIDCaiJiWenDu;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDPeiHeBi;
import com.coresoft.base.RFIDReport;
import com.coresoft.base.RFIDReportFile;
import com.coresoft.base.RFIDSegment;
import com.coresoft.base.RFIDSegmentPart;
import com.coresoft.rfidread.R;
import com.coresoft.sql.MySQLBase;
import com.coresoft.sql.MySql;
import com.coresoft.utils.HttpUpload;
import com.coresoft.utils.HttpUtil;
import com.coresoft.utils.NetUtil;
import com.coresoft.utils.nlog;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.*;
import static com.coresoft.base.IntentDef.INTENT_UPLOAD_ADDR;
import static com.coresoft.base.IntentDef.getInnerSDCardPath;

/**
 * Created by Tangxl on 2017/5/29.
 */

public class ServiceProcessor implements IntentDef.OnSqlReportListener {
    private Context mContext = null;
    private MySql mMySql = null;
    private LogicReceiver mLogicReceiver = null;
    private int mNetState = NetUtil.NETWORN_NONE;
    private boolean mOfflineUsed = false;

    public ServiceProcessor(Context context){
        mContext = context;
        InitBroadCast();
        mMySql = new MySql(MySQLBase.SQL_TYPE.SQL_JDBC,mContext);
        mMySql.SetOnSqlDataReportListener(this);
        mNetState = NetUtil.getNetworkState(mContext);
        nlog.Info("Logic Init =================mNetState ["+mNetState+"] mOfflineUsed ["+mOfflineUsed+"]");
        if(mOfflineUsed == false){
            if(mNetState == NetUtil.NETWORN_NONE){
                Toast.makeText(mContext, R.string.toast_hit_net_error, Toast.LENGTH_LONG).show();
            }else{
                mMySql.Connect();
            }
        }
    }

    public void ServiceProcessorStop(){
        mMySql.DisConnect();
        mContext.unregisterReceiver(mLogicReceiver);
    }

    public boolean getSqlState(){
        return mMySql.IsOnline();
    }

    public void SqlConnect(){
        mMySql.Connect();
    }

    private void InitBroadCast()
    {
        mLogicReceiver = new LogicReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(mLogicReceiver, mIntentFilter);
    }

    @Override
    public void OnSqlDataReport(int Oper, ResultSet Result) {
        // TODO Auto-generated method stub
        switch(Oper)
        {
            case MySQLBase.SQL_OPER.SQL_RFID_SEGMENTPART:
            {
                nlog.Info("Service =============== SQL_RFID_SEGMENTPART");
                RFIDSegmentPart mRFIDSegmentPart = new RFIDSegmentPart(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDSegmentPart);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_SEGMENTPART);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_SEGMENT:
            {
                nlog.Info("Service =============== SQL_RFID_SEGMENT");
                RFIDSegment mRFIDSegment = new RFIDSegment(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDSegment);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_SEGMENT);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_ONLYCARD:
            {
                nlog.Info("Service =============== SQL_RFID_ONLYCARD");
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_ONLYCARD);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,0);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_REPORTFILE:
            {
                RFIDReportFile mRFIDReportFile = new RFIDReportFile(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDReportFile);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_REPORTFILE);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_FINISH:
            {
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_FINISH);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,0);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_LIUCHENG:
            {
                RFIDLiuCheng mRFIDLiuCheng = new RFIDLiuCheng(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDLiuCheng);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_LIUCHENG);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_FINISH_ERROR:
            {
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_FINISH_ERROR);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,0);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_INSERT:
            {
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_INSERT);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,0);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_INSERT_ERROR:
            {
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_INSERT_ERROR);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN,0);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_LIUCHENG_INSERT:
                break;

            case MySQLBase.SQL_OPER.SQL_RFID_INSERTFILE:
                break;

            case MySQLBase.SQL_OPER.SQL_RFID_SHUIYAMCHIWENDU:
            {
                nlog.Info("Service =============== SQL_RFID_SHUIYAMCHIWENDU");
                RFIDCaiJiWenDu mRFIDCaiJiWenDu = new RFIDCaiJiWenDu(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDCaiJiWenDu);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_SHUIYANCHIWENDU);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_YAOWEN:
            {
                nlog.Info("Service =============== SQL_RFID_YAOWEN");
                RFIDCaiJiWenDu mRFIDCaiJiWenDu = new RFIDCaiJiWenDu(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDCaiJiWenDu);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_YAOWEN);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_PEIHEBI: {
                nlog.Info("Service =============== SQL_RFID_PEIHEBI");
                RFIDPeiHeBi mRFIDPeiHeBi = new RFIDPeiHeBi(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDPeiHeBi);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_PEIHEBI);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_RFID_LIUCHENG_IMAGE:
            {
                RFIDReportFile mRFIDReportFile = new RFIDReportFile(Result);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA, mRFIDReportFile);
                Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
                intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_RFID_LIUCHENG_IMAGE);
                intent.putExtra(IntentDef.INTENT_COMM_DATALEN, 1);
                intent.putExtras(mBundle);
                mContext.sendBroadcast(intent);
                break;
            }

            case MySQLBase.SQL_OPER.SQL_NONE:
                break;

            default:
                break;
        }
    }

    public void EchoError()
    {
        Intent intent = new Intent(IntentDef.MODULE_RESPONSION);
        intent.putExtra(IntentDef.INTENT_COMM_CMD, INTENT_COMM_CMD_SQL_FINISH_ERROR);
        intent.putExtra(IntentDef.INTENT_COMM_DATALEN,0);
        mContext.sendBroadcast(intent);
    }

    public void GetRFIDSegmentPartFromNet(String card)
    {
        if(mOfflineUsed == false){
            String Url = "select * from info_Guangpian WHERE Convert(nvarchar(15),cRFID)=Convert(nvarchar(15),"+card+")";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_SEGMENTPART, Url);
        }
        else{
            EchoError();
        }
    }

    public void GetRFIDSegmentFormNet(String ID)
    {
        if(mOfflineUsed == false)
        {
            String Url = "select * from Info_Shengchan WHERE Convert(nvarchar(30),cShengchanID)=Convert(nvarchar(30),"+ID+")";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_SEGMENT,Url);
        }else{
            EchoError();
        }
    }

    public void GetRFIDReport(String Type, String chuanhao, String cManufacture)
    {
        if(mOfflineUsed == false)
        {
            String DEM = "select b.* from sp_reporter a,sp_Image b where a.crptUUID=b.c_Report_UUID and cast(a.cBGinfo1 as int)<= '100' and cast(a.cBGinfo2 as int)>= '100' AND a.creportstype= '06'  AND a.cManufacture= '...'";
            String Url = "select b.* from sp_reporter a,sp_Image b where a.crptUUID=b.c_Report_UUID and cast(a.cBGinfo1 as int)<= \'"+ chuanhao+"\' and cast(a.cBGinfo2 as int) >= \'"+ chuanhao + "\' AND a.creportstype= \'" + Type+"\' AND a.cManufacture = \'" + cManufacture + "\'";

            nlog.Info("GetRFIDReport ["+Url+"]");

            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_REPORTFILE,Url);
        }else{
            EchoError();
        }
    }

    public void GetRFIDYaoWenDu(String type, String number, Timestamp mDateIn , Timestamp mDateOut)
    {
        if(mOfflineUsed == false)
        {
            String Url = "select * from pr_WenDu where cStyte = \'"+ type+"\' and cXLDnode = \'" + number + "\'  and dCaiJI >= \'"+ mDateIn + "\' and dCaiJI <= \'" + mDateOut+"\'";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_YAOWEN,Url);
        }else{
            EchoError();
        }

    }

    public void GetRFIDShuiYanChi(String type, String number,  Timestamp mDateIn , Timestamp mDateOut){
        if(mOfflineUsed == false)
        {
            String Url = "select * from pr_WenDu where cStyte = \'"+ type+"\' and cXLDnode = \'" + number + "\'  and dCaiJI >= \'"+ mDateIn + "\' and dCaiJI <= \'" + mDateOut+"\'";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_SHUIYAMCHIWENDU,Url);
        }else{
            EchoError();
        }
    }

    public void GetRFIDPeiHeBi(String ID, Timestamp mDateIn){
        if(mOfflineUsed == false)
        {
            String Url = "select * from pr_PeiHeBi where cJBLnode = \'"+ ID+"\'  and dCaiJI > DATEADD(Minute, - 10,\'"+ mDateIn + "\') and dCaiJI < DATEADD(Minute, + 10,\'" + mDateIn+"\')";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_PEIHEBI,Url);
        }else{
            EchoError();
        }
    }

    public void GetRFIDLiucheng(String ChipId, String cManufacture)
    {
        if(mOfflineUsed == false)
        {
            String Url = "select * from lc_lliucheng WHERE Convert(nvarchar(15),cRFID)=Convert(nvarchar(15),"+ChipId+")";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_LIUCHENG,Url);
        }else{
            EchoError();
        }
    }
    public void InsertLiucheng(RFIDLiuCheng liucheng)
    {
        if(mOfflineUsed == false)
        {
            String Url = "INSERT INTO lc_lliucheng (cliucheng_UUID,cShengchanID,cFenkuaihao,cRFID,cLCname,cLCreslut,cLCchuli,cLCtext,cLCpreson,cLCtime) VALUES ( \'"
                    +liucheng.cliucheng_UUID+ "\',\'"
                    +liucheng.cShengchanID + "\',\'"
                    +liucheng.cFenkuaihao+ "\',\'"
                    +liucheng.cRFID+ "\',\'"
                    +liucheng.cLCname+ "\',\'"
                    +liucheng.cLCreslut+ "\',\'"
                    +liucheng.cLCchuli+ "\',\'"
                    +liucheng.cLCtext+ "\',\'"
                    +liucheng.cLCpreson+ "\',\'"
                    +liucheng.cLCtime+ "\')";
            nlog.Info("InsertLiucheng ============== ["+Url+"]");
            mMySql.ExecInsert(MySQLBase.SQL_OPER.SQL_RFID_LIUCHENG_INSERT,Url);
        }else{
            EchoError();
        }
    }

    public void InsertFile(RFIDReportFile Image)
    {
        if(mOfflineUsed == false)
        {
            String Url = "INSERT INTO sp_image (cliucheng_UUID,c_UUID,ReportDate,cfilehouzui) VALUES ( \'"
                    +Image.cliucheng_UUID+ "\',\'"
                    +Image.c_UUID + "\',\'"
                    +Image.ReportDate + "\',\'"
                    +Image.cfilehouzui+ "\')";
            nlog.Info("InsertFile ============== ["+Url+"] ["+Image.c_UUID.length()+"]");
            mMySql.ExecInsert(MySQLBase.SQL_OPER.SQL_RFID_INSERTFILE,Url);
            String FilePath =getInnerSDCardPath()+"/"+IntentDef.DEFAULT_DIR+"/"+Image.cliucheng_UUID+".jpg";
            HttpUtil.UploadFile(INTENT_UPLOAD_ADDR,FilePath, null);

        }else{
            EchoError();
        }
    }

    public void UploadFile(RFIDReportFile Image){
        if(mOfflineUsed == false)
        {
            String Url = "INSERT INTO sp_image (cliucheng_UUID,c_UUID,ReportDate,cfilehouzui) VALUES ( \'"
                    +Image.cliucheng_UUID+ "\',\'"
                    +Image.c_UUID + "\',\'"
                    +Image.ReportDate + "\',\'"
                    +Image.cfilehouzui+ "\')";
            nlog.Info("InsertFile ============== ["+Url+"] ["+Image.c_UUID.length()+"]");
            mMySql.ExecInsert(MySQLBase.SQL_OPER.SQL_RFID_INSERTFILE,Url);
        }else{
            EchoError();
        }
    }

    public void GetRFIDLiuchengFile(String liucheng_id)
    {
        if(mOfflineUsed == false)
        {
            String Url = "select * from sp_image WHERE cliucheng_UUID=\'"+liucheng_id+"\'";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_LIUCHENG_IMAGE,Url);
        }else{
            EchoError();
        }
    }

    public void GetAllRFIDSegmentPartFromNet()
    {
        if(mOfflineUsed == false){
            String Url = "select * from info_Guangpian";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_ALLSEGMENTPART, Url);
        }
        else{
            EchoError();
        }
    }

    public void GetAllRFIDSegmentFormNet()
    {
        if(mOfflineUsed == false)
        {
            String Url = "select * from Info_Shengchan";
            mMySql.Exec(MySQLBase.SQL_OPER.SQL_RFID_ALLSEGMENT,Url);
        }else{
            EchoError();
        }
    }

    public class LogicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            if(arg1.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") && (mOfflineUsed == false))
            {
                int NewState = NetUtil.getNetworkState(mContext);
                nlog.Info("android.net.conn.CONNECTIVITY_CHANGE ========= ["+NewState+"]");
                if(mNetState != NewState){
                    mNetState = NewState;
                    if(mNetState ==  NetUtil.NETWORN_NONE){
                        Toast.makeText(mContext, R.string.toast_hit_net_error, Toast.LENGTH_LONG).show();
                        mMySql.DisConnect();
                    }else{
                        if(!mMySql.IsOnline()){
                            mMySql.Connect();
                        }
                    }
                }
            }
        }
    }
}
