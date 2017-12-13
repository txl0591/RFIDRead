package com.coresoft.rfidread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coresoft.base.Common;
import com.coresoft.base.IntentDef;
import com.coresoft.base.RFIDCaiJiWenDu;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDPeiHeBi;
import com.coresoft.base.RFIDReportFile;
import com.coresoft.base.RFIDSegment;
import com.coresoft.base.RFIDSegmentPart;
import com.coresoft.client.MainClient;
import com.coresoft.database.SysDBHelper;
import com.coresoft.sql.MySQLBase;
import com.coresoft.sql.MySql;
import com.coresoft.utils.CardManager;
import com.coresoft.utils.nlog;
import com.coresoft.zxing.CaptureActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;

import static com.coresoft.base.IntentDef.ACTIVITY_MESSAGE_LIUCHENG;
import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.*;
import static com.coresoft.base.IntentDef.LIUCHENG_VIEWINDEX.*;

/**
 * Created by Tangxl on 2017/5/31.
 */

public class ReadActivity extends Activity implements IntentDef.OnFragmentListener, IntentDef.OnCommDataReportListener{

    // zxing
    private final int REQUEST_CODE = 0xa1;

    private final static int SHOW_WAIT = 0xF1F1;
    private final static int HIDE_WAIT = 0xF1F2;
    private final static int TOAST_SHOW = 0xF1F3;
    private final static int SHOW_ACTIVITY = 0xF1F4;
    private final static int SHOW_RFID_ZXING = 0xF1F5;
    private final static int SHOW_SQL_ERR = 0xF1F6;

    private ReadPartIIFragment mReadPartIIFragment = null;
    private ReadPartIFragment mReadPartIFragment = null;
    private NfcAdapter mNfcAdapter = null;
    private PendingIntent mPendingIntent = null;

    private RFIDSegment mRFIDSegment;
    private RFIDSegmentPart mRFIDSegmentPart;

    private SlidingMenu mSlidingMenu = null;
    private LeftMenuFragment mLeftMenuFragment = null;

    private EditText mEditID;

    private Handler mHandler = null;
    private ProgressDialog mProgressDialog = null;
    private TimerTask mTimerTask = null;
    private Timer mTimer = null;
    private long mCardNum = 0;
    private long mCardNumBack = 0;
    private AlertDialog.Builder mAlertDialog = null;
    private AlertDialog mAlertDialogShow = null;
    private MainClient mMainClient = null;
    private ArrayList<RFIDReportFile> mRFIDReportFileList = new ArrayList<RFIDReportFile>();
    private ArrayList<RFIDLiuCheng> mRFIDLiuChengList = new ArrayList<RFIDLiuCheng>();
    private ArrayList<RFIDCaiJiWenDu> mRFIDCaiJiWenDuList = new ArrayList<RFIDCaiJiWenDu>();
    private ArrayList<RFIDPeiHeBi> mRFIDPeiHeBiList = new ArrayList<RFIDPeiHeBi>();
    private int mSQLCmd = 0;
    private int mNFC = 0;
    private int mChooseListId = 0;
    private int mReadAvtivityId = 0;
    private String zhibaolist [] = {
            IntentDef.RFIDReportType.ReportType_shuinizhibao,
            IntentDef.RFIDReportType.ReportType_gangjingzhibao,
            IntentDef.RFIDReportType.ReportType_shazhibaozhengshu,
            IntentDef.RFIDReportType.ReportType_shizhibaozhengshu,
            IntentDef.RFIDReportType.ReportType_kuangzhafengzhibao,
            IntentDef.RFIDReportType.ReportType_fengmeihuizhibao,
            IntentDef.RFIDReportType.ReportType_waijiajizhibao,
            IntentDef.RFIDReportType.ReportType_hunningtuzhibao,
            IntentDef.RFIDReportType.ReportType_qitazhibaoshu
    };

    private String jianyanlist[] = {
            IntentDef.RFIDReportType.ReportType_kangyaqiangdu,
            IntentDef.RFIDReportType.ReportType_kangshengxingneng,
            IntentDef.RFIDReportType.ReportType_qitazijian,
            IntentDef.RFIDReportType.ReportType_chuchangjianyan,
            IntentDef.RFIDReportType.ReportType_xingshijianyan,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Bundle mBundle = getIntent().getExtras();
        mCardNum = 0;
        mReadAvtivityId = 0;

        mMainClient = new MainClient(this);
        mMainClient.setmDataReportListener(this);
        InitNFCLocal();
        if (savedInstanceState == null) {
            mReadPartIFragment = new ReadPartIFragment(this,0);
            getFragmentManager().beginTransaction()
                    .add(R.id.read_layout1, mReadPartIFragment)
                    .commit();

            mReadPartIFragment.setOnFragmentListener(this);

            mReadPartIIFragment = new ReadPartIIFragment(this,0);
            getFragmentManager().beginTransaction()
                    .add(R.id.read_layout2, mReadPartIIFragment)
                    .commit();
            mReadPartIIFragment.setOnFragmentListener(this);
        }

        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMenu(R.layout.left_menu);

        mLeftMenuFragment = new LeftMenuFragment(this,0);
        getFragmentManager().beginTransaction()
                .add(R.id.left_menuconfig, mLeftMenuFragment)
                .commit();

        mLeftMenuFragment.setOnFragmentListener(this);

        mHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message arg0) {
                // TODO Auto-generated method stub
                switch(arg0.what)
                {
                    case INTENT_COMM_CMD_FIND_SQL:
                        mMainClient.GetRFIDSegmentPartFromNet(String.valueOf(mCardNum));
                        break;

                    case INTENT_COMM_CMD_SQL_RFID_SEGMENT:
                    {
                        mSQLCmd = arg0.what;
                        mRFIDSegment = (RFIDSegment)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        ArrayList<String> Value = Common.GetStringSegment(mRFIDSegment);
                        if(Value == null){
                            mReadPartIFragment.UpDataRFIDNumber(String.valueOf(mCardNum));
                        }else{
                            mReadPartIFragment.UpDataRFID(Value);
                            mCardNumBack = mCardNum;
                        }
                        break;
                    }
                    case INTENT_COMM_CMD_SQL_RFID_SEGMENTPART:
                    {
                        mSQLCmd = arg0.what;
                        mRFIDSegmentPart = (RFIDSegmentPart)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        MySQLBase.PrintSegmentPart(mRFIDSegmentPart);
                        ArrayList<String> Value = Common.GetStringSegmentPart(getApplicationContext(),mRFIDSegmentPart);
                        if(Value == null){
                            mReadPartIFragment.UpDataRFIDNumber(String.valueOf(mCardNum));
                             mRFIDSegmentPart.cShengchanID = null;
                        }else{
                            mReadPartIFragment.UpDataRFID(Value);
                            mCardNumBack = mCardNum;
                        }
                        switch(mReadAvtivityId)
                        {
                            case R.string.liucheng_chuchangjianyan:
                                StartLiucheng(getResources().getString(R.string.liucheng_chuchangjianyan));
                                break;
                            case R.string.liucheng_jinchangjianyan:
                                StartLiucheng(getResources().getString(R.string.liucheng_jinchangjianyan));
                                break;
                            case R.string.menu_shigong_record:
                                StartLiucheng(getResources().getString(R.string.menu_shigong_record));
                                break;
                            case R.string.menu_xiufu_record:
                                StartLiucheng(getResources().getString(R.string.menu_xiufu_record));
                                break;

                            default:
                                break;
                        }


                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_REPORTFILE:
                    {
                        mSQLCmd = arg0.what;
                        RFIDReportFile mRFIDReportFile = (RFIDReportFile)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        int index = mRFIDReportFileList.size();
                        mRFIDReportFileList.add(index,mRFIDReportFile);
                        index++;
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_ONLYCARD:
                        mSQLCmd = arg0.what;
                        mReadPartIFragment.UpDataRFIDNumber(String.valueOf(mCardNum));
                        mCardNumBack = mCardNum;
                        mHandler.sendEmptyMessage(HIDE_WAIT);
                        break;

                    case INTENT_COMM_CMD_SQL_RFID_LIUCHENG:
                    {
                        mSQLCmd = arg0.what;
                        RFIDLiuCheng mRFIDLiuCheng = (RFIDLiuCheng)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        if(mRFIDLiuCheng == null){
                            mHandler.sendEmptyMessage(TOAST_SHOW);
                        }else{
                            MySQLBase.PrintLiuCheng(mRFIDLiuCheng);
                            int index = mRFIDLiuChengList.size();
                            mRFIDLiuChengList.add(index,mRFIDLiuCheng);
                            index++;
                        }
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_FINISH_ERROR:
                        if(mNFC == 0) {
                            mCardNum = mCardNumBack;
                        }
                        mReadPartIFragment.UpDataRFIDNumber(String.valueOf(mCardNum));
                        mHandler.sendEmptyMessage(HIDE_WAIT);
                        mHandler.sendEmptyMessage(TOAST_SHOW);
                        mNFC = 0;
                        break;

                    case INTENT_COMM_CMD_SQL_FINISH:
                    {
                        mHandler.sendEmptyMessage(HIDE_WAIT);
                        if(mSQLCmd == INTENT_COMM_CMD_SQL_RFID_REPORTFILE)
                        {
                            if(mRFIDReportFileList.size() > 0) {
                                ShowReportList();
                            }else{
                                mHandler.sendEmptyMessage(TOAST_SHOW);
                            }
                        }else if(mSQLCmd == INTENT_COMM_CMD_SQL_RFID_LIUCHENG){
                            StartLiuchengView();
                        } else if(mSQLCmd == INTENT_COMM_CMD_SQL_RFID_YAOWEN){
                            StartWenDu();
                        } else if (mSQLCmd == INTENT_COMM_CMD_SQL_RFID_SHUIYANCHIWENDU){
                            StartWenDu();
                        } else if (mSQLCmd == INTENT_COMM_CMD_SQL_RFID_PEIHEBI){
                            StartPeiHeBi();
                        }
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_INSERT_LIUCHENG:
                    {
                        RFIDLiuCheng mRFIDLiuCheng = (RFIDLiuCheng)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        MySQLBase.PrintLiuCheng(mRFIDLiuCheng);
                        mMainClient.InsertLiucheng(mRFIDLiuCheng);
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_INSERT_FILE:
                    {
                        RFIDReportFile mRFIDReportFile = (RFIDReportFile)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        mMainClient.InsertFile(mRFIDReportFile);
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_UPLOAD_FILE:
                    {
                        RFIDReportFile mRFIDReportFile = (RFIDReportFile)arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        mMainClient.UploadFile(mRFIDReportFile);
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_INSERT:
                    {
                        Toast.makeText(getApplicationContext(), R.string.toast_hit_upgrade_success, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_INSERT_ERROR:
                    {
                        Toast.makeText(getApplicationContext(), R.string.toast_hit_upgrade_error, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case INTENT_COMM_CMD_SQL_RFID_SHUIYANCHIWENDU:
                    case INTENT_COMM_CMD_SQL_RFID_YAOWEN: {
                        mSQLCmd = arg0.what;
                        RFIDCaiJiWenDu mRFIDCaiJiWenDu = (RFIDCaiJiWenDu) arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        if(mRFIDCaiJiWenDu == null){
                            mHandler.sendEmptyMessage(TOAST_SHOW);
                        }else{
                            MySQLBase.PrintWenDu(mRFIDCaiJiWenDu);
                            int index = mRFIDCaiJiWenDuList.size();
                            mRFIDCaiJiWenDuList.add(index,mRFIDCaiJiWenDu);
                        }
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_PEIHEBI:{
                        mSQLCmd = arg0.what;
                        RFIDPeiHeBi mRFIDPeiHeBi = (RFIDPeiHeBi) arg0.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        if(mRFIDPeiHeBi == null){
                            mHandler.sendEmptyMessage(TOAST_SHOW);
                        }else{
                            MySQLBase.PrintPeiHeBi(mRFIDPeiHeBi);
                            int index = mRFIDPeiHeBiList.size();
                            mRFIDPeiHeBiList.add(index,mRFIDPeiHeBi);
                        }
                        break;
                    }

                    case TOAST_SHOW:
                        Toast.makeText(getApplicationContext(), R.string.toast_hit_link_error, Toast.LENGTH_SHORT).show();
                        break;

                    case SHOW_SQL_ERR:
                        Toast.makeText(getApplicationContext(), R.string.toast_hit_sqlserver_error, Toast.LENGTH_SHORT).show();
                        break;

                    case SHOW_WAIT:
                        ShowWaitDialog();
                        break;

                    case HIDE_WAIT:
                        HideWaitDialog();
                        break;

                    case SHOW_ACTIVITY:
                    {
                        String Url = arg0.getData().getString("URL");
                        String FileName = arg0.getData().getString("FILENAME");
                        StartDisplay(Url, FileName);
                        break;
                    }

                    case SHOW_RFID_ZXING:
                        ShowRfidZxing();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });

        if(mCardNum != 0){
            mHandler.sendEmptyMessage(SHOW_WAIT);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(INTENT_COMM_CMD_FIND_SQL);
                }
            }.start();

        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mMainClient.MainClientStop();
        mMainClient = null;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        DisableNFC();
        if (mSlidingMenu.isMenuShowing())
            mSlidingMenu.toggle();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        EnableNFC();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mCardNum > 0)
        {
            switch(item.getItemId()){
                case R.id.action_jinchang:
                    StartLiucheng(getResources().getString(R.string.liucheng_chuchangjianyan));
                    break;

                case R.id.action_chuchang:
                    StartLiucheng(getResources().getString(R.string.liucheng_jinchangjianyan));
                    break;

                case R.id.action_shigong:
                    StartLiucheng(getResources().getString(R.string.liucheng_shigojilu));
                    break;

                case R.id.action_xiufu:
                    StartLiucheng(getResources().getString(R.string.liucheng_xiufujilu));
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String str = Common.ByteArrayToHexString(tag.getId());
            mCardNum = Long.parseLong(str, 16);
            mNFC = 1;
            nlog.Info("NFC==============================["+str+"] ["+String.valueOf(mCardNum)+"]");
            if(mMainClient.getSqlState()) {
                mMainClient.GetRFIDSegmentPartFromNet(String.valueOf(mCardNum));
                ShowWaitDialog();
            }else{
                mHandler.sendEmptyMessage(SHOW_SQL_ERR);
                mMainClient.SqlConnect();
            }
        }
    }

    private void InitNFCLocal(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(null != mNfcAdapter){
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        }
    }

    private void DisableNFC(){
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private void EnableNFC(){
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, CardManager.FILTERS, CardManager.TECHLISTS);
    }

    @Override
    public void OnFragmentReport(View view) {

    }

    @Override
    public void OnFragmentReport(String Id) {
        if(Id.equals(getResources().getString(R.string.key_read_chipid)))
        {
            mHandler.sendEmptyMessage(SHOW_RFID_ZXING);
            //StartZxing();
        }else if(Id.equals(getResources().getString(R.string.key_read_zhibaozhiliao))){
            ShowChooseList(R.array.nfc_item_zhibaozhiliao);
        }else if(Id.equals(getResources().getString(R.string.key_read_jianyanjiance))){
            ShowChooseList(R.array.nfc_item_kanyaqiangdu);
        }else if(Id.equals(getResources().getString(R.string.key_read_jianliqueren))){
            GetLiuCheng();
        } else if (Id.equals(getResources().getString(R.string.key_read_yaohao))){
            GetYaoHao();
        } else if (Id.equals(getResources().getString(R.string.key_read_shuiyangchihao))){
            GetShuiYanChi();
        } else if(Id.equals(getResources().getString(R.string.key_read_touliaoqingdan))){
            GetTouliaoqingdan();
        }else if(Id.equals(getString(R.string.key_pref_menu_chuchang)))
        {
            if(mCardNum > 0){
                StartLiucheng(getResources().getString(R.string.liucheng_chuchangjianyan));
            }
        }else if(Id.equals(getString(R.string.key_pref_menu_jingchang)))
        {
            if(mCardNum > 0) {
                StartLiucheng(getResources().getString(R.string.liucheng_jinchangjianyan));
            }
        }else if(Id.equals(getString(R.string.key_pref_menu_shigongjilu)))
        {
            if(mCardNum > 0) {
                StartLiucheng(getResources().getString(R.string.liucheng_shigojilu));
            }
        }else if(Id.equals(getString(R.string.key_pref_menu_xiufujilu)))
        {
            if(mCardNum > 0) {
                StartLiucheng(getResources().getString(R.string.liucheng_xiufujilu));
            }
        }
    }

    private void ShowRfidZxing(){
        LayoutInflater factory = LayoutInflater.from(this);
        View LogView = factory.inflate(R.layout.login, null);

        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setTitle(R.string.toast_hit_input_id);
        mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
        mAlertDialog.setView(LogView);
        final EditText mEditID = (EditText) LogView.findViewById(R.id.EditID);
        Button mSuccess = (Button)LogView.findViewById(R.id.ButtonOk);
        mSuccess.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                    mReadPartIFragment.gCardNumPreference().setSummary(mEditID.getText().toString());
                    mCardNum = Long.parseLong(mEditID.getText().toString());
                    mMainClient.GetRFIDSegmentPartFromNet(mEditID.getText().toString());
                    ShowWaitDialog();
                    mAlertDialogShow.dismiss();
            }
        });
        Button mCancle = (Button)LogView.findViewById(R.id.ButtonCancle);
        mCancle.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAlertDialogShow.dismiss();
            }
        });
        Button zxing = (Button)LogView.findViewById(R.id.zxing);
        zxing.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                StartZxing();
                mAlertDialogShow.dismiss();
            }
        });

        mAlertDialog.create();
        mAlertDialogShow = mAlertDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)mEditID.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEditID, 0);
            }

        }, 50);
        mAlertDialogShow.setCanceledOnTouchOutside(true);
    }

    public void GetTouliaoqingdan(){
        if(!mRFIDPeiHeBiList.isEmpty()){
            mRFIDPeiHeBiList.clear();
        }
        if(null != mRFIDSegmentPart && mRFIDSegmentPart.cJBcode != null){
            mMainClient.GetRFIDPeiHeBi(mRFIDSegmentPart.cJBcode, mRFIDSegmentPart.dShijiDate);
        }
    }

    public void GetYaoHao(){
        if(!mRFIDCaiJiWenDuList.isEmpty()){
            mRFIDCaiJiWenDuList.clear();
        }
        if(mRFIDSegmentPart != null)
        {
            String Name = mReadPartIFragment.GetPreference(R.string.key_read_yaohao);
            mMainClient.GetRFIDYaoWenDu(getResources().getString(R.string.wendu_type_yaowen), Name, mRFIDSegmentPart.dRYdate, mRFIDSegmentPart.dCYdate);
        }

    }

    public void GetShuiYanChi(){
        if(!mRFIDCaiJiWenDuList.isEmpty()){
            mRFIDCaiJiWenDuList.clear();
        }
        if(null != mReadPartIFragment){
            String Name = mReadPartIFragment.GetPreference(R.string.key_read_shuiyangchihao);
            if(null != Name){
                mMainClient.GetRFIDShuiYanChi(getResources().getString(R.string.wendu_type_shuichi), Name, mRFIDSegmentPart.dRCdate, mRFIDSegmentPart.dCCdate);
            }
        }
    }

    public void GetLiuCheng(){
        if(!mRFIDLiuChengList.isEmpty()){
            mRFIDLiuChengList.clear();
        }

        if(null != mRFIDSegmentPart) {
            ShowWaitDialog();
            mMainClient.GetRFIDLiucheng(String.valueOf(mCardNum), mRFIDSegmentPart.cManufacture);
        }
    }

    public void GetReportList(String Id){
        if(!mRFIDReportFileList.isEmpty())
        {
            mRFIDReportFileList.clear();
        }
        if( Id.equals("none")){
            Toast.makeText(getApplicationContext(), R.string.toast_hit_link_error, Toast.LENGTH_SHORT).show();
        }else {
            mSQLCmd = INTENT_COMM_CMD_SQL_RFID_REPORTFILE;

            if(null != mRFIDSegmentPart){
                ShowWaitDialog();
                mMainClient.GetRFIDReport(String.valueOf(Id), mRFIDSegmentPart.cShengchanID,mRFIDSegmentPart.cManufacture);
            }
        }
    }

    public void ShowWaitDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMessage(getText(R.string.toast_hit_load_wait));
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void HideWaitDialog()
    {
        mProgressDialog.hide();
    }

    public void SetAlertDialog(DialogInterface arg0, boolean show){
        if(show){
            try {
                Field mField = arg0.getClass().getSuperclass().getDeclaredField("mShowing");
                mField.setAccessible(true);
                mField.set(arg0,true);
            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try {
                Field mField = arg0.getClass().getSuperclass().getDeclaredField("mShowing");
                mField.setAccessible(true);
                mField.set(arg0,false);
            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void StartDisplay(String FullPath, String Filename){
        Intent intent=new Intent(this, DisplayActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_URL, FullPath);
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_FILENAME, Filename);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void StartLiucheng(String name){
        Intent intent=new Intent(this, AddLiuCheng.class);
        Bundle bundle=new Bundle();
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_LIUCHENGNAME, name);
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_SHENGCHANGID, mRFIDSegmentPart.cShengchanID);
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_RFID, mRFIDSegmentPart.cRFID);
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_FENGKUAIHAO, mRFIDSegmentPart.cFenkuaihao);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void StartLiuchengView(){
        Intent intent=new Intent(this, ViewLiuCheng.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentDef.ACTIVITY_MESSAGE_LIUCHENG,mRFIDLiuChengList);
        bundle.putInt(IntentDef.ACTIVITY_MESSAGE_PAGEINDEX,PAGE_LIUCHENG_MAIN);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void StartPeiHeBi(){
        Intent intent=new Intent(this, ViewLiuCheng.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentDef.ACTIVITY_MESSAGE_PEIHEBI,mRFIDPeiHeBiList);
        bundle.putInt(IntentDef.ACTIVITY_MESSAGE_PAGEINDEX,PAGE_PEIHEBI_MAIN);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void StartWenDu(){
        Intent intent=new Intent(this, ViewLiuCheng.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentDef.ACTIVITY_MESSAGE_WENDU,mRFIDCaiJiWenDuList);
        bundle.putInt(IntentDef.ACTIVITY_MESSAGE_PAGEINDEX,PAGE_YAOHAO_MAIN);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void StartShuiYanChi(){
        Intent intent=new Intent(this, ViewLiuCheng.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentDef.ACTIVITY_MESSAGE_WENDU,mRFIDCaiJiWenDuList);
        bundle.putInt(IntentDef.ACTIVITY_MESSAGE_PAGEINDEX,PAGE_YAOHAO_MAIN);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void ShowReportList()
    {
        String[] mString = new String[mRFIDReportFileList.size()];
        for(int i = 0; i < mRFIDReportFileList.size(); i++){
            RFIDReportFile mRFIDReportFile = mRFIDReportFileList.get(i);
            mString[i] = getResources().getString(R.string.report_list_id)+String.valueOf(i+1) + ": "+ mRFIDReportFile.ReportID;
        }

        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_info)
       	    .setTitle(getResources().getString(R.string.report_list_id))
        	.setItems(mString,  new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RFIDReportFile mRFIDReportFile = mRFIDReportFileList.get(which);
                    String FileName1 = mRFIDReportFile.c_UUID+mRFIDReportFile.cfilehouzui;
                    String FileName = null;
                    try {
                        FileName = URLEncoder.encode(FileName1, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String Url = IntentDef.INTENT_DOWNLOAD_ADDR+FileName;
                    Message msg = new Message();
                    msg.what = SHOW_ACTIVITY;
                    Bundle mBundle = new Bundle();
                    mBundle.putString("URL",Url);
                    mBundle.putString("FILENAME",FileName);
                    msg.setData(mBundle);
                    mHandler.sendMessage(msg);
                }
            })
        	.show();
    }

    private void ShowChooseList(int Res)
    {
        mChooseListId = Res;
        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
        mAlertDialog.setItems(Res, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                String Id = "none";
                if(mChooseListId == R.array.nfc_item_zhibaozhiliao)
                {
                    if(arg1 < zhibaolist.length){
                        Id = zhibaolist[arg1];
                    }

                }else if(R.array.nfc_item_kanyaqiangdu == mChooseListId){
                    if(arg1 < jianyanlist.length){
                        Id = jianyanlist[arg1];
                    }
                }
                GetReportList(Id);
            }

        });
        mAlertDialog.create();
        mAlertDialogShow = mAlertDialog.show();
    }

    private void StartZxing(){
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("RESULT");
            if(Common.isNumeric(result)){
                mReadPartIFragment.gCardNumPreference().setSummary(result);
                mCardNum = Long.parseLong(result);
                if(mMainClient.getSqlState()) {
                    mMainClient.GetRFIDSegmentPartFromNet(result);
                    ShowWaitDialog();
                }else{
                    mHandler.sendEmptyMessage(SHOW_SQL_ERR);
                    mMainClient.SqlConnect();
                }

            } else {

                int post = result.indexOf("?ChipID=");
                if(-1 == post){
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                            .show();
                }else{
                    String RealResult= result.substring(post+8);
                    mReadPartIFragment.gCardNumPreference().setSummary(RealResult);
                    mCardNum = Long.parseLong(RealResult);
                    if(mMainClient.getSqlState()) {
                        mMainClient.GetRFIDSegmentPartFromNet(RealResult);
                        ShowWaitDialog();
                    }else{
                        mHandler.sendEmptyMessage(SHOW_SQL_ERR);
                        mMainClient.SqlConnect();
                    }
                }
            }
        }
    }
    @Override
    public void OnResponsionReport(int Cmd, Bundle Data, int DataLen) {
        Message msg = new Message();
        msg.what = Cmd;
        if(DataLen > 0){
            Bundle mBundle = new Bundle();
            Serializable mSerializable = Data.getSerializable(IntentDef.INTENT_COMM_DATA);
            if(mSerializable != null){
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA,mSerializable);
            }

            String Param = Data.getString(IntentDef.INTENT_COMM_PARAM);
            if(Param != null){
                mBundle.putString(IntentDef.INTENT_COMM_PARAM,Param);
            }
            msg.setData(mBundle);
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void OnDistributeReport(int Cmd, Bundle Data, int DataLen) {

    }

    /**
     *  ReadPartIIFragment
     */

    class ReadPartIIFragment extends FragmentBase{

        public ReadPartIIFragment(Context context, int SelfId) {
            super(context, SelfId);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.read_nfc_id2);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if(null != mOnFragmentListener){
                mOnFragmentListener.OnFragmentReport(preference.getKey());
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }

    /**
     * ReadPartIFragment
     */

    class ReadPartIFragment extends FragmentBase{

        private ArrayList<Preference> mPreferenceList = null;
        private Preference mcRFID = null;
        private final int mReadCardID[] =
        {
                R.string.key_read_chipid,
                R.string.key_read_shengchengdangwei,
                R.string.key_read_gongchengmingcheng,
                R.string.key_read_guanpianleixing,
                R.string.key_read_huanhao,
                R.string.key_read_fenkuaihao,
                R.string.key_read_chanpinghegezheng,
                R.string.key_read_shengchangrenwudanhao,
//                R.string.key_read_shengchangriqi,
                R.string.key_read_shengchangbanci,
                R.string.key_read_rumohunningtubiaomianwendu,
                R.string.key_read_gangmobianhao,
                R.string.key_read_gujiabianhao,
                R.string.key_read_touliaoqingdan,
                R.string.key_read_yaohao,
                R.string.key_read_ruyaoshijian,
                R.string.key_read_chuyaoshijian,
                R.string.key_read_tuomoqiwen,
                R.string.key_read_tuomoguanpianwendu,
                R.string.key_read_shuiyangchihao,
                R.string.key_read_shuiyangchishijian,
                R.string.key_read_chushuiyangchishijian,
                R.string.key_read_jingchuruchang,
                R.string.key_read_duichangyanghu,
                R.string.key_read_guanpianxiufu,
        };

        public ReadPartIFragment(Context context, int SelfId) {
            super(context, SelfId);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.read_nfc_id);
            mcRFID = (PreferenceScreen) findPreference(getResources().getString(R.string.key_read_chipid));
            mPreferenceList = new ArrayList<Preference>();
            for(int i = 0; i < mReadCardID.length; i++)
            {
                Preference mPreference = findPreference(getResources().getString(mReadCardID[i]));
                mPreferenceList.add(mPreference);
            }
        }

        public String GetPreference(int Id){
            Preference mPreference = findPreference(getResources().getString(Id));
            if(mPreference.getSummary() != null){
                return mPreference.getSummary().toString();
            }
            return null;
        }

        public void ClearRFID()
        {
            for(int i = 0; i < mReadCardID.length; i++)
            {
                Preference mPreference = mPreferenceList.get(i);
                mPreference.setSummary(" ");
            }
        }

        public void UpDataRFID(ArrayList<String> Value)
        {
            nlog.Info("Update ============== ["+Value.size()+"] mPreferenceList.size() ["+mPreferenceList.size()+"]");
            ClearRFID();
            for(int i = 0; i < mPreferenceList.size(); i++)
            {
                Preference mPreference = mPreferenceList.get(i);
                mPreference.setSummary(Value.get(i));
            }
        }

        public void UpDataRFIDNumber(String num){
            Preference mPreference = mPreferenceList.get(0);
            mPreference.setSummary(num);
        }

        public Preference gCardNumPreference(){
            return mcRFID;
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if(null != mOnFragmentListener){
                mOnFragmentListener.OnFragmentReport(preference.getKey());
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }
}
