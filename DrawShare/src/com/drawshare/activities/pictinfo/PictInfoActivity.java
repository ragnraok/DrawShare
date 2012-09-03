package com.drawshare.activities.pictinfo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.base.BaseFragmentActivity;
import com.drawshare.activities.userprofile.OtherUserIndexActivity;
import com.drawshare.adapter.TabsAdapter;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class PictInfoActivity extends BaseFragmentActivity implements OnTabChangeListener, OnClickListener {

	private String userId = null;
	private String pictId = null;
	private String avatarURL = null;
	
	private TabHost tabHost = null;
	private ViewPager viewPager = null;
	private TabsAdapter tabsAdapter = null;
	private Button drawButton = null;
	private ImageView avatarImageView = null;
	
	private AlertDialog dialog = null;
	private Handler handler = new Handler();
	
	private ArrayList<Drawable> notSelectDrawables = new ArrayList<Drawable>();
	private ArrayList<Drawable> selectDrawables = new ArrayList<Drawable>();
	private ArrayList<String> tabIdList = new ArrayList<String>();
	private ArrayList<View> viewList = new ArrayList<View>();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pict_info);
        this.userId = getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.USER_ID);
        this.pictId = getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.PICT_ID);
        findAllView();
        dialog = new AlertDialog.Builder(this).setTitle("Please Wait...").setView(DrawShareUtil.getWaitDialogView(this)).create();
        
        tabHost.setup();
        tabsAdapter = new TabsAdapter(this, tabHost, viewPager);
        initDrawables();
        initTabIdList();
        tabsAdapter.addTab(tabHost.newTabSpec(tabIdList.get(0)).setIndicator(generateTabIndicatorView(0)), 
        		PictShowFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabIdList.get(1)).setIndicator(generateTabIndicatorView(1)), 
        		PictCommentsFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabIdList.get(2)).setIndicator(generateTabIndicatorView(2)), 
        		PictForkVersionFragment.class, null);
        
        this.viewList.get(0).setBackgroundDrawable(selectDrawables.get(0));
        this.tabHost.setCurrentTab(0);
        this.tabHost.setOnTabChangedListener(this);
        setUpView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pict_info, menu);
        return true;
    }
    
    private void findAllView() {
    	this.avatarImageView = (ImageView) findViewById(R.id.pict_info_avatar_image);
    	this.drawButton = (Button) findViewById(R.id.pict_info_draw_button);
    	
    	tabHost = (TabHost) findViewById(android.R.id.tabhost);   
        viewPager = (ViewPager) findViewById(R.id.pict_info_view_pager);
    }
    
    private void initDrawables() {
    	Resources resources = getResources();
    	this.notSelectDrawables.add(resources.getDrawable(R.drawable.pict_show_1));
    	this.notSelectDrawables.add(resources.getDrawable(R.drawable.pict_comment_1));
    	this.notSelectDrawables.add(resources.getDrawable(R.drawable.edit_history_1));
    	
    	this.selectDrawables.add(resources.getDrawable(R.drawable.pict_show_2));
    	this.selectDrawables.add(resources.getDrawable(R.drawable.pict_comment_2));
    	this.selectDrawables.add(resources.getDrawable(R.drawable.edit_history_2));
    }
    
    private void initTabIdList() {
    	this.tabIdList.add("pict_show");
    	this.tabIdList.add("pict_comment");
    	this.tabIdList.add("pict_edit_history");
    }
    
    private View generateTabIndicatorView(int index) {
    	LayoutInflater inflater = this.getLayoutInflater();
    	View view = inflater.inflate(R.layout.tab_indicator_view, null, false);
    	//view.setBackgroundDrawable(this.selectDrawables.get(index));	
    	view.setBackgroundDrawable(notSelectDrawables.get(index));
    	viewList.add(view);
    	
    	return view;
    }

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < viewList.size(); i++) {
			this.viewList.get(i).setBackgroundDrawable(notSelectDrawables.get(i));
		}
		int index = this.tabIdList.indexOf(tabId);
		this.viewList.get(index).setBackgroundDrawable(selectDrawables.get(index));
		this.viewPager.setCurrentItem(index);
	}
	
	private void setUpView() {
		if (this.application.getNetworkState()) {
			final UserProfileTask task = new UserProfileTask();
			dialog.show();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Bitmap bitmap = task.execute().get();
						if (bitmap != null) {
							avatarImageView.setImageBitmap(bitmap);
							Log.d(Constant.LOG_TAG, "set the other avatar");
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dialog.dismiss();
				}
			}, 500);
		}
		else {
			Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
		this.drawButton.setOnClickListener(this);
		this.avatarImageView.setOnClickListener(this);
	}
	
	private void getAndSetUserProfile() {
    	JSONObject profileObject;
		try {
			profileObject = UserProfile.getProfile(this.userId);
			//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
			if (profileObject != null) {
				//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
				this.avatarURL = profileObject.getString("avatar_url");
				//this.username = profileObject.getString("username");
				//this.username = username.substring(0, 1).toUpperCase() + username.substring(1);
			}
		} catch (UserNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private class UserProfileTask extends AsyncTask<Void, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getAndSetUserProfile();
			if (avatarURL != null) {
				Bitmap avatar = Util.urlToBitmap(avatarURL, DrawShareConstant.USER_INDEX_AVATAR_SIZE);
				if (avatar != null) {
					return avatar;
				}
				else {
					return null;
				}
			}
			return null;
		}
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pict_info_avatar_image:
			Intent intent = new Intent(this, OtherUserIndexActivity.class);
			intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, this.userId);
			intent.putExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, false);
			startActivity(intent);
			break;
		case R.id.pict_info_draw_button:
			// go to draw panel..
		}
	}
}
