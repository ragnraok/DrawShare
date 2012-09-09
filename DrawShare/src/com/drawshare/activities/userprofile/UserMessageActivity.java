package com.drawshare.activities.userprofile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseActivity;
import com.drawshare.adapter.FollowMessageAdapter;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.FollowMessageNetRenderer;
import com.drawshare.render.object.FollowMessage;
import com.drawshare.util.DrawShareUtil;

public class UserMessageActivity extends BaseActivity {

	private ListView listView = null;
	private ProgressBar progressBar = null;
	
	private Handler handler = new Handler();
	private ArrayList<FollowMessage> messageList = null;
	private FollowMessageAdapter adapter = null;
	
	private ProgressDialog progressDialog = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        findAllView();
        setUpView();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_message, menu);
        return true;
    }
    */

	@Override
	protected void findAllView() {
		// TODO Auto-generated method stub
		super.findAllView();
		this.listView = (ListView) findViewById(R.id.user_message_list);
		this.progressBar = (ProgressBar) findViewById(R.id.user_message_progress_bar);
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		super.setUpView();
		if (this.application.getNetworkState()) {
			//progressDialog = DrawShareUtil.getWaitProgressDialog(this);
			handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (msg.what == 1) {
						//progressDialog.dismiss();
						if ((ArrayList<FollowMessage>) msg.obj != null) {
							listView.setVisibility(View.VISIBLE);
							progressBar.setVisibility(View.INVISIBLE);
							adapter = new FollowMessageAdapter(UserMessageActivity.this, 
									listView, (ArrayList<FollowMessage>) msg.obj, true);
							listView.setAdapter(adapter);
						}
					}
				}
			};
			new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					FollowMessageNetRenderer renderer = new FollowMessageNetRenderer(UserIdHandler.getUserId(UserMessageActivity.this), 
							ApiKeyHandler.getApiKey(UserMessageActivity.this), 30);
					ArrayList<FollowMessage> messageList = null;
					try {
						messageList = renderer.renderToList();
						//return messageList;
					} catch (AuthFailException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					} catch (UserNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (PictureNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = messageList;
					handler.sendMessage(msg);
				}
			}).start();
			/*
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserMessageTask task = new UserMessageTask();
					try {
						messageList = task.execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (messageList != null) {
						// set the adapter
						listView.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.INVISIBLE);
						adapter = new FollowMessageAdapter(UserMessageActivity.this, 
								listView, messageList, true);
						listView.setAdapter(adapter);
					}
					progressDialog.dismiss();
				}
			}, 500);*/
		}
		else {
			Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (this.adapter != null)
			this.adapter.stopLoad();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null)
			this.adapter.resumeLoad();
	}

	@Override
	protected void setViewAction() {
		// TODO Auto-generated method stub
		super.setViewAction();
	}
	/*
	private class UserMessageTask extends AsyncTask<Void, Integer, ArrayList<FollowMessage>> {

		@Override
		protected ArrayList<FollowMessage> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//return null;
			FollowMessageNetRenderer renderer = new FollowMessageNetRenderer(UserIdHandler.getUserId(UserMessageActivity.this), 
					ApiKeyHandler.getApiKey(UserMessageActivity.this), 30);
			try {
				ArrayList<FollowMessage> messageList = renderer.renderToList();
				return messageList;
			} catch (AuthFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PictureNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}*/
}
