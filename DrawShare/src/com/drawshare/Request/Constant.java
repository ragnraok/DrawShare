package com.drawshare.Request;

public class Constant {
	public static final String SITE = "http://drawshare.herokuapp.com"; // the site without tail slash
	
	public static final String LOG_TAG = "Ragnarok";
	
	public static class UserProfile {
		public static String BASE = "/user_profile/";
		
		public static final String LOGIN = BASE + "login/";
		public static final String REGISTER = BASE + "register/";
		public static final String GET_PROFILE = BASE + "get_profile/";
		public static final String SET_PROFILE = BASE + "set_profile/";
		public static final String SET_AVATAR = BASE + "set_avatar/";
		public static final String GET_FOLLOW_INFO = BASE + "get_follow_info/";
		public static final String FOLLOW_USER = BASE + "follow_user/";
		public static final String UNFOLLOW_USER = BASE + "unfollow_user/";
		public static final String GET_COLLECT_PICTURE = BASE + "get_collect_picture/";
		public static final String ADD_COLLECT_PICTURE = BASE + "add_collect_picture/";
		public static final String DELETE_COLLECT_PICTURE = BASE + "delete_collect_picture/";
		public static final String GET_FOLLOW_STATUS = BASE + "get_follow_status/";
		public static final String GET_COLLECT_STATUS = BASE + "get_collect_status/";
	}
	
	public static class Message {
		public static String BASE = "/message/";
		
		public static final String GET_FORK_MESSAGE = BASE + "get_fork_message/";
		public static final String GET_FOLLOW_MESSAGE = BASE + "get_follow_message/";
		public static final String GET_UNREAD_MESSAGE_NUM = BASE + "get_unread_message_number/";
	}
	
	public 	static class Activity {
		public static String BASE = "/activity/";
		
		public static final String GET_FOLLOWED_ACTIVITIES = BASE + "get_followed_activities/";
	}
	
	public static class Picture {
		public static String BASE = "/edit_picture/";
		
		public static final String GET_PICT_INFO = BASE + "get_pict_info/";
		public static final String ADD_NEW_PICT = BASE + "add_new_pict/";
		public static final String GET_PICT_COMMENTS = BASE + "get_pict_comments/";
		public static final String ADD_PICT_COMMENT = BASE + "add_pict_comment/";
		public static final String DELETE_PICT = BASE + "delete_pict/";
		public static final String GET_TOP_PICT = BASE + "get_top_pict/";
		public static final String GET_PICT_EDIT_HISTORY = BASE + "get_pict_edit_history/";
		public static final String FORK_PICT = BASE + "fork_pict/";
		public static final String GET_USER_PICT = BASE + "get_user_pict/";
	}
}
