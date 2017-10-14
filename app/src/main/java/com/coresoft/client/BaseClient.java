package com.coresoft.client;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.coresoft.base.IntentDef;
import com.coresoft.service.MainService;
import com.coresoft.utils.nlog;

public class BaseClient {
	private static final String tag="CoreSoft";
	protected Context mContext = null;
	protected MainService mMainService = null;
	protected String mServiceName = null;
	protected ClientReceiver mClientReceiver = null;
	private Handler mHandler = null;
	private IntentDef.OnCommDataReportListener mDataReportListener;

	public BaseClient(Context context)
	{
		mContext = context;
	}
	
	protected  ServiceConnection mServiceConn = new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder service) {
			mMainService = ((MainService.MainBinder)service).getMainService();
			Log.d(tag, "service connected mainservice..."+mMainService+" service "+service);
		}

		public void onServiceDisconnected(ComponentName name) {
			mMainService = null;
			Log.d(tag, "service Disconnected mainservice...");
		}
		
	};
	
	protected void StartIPC(Context context,String serviceName)
	{
		mServiceName = serviceName;
		Intent serviceIntent = new Intent(serviceName);
		context.startService(serviceIntent);
		context.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);		
	}
	
	protected void StopIPC(Context context, String serviceName)
	{
	    if ( null != mServiceConn )
	    {
	        context.unbindService(mServiceConn);
	        mServiceConn = null;
	    }
        Intent serviceIntent = new Intent(serviceName);
        context.stopService(serviceIntent);  
	}

	protected void startReceiver(Context context, String broadcastName) {
		mHandler = new Handler(Looper.myLooper());
		mClientReceiver = new ClientReceiver();
		IntentFilter filter = new IntentFilter(broadcastName);
		context.registerReceiver(mClientReceiver, filter);
	}
	
	protected void startReceiver(Context context, String[] broadcastList) {
		mHandler = new Handler(Looper.myLooper());
		mClientReceiver = new ClientReceiver();
		IntentFilter filter = new IntentFilter();
		for (String s : broadcastList) {
			filter.addAction(s);
		}
		context.registerReceiver(mClientReceiver, filter);
	}
	
	protected void stopReceiver(Context context, String broadcastName) {        
	    if ( null != mClientReceiver )
	    {
	        context.unregisterReceiver(mClientReceiver);
	        mClientReceiver = null;
	    }
    }

	class ClientReceiver extends BroadcastReceiver {
		 
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if (action.equals(IntentDef.MODULE_DISTRIBUTE)){

			}else if (action.equals(IntentDef.MODULE_RESPONSION)){
				Bundle mBundle = null;
				int cmd = intent.getIntExtra(IntentDef.INTENT_COMM_CMD,IntentDef.INTENT_TYPE_INVALID);
				int datalen = intent.getIntExtra(IntentDef.INTENT_COMM_DATALEN, 0);
				if(datalen > 0){
					mBundle = intent.getExtras();
				}
				nlog.Info("MODULE_RESPONSION=====================["+cmd+"] datalen ["+datalen+"]");
				if(mDataReportListener != null){
					mDataReportListener.OnResponsionReport(cmd,mBundle,datalen);
				}
			}
		}
	}

	public void setmDataReportListener(
			IntentDef.OnCommDataReportListener DataReportListener) {
		mDataReportListener = DataReportListener;
	}

}

