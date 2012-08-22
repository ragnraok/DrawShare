package com.drawshare.activities.base;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.activities.userprofile.LoginActivity;
import com.drawshare.activities.userprofile.RegisterActivity;
import com.drawshare.adapter.HotestPictAdapter;
import com.drawshare.render.cacheRenderer.HotestPictCacheRenderer;
import com.drawshare.render.netRenderer.HotestPictNetRenderer;
import com.drawshare.render.object.Picture;

public class HotestPictureActivity extends BaseActivity implements OnClickListener {

	private GridView gridView = null;
	private Button loginButton = null;
	private Button registerButton = null;
	ArrayList<Picture> pictList = null;
	private ProgressDialog dialog = null;
	private Handler handler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotest_picture);
       
        findAllView();
        setUpView();
        setViewAction();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hotest_picture, menu);
        return true;
    }
*/
	@Override
	protected void findAllView() {
		// TODO Auto-generated method stub
		super.findAllView();
		this.registerButton = (Button) findViewById(R.id.hotest_pict_register_button);
		this.loginButton = (Button) findViewById(R.id.hotest_pict_login_button);
		this.gridView = (GridView) findViewById(R.id.hotest_pict_pict_grid);
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		super.setUpView();
		Resources resources = this.getResources();
		boolean netSatus = this.application.getNetworkState();
		// first get the json
		if (netSatus == true) {
			HotestPictNetRenderer renderer = new HotestPictNetRenderer(10);
			pictList = renderer.renderToList();
			//loadHotestPict();
			Log.d(Constant.LOG_TAG, "render from net");
		}
		else {
			Toast.makeText(this, resources.getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
			HotestPictCacheRenderer renderer = new HotestPictCacheRenderer();
			pictList = renderer.renderToList();
			Log.d(Constant.LOG_TAG, "render from cache");
		}
		if (pictList == null) {
			Log.d(Constant.LOG_TAG, "the pictList is null");
			pictList = new ArrayList<Picture>(10);
			setPictListSize(pictList);
		}
		if (pictList.size() < 10) {
			setPictListSize(pictList);
		}
		Log.d(Constant.LOG_TAG, "the pictList size is " + pictList.size());
		// then create an adapter
		HotestPictAdapter adapter = new HotestPictAdapter(this, gridView, pictList, this.application.getNetworkState());
		// set the gridView's adapter
		gridView.setAdapter(adapter);
		
	}
	
	private void loadHotestPict() {
		Resources resources = this.getResources();
		dialog = ProgressDialog.show(this, resources.getString(R.string.waiting_title), 
				resources.getString(R.string.waiting_title));
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HotestPictNetRenderer renderer = new HotestPictNetRenderer(10);
				HotestPictureActivity.this.pictList = renderer.renderToList();
				Log.d(Constant.LOG_TAG, "get the render list");
				dialog.dismiss();
			}
			
		});
	}
	
	private void setPictListSize(ArrayList<Picture> pictList) {
		Log.d(Constant.LOG_TAG, "set the pictList size");
		while (pictList.size() < 10) {
			pictList.add(new Picture());
		}
	}

	@Override
	protected void setViewAction() {
		// TODO Auto-generated method stub
		super.setViewAction();
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		Intent nextIntent = null;
		switch (viewId) {
		case R.id.hotest_pict_login_button:
			nextIntent = new Intent(this, LoginActivity.class);
			break;
		case R.id.hotest_pict_register_button:
			nextIntent = new Intent(this, RegisterActivity.class);
			break;
		}
		if (nextIntent != null) {
			this.startActivity(nextIntent);
		}
	}
}
