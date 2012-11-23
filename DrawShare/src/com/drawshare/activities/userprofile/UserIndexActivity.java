package com.drawshare.activities.userprofile;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.message.Message;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.base.BaseFragmentActivity;
import com.drawshare.activities.editpict.DrawActivity;
import com.drawshare.adapter.TabsAdapter;
import com.drawshare.asyncloader.AsyncImageLoader;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.UserNameHandler;
import com.drawshare.render.object.User;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class UserIndexActivity extends BaseFragmentActivity implements OnTabChangeListener, OnClickListener,
	LoaderCallbacks<User> {
	
	private TabHost tabHost = null;
	private ViewPager viewPager = null;
	private TabsAdapter tabsAdapter = null;
	
	private ImageView avatarImage = null;
	private TextView userNameTextView = null;
	private Button messageButton = null;
	private Button messageRemindButton = null;
	private TextView messageNumTextView = null;
	private Button drawButton = null;
	
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<Drawable> drawablesList = new ArrayList<Drawable>();
	
	private ArrayList<Drawable> notSelectDrawables = new ArrayList<Drawable>();
	private ArrayList<Drawable> selectoDrawables = new ArrayList<Drawable>();
	private ArrayList<String> tabTagList = new ArrayList<String>();
	
	private Handler handler = null;
	//private AlertDialog dialog = null;
	private ProgressDialog progressDialog = null;
	
	//private String shortDescription = null;
	//private String email = null;
	//private String avatarURL = null;
	private User user = null;
	private String unreadMessageNum = null;
	
	private AsyncImageLoader avatarLoader = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);
       
        findAllView();
        
        tabHost.setup();
        //dialog = new AlertDialog.Builder(this).setTitle("Please Wait...").setView(DrawShareUtil.getWaitDialogView(this)).create();
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
    
    

	private void findAllView() {
    	tabHost = (TabHost) findViewById(android.R.id.tabhost);   
        viewPager = (ViewPager) findViewById(R.id.user_index_view_pager);
        avatarImage = (ImageView) findViewById(R.id.user_index_avatar);
    	userNameTextView = (TextView) findViewById(R.id.user_index_username_text);
    	messageRemindButton = (Button) findViewById(R.id.user_index_message_remind_button);
    	messageNumTextView = (TextView) findViewById(R.id.user_index_message_num_text);
    	messageButton = (Button) findViewById(R.id.user_index_message_button);
    	drawButton = (Button) findViewById(R.id.user_index_draw_button);
    	

    }
    
    private void setUpView() {
    	String username = UserNameHandler.getUserName(this);
    	username = username.substring(0, 1).toUpperCase() + username.substring(1);
    	this.userNameTextView.setText(username);
    	
    	//progressDialog = DrawShareUtil.getWaitProgressDialog(this);
    	if (this.application.getNetworkState()) {
    		/*
    		handler = new Handler() {

				@Override
				public void handleMessage(android.os.Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (msg.what == 1) {
						progressDialog.dismiss();
						if ((Bitmap) msg.obj != null) {
							avatarImage.setImageBitmap((Bitmap) msg.obj);
						}
						messageNumTextView.setText(String.valueOf(unreadMessageNum));
					}
				}
    			
    		};
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setUnreadMessageNum(); // first set the unread message numbers
					avatarURL = getAvatarURLAndSetOtherProfile();					
					Bitmap avatar = Util.urlToBitmap(avatarURL, DrawShareConstant.USER_INDEX_AVATAR_SIZE);
					
					android.os.Message message = handler.obtainMessage();
					message.what = 1;
					message.obj = avatar;
					handler.sendMessage(message);
				}
			}).start();*/
    		this.avatarLoader = new AsyncImageLoader(true);
    		this.getSupportLoaderManager().initLoader(0, null, this);
    	}
    	else {
    		Toast.makeText(this, this.getResources().getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
    	}
    	
    	this.avatarImage.setOnClickListener(this);
    	this.messageButton.setOnClickListener(this);
    	this.messageRemindButton.setOnClickListener(this);
    	this.drawButton.setOnClickListener(this);
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

	/*
	private String getAvatarURLAndSetOtherProfile() {
    	JSONObject profileObject;
		try {
			profileObject = UserProfile.getProfile(UserIdHandler.getUserId(this));
			//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
			if (profileObject != null) {
				//Log.d(Constant.LOG_TAG,	"the profileObject is " + profileObject.toString());
				String avatarUrl = profileObject.getString("avatar_url");
				this.shortDescription = profileObject.getString("short_description");
				this.email = profileObject.getString("email");
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
    }*/
	
	/*
	private void setUnreadMessageNum() {
		try {
			JSONObject messageObject = Message.getUnreadMessageNum(UserIdHandler.getUserId(this), ApiKeyHandler.getApiKey(this));
			this.unreadMessageNum = String.valueOf(messageObject.getInt("unread_message_number"));
		} catch (AuthFailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.unreadMessageNum = "0";
		} catch (UserNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.unreadMessageNum = "0";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.unreadMessageNum = "0";
		}
		//this.messageNumTextView.setText(this.unreadMessageNum);
	}*/
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_index_avatar:
			Intent profileIntent = new Intent(UserIndexActivity.this, UserProfileActivity.class);
			//profileIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_NAME, UserNameHandler.getUserName(this));
			//profileIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_EMAIL, this.email);
			//profileIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_SHORT_DESCRIPTION, this.shortDescription);
			profileIntent.putExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, true);
			profileIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, UserIdHandler.getUserId(this));
			//profileIntent.putExtra(DrawShareConstant.EXTRA_KEY.AVATAR_URL, this.avatarURL);
			startActivity(profileIntent);
			//finish();
			break;
		case R.id.user_index_message_button:
		case R.id.user_index_message_remind_button:
			Intent messageIntent = new Intent(UserIndexActivity.this, UserMessageActivity.class);
			startActivity(messageIntent);
			break;
		case R.id.user_index_draw_button:
			//Intent drawIntent = new Intent(UserIndexActivity.this, DrawActivity.class);
			//drawIntent.putExtra(DrawShareConstant.EXTRA_KEY.IF_FORK, false);
			//startActivity(drawIntent);
			if (DrawShareUtil.ifLogin(this)) {
				Intent drawIntent = new Intent(UserIndexActivity.this, DrawActivity.class);
				drawIntent.putExtra(DrawShareConstant.EXTRA_KEY.IF_FORK, false);
				startActivity(drawIntent);
			}
			else {
				Toast.makeText(this, getString(R.string.please_login_first), Toast.LENGTH_LONG).show();
			}
			break;
		}
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.application.getNetworkState()) {
			Log.d(Constant.LOG_TAG, "onResume");
			this.getSupportLoaderManager().getLoader(0).reset();
			this.getSupportLoaderManager().getLoader(0).startLoading();
		}
		else {
			Toast.makeText(this, this.getResources().getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}



	@Override
	public Loader<User> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new UserInfoLoader(this);
	}



	@Override
	public void onLoadFinished(Loader<User> loader, User data) {
		// TODO Auto-generated method stub
		this.user = data;
		if (data != null) {
			this.messageNumTextView.setText(data.unreadMsgNum);
			if (data.avatarUrl != null) {
				this.avatarLoader.loadImage(0, data.avatarUrl, new AsyncImageLoader.ImageLoadListener() {
					
					@Override
					public void onImageLoad(Integer rowNum, Bitmap bitmap) {
						// TODO Auto-generated method stub
						avatarImage.setImageBitmap(bitmap);
						Log.d(Constant.LOG_TAG, "set index avatar");
					}
					
					@Override
					public void onError(Integer rowNum) {
						// TODO Auto-generated method stub
						avatarImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_pict_temp));
					}
				}, DrawShareConstant.USER_INDEX_AVATAR_SIZE);
			}
		}
		else {
			Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}



	@Override
	public void onLoaderReset(Loader<User> arg0) {
		// TODO Auto-generated method stub
		this.user = null;
		this.avatarImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_pict_temp));
		this.messageNumTextView.setText("..");
	}
	
	private static class UserInfoLoader extends AsyncTaskLoader<User> {

		private Context context = null;
		private User u = null;
		
		public UserInfoLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
			//this.u = new User();
		}

		@Override
		public User loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			try {
				JSONObject profileObject = UserProfile.getProfile(UserIdHandler.getUserId(context));
				u = new User();
				u.avatarUrl = profileObject.getString("avatar_url");
				try {
					JSONObject messageObject = Message.getUnreadMessageNum(
							UserIdHandler.getUserId(context), ApiKeyHandler.getApiKey(context));
					u.unreadMsgNum = String.valueOf(messageObject.getInt("unread_message_number"));
				} catch (AuthFailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					u.unreadMsgNum = "0";
				} catch (UserNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					u.unreadMsgNum = "0";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					u.unreadMsgNum = "0";
				}
				return u;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				u = null;
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.u = null;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (u != null) {
				this.deliverResult(u);
			}
			else {
				forceLoad();
			}
		}
		
		
	}
	
	
}
