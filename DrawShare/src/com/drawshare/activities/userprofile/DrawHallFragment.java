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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.adapter.DrawHallAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.render.netRenderer.HotestPictNetRenderer;
import com.drawshare.render.object.Picture;

public class DrawHallFragment extends BaseFragment implements LoaderCallbacks<ArrayList<Picture>> {

	private Button searchButton = null;
	private GridView pictsGridView = null;
	private ProgressBar progressBar = null;
	
	private ArrayList<Picture> drawHallPicts = null;
	private DrawHallAdapter adapter = null;
	private boolean netStatus = false;
	
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
			Toast.makeText(this.getActivity(), this.getResources().getString(R.string.network_unavailable),
					Toast.LENGTH_LONG).show();
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
		if (netStatus)
			this.getLoaderManager().getLoader(0).stopLoading();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null) {
			this.adapter.resumeLoad();
		}
		if (netStatus)
			this.getLoaderManager().getLoader(0).startLoading();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_draw_hall, container, false);
		this.searchButton = (Button) view.findViewById(R.id.draw_hall_search_box_button);
		this.pictsGridView = (GridView) view.findViewById(R.id.draw_hall_pict_grid);
		this.progressBar = (ProgressBar) view.findViewById(R.id.draw_hall_progress_bar);
		/*
		ArrayList listData = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 30; i++) {
        	HashMap<String, Object> item = new HashMap<String, Object>();
        	item.put("draw_hall_grid_pict_image", R.drawable.default_pict);
        	listData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), listData, R.layout.draw_hall_grid, 
        		new String[] {"draw_hall_grid_pict_image"}, new int[] {R.id.draw_hall_grid_pict_image});
		
        this.pictsGridView.setAdapter(adapter);*/
        
		return view;
	}

	@Override
	public Loader<ArrayList<Picture>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new DrawHallLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Picture>> loader,
			ArrayList<Picture> data) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.INVISIBLE);
		
		this.drawHallPicts = data;
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		this.adapter = new DrawHallAdapter(this.getActivity(), pictsGridView, data, application.getNetworkState());
		this.pictsGridView.setAdapter(adapter);
		this.pictsGridView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Picture>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.pictsGridView.setVisibility(View.INVISIBLE);
		
		this.drawHallPicts = null;
		this.adapter = null;
	}
	
	private static class DrawHallLoader extends AsyncTaskLoader<ArrayList<Picture>> {

		private Context context = null;
		private ArrayList<Picture> pictList = null;
		
		public DrawHallLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public ArrayList<Picture> loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			HotestPictNetRenderer renderer = new HotestPictNetRenderer(20);
			
			this.pictList = renderer.renderToList();
			
			if (pictList != null) {
				return pictList;
			}
			return null;
		}

		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.pictList = null;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (this.pictList  != null) {
				this.deliverResult(pictList);
			}
			else if (this.pictList == null) {
				this.forceLoad();
			}
		}
		
		
		
	}

}
