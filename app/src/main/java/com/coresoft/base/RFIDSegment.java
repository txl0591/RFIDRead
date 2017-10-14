package com.coresoft.base;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.coresoft.utils.nlog;

public class RFIDSegment implements Serializable {
	public int SCBHpid;
	public String cShengchanID;
	public String cRKcode;
	public String cStyteGP;
	public String cGMnode;
	public Timestamp dJihuaDate;
	public int iBanc;
	public Timestamp dShijiDate;
	public String cZYname;
	public Timestamp dRYdate;
	public Timestamp dCYdate;
	public String STATE;
	
	public RFIDSegment(ResultSet Rst){
		try {
			SCBHpid = Rst.getInt("SCBHpid");
			cShengchanID = Rst.getString("cShengchanID");
			cRKcode = Rst.getString("cRKcode");
			cStyteGP = Rst.getString("cStyteGP");
			cGMnode = Rst.getString("cGMnode");
			dJihuaDate = Rst.getTimestamp("dJihuaDate");
			iBanc = Rst.getInt("iBanc");
			dShijiDate = Rst.getTimestamp("dShijiDate");
			cZYname = Rst.getString("cZYname");
			dRYdate = Rst.getTimestamp("dRYdate");
			dCYdate = Rst.getTimestamp("dCYdate");
			STATE = Rst.getString("STATE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
	}
}
