package com.coresoft.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tangxl on 2017/9/29.
 */

public class SysDBHelper {

    public Context mContext = null;
    private static boolean LoginInState = false;

    public SysDBHelper(Context context){
        mContext = context;
    }

    public void SaveUserInfo(String Name, String Passwd, String DanWei, String ID, int type){
//        SharedPreferences userSettings = mContext.getSharedPreferences("setting", 0);
//        SharedPreferences.Editor editor = userSettings.edit();

    }

    public static boolean GetLoginInState()
    {
        return LoginInState;
    }

    public static void SetLoginInState(boolean State)
    {
        LoginInState = State;
    }
}
