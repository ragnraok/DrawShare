package com.drawshare.adapter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.picture.UserPicture;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.userprofile.UserCollectionFragment;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;
import android.widget.Button;

public class UserCollectionAdapter extends BaseAsyncAdapter<Picture>  {
	
	private ProgressDialog dialog = null;
	private boolean ifMyself = false;
	
	public UserCollectionAdapter(Context context, AbsListView view,
			ArrayList<Picture> dataSet, boolean netStatus, boolean ifMyself) {
		super(context, view, dataSet, R.id.user_collect_grid_pict_image, netStatus, R.layout.user_collect_grid);
		// TODO Auto-generated constructor stub
		this.ifMyself = ifMyself;
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		final Picture picture = this.dataSet.get(position);
		
		Button deleteButton = (Button) convertView.findViewById(R.id.user_collect_grid_delete_button);
		if (ifMyself == false) {
			deleteButton.setVisibility(View.INVISIBLE);
		}
		else {
			deleteButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(context, "Uncollect a picture", Toast.LENGTH_LONG).show();
					dialog = ProgressDialog.show(context, context.getResources().getString(R.string.waiting_title), "");
					final Handler handler = new Handler() {
	
						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							super.handleMessage(msg);
							dialog.dismiss();
							if ((Boolean) msg.obj) {
								Toast.makeText(context, context.getResources().getString(R.string.delete_collection_success), 
										Toast.LENGTH_LONG).show();
								
							}
							else {
								Toast.makeText(context, context.getResources().getString(R.string.delete_collection_failed), 
										Toast.LENGTH_LONG).show();
							}
						}
						
					};
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean success = false;
							try {
								success = UserProfile.deleteCollectPicture(ApiKeyHandler.getApiKey(context), 
										UserIdHandler.getUserId(context), picture.pictureId);
							} catch (AuthFailException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								success = false;
							} catch (UserNotExistException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								success = false;
							}
							Message message = handler.obtainMessage(1, success);
							handler.sendMessage(message);
						}
					}).start();
				}
			});
		}
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		Picture picture = this.dataSet.get(position);
		this.defaultImageLoader.loadImage(position, picture.pictURL, this.defaultListener, DrawShareConstant.USER_COLLECT_SIZE);
	}

}
