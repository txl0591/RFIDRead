package com.coresoft.rfidread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.coresoft.base.IntentDef;
import com.coresoft.base.RFIDCaiJiWenDu;
import com.coresoft.base.RFIDLiuCheng;
import com.coresoft.base.RFIDPeiHeBi;
import com.coresoft.base.RFIDReportFile;
import com.coresoft.client.MainClient;
import com.coresoft.utils.nlog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.coresoft.base.IntentDef.ACTIVITY_MESSAGE_LIUCHENG;
import static com.coresoft.base.IntentDef.ACTIVITY_MESSAGE_PAGEINDEX;
import static com.coresoft.base.IntentDef.ACTIVITY_MESSAGE_PEIHEBI;
import static com.coresoft.base.IntentDef.ACTIVITY_MESSAGE_WENDU;
import static com.coresoft.base.IntentDef.INTENT_COMM_CMD_ID.*;
import static com.coresoft.base.IntentDef.LIUCHENG_VIEWINDEX.*;

/**
 * Created by Tangxl on 2017/6/11.
 */

public class ViewLiuCheng extends Activity implements AdapterView.OnItemClickListener, IntentDef.OnCommDataReportListener {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private EditText mEditText;
    private Button mButton;
    private Button mButtonSnap;
    private Button mButtonUpload;
    private ArrayList<RFIDLiuCheng> mRFIDLiuChengList;
    private ArrayList<RFIDCaiJiWenDu> mRFIDCaiJiWenDuList;
    private ArrayList<RFIDPeiHeBi> mRFIDPeiHeBiList;
    private RFIDLiuCheng mRFIDLiuCheng = null;
    private RFIDCaiJiWenDu mRFIDCaiJiWenDu = null;
    private RFIDPeiHeBi mRFIDPeiHeBi = null;
    private RFIDReportFile mRFIDReportFile = null;
    private int mPage = 0;
    private Handler mHandler = null;
    private SurfaceView mSurfaceView;
    private MainClient mMainClient = null;
    private int mSQLCmd  = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainClient = new MainClient(this);
        mMainClient.setmDataReportListener(this);
        Bundle mBundle = getIntent().getExtras();
        mRFIDLiuChengList = (ArrayList<RFIDLiuCheng>)mBundle.getSerializable(ACTIVITY_MESSAGE_LIUCHENG);
        mRFIDCaiJiWenDuList = (ArrayList<RFIDCaiJiWenDu>)mBundle.getSerializable(ACTIVITY_MESSAGE_WENDU);
        mRFIDPeiHeBiList = (ArrayList<RFIDPeiHeBi>)mBundle.getSerializable(ACTIVITY_MESSAGE_PEIHEBI);
        int Page = mBundle.getInt(ACTIVITY_MESSAGE_PAGEINDEX,PAGE_LIUCHENG_MAIN);

        if(Page == PAGE_LIUCHENG_MAIN){
            setContentView(R.layout.liucheng_add);
            mListView = (ListView)findViewById(R.id.liucheng_add);
            mListView.setOnItemClickListener(this);
            mButton  = (Button)findViewById(R.id.liucheng_ok);
            mButton.setVisibility(View.INVISIBLE);
            mButtonSnap  = (Button)findViewById(R.id.liucheng_snap);
            mButtonSnap.setVisibility(View.INVISIBLE);
            mButtonUpload = (Button)findViewById(R.id.liucheng_upload);
            mButtonUpload.setVisibility(View.INVISIBLE);
        }else if(Page == PAGE_YAOHAO_MAIN || Page == PAGE_PEIHEBI_MAIN){
            setContentView(R.layout.liucheng_view);
            mListView = (ListView)findViewById(R.id.liucheng_add);
            mListView.setOnItemClickListener(this);
        }

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what)
                {
                    case PAGE_LIUCHENG_MAIN:
                        mPage = msg.what;
                        updateList_1();
                        break;

                    case PAGE_LIUCHENG_PAGE1:
                        mPage = msg.what;
                        updateList_2();
                        break;

                    case PAGE_YAOHAO_MAIN:
                        mPage = msg.what;
                        updateList_3();
                        break;

                    case PAGE_YAOHAO_PAGE1:
                        mPage = msg.what;
                        updateList_4();
                        break;

                    case PAGE_PEIHEBI_MAIN:
                        mPage = msg.what;
                        updateList_5();
                        break;

                    case PAGE_PEIHEBI_PAGE1:
                        mPage = msg.what;
                        updateList_6();
                        break;

                    case INTENT_COMM_CMD_SQL_FINISH_ERROR:
                        if(mSQLCmd == INTENT_COMM_CMD_SQL_RFID_LIUCHENG_IMAGE){
                            Toast.makeText(getApplicationContext(),R.string.toast_hit_liucheng_snap,Toast.LENGTH_SHORT).show();
                        }
                        mSQLCmd = -1;
                        break;

                    case INTENT_COMM_CMD_SQL_FINISH: {
                        if(mSQLCmd == INTENT_COMM_CMD_SQL_RFID_LIUCHENG_IMAGE){
                            ShowLiuChengImage();
                        }
                        mSQLCmd = -1;
                        break;
                    }

                    case INTENT_COMM_CMD_SQL_RFID_LIUCHENG_IMAGE: {
                        mSQLCmd = msg.what;
                        mRFIDReportFile = (RFIDReportFile)msg.getData().getSerializable(IntentDef.INTENT_COMM_DATA);
                        break;
                    }
                }
            }
        };
        mHandler.sendEmptyMessage(Page);
    }

    public void StartDisplay(String FullPath, String Filename){
        Intent intent=new Intent(this, DisplayActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_URL, FullPath);
        bundle.putString(IntentDef.ACTIVITY_MESSAGE_FILENAME, Filename);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void ShowLiuChengImage(){
        String FileName = mRFIDReportFile.cliucheng_UUID+mRFIDReportFile.cfilehouzui;
        String Url = IntentDef.INTENT_DOWNLOAD_ADDR+FileName;
        nlog.Info("ShowLiuChengImage ["+FileName+"] Url["+Url+"]");
        StartDisplay(Url,FileName);
    }

    private void updateList_1()
    {
        int j = 0;
        ArrayList<String> mString = new ArrayList<String>();
        for(int i = mRFIDLiuChengList.size(); i > 0; i--)
        {
            RFIDLiuCheng mRFIDLiuCheng = mRFIDLiuChengList.get(i-1);
            mString.add(j,mRFIDLiuCheng.cLCtime.toString()+" "+mRFIDLiuCheng.cLCname+ " "+mRFIDLiuCheng.cLCpreson+" "+mRFIDLiuCheng.cLCreslut);
            j++;
        }
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    private void updateList_2()
    {
        ArrayList<String> mString = new ArrayList<String>();
        mString.add(0,getResources().getString(R.string.liucheng_name) +": "+mRFIDLiuCheng.cLCname);
        mString.add(1,getResources().getString(R.string.nfc_read_chipid) +": "+mRFIDLiuCheng.cRFID);
        mString.add(2,getResources().getString(R.string.nfc_read_huanhao) +": "+mRFIDLiuCheng.cShengchanID);
        mString.add(3,getResources().getString(R.string.nfc_read_fenkuaihao) +": "+mRFIDLiuCheng.cFenkuaihao);
        mString.add(4,getResources().getString(R.string.liucheng_caozuoren) +": "+mRFIDLiuCheng.cLCpreson);
        mString.add(5,getResources().getString(R.string.liucheng_yanshoujielun) +": "+mRFIDLiuCheng.cLCreslut);
        mString.add(6,getResources().getString(R.string.liucheng_chulifangshi) +": "+mRFIDLiuCheng.cLCchuli);
        if (mRFIDLiuCheng.cLCtext.length() > 20) {
            String cLCtext = String.copyValueOf(mRFIDLiuCheng.cLCtext.toCharArray(),0,20)+ "...";
            mString.add(7, getResources().getString(R.string.liucheng_wenzishuoming) + ": " + cLCtext);
        }else{
            mString.add(7, getResources().getString(R.string.liucheng_wenzishuoming) + ": " + mRFIDLiuCheng.cLCtext);
        }
        mString.add(8,getResources().getString(R.string.liucheng_querenshijian) +": "+mRFIDLiuCheng.cLCtime.toString());
        mString.add(9,getResources().getString(R.string.liucheng_image));
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    private void updateList_4()
    {
        ArrayList<String> mString = new ArrayList<String>();
        mString.add(0,mRFIDCaiJiWenDu.cXLDnode.toString());
        mString.add(1,getResources().getString(R.string.wendu_time) +": "+mRFIDCaiJiWenDu.dCaiJI.toString());
        for (int i = 0; i < 16; i++)
        {
            mString.add(i+2,"   "+getResources().getString(R.string.wendu_wendu) +String.valueOf(i+1)+": "+mRFIDCaiJiWenDu.nTongDao.get(i).toString());
        }
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    private void updateList_3()
    {
        if(mRFIDCaiJiWenDuList.isEmpty())
        {
            return;
        }
        int j = 0;
        ArrayList<String> mString = new ArrayList<String>();
        for(int i = mRFIDCaiJiWenDuList.size(); i > 0; i--)
        {
            RFIDCaiJiWenDu mRFIDCaiJiWenDu = mRFIDCaiJiWenDuList.get(i-1);
            mString.add(j,mRFIDCaiJiWenDu.dCaiJI.toString()+" "+mRFIDCaiJiWenDu.cXLDnode.toString()+" ...");
            j++;
        }
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    private void updateList_5(){
        if(mRFIDPeiHeBiList.isEmpty())
        {
            return;
        }
        int j = 0;
        ArrayList<String> mString = new ArrayList<String>();
        for(int i = mRFIDPeiHeBiList.size(); i > 0; i--)
        {
            RFIDPeiHeBi mRFIDPeiHeBi = mRFIDPeiHeBiList.get(i-1);
            //mString.add(j,mRFIDPeiHeBi.dCaiJI.toString()+" "+mRFIDPeiHeBi.cPHBnode+" ...");
            mString.add(j,getResources().getString(R.string.peihebi_peihebidanhao) +": "+mRFIDPeiHeBi.cPHBnode+" ...");
            j++;
        }
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    private void updateList_6()
    {
        ArrayList<String> mString = new ArrayList<String>();
        mString.add(0,getResources().getString(R.string.peihebi_jiaobantaihao) +": "+mRFIDPeiHeBi.cJBLnode);
//        mString.add(1,getResources().getString(R.string.peihebi_caijishijian) +": "+mRFIDPeiHeBi.dCaiJI.toString());
        mString.add(1,getResources().getString(R.string.peihebi_xialiaodanhao) +": "+mRFIDPeiHeBi.cXLDnode);
        mString.add(2,getResources().getString(R.string.peihebi_peihebidanhao) +": "+mRFIDPeiHeBi.cPHBnode);
        mString.add(3,getResources().getString(R.string.peihebi_shuini) +": "+mRFIDPeiHeBi.nShuiNi);
        mString.add(4,getResources().getString(R.string.peihebi_fenmeihui) +": "+mRFIDPeiHeBi.nFenMeiHui);
        mString.add(5,getResources().getString(R.string.peihebi_kuanfen) +": "+mRFIDPeiHeBi.nKuangFen);
        mString.add(6,getResources().getString(R.string.peihebi_shi1) +": "+mRFIDPeiHeBi.nShi1);
        mString.add(7,getResources().getString(R.string.peihebi_shi2) +": "+mRFIDPeiHeBi.nShi2);
        mString.add(8,getResources().getString(R.string.peihebi_sha1) +": "+mRFIDPeiHeBi.nSha1);
        mString.add(9,getResources().getString(R.string.peihebi_sha2) +": "+mRFIDPeiHeBi.nSha2);
        mString.add(10,getResources().getString(R.string.peihebi_waijiaji1) +": "+mRFIDPeiHeBi.nWaiJiaJi1);
        mString.add(11,getResources().getString(R.string.peihebi_waijiaji2) +": "+mRFIDPeiHeBi.nWaiJiaJi2);
        mString.add(12,getResources().getString(R.string.peihebi_waijiaji3) +": "+mRFIDPeiHeBi.nWaiJiaJi3);
        mString.add(13,getResources().getString(R.string.peihebi_shuiyongliang) +": "+mRFIDPeiHeBi.nShuii);
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,mString);
        mListView.setAdapter(mAdapter);
    }

    private void get_image()
    {
        mMainClient.GetRFIDLiuchengFile(mRFIDLiuCheng.cliucheng_UUID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainClient.MainClientStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(mPage)
        {
            case PAGE_LIUCHENG_MAIN:
                Page1OnItemClick(position);
                break;

            case PAGE_LIUCHENG_PAGE1:
                Page2OnItemClick(position);
                break;

            case PAGE_YAOHAO_MAIN:
                Page3OnItemClick(position);
                break;

            case PAGE_PEIHEBI_MAIN:
                PagePeiHeBinItemClick(position);
                break;

            default:
                break;
        }
    }

    public void Page1OnItemClick(int postion)
    {
        int index = mRFIDLiuChengList.size()-postion-1;
        mRFIDLiuCheng = mRFIDLiuChengList.get(index);
        mHandler.sendEmptyMessage(PAGE_LIUCHENG_PAGE1);
    }

    public void Page2OnItemClick(int postion)
    {
        if(postion == 7){
            choose_wenzishuru();
        }else if (postion == 9){
            get_image();
        }
    }

    public void Page3OnItemClick(int postion)
    {
        int index = mRFIDCaiJiWenDuList.size()-postion-1;
        mRFIDCaiJiWenDu = mRFIDCaiJiWenDuList.get(index);
        mHandler.sendEmptyMessage(PAGE_YAOHAO_PAGE1);
    }

    public void PagePeiHeBinItemClick(int postion)
    {
        int index = mRFIDPeiHeBiList.size()-postion-1;
        mRFIDPeiHeBi = mRFIDPeiHeBiList.get(index);
        mHandler.sendEmptyMessage(PAGE_PEIHEBI_PAGE1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(mPage == PAGE_LIUCHENG_MAIN || mPage == PAGE_YAOHAO_MAIN|| mPage == PAGE_PEIHEBI_MAIN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
            }
            return false;
        }else if(mPage == PAGE_LIUCHENG_PAGE1) {
            mHandler.sendEmptyMessage(PAGE_LIUCHENG_MAIN);
            return true;
        }else if(mPage == PAGE_YAOHAO_PAGE1) {
            mHandler.sendEmptyMessage(PAGE_YAOHAO_MAIN);
            return true;
        }else if(mPage == PAGE_PEIHEBI_PAGE1) {
            mHandler.sendEmptyMessage(PAGE_PEIHEBI_MAIN);
            return true;
        }

        return true;
    }

    public void choose_wenzishuru()
    {
        mEditText = new EditText(this);
        mEditText.setHeight(500);
        mEditText.setInputType(InputType.TYPE_NULL);
        mEditText.setGravity(Gravity.TOP);
        mEditText.setSingleLine(false);
        mEditText.setHorizontallyScrolling(false);
        mEditText.setText(mRFIDLiuCheng.cLCtext);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        new  AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.liucheng_wenzishuoming) )
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(mEditText)
                .show();
    }

    @Override
    public void OnResponsionReport(int Cmd, Bundle Data, int DataLen) {

        Message msg = new Message();
        msg.what = Cmd;
        if(DataLen > 0){
            Serializable mSerializable = Data.getSerializable(IntentDef.INTENT_COMM_DATA);
            if(mSerializable != null){
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(IntentDef.INTENT_COMM_DATA,mSerializable);
                msg.setData(mBundle);
            }
        }
        nlog.Info("ViewLiucheng ================ OnResponsionReport ["+Cmd+"]");
        mHandler.sendMessage(msg);
    }

    @Override
    public void OnDistributeReport(int Cmd, Bundle Data, int DataLen) {
    }
}
