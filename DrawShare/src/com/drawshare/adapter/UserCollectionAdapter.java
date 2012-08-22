package com.drawshare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.picture.UserPicture;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;
import android.widget.Button;

public class UserCollectionAdapter extends BaseAsyncAdapter<Picture>  {

	public UserCollectionAdapter(Context context, AbsListView view,
			ArrayList<Picture> dataSet,
			boolean netStatus) {
		super(context, view, dataSet, R.id.user_collect_grid_pict_image, netStatus, R.layout.user_collect_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		Button deleteButton = (Button) convertView.findViewById(R.id.user_collect_grid_delete_button);
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Uncollect a picture", Toast.LENGTH_LONG).show();
				/*
				Handler handler = new Handler() {
					
				};
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					}
				}).start();*/
			}
		});
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		Picture picture = this.dataSet.get(position);
		this.defaultImageLoader.loadImage(position, picture.pictURL, this.defaultListener, DrawShareConstant.USER_COLLECT_SIZE);
	}

}
