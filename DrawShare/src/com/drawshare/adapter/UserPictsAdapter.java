package com.drawshare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.drawshare.R;
import com.drawshare.activities.pictinfo.PictInfoActivity;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class UserPictsAdapter extends BaseAsyncAdapter<Picture> {

	public UserPictsAdapter(Context context, AbsListView view,
			ArrayList<Picture> dataSet,boolean netStatus) {
		super(context, view, dataSet, R.id.user_pict_grid_image, netStatus, R.layout.user_picts_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		/*
		final Picture picture = this.dataSet.get(position);
		ImageView pictImageView = (ImageView) convertView.findViewById(R.id.user_pict_grid_image);
		pictImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, PictInfoActivity.class);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, picture.createUserId);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.PICT_ID, picture.pictureId);
				context.startActivity(intent);
			}
		});*/
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		Picture picture = this.dataSet.get(position);
		//this.imageLoader.loadImage(position, picture.pictURL, listener, DrawShareConstant.USER_PICTS_SIZE);
		this.defaultImageLoader.loadImage(position, picture.pictURL, this.defaultListener, 
					DrawShareConstant.USER_INDEX_AVATAR_SIZE);
	}

}
