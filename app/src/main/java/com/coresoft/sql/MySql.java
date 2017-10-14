package com.coresoft.sql;

import android.content.Context;
import android.os.Handler;

import java.sql.ResultSet;

import com.coresoft.base.IntentDef.OnSqlReportListener;
import com.coresoft.base.IntentDef.SQL_STATE;
import com.coresoft.sql.MySQLBase.SQL_OPER;
import com.coresoft.sql.MySQLBase.SQL_TYPE;
import com.coresoft.utils.nlog;

public class MySql implements OnSqlReportListener {

	private Context mContext = null;
	private final static String URL = "jdbc:jtds:sqlserver://738233.cicp.net:25938/SThree";
	private final static String NAME = "sa";
	private final static String PASSWORD = "905E27CF6F8CCF0CF5259F9B3DCE1F";
	public OnSqlReportListener mOnSqlReportListener = null;
	
	private int mType = SQL_TYPE.SQL_JDBC;
	private MySQLBase mMySQLBase = null;
	private static int mSQLState = SQL_STATE.SQL_STATE_IDLE;

	public MySql(int Type, Context context){
		mType = Type;
		mContext = context;
		switch(mType)
		{
		case SQL_TYPE.SQL_JDBC:
			mMySQLBase = new MySqlJDBC(this);
			break;
			
		case SQL_TYPE.SQL_JSON:
			break;
			
		case SQL_TYPE.SQL_HTML:
			break;	
			
		default:
			break;
		}
	}
	
	public static boolean IsOnline(){
		if(SQL_STATE.SQL_STATE_IDLE == mSQLState){
			return false;
		}
		return true;
	}
	
	public void SetOnSqlDataReportListener(OnSqlReportListener Listener)
	{
		mOnSqlReportListener = Listener;
	}
	
	private class ConnectThread extends Thread{
		private String mUrl;
		private String mName;
		private String mPassword;
		
		public ConnectThread (String Url, String Name, String Password){
			mUrl = Url;
			mName = Name;
			mPassword = Password;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if(true == mMySQLBase.Connect(mUrl, mName, mPassword)){
				mSQLState = SQL_STATE.SQL_STATE_ONLINE;

			}else{
				mSQLState = SQL_STATE.SQL_STATE_IDLE;
			}
		}
	}
	
	private class ExecThread extends Thread{
		private String mUrl;
		private int mOper;
		
		public ExecThread(int Oper, String Url){
			mUrl = Url;
			mOper = Oper;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			mMySQLBase.Exec(mOper, mUrl);
		}
	}
	
	public void Connect(){
		if(mSQLState == SQL_STATE.SQL_STATE_IDLE){
			new ConnectThread(URL, NAME, PASSWORD).start();	
		}
	}
	
	public void DisConnect(){
		if(mSQLState != SQL_STATE.SQL_STATE_IDLE)
		{
			mMySQLBase.DisConnect();
			mSQLState = SQL_STATE.SQL_STATE_IDLE;
		}
	}
	
	public void Exec(int Oper,String Url){
		if(IsOnline()){
			new ExecThread(Oper, Url).start();
		}else{
			mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_ONLYCARD, null);
		}
	}

	private class ExecInsertThread extends Thread{
		private String mUrl;
		private int mOper;

		public ExecInsertThread(int Oper, String Url){
			mUrl = Url;
			mOper = Oper;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			mMySQLBase.ExecInerst(mOper, mUrl);
		}
	}

	public void ExecInsert(int Oper,String Url){
		if(IsOnline()){
			new ExecInsertThread(Oper, Url).start();
		}else{
			mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_FINISH_ERROR, null);
		}
	}

	@Override
	public void OnSqlDataReport(int Oper, ResultSet Result) {
		// TODO Auto-generated method stub
		if(mOnSqlReportListener != null){
			mOnSqlReportListener.OnSqlDataReport(Oper, Result);
		}else{
			nlog.Info("MySql==========OnSqlDataReport============= Error");
		}
	}
}
