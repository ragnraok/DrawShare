package com.drawshare.application;

import com.drawshare.Request.Constant;
import com.drawshare.asyncloader.AsyncImageLoader;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DrawShareApplication extends Application {
	
	private boolean ifNetAvailable = false;
	
	private void detectNetState() {
		ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (manager == null) {
			this.ifNetAvailable = false;
			return;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		if (networkInfo == null || !networkInfo.isAvailable()) {
			this.ifNetAvailable = false;
			return;
		}
		this.ifNetAvailable = true;
	}
	
	public boolean getNetworkState() {
		detectNetState(); // first redetect the network state then get the variable
		return this.ifNetAvailable;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.detectNetState();
		 Log.d("Ragnarok", "the network is " + getNetworkState());
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		AsyncImageLoader.cleanRamCache();
		Log.d(Constant.LOG_TAG, "onTerminate clear the ram cache");
	}
	
}
