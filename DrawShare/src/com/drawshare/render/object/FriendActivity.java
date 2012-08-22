package com.drawshare.render.object;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;

public class FriendActivity {
	public static final int EDIT_TYPE_CREATE = 1;
	public static final int EDIT_TYPE_FORK = 2;
	
	public String userId = null;
	public String userName = null;
	public String userAvatarUrl = null;
	public String pictureId = null;
	public String pictureTitle = null;
	public String pictureURL = null;
	public Bitmap picture = null;
	public int editType = 0;
	public Date editDate = null;
}
