package com.coresoft.rfidread;

import com.coresoft.base.IntentDef;
import com.coresoft.utils.nlog;

import android.content.Context;
import android.preference.PreferenceFragment;
import android.view.View;

public class FragmentBase extends PreferenceFragment {
	
	public Context mContext = null;
	public IntentDef.OnFragmentListener mOnFragmentListener = null;

	public FragmentBase(Context context, int SelfId){
		mContext = context;
	}

	public void setOnFragmentListener(IntentDef.OnFragmentListener Listener){
		mOnFragmentListener = Listener;
	}



	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
}
