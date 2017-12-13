package com.coresoft.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.coresoft.rfidread.DisplayActivity;

public class IntentDef {

	public static final String SERVICE_NAME_MAIN			="com.rfid.service.MainService";

	public static final String MODULE_RESPONSION			="rfid.intent.action.MODULE_RESPONSION";
	public static final String MODULE_DISTRIBUTE			="rfid.intent.action.MODULE_DISTRIBUTE";

	public static final String INTENT_COMM_CMD				="rfid.intent.netcomm.CMD";
	public static final String INTENT_COMM_DATA			="rfid.intent.netcomm.DATA";
	public static final String INTENT_COMM_DATALEN			="rfid.intent.netcomm.DATALEN";
	public static final String INTENT_COMM_PARAM			="rfid.intent.netcomm.PARAM";
	public static final int    INTENT_TYPE_INVALID        = -1;

	public static final String INTENT_DOWNLOAD_ADDR 			= "http://47.100.126.101:80/download.ashx?filename=";
	public static final String INTENT_UPLOAD_ADDR 			= "http://47.100.126.101:80/Upload.ashx";

	public final static String DEFAULT_DIR = "CoreSoft";
	public final static String ACTIVITY_MESSAGE_URL = "ACTIVITY_MESSAGE_URL";
	public final static String ACTIVITY_MESSAGE_FILENAME = "ACTIVITY_MESSAGE_FILENAME";
    public final static String ACTIVITY_MESSAGE_LIUCHENGNAME = "ACTIVITY_MESSAGE_LIUCHENGNAME";
    public final static String ACTIVITY_MESSAGE_RFID = "ACTIVITY_MESSAGE_RFID";
    public final static String ACTIVITY_MESSAGE_SHENGCHANGID = "ACTIVITY_MESSAGE_SHENGCHANGID";
    public final static String ACTIVITY_MESSAGE_FENGKUAIHAO = "ACTIVITY_MESSAGE_FENGKUAIHAO";
	public final static String ACTIVITY_MESSAGE_LIUCHENG = "ACTIVITY_MESSAGE_LIUCHENG";
	public final static String ACTIVITY_MESSAGE_WENDU = "ACTIVITY_MESSAGE_WENDU";
    public final static String ACTIVITY_MESSAGE_PEIHEBI = "ACTIVITY_MESSAGE_PEIHEBI";
	public final static String ACTIVITY_MESSAGE_PAGEINDEX = "ACTIVITY_MESSAGE_PAGEINDEX";
	public final static String ACTIVITY_MESSAGE_CARDNUM = "ACTIVITY_MESSAGE_CARDNUM";
    public final static String ACTIVITY_MESSAGE_READACTIVITY_ID = "ACTIVITY_MESSAGE_READACTIVITY_ID";

	public class  INTENT_COMM_CMD_ID
	{
		public static final int INTENT_COMM_CMD_SQL_RFID_SEGMENTPART = 0xFF01;
		public static final int INTENT_COMM_CMD_SQL_RFID_SEGMENT = 0xFF02;
		public static final int INTENT_COMM_CMD_SQL_RFID_ONLYCARD = 0xFF03;
		public static final int INTENT_COMM_CMD_SQL_RFID_REPORTFILE = 0xFF04;
        public static final int  INTENT_COMM_CMD_SQL_RFID_LIUCHENG = 0xFF05;
		public static final int  INTENT_COMM_CMD_SQL_RFID_INSERT_LIUCHENG = 0xFF06;
		public static final int  INTENT_COMM_CMD_SQL_RFID_INSERT_FILE = 0xFF07;
		public final static int INTENT_COMM_CMD_SQL_RFID_YAOWEN = 0xFF08;
		public final static int INTENT_COMM_CMD_SQL_RFID_SHUIYANCHIWENDU = 0xFF09;
		public final static int INTENT_COMM_CMD_SQL_RFID_PEIHEBI = 0xFF0A;
		public static final int INTENT_COMM_CMD_SQL_RFID_LIUCHENG_IMAGE = 0xFF0B;
		public static final int  INTENT_COMM_CMD_SQL_RFID_UPLOAD_FILE = 0xFF0C;


		public static final int INTENT_COMM_CMD_SQL_FINISH = 0xFFF1;
		public static final int INTENT_COMM_CMD_SQL_FINISH_ERROR = 0xFFF2;
		public static final int INTENT_COMM_CMD_SQL_INSERT = 0xFFF3;
		public static final int INTENT_COMM_CMD_SQL_INSERT_ERROR = 0xFFF4;
		public static final int INTENT_COMM_CMD_FIND_SQL = 0xFFF5;
        public static final int INTENT_COMM_CMD_WAIT = 0xFFF6;
	}

	public class BROADCASE_STRING
	{
		public static final String UI = "BROADCASE_UI";
		public static final String RETURN_UI = "BROADCASE_RETURNUI";
		public static final String LOGIC_CARD_ID = "BROADCASE_LOGIC_CARDID";
		public static final String LOGIC_CARD_SCBH = "BROADCASE_LOGIC_CARD_SCBH";
	}
	
	public class LOGIC_OPER 
	{
		public static final int SQL = 0x01;
	}
		
	public class LOG_LEVEL{
		public static final int LOG_LOW = 0x01;
		public static final int LOG_MIDDLE = 0x02;
		public static final int LOG_HIGH = 0x04;
		public static final int LOG_PRINT = 0x08;
	}
	
	public class SQL_TABLE{
		public static final String TABLE_Segment = "info_Shengchan";
		public static final String TABLE_SegmentPart = "info_Guangpian";
	}
	
	public class SQL_STATE{
		public static final int SQL_STATE_IDLE = 0x01;
		public static final int SQL_STATE_ONLINE = 0x02;
	}

	public class LIUCHENG_VIEWINDEX{
		public final static int PAGE_LIUCHENG_MAIN = 0xD1D1;
		public final static int PAGE_LIUCHENG_PAGE1 = 0xD1D2;
		public final static int PAGE_YAOHAO_MAIN = 0xD1D3;
		public final static int PAGE_YAOHAO_PAGE1 = 0xD1D4;
		public final static int PAGE_PEIHEBI_MAIN = 0xD1D5;
		public final static int PAGE_PEIHEBI_PAGE1 = 0xD1D6;
	}

	public interface OnFragmentListener
	{
		public void OnFragmentReport(View view);
		public void OnFragmentReport(String Id);
	}

	public interface OnSqlReportListener
    {
        public void OnSqlDataReport(int Oper, ResultSet Result);
    }

    public interface OnHttpReportListener
	{
		public void OnHttpDataReport(int Oper, long param1, long param2);
	}

	public class NFC_TYPE
	{
		public final static int NFC_NONE = 0x00;
		public final static int NFC_SYS = 0x01;
		public final static int NFC_EXT = 0x02;
	}
	
	public class NFC_STATE
	{
		public final static int NFC_STATE_NONE = 0x00;
		public final static int NFC_STATE_IDLE = 0x01;
		public final static int NFC_STATE_WORK = 0x01;
	}
	
	public class FTP_STATE
	{
		public static final String FTP_CONNECT_SUCCESSS = "Ftp Connect Success";  
	    public static final String FTP_CONNECT_FAIL = "Ftp Connect Error";  
	    public static final String FTP_DISCONNECT_SUCCESS = "Ftp Disconnect";  
	    public static final String FTP_FILE_NOTEXISTS = "Ftp No File";  
	      
	    public static final String FTP_UPLOAD_SUCCESS = "Ftp Upload Success";  
	    public static final String FTP_UPLOAD_FAIL = "Ftp  Upload Error";  
	    public static final String FTP_UPLOAD_LOADING = "Ftp Uploading";  
	  
	    public static final String FTP_DOWN_LOADING = "Ftp Downloading";  
	    public static final String FTP_DOWN_SUCCESS = "Ftp Download Success";  
	    public static final String FTP_DOWN_FAIL = "Ftp Download Error";  
	      
	    public static final String FTP_DELETEFILE_SUCCESS = "Ftp Delete Success";  
	    public static final String FTP_DELETEFILE_FAIL = "Ftp Delete Error";
	}

	public class HttpState
	{
		public final static int DOWNLOAD_START = 0;
		public final static int DOWNLOAD_ING = 1;
		public final static int DOWNLOAD_SUCCESS = 2;
		public final static int DOWNLOAD_ERROR = 3;

		public final static int UPLOAD_START = 10;
		public final static int UPLOAD_ING = 11;
		public final static int UPLOAD_SUCCESS = 12;
		public final static int UPLOAD_ERROR = 13;
	};
	
	public static String getInnerSDCardPath() {    
        return Environment.getExternalStorageDirectory().getPath();    
    }  
  
    public static List<String> getExtSDCardPath()  
    {  
        List<String> lResult = new ArrayList<String>();  
        try {  
            Runtime rt = Runtime.getRuntime();  
            Process proc = rt.exec("mount");  
            InputStream is = proc.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            String line;  
            while ((line = br.readLine()) != null) {  
                if (line.contains("extSdCard"))  
                {  
                    String [] arr = line.split(" ");  
                    String path = arr[1];  
                    File file = new File(path);  
                    if (file.isDirectory())  
                    {  
                        lResult.add(path);  
                    }  
                }  
            }  
            isr.close();  
        } catch (Exception e) {  
        }  
        return lResult;  
    }

    public class RFIDReportType
	{
		public final static String ReportType_xingshijianyan = "01";//型式检验
		public final static String ReportType_chuchangjianyan = "02";//出厂检验
		public final static String ReportType_kangyaqiangdu = "03";//抗压强度
		public final static String ReportType_kangshengxingneng = "04";//抗渗性能
		public final static String ReportType_qitazijian = "05";//其它自检
		public final static String ReportType_shuinizhibao = "06";//水泥质保
		public final static String ReportType_gangjingzhibao = "07";//钢筋质保
		public final static String ReportType_hunningtuzhibao = "08"; //混凝土质保
		public final static String ReportType_waijiajizhibao = "09"; //外加剂质保
		public final static String ReportType_fengmeihuizhibao = "10";//粉煤灰质保
		public final static String ReportType_kuangzhafengzhibao = "11";//矿渣粉质保
		public final static String ReportType_shazhibaozhengshu = "12";//砂质保书
		public final static String ReportType_shizhibaozhengshu = "13";//石质保书
		public final static String ReportType_qitazhibaoshu = "14";//其它质保书
		public final static String ReportType_shuinizijian = "15";//水泥自检
		public final static String ReportType_gangjinzhijian = "16";//钢筋自检
		public final static String ReportType_fengmeihuizhijian = "17";//粉煤灰自检
		public final static String ReportType_kuangzhafengzijian = "18";//矿渣粉自检
		public final static String ReportType_waijiajizhijian = "19";//外加剂自检
		public final static String ReportType_gangmozijian = "20";//钢模自检
		public final static String ReportType_gangjingwangzijian = "21";//钢筋网自检
		public final static String ReportType_yaowendujiance = "22";//窑温度监测
		public final static String ReportType_waigaungzijian = "23";//外观自检
	}

	public interface OnCommDataReportListener
	{
		public void OnResponsionReport(int Cmd, Bundle Data, int DataLen);
		public void OnDistributeReport(int Cmd, Bundle Data, int DataLen);
	}

	public interface  OnLogUserReportListener{
		public void OnLogUserReport(boolean State);
	}
}
