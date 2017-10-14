package com.coresoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coresoft.base.RFIDSegmentPart;

import java.sql.Timestamp;

/**
 * Created by Tangxl on 2017/9/26.
 */

public class RFIDSegmentPartDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RfidSegmentPart.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TBL_NAME = "RfidSegmentPart";

    public static final String TBL_ID = "ID";
    public static final String TBL_GPXXpid = "GPXXpid";
    public static final String TBL_cShengchanID = "cShengchanID";
    public static final String TBL_cFenkuaihao = "cFenkuaihao";
    public static final String TBL_cRFID = "cRFID";
    public static final String TBL_cStyteGP = "cStyteGP";
    public static final String TBL_cGMnode = "cGMnode";
    public static final String TBL_dJihuaDate = "dJihuaDate";
    public static final String TBL_dShijiDate = "dShijiDate";
    public static final String TBL_iBanc = "iBanc";
    public static final String TBL_cZYname = "cZYname";
    public static final String TBL_dRYdate = "dRYdate";
    public static final String TBL_dCYdate = "dCYdate";
    public static final String TBL_cDCcode = "cDCcode";
    public static final String TBL_iHang = "iHang";
    public static final String TBL_ilie = "ilie";
    public static final String TBL_iCeng = "iCeng";
    public static final String TBL_cRCcode = "cRCcode";
    public static final String TBL_cCCcode = "cCCcode";
    public static final String TBL_cZBScode = "cZBScode";
    public static final String TBL_STATE = "STATE";
    public static final String TBL_cJHcode = "cJHcode";
    public static final String TBL_cXScode = "cXScode";
    public static final String TBL_cCCHcode = "cCCHcode";
    public static final String TBL_cKYcode = "cKYcode";
    public static final String TBL_cChicode = "cChicode";
    public static final String TBL_cJBcode = "cJBcode";
    public static final String TBL_dRCdate = "dRCdate";
    public static final String TBL_dCCdate = "dCCdate";

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

    private static final String CREATE_TBL = " create table " + TBL_NAME
            + "(" + TBL_ID + " INTEGER primary key autoincrement,"
            + TBL_GPXXpid + " varchar(25),"
            + TBL_cShengchanID + " varchar(30),"
            + TBL_cFenkuaihao + " varchar(60),"
            + TBL_cRFID + " varchar(60),"
            + TBL_cStyteGP + " varchar(60),"
            + TBL_cGMnode + " varchar(60),"
            + TBL_dJihuaDate + " varchar(20),"
            + TBL_dShijiDate + " varchar(20),"
            + TBL_iBanc + " varchar(60),"
            + TBL_cZYname + " varchar(60),"
            + TBL_dRYdate + " varchar(20),"
            + TBL_dCYdate + " varchar(20),"
            + TBL_cDCcode + " varchar(60),"
            + TBL_iHang + " int,"
            + TBL_ilie + " int,"
            + TBL_iCeng + " int,"
            + TBL_cRCcode + " varchar(60),"
            + TBL_cCCcode + " varchar(60),"
            + TBL_cZBScode + " varchar(60),"
            + TBL_STATE + " int,"
            + TBL_cJHcode + " varchar(60),"
            + TBL_cXScode + " varchar(60),"
            + TBL_cCCHcode + " varchar(60),"
            + TBL_cKYcode + " varchar(60),"
            + TBL_cChicode + " varchar(60),"
            + TBL_cJBcode + " varchar(60),"
            + TBL_dRCdate + " varchar(20),"
            + TBL_dCCdate + " varchar(20))";

    private SQLiteDatabase mSQLiteDatabase;

    public RFIDSegmentPartDBHelper(Context context) {
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

    public Cursor query(String SJBH){
        SQLiteDatabase db = getWritableDatabase();
        mSQLiteDatabase = db;
        String selection = TBL_cRFID+"=?";
        String[] selectionArgs = new String[]{SJBH};
        Cursor c = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
        return c;
    }

    private ContentValues CreateContentValues(RFIDSegmentPart Part){
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(TBL_GPXXpid,Part.GPXXpid);
        mContentValues.put(TBL_cShengchanID,Part.cShengchanID);
        mContentValues.put(TBL_cFenkuaihao,Part.cFenkuaihao);
        mContentValues.put(TBL_cRFID,Part.cRFID);
        mContentValues.put(TBL_cStyteGP,Part.cStyteGP);
        mContentValues.put(TBL_cGMnode,Part.cGMnode);
        String dJihuaDate = Part.dJihuaDate.toString();
        mContentValues.put(TBL_dJihuaDate,dJihuaDate);
        String dShijiDate = Part.dShijiDate.toString();
        mContentValues.put(TBL_dShijiDate,dShijiDate);
        mContentValues.put(TBL_iBanc,Part.iBanc);
        mContentValues.put(TBL_cZYname,Part.cZYname);
        String dRYdate = Part.dRYdate.toString();
        mContentValues.put(TBL_dRYdate,dRYdate);
        String dCYdate = Part.dCYdate.toString();
        mContentValues.put(TBL_dCYdate,dCYdate);
        mContentValues.put(TBL_cDCcode,Part.cDCcode);
        mContentValues.put(TBL_iHang,Part.iHang);
        mContentValues.put(TBL_ilie,Part.ilie);
        mContentValues.put(TBL_iCeng,Part.iCeng);
        mContentValues.put(TBL_cRCcode,Part.cRCcode);
        mContentValues.put(TBL_cCCcode,Part.cCCcode);
        mContentValues.put(TBL_cZBScode,Part.cZBScode);
        mContentValues.put(TBL_STATE,Part.STATE);
        mContentValues.put(TBL_cJHcode,Part.cJHcode);
        mContentValues.put(TBL_cXScode,Part.cXScode);
        mContentValues.put(TBL_cCCHcode,Part.cCCHcode);
        mContentValues.put(TBL_cKYcode,Part.cKYcode);
        mContentValues.put(TBL_cChicode,Part.cChicode);
        mContentValues.put(TBL_cJBcode,Part.cJBcode);
        String dRCdate = Part.dRCdate.toString();
        mContentValues.put(TBL_dRCdate,dRCdate);
        String dCCdate = Part.dRCdate.toString();
        mContentValues.put(TBL_dCCdate,dCCdate);

        return mContentValues;
    }

    public boolean AddRFIDSegmentPart(RFIDSegmentPart Part){
        boolean ret = true;
        SQLiteDatabase db = getWritableDatabase();
        mSQLiteDatabase = db;

        String selection = TBL_cRFID+"=?";
        String[] selectionArgs = new String[]{Part.cRFID};
        Cursor c = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
        if (c.getCount() > 0){
            ret = false;
        }
        c.close();
        if(ret){
            ContentValues mContentValues = CreateContentValues(Part);
            db.insert(TBL_NAME, null, mContentValues);
        }

        return ret;
    }

    public void UpdateRFIDSegmentPart(RFIDSegmentPart Part){
        SQLiteDatabase db = getWritableDatabase();
        mSQLiteDatabase = db;
        ContentValues mContentValues = CreateContentValues(Part);
        String selection = TBL_cRFID+"=?";
        String[] selectionArgs = new String[]{Part.cRFID};
        db.update(TBL_NAME, mContentValues, selection, selectionArgs);
    }

}
