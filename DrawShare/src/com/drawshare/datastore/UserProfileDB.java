package com.drawshare.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class UserProfileDB extends SQLiteOpenHelper {
	
	public static final String VERSION = "1";
	
	public static final String USERPROFILE_DB_FILENAME = "userprofile.db";
	public static final String USERPROFILE_DB_TABLENAME = "userprofile";
	
	/**
	 * 用户的id
	 */
	public static final String COLUMN_USERID = "user_id";
	
	/**
	 * 用户名
	 */
	public static final String COLUMN_USERNAME = "username";
	
	/**
	 * 用户email
	 */
	public static final String COLUMN_EMAIL = "email";
	
	/**
	 * 用户头像
	 */
	public static final String COLUMN_AVATAR = "avatar";
	
	/**
	 * 用户头像
	 */
	public static final String COLUMN_THUMBNAIL = "thumbnail";
	
	/**
	 * 用户收藏图片数目
	 */
	public static final String COLUMN_COLLECT_NUM = "collect_num";
	
	private static final String CREATE_PROFILE_DB_SQL = "CREATE TABLE " + USERPROFILE_DB_TABLENAME +" ( " +
				BaseColumns._ID + " INT PRIMARY KEY, " +
				COLUMN_USERID + " VARCHAR(80), " +
				COLUMN_USERNAME + " VARCHAR(80), " + 
				COLUMN_EMAIL + " VARCHAR(80), " + 
				COLUMN_AVATAR + " BLOB, " + 
				COLUMN_THUMBNAIL + " BLOB" + ")";
				

	public UserProfileDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROFILE_DB_SQL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
