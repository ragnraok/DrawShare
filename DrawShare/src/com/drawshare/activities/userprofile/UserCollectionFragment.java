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
import com.drawshare.adapter.UserCollectionAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.UserCollectionNetRenderer;
import com.drawshare.render.object.FriendActivity;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class UserCollectionFragment extends BaseFragment implements LoaderCallbacks<ArrayList<Picture>>{

	private GridView gridView = null;
	private ProgressBar progressBar = null;
	
	private static String userId = null;
	
	private UserCollectionAdapter adapter = null;
	
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
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		if (application.getNetworkState() == true) {
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
		this.getLoaderManager().getLoader(0).stopLoading();
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null) {
			adapter.resumeLoad();
		}
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
		
		adapter = new UserCollectionAdapter(this.getActivity(), gridView, data, application.getNetworkState());
		this.gridView.setAdapter(adapter);
		this.gridView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Picture>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.gridView.setVisibility(View.INVISIBLE);
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

}
