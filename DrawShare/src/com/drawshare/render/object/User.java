package com.drawshare.render.object;

import android.graphics.Bitmap;

public class User {
	public static final int NO_COLLECT_PICT = -1;
	
	public String userId = null;
	public String username = null;
	public String email = null;
	public String shortDescription = null;
	public int collectNum = NO_COLLECT_PICT;
	public String avatarUrl = null;
	public Bitmap avatar = null;
	public String unreadMsgNum = null;
}
