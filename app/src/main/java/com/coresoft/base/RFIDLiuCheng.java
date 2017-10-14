package com.coresoft.base;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Tangxl on 2017/6/11.
 */

public class RFIDLiuCheng implements Serializable {
    public int LCpid;
    public String cLCname;
    public String cliucheng_UUID;
    public String cLCreslut;
    public String cLCchuli;
    public String cLCtext;
    public String cLCpreson;
    public Timestamp cLCtime;
    public String cShengchanID;
    public String cFenkuaihao;
    public String cRFID;

    public RFIDLiuCheng(ResultSet Rst){
        try {
            LCpid = Rst.getInt("LCpid");
            cLCname = Rst.getString("cLCname");
            cliucheng_UUID = Rst.getString("cliucheng_UUID");
            cLCreslut = Rst.getString("cLCreslut");
            cLCchuli = Rst.getString("cLCchuli");
            cLCtext = Rst.getString("cLCtext");
            cLCpreson = Rst.getString("cLCpreson");
            cLCtime = Rst.getTimestamp("cLCtime");
            cShengchanID = Rst.getString("cShengchanID");
            cFenkuaihao = Rst.getString("cFenkuaihao");
            cRFID = Rst.getString("cRFID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RFIDLiuCheng(){

    }
}
