package com.drawshare.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.drawshare.R;

public class DrawShareUtil {
	public static View getWaitDialogView(Context context) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = inflater.inflate(R.layout.waiting_dialog, null, false);
    	
    	return view;
    }
}
