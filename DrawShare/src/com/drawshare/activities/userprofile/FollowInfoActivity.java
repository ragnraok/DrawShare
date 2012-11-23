package com.drawshare.activities.userprofile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseActivity;
import com.drawshare.adapter.FollowInfoAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.render.netRenderer.UserFollowNetRenderer;
import com.drawshare.render.object.User;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class FollowInfoActivity extends BaseActivity {

	private ListView listView = null;
	private String userId = null;
	private boolean ifGetFollower = false;
	//private AlertDialog dialog = null;
	private Handler handler = null;
	private ProgressDialog progressDialog = null;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_info);
        findAllView();
        setUpView();
    }
	
    @Override
	protected void findAllView() {
		// TODO Auto-generated method stub
		super.findAllView();
		this.listView = (ListView) findViewById(R.id.follow_info_list);
		
		this.userId = this.getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.USER_ID);
		this.ifGetFollower = this.getIntent().getBooleanExtra(DrawShareConstant.EXTRA_KEY.IF_GET_FOLLOWERS, false);
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		super.setUpView();
		if (!this.application.getNetworkState()) {
			Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
		else {
			//dialog = new AlertDialog.Builder(this).setTitle("Please Wait...").setView(DrawShareUtil.getWaitDialogView(this)).create();
			//final FollowInfoTask task = new FollowInfoTask();
			//dialog.show();
			/*
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						ArrayList<User> userList = task.execute().get();
						if (userList != null) {
							FollowInfoAdapter adapter = new FollowInfoAdapter(FollowInfoActivity.this, listView, 
									userList, FollowInfoActivity.this.application.getNetworkState());
							listView.setAdapter(adapter);
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
			}, 500);*/
			progressDialog = DrawShareUtil.getWaitProgressDialog(this);
			progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						progressDialog.dismiss();
						finish();
						return true;
					}
					return false;
				}
			});
			handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (msg.what == 1) {
						progressDialog.dismiss();
						ArrayList<User> userList = (ArrayList<User>) msg.obj;
						if (userList != null) {
							FollowInfoAdapter adapter = new FollowInfoAdapter(FollowInfoActivity.this, listView, 
									userList, FollowInfoActivity.this.application.getNetworkState());
							listView.setAdapter(adapter);
						}
					}
				}
				
			};
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserFollowNetRenderer renderer = new UserFollowNetRenderer(userId, ifGetFollower);
					ArrayList<User> data = null;
					try {
						data  = renderer.renderToList();
					} catch (AuthFailException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						data = null;
					} catch (UserNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						data = null;
					}
					Message message = handler.obtainMessage();
					message.what = 1;
					message.obj = data;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	@Override
	protected void setViewAction() {
		// TODO Auto-generated method stub
		super.setViewAction();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_follow_info, menu);
        return true;
    }
    
    /*
    private class FollowInfoTask extends AsyncTask<Void, Integer, ArrayList<User>> {

    	private ArrayList<User> data = null;
    	
		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			UserFollowNetRenderer renderer = new UserFollowNetRenderer(userId, ifGetFollower);
			try {
				data  = renderer.renderToList();
				return data;
			} catch (AuthFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				data = null;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				data = null;
			}
			return null;
		}
    	
    }*/
}
