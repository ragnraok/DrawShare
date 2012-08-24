package com.drawshare.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.activities.userprofile.LoginActivity;
import com.drawshare.activities.userprofile.UserIndexActivity;
import com.drawshare.asyncloader.AsyncImageLoader;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.UserNameHandler;
import com.drawshare.util.DrawShareConstant;

public class SplashScreenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        
        Thread startApp = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean login = ifLogin();
				Intent nextIntent = null;
				AsyncImageLoader.cleanRamCache();
				if (login == true) {
					// enter the userprofile activity
					//nextIntent = new Intent(SplashScreenActivity.this, HotestPictureActivity.class);
					nextIntent = new Intent(SplashScreenActivity.this, UserIndexActivity.class);
					nextIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, UserIdHandler.getUserId(SplashScreenActivity.this));
					Log.d(Constant.LOG_TAG, "the userid is " + UserIdHandler.getUserId(SplashScreenActivity.this) + 
							", the apikey is " + ApiKeyHandler.getApiKey(SplashScreenActivity.this) + ", the username is " +
							UserNameHandler.getUserName(SplashScreenActivity.this));
				}
				else {
					//nextIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
					nextIntent = new Intent(SplashScreenActivity.this, HotestPictureActivity.class);
					//nextIntent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
				}
				startActivity(nextIntent);
				finish();
			}
        	
        });
        startApp.start();
    }
    
    private boolean ifLogin() {
    	String userId = UserIdHandler.getUserId(this);
    	String apiKey = ApiKeyHandler.getApiKey(this);
    	String username = UserNameHandler.getUserName(this);
    	if (userId == null || apiKey == null || username == null) {
    		//Toast.makeText(SplashScreenActivity.this, "还没登录", Toast.LENGTH_LONG).show();
    		return false;
    	}
    	else {
    		//Toast.makeText(SplashScreenActivity.this, "已经登录", Toast.LENGTH_LONG).show();
			//Toast.makeText(SplashScreenActivity.this,  "the userid is " + UserIdHandler.getUserId(SplashScreenActivity.this) + 
			//		", the apikey is " + ApiKeyHandler.getApiKey(SplashScreenActivity.this), Toast.LENGTH_LONG).show();
			return true;
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
        return true;
    }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(Constant.LOG_TAG, "on stop the slapshScreen");
		AsyncImageLoader.cleanRamCache();
		super.onStop();
	}
    
    
}
