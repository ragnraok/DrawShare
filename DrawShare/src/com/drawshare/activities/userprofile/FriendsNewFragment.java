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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseUserFragment;
import com.drawshare.activities.base.HotestPictureActivity;
import com.drawshare.adapter.FriendsNewAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.render.netRenderer.FriendsNewsNetRenderer;
import com.drawshare.render.object.FriendActivity;
import com.drawshare.util.DrawShareUtil;

public class FriendsNewFragment extends BaseUserFragment implements LoaderCallbacks<ArrayList<FriendActivity>> {

	private ArrayList<FriendActivity> friendNews = null;
	
	private ListView listView = null;
	private ProgressBar progressBar = null;
	private FriendsNewAdapter adapter = null;
	private boolean netStatus = false;
	private boolean ifFinishLoad = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		this.netStatus = application.getNetworkState();
		if (netStatus)
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
	public Loader<ArrayList<FriendActivity>> onCreateLoader(int arg0,
			Bundle arg1) {
		// TODO Auto-generated method stub
		return new FriendNewsLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<FriendActivity>> loader,
			ArrayList<FriendActivity> data) {
		// TODO Auto-generated method stub
		this.friendNews = data;
		Log.d(Constant.LOG_TAG, "the data.size is " + data.size());
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		if (ifFinishLoad == false) {
			adapter = new FriendsNewAdapter(this, listView, data, application.getNetworkState());
			ifFinishLoad = true;
		}
		this.listView.setAdapter(adapter);
		
		this.progressBar.setVisibility(View.INVISIBLE);
		this.listView.setVisibility(View.VISIBLE);
	
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<FriendActivity>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.listView.setVisibility(View.INVISIBLE);
		
		this.friendNews = null;
		this.ifFinishLoad = false;
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
				friendNews = null;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				friendNews = null;
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
					DrawShareUtil.logout(FriendsNewFragment.this.getActivity());
					// go to hotest pictures activity
					Intent intent = new Intent(FriendsNewFragment.this.getActivity(), HotestPictureActivity.class);
					startActivity(intent);
					FriendsNewFragment.this.getActivity().finish();
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
