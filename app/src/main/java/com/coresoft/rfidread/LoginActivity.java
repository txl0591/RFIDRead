package com.coresoft.rfidread;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.coresoft.client.MainClient;
import com.coresoft.database.SysDBHelper;

/**
 * Created by Tangxl on 2017/5/29.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText login_edit_user = null;
    private EditText login_edit_name = null;
    private EditText login_edit_ltd = null;
    private EditText login_edit_type = null;
    private EditText login_edit_userid = null;

    private TextView login_in = null;
    private TextView login_out = null;
    private MainClient mainClient = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainClient = new MainClient(this);
        login_edit_user = (EditText)findViewById(R.id.login_edit_user);
        login_edit_name = (EditText)findViewById(R.id.login_edit_name);
        login_edit_ltd = (EditText)findViewById(R.id.login_edit_ltd);
        login_edit_type = (EditText)findViewById(R.id.login_edit_type);
        login_edit_userid = (EditText)findViewById(R.id.login_edit_userid);

        login_in = (TextView)findViewById(R.id.btn_login);
        login_in.setOnClickListener(this);
        login_out = (TextView)findViewById(R.id.btn_logout);
        login_out.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainClient.MainClientStop();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                SysDBHelper.SetLoginInState(true);
                finish();
                break;

            case R.id.btn_logout:
                finish();
                break;
        }
    }
}
