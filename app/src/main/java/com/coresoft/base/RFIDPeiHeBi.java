package com.coresoft.base;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Tangxl on 2017/7/8.
 */

public class RFIDPeiHeBi implements Serializable {

    public int JBLpid;
    public String cJBLnode;
    public String cXLDnode;
    public String cPHBnode;
    public float nFangLiang;
    public float nShuiNi;
    public float nFenMeiHui;
    public float nKuangFen;
    public float nShi1;
    public float nShi2;
    public float nSha1;
    public float nSha2;
    public float nWaiJiaJi1;
    public float nWaiJiaJi2;
    public float nWaiJiaJi3;
    public float nShuii;
    public float nJianBan;
    public Timestamp dCaiJI;

    public RFIDPeiHeBi(ResultSet Rst){
        try {
            JBLpid = Rst.getInt("JBLpid");
            cJBLnode = Rst.getString("cJBLnode");
            cXLDnode = Rst.getString("cXLDnode");
            cPHBnode = Rst.getString("cPHBnode");
            nFangLiang = Rst.getByte("nFangLiang");
            nShuiNi = Rst.getFloat("nShuiNi");
            nFenMeiHui = Rst.getFloat("nFenMeiHu");
            nKuangFen = Rst.getFloat("nKuangFen");
            nShi1 = Rst.getFloat("nShi1");
            nShi2 = Rst.getFloat("nShi2");
            nSha1 = Rst.getFloat("nSha1");
            nSha2 = Rst.getFloat("nSha2");
            nWaiJiaJi1 = Rst.getFloat("nWaiJiaJi1");
            nWaiJiaJi2 = Rst.getFloat("nWaiJiaJi2");
            nWaiJiaJi3 = Rst.getFloat("nWaiJiaJi3");
            nShuii = Rst.getFloat("nShuii");
            nJianBan = Rst.getFloat("nJianBan");
            dCaiJI = Rst.getTimestamp("dCaiJI");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
