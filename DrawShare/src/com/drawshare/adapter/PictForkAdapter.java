package com.drawshare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.drawshare.R;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class PictForkAdapter extends BaseAsyncAdapter<Picture> {

	public PictForkAdapter(Context context, AbsListView view,
			ArrayList<Picture> dataSet,
			boolean netStatus) {
		super(context, view, dataSet, R.id.pict_fork_grid_image, netStatus, R.layout.pict_fork_grid);
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
		this.defaultImageLoader.loadImage(position, picture.pictURL, this.defaultListener, DrawShareConstant.PICT_FORK_GRID_IMAGE_SIZE);
	}

}
