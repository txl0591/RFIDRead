package com.coresoft.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Tangxl on 2017/6/10.
 */

public class RFIDReport {
    public int SP_pid;
    public String c_UUID;
    public String ReportID;
    public String creportname;
    public Timestamp ReportDate;
    public String creportstyte;
    public String cinstitution;
    public String cBGresult;
    public String cBGjieguo;
    public String Ckaishihuan;
    public String Cjiezhihuan;

    public RFIDReport(ResultSet Rst){
        try {
            SP_pid = Rst.getInt("SP_pid");
            c_UUID = Rst.getString("c_UUID");
            ReportID = Rst.getString("ReportID");
            creportname = Rst.getString("creportname");
            ReportDate = Rst.getTimestamp("ReportDate");
            creportstyte = Rst.getString("creportstyte");
            cinstitution = Rst.getString("cinstitution");
            cBGresult = Rst.getString("cBGresult");
            cBGjieguo = Rst.getString("cBGjieguo");
            Ckaishihuan = Rst.getString("Ckaishihuan");
            Cjiezhihuan = Rst.getString("Cjiezhihuan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
