package com.drawshare.activities.userprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.drawshare.R;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.adapter.UserPictsAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.UserPictureNetRenderer;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class UserPictsFragment extends BaseFragment implements LoaderCallbacks<ArrayList<Picture>> {

	private GridView gridView = null;
	private ProgressBar progressBar = null;
	
	private ArrayList<Picture> pictList = null;
	private UserPictsAdapter adapter = null;
	
	private static String userId = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		
		if (application.getNetworkState()) {
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
			this.adapter.stopLoad();
		}
		this.getLoaderManager().getLoader(0).stopLoading();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null) {
			this.adapter.resumeLoad();
		}
		this.getLoaderManager().getLoader(0).startLoading();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_picts, container, false);
		this.gridView = (GridView) view.findViewById(R.id.user_picts_grid);
		this.progressBar = (ProgressBar) view.findViewById(R.id.user_picts_progress_bar); 
		
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
		
		adapter = new UserPictsAdapter(this.getActivity(), gridView, data, application.getNetworkState());
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
}
