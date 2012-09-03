package com.drawshare.adapter;

import java.text.SimpleDateFormat;
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
import com.drawshare.render.object.Comment;
import com.drawshare.util.DrawShareConstant;

public class PictCommentAdapter extends BaseAsyncAdapter<Comment>{

	public PictCommentAdapter(Context context, AbsListView view,
			ArrayList<Comment> dataSet,
			boolean netStatus) {
		super(context, view, dataSet, R.id.pict_comment_grid_avatar_image, netStatus, R.layout.pict_comment_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		final Comment comment = this.dataSet.get(position);
		String pictComment = comment.pictureComment;
		String userName = comment.commentUserName;
		String commentDate = new SimpleDateFormat("yyyy-MM-dd").format(comment.commentDate);
		
		TextView pictCommentTextView = (TextView) convertView.findViewById(R.id.pict_comment_grid_comment_text);
		TextView userNameTextView = (TextView) convertView.findViewById(R.id.pict_comment_grid_username_text);
		TextView commentDateTextView = (TextView) convertView.findViewById(R.id.pict_comment_grid_date_text);
		
		pictCommentTextView.setText(pictComment);
		userNameTextView.setText(userName);
		commentDateTextView.setText(commentDate);
		
		ImageView avatar = (ImageView) convertView.findViewById(R.id.pict_comment_grid_avatar_image);
		avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userId = comment.commentUserId;
				Intent intent = new Intent(context, OtherUserIndexActivity.class);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, false);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, userId);
				context.startActivity(intent);
			}
		});
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		Comment comment = this.dataSet.get(position);
		this.defaultImageLoader.loadImage(position, comment.commentUserAvatarUrl, this.defaultListener, 
				DrawShareConstant.PICT_COMMENT_AVATAR_SIZE);
	}

}
