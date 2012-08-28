package com.drawshare.activities.base;

import java.util.ArrayList;

import android.R.anim;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.drawshare.R;
import com.drawshare.application.DrawShareApplication;

public abstract class BaseFragmentActivity extends FragmentActivity {

	public DrawShareApplication application = null;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.application = (DrawShareApplication) this.getApplication();
		this.getWindow().setBackgroundDrawableResource(R.drawable.background_color); // set the background color(white)
		super.onCreate(bundle);
		
	}
}
