package com.coresoft.utils;

import com.coresoft.base.IntentDef.LOG_LEVEL;

import android.util.Log;

public class nlog {
private final static String TAG = "CoreSoft";
		
	private static int mLogLevel = (LOG_LEVEL.LOG_LOW|LOG_LEVEL.LOG_PRINT|LOG_LEVEL.LOG_HIGH|LOG_LEVEL.LOG_MIDDLE);

	public static void Info(String log){
		Log.d(TAG,"[Java] "+log);
	}
	
	public static void IfInfo(int level, String log)
	{
		if (0 != (level&mLogLevel))
		{
			Info(log);
		}
	}
}
