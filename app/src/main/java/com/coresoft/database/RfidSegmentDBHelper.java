package com.coresoft.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;

/**
 * Created by Tangxl on 2017/9/26.
 */

public class RfidSegmentDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RfidSegment.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TBL_NAME = "RfidSegment";

    public static final String TBL_ID = "ID";
    public static final String TBL_SCBHpid = "SCBHpid";
    public static final String TBL_cShengchanID = "cShengchanID";
    public static final String TBL_cRKcode = "cRKcode";
    public static final String TBL_cStyteGP = "cStyteGP";
    public static final String TBL_cGMnode = "cGMnode";
    public static final String TBL_dJihuaDate = "dJihuaDate";
    public static final String TBL_iBanc = "iBanc";
    public static final String TBL_dShijiDate = "dShijiDate";
    public static final String TBL_cZYname = "cZYname";
    public static final String TBL_dRYdate = "dRYdate";
    public static final String TBL_dCYdate= "dCYdate";
    public static final String TBL_STATE = "STATE";

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

    private static final String CREATE_TBL = " create table " + TBL_NAME
            + "(" + TBL_ID + " INTEGER primary key autoincrement,"
            + TBL_SCBHpid + " varchar(25),"
            + TBL_cShengchanID + " varchar(30),"
            + TBL_cRKcode + " varchar(60),"
            + TBL_cStyteGP + " varchar(70),"
            + TBL_cGMnode + " varchar(20),"
            + TBL_dJihuaDate + " varchar(20),"
            + TBL_iBanc + " varchar(20),"
            + TBL_dShijiDate + " varchar(20),"
            + TBL_cZYname + " varchar(20),"
            + TBL_dRYdate + " varchar(20),"
            + TBL_dCYdate + " varchar(50),"
            + TBL_STATE + " varchar(20),"
            + TBL_STATE + " varchar(30))";

    private SQLiteDatabase mSQLiteDatabase;

    public RfidSegmentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        mSQLiteDatabase = db;
        mSQLiteDatabase.execSQL(CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void close(){
        if (mSQLiteDatabase != null){
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
    }
}
