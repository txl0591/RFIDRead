package com.coresoft.base;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Tangxl on 2017/7/7.
 */

public class RFIDCaiJiWenDu implements Serializable {
    public int WDpid;
    public String cStype;
    public String cXLDnode;
    public ArrayList<Byte> nTongDao = new ArrayList<Byte>();
    public Timestamp dCaiJI;

    public RFIDCaiJiWenDu(ResultSet Rst){
        try {
            WDpid = Rst.getInt("WDpid");
            cXLDnode = Rst.getString("cXLDnode");
            for(int i = 0; i < 16; i++)
            {
                String Name = "nTongDao"+String.valueOf(i+1);
                nTongDao.add(i,Rst.getByte(Name));
            }
            dCaiJI = Rst.getTimestamp("dCaiJI");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
