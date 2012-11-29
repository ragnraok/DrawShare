package com.drawshare.activities.userprofile;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.drawshare.activities.pictinfo.PictInfoActivity;
import com.drawshare.adapter.FollowMessageAdapter;
import com.drawshare.adapter.ForkMessageAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.FollowMessageNetRenderer;
import com.drawshare.render.netRenderer.ForkMessageNetRenderer;
import com.drawshare.render.object.FollowMessage;
import com.drawshare.render.object.ForkMessage;
import com.drawshare.util.DrawShareConstant;

public class ForkMessageFragment extends BaseFragment implements LoaderCallbacks<ArrayList<ForkMessage>>, OnItemClickListener {
	private ProgressBar progressBar = null;
	private ListView listView = null;
	
	private ForkMessageAdapter adapter = null;
	private ArrayList<ForkMessage> messageList = null;
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
		
		this.listView.setOnItemClickListener(this);
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
	public Loader<ArrayList<ForkMessage>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new ForkMessageLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ForkMessage>> loader,
			ArrayList<ForkMessage> data) {
		// TODO Auto-generated method stub
		if (data != null) {
			this.progressBar.setVisibility(View.INVISIBLE);
			
			this.messageList = data;
			DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
			if (this.messageList != null) {
				this.adapter = new ForkMessageAdapter(this.getActivity(), listView, messageList, application.getNetworkState());
				this.listView.setAdapter(adapter);
				this.listView.setVisibility(View.VISIBLE);
			}
		}
		else {
			Toast.makeText(this.getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ForkMessage>> arg0) {
		// TODO Auto-generated method stub
		this.progressBar.setVisibility(View.VISIBLE);
		this.listView.setVisibility(View.INVISIBLE);
		
		this.messageList = null;
		this.adapter = null;
	}
	
	private static class ForkMessageLoader extends AsyncTaskLoader<ArrayList<ForkMessage>> {

		private ArrayList<ForkMessage> data = null;
		private Context context = null;
		
		public ForkMessageLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public ArrayList<ForkMessage> loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			ForkMessageNetRenderer renderer = new ForkMessageNetRenderer(UserIdHandler.getUserId(context), 
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		if (this.messageList != null) {
			ForkMessage message = this.messageList.get(position);
			Intent intent = new Intent(this.getActivity(), PictInfoActivity.class);
			intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, UserIdHandler.getUserId(this.getActivity()));
			intent.putExtra(DrawShareConstant.EXTRA_KEY.PICT_ID, message.resultPictId);
			startActivity(intent);
		}
	}
	
}
