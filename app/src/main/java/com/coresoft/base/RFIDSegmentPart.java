package com.coresoft.base;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RFIDSegmentPart implements Serializable {
	public int GPXXpid;
	public String cShengchanID;
	public String cFenkuaihao;
	public String cRFID;
	public String cStyteGP;
	public String cGMnode;
	public Timestamp dJihuaDate;
	public Timestamp dShijiDate;
	public String iBanc; 
	public String cZYname;
	public Timestamp dRYdate;
	public Timestamp dCYdate;
	public String cDCcode;
	public int iHang;
	public int ilie;
	public int iCeng;
	public String cRCcode;
	public String cCCcode;
	public String cZBScode;
	public int STATE;
	public String cJHcode;
	public String cXScode;
	public String cCCHcode;
	public String cKYcode;
	public String cChicode;
	public String cJBcode;
	public Timestamp dRCdate;
	public Timestamp dCCdate;
	
	public RFIDSegmentPart(ResultSet Rst)
	{
		try {
			GPXXpid = Rst.getInt("GPXXpid");
			cShengchanID = Rst.getString("cShengchanID");
			cFenkuaihao = Rst.getString("cFenkuaihao");
			cRFID = Rst.getString("cRFID");
			cStyteGP = Rst.getString("cStyteGP");
			cGMnode = Rst.getString("cGMnode");
			dJihuaDate = Rst.getTimestamp("dJihuaDate");
			dShijiDate = Rst.getTimestamp("dShijiDate");
			iBanc = Rst.getString("iBanc"); 
			cZYname = Rst.getString("cZYname");
			dRYdate = Rst.getTimestamp("dRYdate");
			dCYdate = Rst.getTimestamp("dCYdate");
			cDCcode = Rst.getString("cDCcode");
			iHang = Rst.getInt("iHang");
			ilie = Rst.getInt("ilie");
			iCeng = Rst.getInt("iCeng");
			cRCcode = Rst.getString("cRCcode");
			cCCcode = Rst.getString("cCCcode");
			cZBScode = Rst.getString("cZBScode");
			STATE = Rst.getInt("STATE");
			cJHcode = Rst.getString("cJHcode");
			cXScode = Rst.getString("cXScode");
			cCCHcode = Rst.getString("cCCHcode");
			cKYcode = Rst.getString("cKYcode");
			cChicode = Rst.getString("cChicode");
			dRCdate = Rst.getTimestamp("dRCdate");
			dCCdate = Rst.getTimestamp("dCCdate");
			cJBcode = Rst.getString("cJBcode");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
