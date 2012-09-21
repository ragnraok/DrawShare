package com.drawshare.activities.base;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
	
	private ViewPager viewPager = null;
	private ScheduledExecutorService functionIntroExecutorService = null;
	private Handler functionPagerHandler = null;
	
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
		this.viewPager = (ViewPager) findViewById(R.id.hotest_pict_function_intro_view_pager);
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
		
		viewPager.setAdapter(new FunctionIntroPagerAdapter());
		this.functionPagerHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (viewPager != null)
					viewPager.setCurrentItem(msg.what);
				super.handleMessage(msg);
			}	
		};
	}
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		functionIntroExecutorService = Executors.newSingleThreadScheduledExecutor();
		functionIntroExecutorService.scheduleAtFixedRate(new Runnable() {
			
			public void run() {
				synchronized (viewPager) {
					functionPagerHandler.sendEmptyMessage((viewPager.getCurrentItem() + 1) % 4);
				}
			}
		}, 5, 5, TimeUnit.SECONDS);
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
			finish();
		}
	}
	
	@Override
	protected void onStop() {
		this.functionIntroExecutorService.shutdown();
		super.onStop();
	}
	
	private class FunctionIntroPagerAdapter extends PagerAdapter {

		private Bitmap[] bitmapList = new Bitmap[] {
				BitmapFactory.decodeResource(getResources(), R.drawable.function_intro_1),
				BitmapFactory.decodeResource(getResources(), R.drawable.function_intro_2),
				BitmapFactory.decodeResource(getResources(), R.drawable.function_intro_3),
				BitmapFactory.decodeResource(getResources(), R.drawable.function_intro_4)
		};
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bitmapList.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			ImageView imageView = new ImageView(HotestPictureActivity.this);
			imageView.setImageBitmap(bitmapList[position]);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			((ViewPager) container).addView(imageView);
			return imageView;
		}
		
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
		
	}

}