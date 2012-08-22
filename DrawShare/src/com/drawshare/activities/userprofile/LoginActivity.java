package com.drawshare.activities.userprofile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.userprofile.UserLogin;
import com.drawshare.activities.base.BaseActivity;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.UserNameHandler;

public class LoginActivity extends BaseActivity implements OnClickListener {
	
	private EditText usernameOrEmail = null;
	private EditText password = null;
	private Button loginButton = null;
	
	private Handler handler = null;
	private ProgressDialog dialog = null;
	private boolean success = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.findAllView();
        this.setViewAction();
    }

    @Override
	protected void findAllView() {
		// TODO Auto-generated method stub
		super.findAllView();
		this.usernameOrEmail = (EditText) this.findViewById(R.id.login_uesrname_email_edit);
		this.password = (EditText) this.findViewById(R.id.login_password_edit);
		this.loginButton = (Button) this.findViewById(R.id.login_login_button);
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		super.setUpView();
	}
	
	@Override
	protected void setViewAction() {
		// TODO Auto-generated method stub
		super.setViewAction();
		this.loginButton.setOnClickListener(this);
	}

	private boolean login(String usernameOrEmail, String password) {
		JSONObject loginJson = null;
		try {
			loginJson = UserLogin.login(usernameOrEmail, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		if (loginJson == null) {
			return false;
		}
		
		else {
			try {
				String apiKey = loginJson.getString("api_key");
				String userId = loginJson.getString("user_id");
				String username = loginJson.getString("username");
				ApiKeyHandler.setApiKey(this, apiKey);
				UserIdHandler.setUserId(this, userId);
				UserNameHandler.setUserName(this, username);
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
/*
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
*/
	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final Resources resources = this.getResources();
		if (v.getId() == R.id.login_login_button) {
			if (this.application.getNetworkState() == false) 
				//Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
				Toast.makeText(this, resources.getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
			else {
				if (this.usernameOrEmail.getText().length() == 0 || this.password.getText().length() == 0) {
					//Toast.makeText(this, "Please enter the login information", Toast.LENGTH_LONG).show();
					Toast.makeText(this, resources.getString(R.string.login_info_not_complete), Toast.LENGTH_LONG).show();
					return;
				}
				//dialog.show(this, "Please wait", "Logining", true);
				dialog = ProgressDialog.show(this, resources.getString(R.string.waiting_title), resources.getString(R.string.login_loinging));
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						success = LoginActivity.this.login(usernameOrEmail.getText().toString(), 
								password.getText().toString());
						Message message = Message.obtain();
						message.what = 1;
						handler.sendMessage(message);
					}
					
				}.start();
				
				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						dialog.dismiss();
						if (msg.what == 1) {
							if (success == false)
								//Toast.makeText(LoginActivity.this, "The Username/Email or Password is error", 
								//		Toast.LENGTH_LONG).show();
								Toast.makeText(LoginActivity.this, resources.getString(R.string.login_password_wrong), 
										Toast.LENGTH_LONG).show();
							else {
								// swith to userprofile activity...
								//Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
								Toast.makeText(LoginActivity.this, resources.getString(R.string.login_success), 
										Toast.LENGTH_LONG).show();
							}
						}
					}
					
				};
			}
		}
	}
}
