package com.coresoft.rfidread;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

public class ZhibaoFragment extends FragmentBase {

	public ZhibaoFragment(Context context, int ParentId) {
		super(context, ParentId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.zhibaoziliao);  
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	
}
