package com.drawshare.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.asyncloader.AsyncImageLoader;
import com.drawshare.asyncloader.AsyncImageLoader.ImageLoadListener;
import com.drawshare.render.object.FriendActivity;
import com.drawshare.util.DrawShareConstant;


public class FriendsNewAdapter extends BaseAsyncAdapter<FriendActivity> {
	
	private ImageLoadListener avatarListener = null;
	private AsyncImageLoader avatarLoader = null;
	
	private static final String LOG_TAG = "friendNewsAdapter";
	
	//private Bitmap[] avatarBitmapList = null;
	
	public FriendsNewAdapter(Fragment friendsNewFragment, AbsListView view,
			ArrayList<FriendActivity> dataSet, 
			boolean netStatus) {
		super(friendsNewFragment.getActivity(), view, dataSet, 
				R.id.friends_news_grid_pict_image, netStatus, R.layout.friends_new_grid);
		// TODO Auto-generated constructor stub
		// the defaultListener hold the friends_news_grid_pict_image, and another listener hold the avatar_image
		
		this.avatarLoader = new AsyncImageLoader(netStatus);
		this.setListener();
		
		//avatarBitmapList = new Bitmap[this.dataSet.size()];
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		avatarListener = new ImageLoadListener() {
			
			@Override
			public void onImageLoad(Integer rowNum, Bitmap bitmap) {
				// TODO Auto-generated method stub
				View view = listOrGridView.findViewWithTag(rowNum);
				if (view != null) {
					ImageView image = (ImageView) view.findViewById(R.id.friends_news_grid_avatar_image);
					image.setImageBitmap(bitmap);
					Log.d(Constant.LOG_TAG, "set the avatar image");
					
					//if (avatarBitmapList[rowNum] == null) {
					//	avatarBitmapList[rowNum] = bitmap;
					//	Log.d(Constant.LOG_TAG, "set the avataBitmapList " + rowNum);
					//}
				}
			}
			
			@Override
			public void onError(Integer rowNum) {
				// TODO Auto-generated method stub
				View view = listOrGridView.findViewWithTag(rowNum);
				if (view != null) {
					ImageView image = (ImageView) view.findViewById(R.id.friends_news_grid_avatar_image);
					image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_pict_temp));
					Log.d(Constant.LOG_TAG, "set the avatar on error");
				}
			}
		};
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		FriendActivity activity = this.dataSet.get(position);
		
		TextView username = (TextView) convertView.findViewById(R.id.friends_new_grid_username_text);
		TextView pictname = (TextView) convertView.findViewById(R.id.friends_new_grid_pictname_text);
		TextView editDate = (TextView) convertView.findViewById(R.id.friends_new_grid_edit_date_text);
		
		String editDateString = new SimpleDateFormat("yyyy-MM-dd").format(activity.editDate);
		
		username.setText(activity.userName);
		pictname.setText(activity.pictureTitle);
		editDate.setText(editDateString);
		
		View userProfileView = convertView.findViewById(R.id.friends_new_grid_user_profile_layout);
		View pictInfoView = convertView.findViewById(R.id.friends_new_grid_pict_info_layout);
		
		userProfileView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "get Pict Info", Toast.LENGTH_LONG).show();
			}
		});
		pictInfoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "get User Profile", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		FriendActivity activity = this.dataSet.get(position);
		//this.avatarLoader.loadImage(position, activity.userAvatarUrl, avatarListener, DrawShareConstant.FRIENDS_NEWS_AVATAR_SIZE);
		//this.imageLoader.loadImage(position, activity.pictureURL, this.listener, DrawShareConstant.FRIENDS_NEWS_PICT_SIZE);
		
		this.defaultImageLoader.loadImage(position, activity.pictureURL, this.defaultListener, DrawShareConstant.FRIENDS_NEWS_PICT_SIZE);	
		this.avatarLoader.loadImage(position, activity.userAvatarUrl, this.avatarListener, DrawShareConstant.FRIENDS_NEWS_AVATAR_SIZE);
	}
	

	@Override
	public void stopLoad() {
		// TODO Auto-generated method stub
		super.stopLoad();
		this.avatarLoader.lock();
	}

	@Override
	public void resumeLoad() {
		// TODO Auto-generated method stub
		super.resumeLoad();
		this.avatarLoader.unlock();
	}
	
	

}
