package com.drawshare.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.drawshare.R;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.UserNameHandler;

public class DrawShareUtil {
	public static View getWaitDialogView(Context context) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = inflater.inflate(R.layout.waiting_dialog, null, false);
    	
    	return view;
    }
	
	public static ProgressDialog getWaitProgressDialog(Context context) {
		return ProgressDialog.show(context, "", context.getString(R.string.waiting_title));
	}
	
	public static boolean ifLogin(Context context) {
		String userId = UserIdHandler.getUserId(context);
    	String apiKey = ApiKeyHandler.getApiKey(context);
    	String username = UserNameHandler.getUserName(context);
    	if (userId == null || apiKey == null || username == null) {
    		return false;
    	}
    	else {
			return true;
		}
	}
}
