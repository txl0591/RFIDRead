package com.coresoft.rfidread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coresoft.base.IntentDef;
import com.coresoft.database.SysDBHelper;
import com.coresoft.utils.HttpUtil;
import com.coresoft.utils.Upgrade;
import com.coresoft.utils.nlog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import okhttp3.OkHttpClient;

import static com.coresoft.base.IntentDef.getInnerSDCardPath;

/**
 * Created by Tangxl on 2017/5/31.
 */

public class SysInfoActivity extends Activity implements IntentDef.OnFragmentListener {

    private SysinfoPragment mSysinfoPragment = null;
    private Upgrade mUpgrade = null;
    private SlidingMenu mSlidingMenu = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysinfo);
        mUpgrade = new Upgrade(this);
        if (savedInstanceState == null) {
            mSysinfoPragment = new SysinfoPragment(this,0);
            mSysinfoPragment.SetOnFragmentListener(this);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mSysinfoPragment)
                    .commit();

            mSlidingMenu = new SlidingMenu(this);
            mSlidingMenu.setMode(SlidingMenu.LEFT);
            mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
            mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
            mSlidingMenu.setFadeDegree(0.35f);
            mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
            mSlidingMenu.setMenu(R.layout.left_menu);

            LeftMenuFragment mLeftMenuFragment = new LeftMenuFragment(this,0);
            getFragmentManager().beginTransaction()
                    .add(R.id.left_menuconfig, mLeftMenuFragment)
                    .commit();
        }
    }

    @Override
    public void OnFragmentReport(View view) {
        switch(view.getId())
        {
            case R.id.nfc_sys_version:
                mUpgrade.Start();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSlidingMenu.isMenuShowing())
            mSlidingMenu.toggle();
    }

    @Override
    public void OnFragmentReport(String Id) {

    }

    class SysinfoPragment extends FragmentBase implements View.OnClickListener {

        private Context mContext = null;
        private LinearLayout nfc_sys_version = null;
        private TextView sysinfo_version = null;
        private IntentDef.OnFragmentListener mOnFragmentListener = null;

        public SysinfoPragment(Context context, int SelfId) {
            super(context, SelfId);
            mContext = context;
        }

        public void SetOnFragmentListener(IntentDef.OnFragmentListener listener){
            mOnFragmentListener = listener;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sysinfo, container, false);
            nfc_sys_version = (LinearLayout)rootView.findViewById(R.id.nfc_sys_version);
            nfc_sys_version.setOnClickListener(this);
            sysinfo_version = (TextView) rootView.findViewById(R.id.sysinfo_version);
            sysinfo_version.setText(mContext.getResources().getText(R.string.nfc_sys_version)+ ": "+GetVerion());

            return rootView;
        }



        @Override
        public void onClick(View v) {
            if(mOnFragmentListener != null){
                mOnFragmentListener.OnFragmentReport(v);
            }
        }



        private String GetVerion(){
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info;
            try {
                info = pm.getPackageInfo(mContext.getPackageName(), 0);
                return info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "Unknow";
            }
        }
    }

}
