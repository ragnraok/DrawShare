package com.drawshare.activities.userprofile;

import java.io.File;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.base.BaseActivity;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class UserProfileActivity extends BaseActivity implements OnClickListener, android.content.DialogInterface.OnClickListener {

	private String username = null;
	private String email = null;
	private String shortDescription = null;
	private String avatarURL = null;
	private boolean ifMyself = false;
	private String userId = null;
	
	private TextView usernameTextView = null;
	private TextView emailTextView = null;
	private TextView shortDescriptionTextView = null;
	private TextView avatarTextView = null;
	private ImageView avatarImageView = null;
	private ImageView followedAvatarImageView = null;
	private ImageView followerAvatarImageView = null;
	
	//private AlertDialog dialog = null;
	private ProgressDialog progressDialog = null;
	
	private Handler handler = new Handler();
	
	private EditText descriptionEditText = null;
	
	private static final int SELECT_PICT_CODE  = 1;
	private File tempFile = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.findAllView();
        this.setUpView();
        this.setViewAction();
     
    }

    @Override
	protected void findAllView() {
		// TODO Auto-generated method stub
		super.findAllView();
		this.usernameTextView = (TextView) findViewById(R.id.user_profile_username_text);
		this.emailTextView = (TextView) findViewById(R.id.user_profile_email_text);
		this.shortDescriptionTextView = (TextView) findViewById(R.id.user_profile_intro_text);
		this.avatarTextView = (TextView) findViewById(R.id.user_profile_avatar_text);
		this.avatarImageView = (ImageView) findViewById(R.id.user_profile_avatar_image);
		this.followedAvatarImageView = (ImageView) findViewById(R.id.user_profile_followed_people_avatar_image);
		this.followerAvatarImageView = (ImageView) findViewById(R.id.user_profile_followers_avatar_image);
		
		Intent intent = this.getIntent();
		this.ifMyself = intent.getBooleanExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, false);
		this.userId = intent.getStringExtra(DrawShareConstant.EXTRA_KEY.USER_ID);
				
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		super.setUpView();
		
		
		//dialog = new AlertDialog.Builder(this).setTitle("Please Wait...").setView(DrawShareUtil.getWaitDialogView(this)).create();
		if (this.application.getNetworkState()) {
			final ProfileTask task = new ProfileTask();
			//dialog.show();
			progressDialog = ProgressDialog.show(this, getString(R.string.waiting_title), "");
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Bitmap avatar = task.execute().get();
						if (avatar != null) {
							avatarImageView.setImageBitmap(avatar);
							followedAvatarImageView.setImageBitmap(avatar);
							followerAvatarImageView.setImageBitmap(avatar);
						}
						
						usernameTextView.setText(username);
						emailTextView.setText(email);
						if (shortDescription.length() == 0) {
							shortDescriptionTextView.setText(getResources().getString(R.string.lazy_boy));
							shortDescriptionTextView.setTextColor(Color.GRAY);
						}
						else 
							shortDescriptionTextView.setText(shortDescription);	
						
						//dialog.dismiss();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progressDialog.dismiss();
				}
			}, 500);
		}
		else {
			Toast.makeText(this, this.getResources().getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	
		
		if (ifMyself) {
			this.avatarTextView.setText(this.getResources().getString(R.string.set_user_avatar));
		}
		else {
			this.avatarTextView.setText(this.getResources().getString(R.string.user_avatar));
		}
	}

	@Override
	protected void setViewAction() {
		// TODO Auto-generated method stub
		super.setViewAction();
		if (this.ifMyself) {
			this.avatarImageView.setOnClickListener(this);
			this.shortDescriptionTextView.setOnClickListener(this);
		}
		this.followedAvatarImageView.setOnClickListener(this);
		this.followerAvatarImageView.setOnClickListener(this);
		
	}
	
	private void setUpProfile() {
		try {
			JSONObject profileObject = UserProfile.getProfile(this.userId);
			this.username = profileObject.getString("username");
			this.email = profileObject.getString("email");
			this.shortDescription = profileObject.getString("short_description");
			this.avatarURL = profileObject.getString("avatar_url");
			
		} catch (UserNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_profile, menu);
        return true;
    }*/
	
	private class ProfileTask extends AsyncTask<Void, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//return null;
			setUpProfile();
			Bitmap avatar = Util.urlToBitmap(avatarURL, DrawShareConstant.USER_PROFILE_AVATAR_SIZE);
			if (avatar != null) {
				Log.d(Constant.LOG_TAG, "in userProfile, get the avatar");
				return avatar;
			}
			else {
				return null;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_profile_avatar_image:
			// set the avatar
			setAvatar();
			break;
		case R.id.user_profile_intro_text:
			// set the short description
			new AlertDialog.Builder(this).setTitle(getString(R.string.set_user_description)).setView(
					generateSetShortDescriptionView(this)).setPositiveButton(getString(R.string.confirm), this)
					.setNegativeButton(getString(R.string.cancel), null).show();
			break;
		case R.id.user_profile_followed_people_avatar_image:
			Intent followedIntent = new Intent(UserProfileActivity.this, FollowInfoActivity.class);
			followedIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, this.userId);
			followedIntent.putExtra(DrawShareConstant.EXTRA_KEY.IF_GET_FOLLOWERS, false);
			startActivity(followedIntent);
			break;
		case R.id.user_profile_followers_avatar_image:
			// get the follower list
			Intent followerIntent = new Intent(UserProfileActivity.this, FollowInfoActivity.class);
			followerIntent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, this.userId);
			followerIntent.putExtra(DrawShareConstant.EXTRA_KEY.IF_GET_FOLLOWERS, true);
			startActivity(followerIntent);
			break;
		}
	}
	
	private View generateSetShortDescriptionView(final Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.set_short_description_dialog, null, false);
		descriptionEditText = (EditText) view.findViewById(R.id.set_short_description_edit);
		
		return view;
	}
	
	private void setAvatar() {
		tempFile = tempFile=new File("/sdcard/a.jpg");
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true");

		// 宽高比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		
		// 宽高
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		

		intent.putExtra("output", Uri.fromFile(tempFile));
		intent.putExtra("outputFormat", "JPEG");

		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				SELECT_PICT_CODE);
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICT_CODE) {
				// button.setText(tempFile.exists() + "");
				final Bitmap newAvatar = BitmapFactory.decodeFile(tempFile
						.getAbsolutePath());
				final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.waiting_title), "");
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						progressDialog.dismiss();
						if ((Boolean) msg.obj)
							Toast.makeText(UserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						else {
							Toast.makeText(UserProfileActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
						}
					}
				};
				new Thread(new Runnable() {
					boolean success = false;
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							JSONObject object = UserProfile.setAvatar(userId, newAvatar, 
									ApiKeyHandler.getApiKey(UserProfileActivity.this));
							if (object != null) {
								success = true;
							}
							else {
								success = false;
							}
						} catch (UserNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (AuthFailException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						}
						Message message = handler.obtainMessage(1, success);
						handler.sendMessage(message);
					}
				}).start();
			}
		}
	}

	@Override
	public void onClick(final DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		final String introString = this.descriptionEditText.getText().toString();
		
		if (introString.length() == 0) {
			Toast.makeText(this, getString(R.string.info_not_complete), Toast.LENGTH_LONG).show();
		}
		else {
			final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.waiting_title), "");
			final Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					progressDialog.dismiss();
					if ((Boolean) msg.obj)
						Toast.makeText(UserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
					else {
						Toast.makeText(UserProfileActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
					}
				}
			};
			new Thread(new Runnable() {
				boolean success = false;
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						JSONObject object = UserProfile.setProfile(
								userId, introString, ApiKeyHandler.getApiKey(UserProfileActivity.this));
						if (object != null) {
							success = true;
						}
						else {
							success = false;
						}
					} catch (UserNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						success = false;
					} catch (AuthFailException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						success = false;
					}
					Message message = handler.obtainMessage(1, success);
					handler.sendMessage(message);
				}
			}).start();
		}
	}
}
