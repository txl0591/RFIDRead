package com.coresoft.rfidread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.coresoft.client.MainClient;
import com.coresoft.database.SysDBHelper;

/**
 * Created by Tangxl on 2017/5/29.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText login_edit_user = null;
    private EditText login_edit_name = null;
    private EditText login_edit_ltd = null;
    private Spinner login_edit_type = null;
    private EditText login_edit_userid = null;

    private Button login_in = null;
    private Button login_out = null;
    private MainClient mainClient = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainClient = new MainClient(this);
        login_edit_user = (EditText)findViewById(R.id.login_edit_user);
        login_edit_name = (EditText)findViewById(R.id.login_edit_name);
        login_edit_ltd = (EditText)findViewById(R.id.login_edit_ltd);
        login_edit_type = (Spinner)findViewById(R.id.login_edit_type);
        login_edit_userid = (EditText)findViewById(R.id.login_edit_userid);

        login_in = (Button)findViewById(R.id.login_in);
        login_in.setOnClickListener(this);
        login_out = (Button)findViewById(R.id.login_out);
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
            case R.id.login_in:
                SysDBHelper.SetLoginInState(true);
                finish();
                break;

            case R.id.login_out:
                finish();
                break;
        }
    }
}
