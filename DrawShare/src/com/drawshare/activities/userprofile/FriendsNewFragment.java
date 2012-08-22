package com.drawshare.activities.userprofile;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.adapter.FriendsNewAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.render.netRenderer.FriendsNewsNetRenderer;
import com.drawshare.render.object.FriendActivity;

public class FriendsNewFragment extends BaseFragment implements LoaderCallbacks<ArrayList<FriendActivity>> {

	private ArrayList<FriendActivity> friendNews = null;
	
	private ListView listView = null;
	private ProgressBar progressBar = null;
	private FriendsNewAdapter adapter = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		
		if (application.getNetworkState())
			this.getLoaderManager().initLoader(0, null, this);
		else {
			//Toast.makeText(this.getActivity(), "", duration)
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_friends_news, container, false);
		this.progressBar = (ProgressBar) view.findViewById(R.id.friends_new_loading_bar);
		this.listView = (ListView) view.findViewById(R.id.friends_new_pict_list);
		
		return view;
	}
	
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (this.adapter != null)
			this.adapter.stopLoad();
		this.getLoaderManager().getLoader(0).stopLoading();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.adapter != null)
			this.adapter.resumeLoad();
		this.getLoaderManager().getLoader(0).startLoading();
	}

	@Override
	public Loader<ArrayList<FriendActivity>> onCreateLoader(int arg0,
			Bundle arg1) {
		// TODO Auto-generated method stub
		return new FriendNewsLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<FriendActivity>> loader,
			ArrayList<FriendActivity> data) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.INVISIBLE);
		
		this.friendNews = data;
		Log.d(Constant.LOG_TAG, "the data.size is " + data.size());
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		adapter = new FriendsNewAdapter(this, listView, data, application.getNetworkState());
		this.listView.setAdapter(adapter);
	
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<FriendActivity>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.listView.setVisibility(View.INVISIBLE);
		
		this.friendNews = null;
	}
	
	private static class FriendNewsLoader extends AsyncTaskLoader<ArrayList<FriendActivity>> {

		private ArrayList<FriendActivity> friendNews = null;
		private Context context = null;
		
		public FriendNewsLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public ArrayList<FriendActivity> loadInBackground() {
			// TODO Auto-generated method stub
			FriendsNewsNetRenderer renderer = new FriendsNewsNetRenderer(this.context, 30);
			try {
				friendNews = renderer.renderToList();
				//Log.d(Constant.LOG_TAG, "the friendNews.size is " + friendNews.size());
				return friendNews;
			} catch (AuthFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void deliverResult(ArrayList<FriendActivity> data) {
			// TODO Auto-generated method stub
			super.deliverResult(data);
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (this.friendNews != null) {
				this.deliverResult(this.friendNews);
			}
			else if (this.friendNews == null) {
				this.forceLoad();
			}
		}

		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.friendNews = null;
		}
		
		
		
		
		
	}

}
