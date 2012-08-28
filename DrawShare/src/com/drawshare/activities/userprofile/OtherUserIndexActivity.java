package com.drawshare.activities.userprofile;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserFollow;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.base.BaseFragmentActivity;
import com.drawshare.adapter.TabsAdapter;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class OtherUserIndexActivity extends BaseFragmentActivity implements OnClickListener, OnTabChangeListener {
	
	private TabHost tabHost = null;
	private ViewPager viewPager = null;
	private TabsAdapter tabsAdapter = null;
	
	private ImageView avatarImage = null;
	private Button followUnfollowButton = null;
	private TextView usernameTextView = null;
	
	private boolean ifFollowed = false; // 用户是否已经关注了此用户
	private String userId = null;
	private boolean ifMyself = false;
	private String avatarURL = null;
	private String username = null;
	
	private AlertDialog dialog = null;
	
	private Handler handler = new Handler();
	
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private ArrayList<Drawable> notSelectDrawables = new ArrayList<Drawable>();
	private ArrayList<Drawable> selectDrawables = new ArrayList<Drawable>();
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<String> tabIdList = new ArrayList<String>();
	
	private static final String LOG_TAG = "otherUserIndex";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_index);
        this.userId = this.getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.USER_ID);
        findAllView();
        dialog = new AlertDialog.Builder(this).setTitle("Please Wait...").setView(DrawShareUtil.getWaitDialogView(this)).create();
        
        tabHost.setup();
        // setup the tab host
        tabsAdapter = new TabsAdapter(this, tabHost, viewPager);
        initTabIdList();
        initDrawableList();
        tabsAdapter.addTab(tabHost.newTabSpec(tabIdList.get(0)).setIndicator(generateTabIndicatorView(0)), 
        		UserPictsFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabIdList.get(1)).setIndicator(generateTabIndicatorView(1)), 
        		UserCollectionFragment.class, null);
        
        
        this.viewList.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_picts_2));
        this.tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        setUpView();
        
        
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_other_user_index, menu);
        return true;
    }
*/
    private void initTabIdList() {
    	tabIdList.add("other_user_picts");
    	tabIdList.add("other_user_collection");
    }
    
    private void initDrawableList() {
    	Resources resources = getResources();
    	notSelectDrawables.add(resources.getDrawable(R.drawable.other_user_picts_1));
    	notSelectDrawables.add(resources.getDrawable(R.drawable.other_user_collection_1));
    	selectDrawables.add(resources.getDrawable(R.drawable.other_user_picts_2));
    	selectDrawables.add(resources.getDrawable(R.drawable.other_user_collection_2));
    	
    	drawables.add(resources.getDrawable(R.drawable.other_user_pict_selector));
    	drawables.add(resources.getDrawable(R.drawable.other_user_collection_selector));
    }
    
    private View generateTabIndicatorView(int index) {
    	LayoutInflater inflater = this.getLayoutInflater();
    	View view = inflater.inflate(R.layout.tab_indicator_view, null, false);
    	//view.setBackgroundDrawable(this.selectDrawables.get(index));
    	
    	if (index == 0) {
    		view.setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_picts_1));
    	}
    	else {
    		view.setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_collection_1));
    	}
  
    	
    	viewList.add(view);
    	
    	return view;
    }
    
    @Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int index = this.tabIdList.indexOf(tabId);
		Log.d(LOG_TAG, String.valueOf(index));
		
		//for (int i = 0; i < viewList.size(); i++) {
		//	this.viewList.get(i).setBackgroundDrawable(this.notSelectDrawables.get(i));
		//	Log.d(LOG_TAG, "i = " + i);
		//}
		//this.viewList.get(index).setBackgroundDrawable(this.selectDrawables.get(index));
		this.viewList.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_picts_1));
		this.viewList.get(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_collection_1));
		
		if (index == 0) {
			Log.d(LOG_TAG, "on other_user_picts");
			this.viewList.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_picts_2));
		}
		else {
			Log.d(LOG_TAG, "on other_user_collection");
			this.viewList.get(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.other_user_collection_2));
		}
		
		this.viewPager.setCurrentItem(index);
	}
    
    private void findAllView() {
    	this.avatarImage = (ImageView) findViewById(R.id.other_user_index_avatar_image);
    	this.usernameTextView = (TextView) findViewById(R.id.other_user_index_username_text);
    	this.followUnfollowButton = (Button) findViewById(R.id.other_user_index_follow_unfollow_button);
    	
    	tabHost = (TabHost) findViewById(android.R.id.tabhost);   
        viewPager = (ViewPager) findViewById(R.id.other_user_index_view_pager);
    }
    
    private void setUpView() {
    	if (this.application.getNetworkState()) {
    		final OtherUserProfileTask task = new OtherUserProfileTask();
    		dialog.show();
    		handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Bitmap bitmap = task.execute().get();
						if (bitmap != null) {
							avatarImage.setImageBitmap(bitmap);
							Log.d(Constant.LOG_TAG, "set the other avatar");
						}
						usernameTextView.setText(username);
						if (ifFollowed) {
							followUnfollowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfollow));
						}
						else {
							followUnfollowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.follow));
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
    	
    	this.followUnfollowButton.setOnClickListener(this);
    	this.avatarImage.setOnClickListener(this);
    }
    
    private void getAndSetUserProfile() {
    	JSONObject profileObject;
		try {
			profileObject = UserProfile.getProfile(this.userId);
			//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
			if (profileObject != null) {
				//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
				this.avatarURL = profileObject.getString("avatar_url");
				this.username = profileObject.getString("username");
				this.username = username.substring(0, 1).toUpperCase() + username.substring(1);
			}
			if (DrawShareUtil.ifLogin(this)) {
				this.ifFollowed = UserProfile.getFollowStatus(UserIdHandler.getUserId(this), this.userId);
			}
			else {
				this.ifFollowed = false;
			}
		} catch (UserNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private class OtherUserProfileTask extends AsyncTask<Void, Integer, Bitmap> {

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
		case R.id.other_user_index_avatar_image:
			Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
			userProfileIntent.putExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, false);
			userProfileIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, this.userId);
			startActivity(userProfileIntent);
			break;
		case R.id.other_user_index_follow_unfollow_button:
			// follow or unfollow
			if (this.application.getNetworkState() == false) {
				Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
			}
			else {
				if (ifFollowed) {
					// unfollow
					unfollowUser();
				}
				else {
					// follow
					followUser();
				}
			}
			break;
		}
	}
	
	private void unfollowUser() {
		final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.waiting_title), "");
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				progressDialog.dismiss();
				if (msg.what == 1 && (Boolean) msg.obj) {
					Toast.makeText(OtherUserIndexActivity.this, "取消关注成功", Toast.LENGTH_LONG).show();
					followUnfollowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.follow));
					ifFollowed = false;
				}
				else {
					Toast.makeText(OtherUserIndexActivity.this, "取消关注失败", Toast.LENGTH_LONG).show();
				}
			}
			
		};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = false;
				try {
					success = UserFollow.unfollowUser(UserIdHandler.getUserId(OtherUserIndexActivity.this),
							userId, ApiKeyHandler.getApiKey(OtherUserIndexActivity.this));
				} catch (AuthFailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					success = false;
				} catch (UserNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					success = false;
				}
				Message message = handler.obtainMessage();
				message.what = 1;
				message.obj = success;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	private void followUser() {
		final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.waiting_title), "");
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				progressDialog.dismiss();
				if (msg.what == 2 && (Boolean) msg.obj) {
					Toast.makeText(OtherUserIndexActivity.this, "关注成功", Toast.LENGTH_LONG).show();
					followUnfollowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfollow));
					ifFollowed = true;
				}
				else {
					Toast.makeText(OtherUserIndexActivity.this, "关注失败", Toast.LENGTH_LONG).show();
				}
			}
			
		};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = false;
				try {
					success = UserFollow.followUser(UserIdHandler.getUserId(OtherUserIndexActivity.this),
							userId, ApiKeyHandler.getApiKey(OtherUserIndexActivity.this));
				} catch (AuthFailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					success = false;
				} catch (UserNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					success = false;
				}
				Message message = handler.obtainMessage();
				message.what = 2;
				message.obj = success;
				handler.sendMessage(message);
			}
		}).start();
	}
}
