package com.drawshare.activities.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.drawshare.application.DrawShareApplication;

/**
 * 基础Activity类
 * @author ragnarok
 *
 */
public abstract class BaseActivity extends Activity {
	
	/**
	 * 程序的application对象，在onCreate是创建
	 */
	public DrawShareApplication application = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/**
		 * 所有Activity默认全屏没有标题栏
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		application = (DrawShareApplication) this.getApplication(); // set the application object
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 将所有的控件通过findViewById实例化
	 */
	protected void findAllView() {
		
	}
	
	/**
	 * 
	 */
	protected void setUpView() {
		
	}
	
	/**
	 * 
	 */
	protected void setViewAction() {
		
	}
	
}
