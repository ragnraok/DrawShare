package com.drawshare.activities.userprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.activities.pictinfo.PictInfoActivity;
import com.drawshare.adapter.BaseAsyncAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.UserCollectionNetRenderer;
import com.drawshare.render.object.FriendActivity;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class UserCollectionFragment extends BaseFragment implements LoaderCallbacks<ArrayList<Picture>>, OnItemClickListener {

	private GridView gridView = null;
	private ProgressBar progressBar = null;
	
	private static String userId = null;
	private boolean ifMyself = false;
	
	private UserCollectionAdapter adapter = null;
	private boolean netStatus = false;
	
	private ArrayList<Picture> pictList = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if (this.getActivity().getIntent().getExtras().containsKey(DrawShareConstant.EXTRA_KEY.USER_ID)) {
			this.userId = this.getActivity().getIntent().getExtras().getString(DrawShareConstant.EXTRA_KEY.USER_ID);
		}
		
		if (userId == null) {
			this.userId = UserIdHandler.getUserId(this.getActivity());
		}
		this.ifMyself = this.getActivity().getIntent().getExtras().getBoolean(DrawShareConstant.EXTRA_KEY.IF_MYSELF);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		netStatus = application.getNetworkState();
		if (netStatus) {
			this.getLoaderManager().initLoader(0, null, this);
		}
		else {
			//
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_collect, container, false);
		this.gridView = (GridView) view.findViewById(R.id.user_collect_pict_grid);
		this.progressBar = (ProgressBar) view.findViewById(R.id.user_collect_progress_bar);
		
		this.gridView.setOnItemClickListener(this);
		/*
		ArrayList listData = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 30; i++) {
        	HashMap<String, Object> item = new HashMap<String, Object>();
        	item.put("user_collect_grid_pict_image", R.drawable.default_pict);
        	listData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), listData, R.layout.user_collect_grid, 
        		new String[] {"user_collect_grid_pict_image"}, new int[] {R.id.user_collect_grid_pict_image});
		
        this.gridView.setAdapter(adapter);*/
		
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (this.adapter != null) {
			adapter.stopLoad();
		}
		if (netStatus)
			this.getLoaderManager().getLoader(0).stopLoading();
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null) {
			adapter.resumeLoad();
		}
		if (netStatus)
			this.getLoaderManager().getLoader(0).startLoading();
	}

	@Override
	public Loader<ArrayList<Picture>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new UserCollectionLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Picture>> loader, ArrayList<Picture> data) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.INVISIBLE);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		
		adapter = new UserCollectionAdapter(this.getActivity(), gridView, data, application.getNetworkState(), this.ifMyself);
		this.gridView.setAdapter(adapter);
		this.gridView.setVisibility(View.VISIBLE);
		
		this.pictList = data;
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Picture>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.gridView.setVisibility(View.INVISIBLE);
		
		this.adapter = null;
		this.pictList = null;
	}
	
	private static class UserCollectionLoader extends AsyncTaskLoader<ArrayList<Picture>> {

		private Context context = null;
		private ArrayList<Picture> data = null;
		
		public UserCollectionLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public ArrayList<Picture> loadInBackground() {
			// TODO Auto-generated method stub
			UserCollectionNetRenderer renderer = new UserCollectionNetRenderer(userId);
			
			try {
				this.data = renderer.renderToList();
				return data;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.data = null;
			}
			
			return null;
		}

		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.data = null;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (this.data != null) {
				this.deliverResult(data);
			}
			else {
				this.forceLoad();
			}
		}
		
	}
	
	private void reload() {
		getLoaderManager().restartLoader(0, null, this);
	}
	
	public class UserCollectionAdapter extends BaseAsyncAdapter<Picture>  {
		
		private ProgressDialog dialog = null;
		private Thread deleteCollectionThread = null;
		private Handler handler = null;
		private boolean ifMyself = false;

		public UserCollectionAdapter(final Context context, AbsListView view,
				ArrayList<Picture> dataSet, boolean netStatus, boolean ifMyself) {
			super(context, view, dataSet, R.id.user_collect_grid_pict_image, netStatus, R.layout.user_collect_grid);
			// TODO Auto-generated constructor stub
			//dialog = new ProgressDialog(context);
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
			
			ImageView pictImageView = (ImageView) convertView.findViewById(R.id.user_collect_grid_pict_image);
			pictImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, PictInfoActivity.class);
					intent.putExtra(DrawShareConstant.EXTRA_KEY.PICT_ID, picture.pictureId);
					intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, userId);
					context.startActivity(intent);
				}
			});
			
			deleteCollectionThread = new Thread(new Runnable() {
				
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
			});
			
			Button deleteButton = (Button) convertView.findViewById(R.id.user_collect_grid_delete_button);
			if (this.ifMyself == false) {
				deleteButton.setVisibility(View.INVISIBLE);
			}
			else {
				deleteButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(context).setTitle(context.getResources().getString(
								R.string.delete_collection_confirm_title) + " " + picture.title).setMessage(
										context.getResources().getString(R.string.delete_collection_confirm_content))
										.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dia, int which) {
												// TODO Auto-generated method stub
												dialog = ProgressDialog.show(context, context.getString(R.string.waiting_title), "");
												handler = new Handler() {
	
													@Override
													public void handleMessage(Message msg) {
														// TODO Auto-generated method stub
														super.handleMessage(msg);
														dialog.dismiss();
														if ((Boolean) msg.obj) {
															Toast.makeText(context, context.getResources().getString(R.string.delete_collection_success), 
																	Toast.LENGTH_LONG).show();
															reload();
														}
														else {
															Toast.makeText(context, context.getResources().getString(R.string.delete_collection_failed), 
																	Toast.LENGTH_LONG).show();
														}
													}
													
												};
												deleteCollectionThread.start();
											}
										}).setNegativeButton(context.getString(R.string.cancel), null).show();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		// go to picture profile
	}

}
