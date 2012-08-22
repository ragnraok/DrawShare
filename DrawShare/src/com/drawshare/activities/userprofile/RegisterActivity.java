package com.drawshare.activities.userprofile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.exceptions.UserExistException;
import com.drawshare.Request.userprofile.UserRegister;
import com.drawshare.activities.base.BaseActivity;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.UserNameHandler;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private EditText username = null;
	private EditText email = null;
	private EditText password = null;
	private EditText confirmPassword = null;
	private Button confirmButton = null;
	
	private Handler handler = null;
	private ProgressDialog dialog = null;
	private boolean success = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        findAllView();
        setViewAction();
    }

	@Override
	protected void findAllView() {
		// TODO Auto-generated method stub
		super.findAllView();
		this.username = (EditText) findViewById(R.id.register_username_edit);
		this.password = (EditText) findViewById(R.id.register_password_edit);
		this.confirmPassword = (EditText) findViewById(R.id.register_confirm_password_edit);
		this.email = (EditText) findViewById(R.id.register_email_edit);
		this.confirmButton = (Button) findViewById(R.id.register_register_button);
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
		this.confirmButton.setOnClickListener(this);
	}
	
	private boolean register(String username, String email, String password) {
		JSONObject registerJson = null;
		try {
			registerJson = UserRegister.register(username, email, password, "");
			String userName = registerJson.getString("username");
			String userId = registerJson.getString("user_id");
			String apiKey = registerJson.getString("api_key");
			UserNameHandler.setUserName(this, userName);
			ApiKeyHandler.setApiKey(this, apiKey);
			UserIdHandler.setUserId(this, userId);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final Resources resources = this.getResources();
		if (v.getId() == R.id.register_register_button) {
			if (this.application.getNetworkState() == false) {
				//Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
				Toast.makeText(this, resources.getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
			}
			else {
				final String usernameString = username.getText().toString();
				final String passwordString = password.getText().toString();
				final String confirmPasswordString = confirmPassword.getText().toString();
				final String emailString = email.getText().toString();
				
				if (usernameString.equals("") || passwordString.equals("")
						|| confirmPasswordString.equals("") || emailString.equals("")) {
					//Toast.makeText(this, "Please enter the register information", Toast.LENGTH_LONG).show();
					Toast.makeText(this, resources.getString(R.string.register_info_not_complete), Toast.LENGTH_LONG).show();
				}
				else if (!passwordString.equals(confirmPasswordString)) {
					//Toast.makeText(this, "The password and the confirm password is different", Toast.LENGTH_LONG).show();
					Toast.makeText(this, resources.getString(R.string.register_password_different), Toast.LENGTH_LONG).show();
				}
				else {
					//dialog.show(this, "Please waiting", "Registering", true);
					dialog = ProgressDialog.show(
							this, resources.getString(R.string.waiting_title), resources.getString(R.string.registering));
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							success = register(usernameString, emailString, passwordString);
							Message message = Message.obtain();
							message.what = 1;
							handler.sendMessage(message);
						}
					}).start();
					
					handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							super.handleMessage(msg);
							dialog.dismiss();
							if (msg.what == 1) {
								if (success == false) {
									//Toast.makeText(RegisterActivity.this, "register failed", Toast.LENGTH_LONG).show();
									//Toast.makeText(RegisterActivity.this, resources.getString(R.string.register_failed), 
									//		Toast.LENGTH_LONG).show();
									Toast.makeText(RegisterActivity.this, 
											resources.getString(R.string.register_user_exist), Toast.LENGTH_LONG).show();
								}
								else {
									//Toast.makeText(RegisterActivity.this, "register success", Toast.LENGTH_LONG).show();
									Toast.makeText(RegisterActivity.this, 
											resources.getString(R.string.register_success), Toast.LENGTH_LONG).show();
								}
							}
						}
						
					};
				}
			}
		}
	}
}
