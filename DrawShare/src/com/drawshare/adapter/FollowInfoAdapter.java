package com.drawshare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.drawshare.R;
import com.drawshare.activities.userprofile.OtherUserIndexActivity;
import com.drawshare.activities.userprofile.UserIndexActivity;
import com.drawshare.activities.userprofile.UserProfileActivity;
import com.drawshare.render.object.User;
import com.drawshare.util.DrawShareConstant;

public class FollowInfoAdapter extends BaseAsyncAdapter<User> {

	public FollowInfoAdapter(Context context, AbsListView view,
			ArrayList<User> dataSet, boolean netStatus) {
		super(context, view, dataSet, R.id.follow_info_grid_avatar_image, netStatus, R.layout.follow_info_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		final User user = this.dataSet.get(position);
		TextView usernameTextView = (TextView) convertView.findViewById(R.id.follow_info_grid_username_text);
		TextView emailTextView = (TextView) convertView.findViewById(R.id.follow_info_grid_email_text);
		ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.follow_info_grid_avatar_image);
		
		usernameTextView.setText(user.username);
		emailTextView.setText(user.email);
		avatarImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, OtherUserIndexActivity.class);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, user.userId);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, false);
				context.startActivity(intent);
			}
		});
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		User user = this.dataSet.get(position);
		this.defaultImageLoader.loadImage(position, user.avatarUrl, this.defaultListener, DrawShareConstant.USER_PROFILE_AVATAR_SIZE);
	}

}
