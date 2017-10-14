package com.coresoft.rfidread;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Tangxl on 2017/6/11.
 */

public class LiuchengActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liucheng);
        mListView = (ListView) findViewById(R.id.licheng);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
