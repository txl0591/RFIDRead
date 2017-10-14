package com.coresoft.sql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.coresoft.base.IntentDef.OnSqlReportListener;
import com.coresoft.base.RFIDCaiJiWenDu;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDPeiHeBi;
import com.coresoft.base.RFIDReportFile;
import com.coresoft.base.RFIDSegment;
import com.coresoft.base.RFIDSegmentPart;
import com.coresoft.utils.nlog;

public abstract class MySQLBase{
	
	public class SQL_TYPE{
		public final static int SQL_JDBC = 0x01;
		public final static int SQL_JSON = 0x02;
		public final static int SQL_HTML = 0x03;
	}
	
	public class SQL_OPER{
		public final static int SQL_NONE = 0x00;
		public final static int SQL_RFID_SEGMENT = 0x01;
		public final static int SQL_RFID_SEGMENTPART = 0x02;
		public final static int SQL_RFID_ONLYCARD = 0x03;
		public final static int SQL_RFID_REPORTFILE = 0x04;
        public final static int SQL_RFID_LIUCHENG = 0x05;
		public final static int SQL_RFID_LIUCHENG_INSERT = 0x06;
		public final static int SQL_RFID_YAOWEN = 0x07;
		public final static int SQL_RFID_SHUIYAMCHIWENDU = 0x08;
		public final static int SQL_RFID_PEIHEBI = 0x09;
		public final static int SQL_RFID_INSERTFILE = 0x0A;
		public final static int SQL_RFID_LIUCHENG_IMAGE = 0x0B;
		public final static int SQL_RFID_ALLSEGMENT = 0x0C;
		public final static int SQL_RFID_ALLSEGMENTPART = 0x0D;

		public final static int SQL_RFID_FINISH_ERROR = 0xFC;
		public final static int SQL_RFID_FINISH = 0xFD;
		public final static int SQL_RFID_INSERT_ERROR = 0xFE;
		public final static int SQL_RFID_INSERT = 0xFF;
	}
	
	public int mOper = SQL_OPER.SQL_NONE;
	public OnSqlReportListener mOnSqlReportListener = null;
	public MySQLBase(OnSqlReportListener Listener){
		mOnSqlReportListener = Listener;
	}
	
	public static void PrintSegmentPart(RFIDSegmentPart Part){

//		nlog.Info("*********************************************************");
//		nlog.Info("GPXXpid      ["+Part.GPXXpid+"]");
//		nlog.Info("cShengchanID ["+Part.cShengchanID+"]");
//		nlog.Info("cFenkuaihao  ["+Part.cFenkuaihao+"]");
//		nlog.Info("cRFID 	    ["+Part.cRFID+"]");
//		nlog.Info("cStyteGP     ["+Part.cStyteGP+"]");
//		nlog.Info("cGMnode 	    ["+Part.cGMnode+"]");
//		nlog.Info("iBanc 		["+Part.iBanc+"]");
//		nlog.Info("cZYname 		["+Part.cZYname+"]");
//		nlog.Info("cDCcode 		["+Part.cDCcode+"]");
//		nlog.Info("iHang 		["+Part.iHang+"]");
//		nlog.Info("ilie 		["+Part.ilie+"]");
//		nlog.Info("iCeng 		["+Part.iCeng+"]");
//		nlog.Info("cRCcode 		["+Part.cRCcode+"]");
//		nlog.Info("cCCcode 		["+Part.cCCcode+"]");
//		nlog.Info("cZBScode 	["+Part.cZBScode+"]");
//		nlog.Info("STATE 		["+Part.STATE+"]");
//		nlog.Info("cJHcode 		["+Part.cJHcode+"]");
//		nlog.Info("cXScode 		["+Part.cXScode+"]");
//		nlog.Info("cCCHcode 	["+Part.cCCHcode+"]");
//		nlog.Info("dRYdate 		["+Part.dRYdate.toString()+"]");
//		nlog.Info("dCYdate 		["+Part.dCYdate.toString()+"]");
//		nlog.Info("dRCdate 		["+Part.dRCdate.toString()+"]");
//		nlog.Info("dCCdate 		["+Part.dCCdate.toString()+"]");
//		nlog.Info("dCCdate 		["+Part.dShijiDate.toString()+"]");
//		nlog.Info("*********************************************************");
	}

	public static void PrintReport(RFIDReportFile Part){
//        nlog.Info("*********************************************************");
//        nlog.Info("Img_pid      ["+Part.Img_pid+"]");
//		nlog.Info("SP_pid 		["+Part.SP_pid+"]");
//		nlog.Info("ReportID 		["+Part.ReportID+"]");
//		nlog.Info("ReportDate 		["+Part.ReportDate.toString()+"]");
//		nlog.Info("cjianyanjigou 		["+Part.cjianyanjigou+"]");
//        nlog.Info("cfilehouzui 		["+Part.cfilehouzui+"]");
//		nlog.Info("c_Report_UUID 		["+Part.c_Report_UUID+"]");
//		nlog.Info("c_UUID 		["+Part.c_UUID+"]");
//        nlog.Info("*********************************************************");
    }

    public static void PrintLiuCheng(RFIDLiuCheng Rst){
//		nlog.Info("*********************************************************");
//		nlog.Info("LCpid      	["+Rst.LCpid+"]");
//		nlog.Info("cLCname      	["+Rst.cLCname+"]");
//		nlog.Info("cliucheng_UUID      	["+Rst.cliucheng_UUID+"]");
//		nlog.Info("cLCreslut      	["+Rst.cLCreslut+"]");
//		nlog.Info("cLCchuli      	["+Rst.cLCchuli+"]");
//		nlog.Info("cLCtext      	["+Rst.cLCtext+"]");
//		nlog.Info("cLCpreson      	["+Rst.cLCpreson+"]");
//		nlog.Info("cLCtime      	["+Rst.cLCtime.toString()+"]");
//		nlog.Info("cShengchanID      	["+Rst.cShengchanID+"]");
//		nlog.Info("cFenkuaihao      	["+Rst.cFenkuaihao+"]");
//		nlog.Info("cRFID      	["+Rst.cRFID+"]");
//		nlog.Info("*********************************************************");
	}

	public static void PrintSegment(RFIDSegment Part){
//		nlog.Info("*********************************************************");
//		nlog.Info("SCBHpid      	["+Part.SCBHpid+"]");
//		nlog.Info("cShengchanID     ["+Part.cShengchanID+"]");
//		nlog.Info("cRKcode      	["+Part.cRKcode+"]");
//		nlog.Info("cStyteGP      	["+Part.cStyteGP+"]");
//		nlog.Info("cGMnode      	["+Part.cGMnode+"]");
//		nlog.Info("dJihuaDate      	["+Part.dJihuaDate.toString()+"]");
//		nlog.Info("iBanc      		["+Part.iBanc+"]");
//		nlog.Info("dShijiDate      	["+Part.dShijiDate.toString()+"]");
//		nlog.Info("cZYname      	["+Part.cZYname+"]");
//		nlog.Info("dRYdate      	["+Part.dRYdate.toString()+"]");
//		nlog.Info("dCYdate      	["+Part.dCYdate.toString()+"]");
//		nlog.Info("STATE      		["+Part.STATE+"]");
//		nlog.Info("*********************************************************");
	}

	public static void PrintWenDu(RFIDCaiJiWenDu Rst){
//		nlog.Info("*********************************************************");
//		nlog.Info("cStype      		["+Rst.cStype+"]");
//		nlog.Info("cXLDnode      		["+Rst.cXLDnode+"]");
//		nlog.Info("nTongDao1      		["+Rst.nTongDao.get(0)+"]");
//		nlog.Info("dCaiJI      		["+Rst.dCaiJI.toString()+"]");
//		nlog.Info("*********************************************************");
	}
	public int JBLpid;
	public String cJBLnode;
	public String cXLDnode;
	public String cPHBnode;
	public byte nFangLiang;
	public byte nShuiNi;
	public byte nFenMeiHui;
	public byte nKuangFen;
	public byte nShi1;
	public byte nShi2;
	public byte nSha1;
	public byte nSha2;
	public byte nWaiJiaJi1;
	public byte nWaiJiaJi2;
	public byte nWaiJiaJi3;
	public byte nShuii;
	public byte nJianBan;
	public Timestamp dCaiJI;

	public static void PrintPeiHeBi(RFIDPeiHeBi Rst){
		nlog.Info("*********************************************************");
		nlog.Info("JBLpid ["+Rst.JBLpid+"]");
		nlog.Info("cJBLnode ["+Rst.cJBLnode+"]");
		nlog.Info("cXLDnode ["+Rst.cXLDnode+"]");
		nlog.Info("cPHBnode ["+Rst.cPHBnode+"]");
		nlog.Info("*********************************************************");
	}

	public abstract boolean Connect(String Url, String UserName, String Password);
	public abstract void DisConnect();
	public abstract boolean Exec(int Oper, String sql);
	public abstract RFIDSegmentPart GetRFIDSegmentPart(ResultSet Rst);
	public abstract RFIDSegment GetRFIDSegment(ResultSet Rst);
	public abstract boolean ExecInerst(int Oper, String sql);
}
