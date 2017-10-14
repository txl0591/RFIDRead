package com.coresoft.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.coresoft.base.IntentDef.LOG_LEVEL;
import com.coresoft.base.IntentDef.OnSqlReportListener;
import com.coresoft.base.RFIDSegment;
import com.coresoft.base.RFIDSegmentPart;
import com.coresoft.utils.nlog;

public class MySqlJDBC extends MySQLBase {
	
	public MySqlJDBC(OnSqlReportListener Listener) {
		super(Listener);
		// TODO Auto-generated constructor stub
	}

	private int mLogLevel = (LOG_LEVEL.LOG_MIDDLE);
	private final static String SQL_CLASS = "net.sourceforge.jtds.jdbc.Driver";
	private Connection mConnect = null;
	
	@Override
	public boolean Connect(String Url, String UserName, String Password){
		
		try {
			 Class.forName(SQL_CLASS);  
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			nlog.IfInfo(mLogLevel,"Loader Class Error");
			return false;
		} 
		
		try {
			DriverManager.setLoginTimeout(5);

			Properties theProperties = new Properties();
			if (UserName != null) {
				theProperties.setProperty("user", UserName);
			}
			if (Password != null) {
				theProperties.setProperty("password", Password);
			}

			theProperties.setProperty("useUnicode","true");
			theProperties.setProperty("characterEncoding","utf8");
			mConnect = DriverManager.getConnection(Url,theProperties);
			//mConnect = (Connection) DriverManager.getConnection(Url, UserName, Password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			nlog.IfInfo(mLogLevel,"getConnection SQL Error");
			return false;
		}
		
		nlog.IfInfo(mLogLevel,"SQL Connect Success");
		return true;
	}
	
	@Override
	public void DisConnect(){
		if(null != mConnect)
		{
			try {
				mConnect.close();
				mConnect = null;
				nlog.IfInfo(mLogLevel,"SQL DisConnect Success");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public RFIDSegmentPart GetRFIDSegmentPart(ResultSet Rst){
		RFIDSegmentPart mRFIDSegmentPart = new RFIDSegmentPart(Rst);
		return mRFIDSegmentPart;
	}
	
	@Override
	public RFIDSegment GetRFIDSegment(ResultSet Rst) {
		// TODO Auto-generated method stub
		RFIDSegment mRFIDSegment = new RFIDSegment(Rst);
		return mRFIDSegment;
	}
	
	@Override
	public boolean Exec(int Oper, String sql) {
		// TODO Auto-generated method stub
		if(null == mConnect)
		{
			return false;
		}

		Statement stmt;
		boolean ret = true;
		try {
			stmt = mConnect.createStatement();
			ResultSet mResultSet = stmt.executeQuery(sql);  
			while(mResultSet.next()){
				ret = false;
				if(mOnSqlReportListener != null){
					mOnSqlReportListener.OnSqlDataReport(Oper,mResultSet);
				}
			}
			mResultSet.close();
			stmt.close();
			nlog.IfInfo(mLogLevel, "SQL Exec Success ["+sql+"]");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			nlog.IfInfo(mLogLevel, "SQL Exec Error ["+sql+"]========Err ["+e.getMessage()+"]");
            if(mOnSqlReportListener != null){
                mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_FINISH_ERROR,null);
            }
		}
		if(mOnSqlReportListener != null){
            if(ret){
                mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_FINISH_ERROR,null);
            }else{
                mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_FINISH,null);
            }
		}
		return true;
	}

	@Override
	public boolean ExecInerst(int Oper, String sql) {
		// TODO Auto-generated method stub
		if(null == mConnect)
		{
			return false;
		}

		Statement stmt;
		boolean ret = true;
		try {
			stmt = mConnect.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			nlog.IfInfo(mLogLevel, "SQL Exec Insert Success ["+sql+"]");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			nlog.IfInfo(mLogLevel, "SQL Exec Error ["+sql+"]========Err ["+e.getMessage()+"]");
			if(mOnSqlReportListener != null){
				mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_INSERT_ERROR,null);
			}
		}
		if(mOnSqlReportListener != null){
			mOnSqlReportListener.OnSqlDataReport(SQL_OPER.SQL_RFID_INSERT,null);
		}
		return true;
	}
}
