package com.drawshare.activities.userprofile;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.drawshare.R;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.activities.base.BaseUserFragment;
import com.drawshare.activities.base.HotestPictureActivity;
import com.drawshare.activities.pictinfo.PictInfoActivity;
import com.drawshare.adapter.UserPictsAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.UserPictureNetRenderer;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class UserPictsFragment extends BaseUserFragment 
	implements LoaderCallbacks<ArrayList<Picture>>, OnItemClickListener {

	private GridView gridView = null;
	private ProgressBar progressBar = null;
	
	private ArrayList<Picture> pictList = null;
	private UserPictsAdapter adapter = null;
	
	private static String userId = null;
	private boolean netStatus = false;
	private boolean ifFinishLoad = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		netStatus = application.getNetworkState();
		if (netStatus) {
			this.getLoaderManager().initLoader(0, null, this);
		}
		else {
		}
		
		if (this.getActivity().getIntent().getExtras().containsKey(DrawShareConstant.EXTRA_KEY.USER_ID)) {
			this.userId = this.getActivity().getIntent().getExtras().getString(DrawShareConstant.EXTRA_KEY.USER_ID);
		}
		
		if (userId == null) {
			this.userId = UserIdHandler.getUserId(this.getActivity());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (this.adapter != null) {
			//this.adapter.stopLoad();
		}
		//if (netStatus)
		//	this.getLoaderManager().getLoader(0).stopLoading();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null) {
			//this.adapter.resumeLoad();
		}
		//if (netStatus)
		//	this.getLoaderManager().getLoader(0).startLoading();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_picts, container, false);
		this.gridView = (GridView) view.findViewById(R.id.user_picts_grid);
		this.progressBar = (ProgressBar) view.findViewById(R.id.user_picts_progress_bar);
		this.gridView.setOnItemClickListener(this);
		
		/**
		ArrayList listData = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 30; i++) {
        	HashMap<String, Object> item = new HashMap<String, Object>();
        	item.put("user_pict_grid_image", R.drawable.default_pict);
        	listData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), listData, R.layout.user_picts_grid, 
        		new String[] {"user_pict_grid_image"}, new int[] {R.id.user_pict_grid_image});
		
        this.gridView.setAdapter(adapter);*/
		
		return view;
	}

	@Override
	public Loader<ArrayList<Picture>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new UserPictsLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Picture>> loader,
			ArrayList<Picture> data) {
		// TODO Auto-generated method stub
		this.pictList = data;
		this.progressBar.setVisibility(View.INVISIBLE);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		if (ifFinishLoad == false) {
			adapter = new UserPictsAdapter(this.getActivity(), gridView, data, application.getNetworkState());
			ifFinishLoad = true;
		}
		this.gridView.setAdapter(adapter);
		this.gridView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Picture>> arg0) {
		// TODO Auto-generated method stub
		this.adapter = null;
		this.pictList = null;
		
		this.progressBar.setVisibility(View.VISIBLE);
		this.gridView.setVisibility(View.INVISIBLE);
		
		ifFinishLoad = false;
	}
	
	private static class UserPictsLoader extends AsyncTaskLoader<ArrayList<Picture>> {

		private Context context = null;
		private ArrayList<Picture> pictsList = null;
		
		public UserPictsLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public ArrayList<Picture> loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			UserPictureNetRenderer renderer = new UserPictureNetRenderer(userId);
			try {
				this.pictsList = renderer.renderToList();
				return this.pictsList;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.pictsList = null;
			return pictsList;
			
		}

		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.pictsList = null;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (this.pictsList != null) {
				this.deliverResult(pictsList);
			}
			else {
				this.forceLoad();
			}
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int location, long arg3) {
		// TODO Auto-generated method stub
		if (this.pictList != null) {
			Picture picture = this.pictList.get(location);
			Intent intent = new Intent(this.getActivity(), PictInfoActivity.class);
			intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, this.userId);
			intent.putExtra(DrawShareConstant.EXTRA_KEY.PICT_ID, picture.pictureId);
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.user_index_menu_logout:
			new AlertDialog.Builder(this.getActivity()).setTitle(
					R.string.confirm_logout_title).setMessage(R.string.confirm_logout)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					DrawShareUtil.logout(UserPictsFragment.this.getActivity());
					// go to hotest pictures activity
					Intent intent = new Intent(UserPictsFragment.this.getActivity(), HotestPictureActivity.class);
					startActivity(intent);
					UserPictsFragment.this.getActivity().finish();
				}
			}).setNegativeButton(R.string.cancel, null).show();
			break;
		case R.id.user_index_menu_reload:
			this.getLoaderManager().restartLoader(0, null, this);
		default:
			break;
		}
		return true;
	}
}
