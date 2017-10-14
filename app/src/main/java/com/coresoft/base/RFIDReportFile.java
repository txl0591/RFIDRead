package com.coresoft.base;

import com.coresoft.utils.nlog;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Tangxl on 2017/6/10.
 */

public class RFIDReportFile implements Serializable {
    public int Img_pid;
    public int SP_pid;
    public String ReportID;
    public Timestamp ReportDate;
    public String cjianyanjigou;
    public String cfilehouzui;
    public String c_Report_UUID;
    public String c_UUID;
    public String cliucheng_UUID;

    public RFIDReportFile(ResultSet Rst){
        try {
            Img_pid = Rst.getInt("Img_pid");
            SP_pid = Rst.getInt("SP_pid");
            ReportID = Rst.getString("ReportID");
            ReportDate = Rst.getTimestamp("ReportDate");
            cfilehouzui = Rst.getString("cfilehouzui");
            c_UUID = Rst.getString("c_UUID");
            cjianyanjigou = Rst.getString("cjianyanjigou");
            c_Report_UUID = Rst.getString("c_Report_UUID");
            cliucheng_UUID = Rst.getString("cliucheng_UUID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RFIDReportFile(){

    }
}
