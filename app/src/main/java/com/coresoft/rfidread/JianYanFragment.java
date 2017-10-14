package com.coresoft.rfidread;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

public class JianYanFragment extends FragmentBase {

	public JianYanFragment(Context context, int SelfId) {
		super(context, SelfId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.jianyanjiance);  
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	
}
