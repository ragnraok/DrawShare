package com.drawshare.activities.userprofile;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.adapter.DrawHallAdapter;
import com.drawshare.adapter.FollowMessageAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.FollowMessageNetRenderer;
import com.drawshare.render.object.FollowMessage;
import com.drawshare.util.DrawShareConstant;

public class FollowMessageFragment extends BaseFragment implements LoaderCallbacks<ArrayList<FollowMessage>> {
	
	private ProgressBar progressBar = null;
	private ListView listView = null;
	
	private FollowMessageAdapter adapter = null;
	private ArrayList<FollowMessage> messageList = null;
	private boolean netStatus = false;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		netStatus = application.getNetworkState();
		if (netStatus) {
			getLoaderManager().initLoader(0, null, this);
		}
		else {
			Toast.makeText(getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_message, null);
		listView = (ListView) view.findViewById(R.id.user_message_list);
		progressBar = (ProgressBar) view.findViewById(R.id.user_message_progress_bar);
		return view;
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
	public Loader<ArrayList<FollowMessage>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new FollowMessageLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<FollowMessage>> loader,
			ArrayList<FollowMessage> data) {
		// TODO Auto-generated method stub
		if (data != null) {
			this.progressBar.setVisibility(View.INVISIBLE);
			
			this.messageList = data;
			DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
			if (this.messageList != null) {
				this.adapter = new FollowMessageAdapter(this.getActivity(), listView, messageList, application.getNetworkState());
				this.listView.setAdapter(adapter);
				this.listView.setVisibility(View.VISIBLE);
			}
		} 
		else {
			Toast.makeText(this.getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<FollowMessage>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.listView.setVisibility(View.INVISIBLE);
		
		this.messageList = null;
		this.adapter = null;
	}
	
	private static class FollowMessageLoader extends AsyncTaskLoader<ArrayList<FollowMessage>> {

		private ArrayList<FollowMessage> data = null;
		private Context context = null;
		
		public FollowMessageLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public ArrayList<FollowMessage> loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			FollowMessageNetRenderer renderer = new FollowMessageNetRenderer(UserIdHandler.getUserId(context), 
					ApiKeyHandler.getApiKey(context), 30);
			try {
				this.data = renderer.renderToList();
				if (this.data != null) {
					return data;
				}
			} catch (AuthFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.data = null;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.data = null;
			} catch (PictureNotExistException e) {
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
				forceLoad();
			}
		}
		
		
		
	}

	
}
