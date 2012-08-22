package com.drawshare.activities.userprofile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.activities.base.BaseFragmentActivity;
import com.drawshare.adapter.TabsAdapter;
import com.drawshare.asyncloader.AsyncImageLoader;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.UserNameHandler;
import com.drawshare.util.DrawShareConstant;

public class UserIndexActivity extends BaseFragmentActivity implements OnTabChangeListener {
	
	private TabHost tabHost = null;
	private ViewPager viewPager = null;
	private TabsAdapter tabsAdapter = null;
	
	private ImageView avatarImage = null;
	private TextView userNameTextView = null;
	private Button messageButton = null;
	private Button messageRemindButton = null;
	private TextView messageNumTextView = null;
	
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<Drawable> drawablesList = new ArrayList<Drawable>();
	
	private ArrayList<Drawable> notSelectDrawables = new ArrayList<Drawable>();
	private ArrayList<Drawable> selectoDrawables = new ArrayList<Drawable>();
	private ArrayList<String> tabTagList = new ArrayList<String>();
	
	private Handler handler = new Handler();
	private AlertDialog dialog = null;
	
	private AsyncTask<Void, Void, Bitmap> bitmap = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);
       
        findAllView();
        
        tabHost.setup();
        dialog = new AlertDialog.Builder(this).setTitle("Please Wait...").setView(getWaitDialogView()).create();
        tabsAdapter = new TabsAdapter(this, tabHost, viewPager);
        
        initDrawableList();
        initTabTagList();
        
        tabsAdapter.addTab(tabHost.newTabSpec(tabTagList.get(0)).
        		setIndicator(generateIndicatorView(0)), FriendsNewFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabTagList.get(1)).
        		setIndicator(generateIndicatorView(1)), DrawHallFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabTagList.get(2)).
        		setIndicator(generateIndicatorView(2)), UserPictsFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabTagList.get(3)).
        		setIndicator(generateIndicatorView(3)), UserCollectionFragment.class, null);
        
        this.viewList.get(0).setBackgroundDrawable(selectoDrawables.get(0));
        this.tabHost.setCurrentTab(0);
        
        tabHost.setOnTabChangedListener(this);
        setUpView();
    }
    
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_index, menu);
        return true;
    }
*/
    private void findAllView() {
    	tabHost = (TabHost) findViewById(android.R.id.tabhost);   
        viewPager = (ViewPager) findViewById(R.id.user_index_view_pager);
        avatarImage = (ImageView) findViewById(R.id.user_index_avatar);
    	userNameTextView = (TextView) findViewById(R.id.user_index_username_text);
    	messageRemindButton = (Button) findViewById(R.id.user_index_message_remind_button);
    	messageNumTextView = (TextView) findViewById(R.id.user_index_message_num_text);
    }
    
    private void setUpView() {
    	String username = UserNameHandler.getUserName(this);
    	username = username.substring(0, 1).toUpperCase() + username.substring(1);
    	
    	if (this.application.getNetworkState()) {
    		final ProfileTask profileTask = new ProfileTask();
    		dialog.show();
    		handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					bitmap = profileTask.execute();
					try {
						Bitmap avatar = bitmap.get();
						avatarImage.setImageBitmap(avatar);
						Log.d(Constant.LOG_TAG, "set the avatar");
						dialog.dismiss();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 500);
    	}
    	else {
    		Toast.makeText(this, this.getResources().getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
    	}
    }
    
   
    private void initDrawableList() {
    	Resources resource = this.getResources();
    	drawablesList.add(resource.getDrawable(R.drawable.friends_new_selector));
    	drawablesList.add(resource.getDrawable(R.drawable.draw_hall_selector));
    	drawablesList.add(resource.getDrawable(R.drawable.user_picts_selector));
    	drawablesList.add(resource.getDrawable(R.drawable.collect_pict_selector));
    	
    	notSelectDrawables.add(resource.getDrawable(R.drawable.friends_news_1));
    	notSelectDrawables.add(resource.getDrawable(R.drawable.draw_hall_1));
    	notSelectDrawables.add(resource.getDrawable(R.drawable.user_picts_1));
    	notSelectDrawables.add(resource.getDrawable(R.drawable.collect_pict_1));
    	
    	selectoDrawables.add(resource.getDrawable(R.drawable.friends_news_2));
    	selectoDrawables.add(resource.getDrawable(R.drawable.draw_hall_2));
    	selectoDrawables.add(resource.getDrawable(R.drawable.user_picts_2));
    	selectoDrawables.add(resource.getDrawable(R.drawable.collect_pict_2));
    }
    
    private void initTabTagList() {
    	this.tabTagList.add("friends_news");
    	this.tabTagList.add("draw_hall");
    	this.tabTagList.add("user_picts");
    	this.tabTagList.add("user_collect");
    }
    
    private View generateIndicatorView(int index) {
    	LayoutInflater inflater = this.getLayoutInflater();
    	View view = inflater.inflate(R.layout.tab_indicator_view, null, false);
    	view.setBackgroundDrawable(this.drawablesList.get(index));
    	
    	viewList.add(view);
    	
    	return view;
    }
    
    private View getWaitDialogView() {
    	LayoutInflater inflater = this.getLayoutInflater();
    	View view = inflater.inflate(R.layout.waiting_dialog, null, false);
    	
    	return view;
    }

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "tab changed", Toast.LENGTH_LONG).show();
		
		//this.drawablesList.set(index, object)
		int index = this.tabTagList.indexOf(tabId);
		
		for (int i = 0; i < viewList.size(); i++) {
			this.viewList.get(i).setBackgroundDrawable(this.notSelectDrawables.get(i));
		}
		this.viewList.get(index).setBackgroundDrawable(this.selectoDrawables.get(index));
		
		this.viewPager.setCurrentItem(index);
	}
	
	private String getAvatarURL() {
    	JSONObject profileObject;
		try {
			profileObject = UserProfile.getProfile(UserIdHandler.getUserId(this));
			//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
			if (profileObject != null) {
				//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
				String avatarUrl = profileObject.getString("avatar_url");
				return avatarUrl;
			}
		} catch (UserNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	private class ProfileTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//Log.d(Constant.LOG_TAG, "in doInBackground");
			String avatarURL = getAvatarURL();
			//Log.d(Constant.LOG_TAG, "the avatarURL is " + avatarURL);
			
			Bitmap avatar = Util.urlToBitmap(avatarURL, DrawShareConstant.USER_INDEX_AVATAR_SIZE);
			if (avatar != null) {
				Log.d(Constant.LOG_TAG, "get the avatar");
				return avatar;
			}
			else {
				//Log.d(Constant.LOG_TAG, "the avatar is null");
				return null;
			}
			
		}
		
	}
}
