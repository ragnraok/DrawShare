package com.drawshare.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.TextView;

import com.drawshare.R;
import com.drawshare.render.object.FollowMessage;
import com.drawshare.util.DrawShareConstant;

public class FollowMessageAdapter extends BaseAsyncAdapter<FollowMessage> {

	public FollowMessageAdapter(Context context, AbsListView view,
			ArrayList<FollowMessage> dataSet, 
			boolean netStatus) {
		super(context, view, dataSet, R.id.follow_message_grid_avatar_image, netStatus, R.layout.follow_message_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		FollowMessage message = this.dataSet.get(position);
		TextView usernameTextView = (TextView) convertView.findViewById(R.id.follow_message_grid_username_text);
		TextView followDateTextView = (TextView) convertView.findViewById(R.id.follow_message_grid_follow_date_text);
		
		usernameTextView.setText(message.followUsername);
		followDateTextView.setText(new SimpleDateFormat("yyyy-MM-dd").format(message.followDate));
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		FollowMessage message = this.dataSet.get(position);
		this.defaultImageLoader.loadImage(position, message.followUserAvatarUrl, this.defaultListener, DrawShareConstant.MESSAGE_AVATAR_SIZE);
	}

}
