package com.drawshare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.drawshare.R;
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
