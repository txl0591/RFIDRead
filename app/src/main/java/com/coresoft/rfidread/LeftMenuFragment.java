package com.coresoft.rfidread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.coresoft.base.Common;
import com.coresoft.base.IntentDef;
import com.coresoft.database.SysDBHelper;
import com.coresoft.utils.nlog;
import com.coresoft.zxing.CaptureActivity;

/**
 * Created by Tangxl on 2017/10/2.
 */

public class LeftMenuFragment extends FragmentBase implements IntentDef.OnLogUserReportListener {
    private static String mLeftMenu = "pref_menu_cardinfo";
    private PreferenceScreen mUserPreferenceScreen = null;

    public LeftMenuFragment(Context context, int SelfId) {
        super(context, SelfId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencemenu);
        mUserPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference(getString(R.string.key_pref_menu_login_user));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if(null != mLeftMenu && mLeftMenu.equals(preference.getKey())){
            return true;
        }

        if(preference.getKey().equals(getString(R.string.key_pref_menu_login_user)))
        {
            if(SysDBHelper.GetLoginInState()){
                Common.StartLogout(mContext,this);
            }else{
                StartLogin();
            }
            return true;
        }

        mLeftMenu = preference.getKey();

        if(preference.getKey().equals(getString(R.string.key_pref_menu_cardinfo)))
        {
            StartReadCard();
        }else if(preference.getKey().equals(getString(R.string.key_pref_menu_chuchang)))
        {
            if(SysDBHelper.GetLoginInState()){
                if(mOnFragmentListener != null){
                    mOnFragmentListener.OnFragmentReport(preference.getKey());
                }
            }else{
                Toast.makeText(mContext, R.string.toast_hit_login_error, Toast.LENGTH_SHORT).show();
            }
        }else if(preference.getKey().equals(getString(R.string.key_pref_menu_jingchang)))
        {
            if(SysDBHelper.GetLoginInState()){
                if(mOnFragmentListener != null){
                    mOnFragmentListener.OnFragmentReport(preference.getKey());
                }
            }else{
                Toast.makeText(mContext, R.string.toast_hit_login_error, Toast.LENGTH_SHORT).show();
            }
        }else if(preference.getKey().equals(getString(R.string.key_pref_menu_shigongjilu)))
        {
            if(SysDBHelper.GetLoginInState()){
                if(mOnFragmentListener != null){
                    mOnFragmentListener.OnFragmentReport(preference.getKey());
                }
            }else{
                Toast.makeText(mContext, R.string.toast_hit_login_error, Toast.LENGTH_SHORT).show();
            }
        }else if(preference.getKey().equals(getString(R.string.key_pref_menu_xiufujilu)))
        {
            if(SysDBHelper.GetLoginInState()){
                if(mOnFragmentListener != null){
                    mOnFragmentListener.OnFragmentReport(preference.getKey());
                }
            }else{
                Toast.makeText(mContext, R.string.toast_hit_login_error, Toast.LENGTH_SHORT).show();
            }
        }else if(preference.getKey().equals(getString(R.string.key_pref_menu_sysinfo)))
        {
            StartSyslog();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void setUserName(){
        if(SysDBHelper.GetLoginInState()){
            mUserPreferenceScreen.setSummary("张三");
        }else{
            mUserPreferenceScreen.setSummary(getString(R.string.nfc_sys_logout));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserName();
    }

    private void StartSyslog(){
        Intent intent= new Intent(mContext, SysInfoActivity.class);
        startActivity(intent);
    }

    private void StartReadCard(){
        Intent intent= new Intent(mContext, ReadActivity.class);
        startActivity(intent);
    }

    private void StartLogin(){
        Intent intent= new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    private void StartZxing(){
        Intent intent= new Intent(mContext, CaptureActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnLogUserReport(boolean State) {
        setUserName();
    }
}
