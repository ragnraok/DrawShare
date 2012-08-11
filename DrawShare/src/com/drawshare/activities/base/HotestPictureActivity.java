package com.drawshare.activities.base;

import com.drawshare.R;
import com.drawshare.R.layout;
import com.drawshare.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HotestPictureActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotest_picture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hotest_picture, menu);
        return true;
    }
}
