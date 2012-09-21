package com.drawshare.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.drawshare.R;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class HotestPictAdapter extends BaseAsyncAdapter<Picture> {

	public HotestPictAdapter(final Context context, AbsListView view, ArrayList<Picture> dataSet,  
			boolean netStatus) {
		super(context, view, dataSet, R.id.hotest_pict_grid_image, netStatus, R.layout.hotest_pict_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		//Log.d(Constant.LOG_TAG, "in setImage");
		Picture picture = this.dataSet.get(position);
		//if (picture.pictURL == null) {
		//	Log.d(Constant.LOG_TAG, "the pictURL is null");
		//	ImageView view = (ImageView) convertView.findViewById(R.id.hotest_pict_grid_image);
		//	view.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_pict_temp));
		//}
		//else {
			//this.imageLoader.loadImage(position, picture.pictURL, listener, DrawShareConstant.THUMBNAIL_IMAGE_SIZE);
		//
		
		this.defaultImageLoader.loadImage(position, picture.pictURL, this.defaultListener, 100);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}


}
