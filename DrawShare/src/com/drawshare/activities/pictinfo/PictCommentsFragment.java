package com.drawshare.activities.pictinfo;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.PictureOrUserNotExistException;
import com.drawshare.Request.picture.PictureComment;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.adapter.PictCommentAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.render.netRenderer.PictCommentNetRenderer;
import com.drawshare.render.object.Comment;
import com.drawshare.util.DrawShareConstant;

public class PictCommentsFragment extends BaseFragment implements LoaderCallbacks<ArrayList<Comment>>, OnClickListener {

	private ListView listView = null;
	private ProgressBar progressBar = null;
	private EditText pictCommentEditText = null;
	private Button commentButton = null;
	
	private String pictureId = null;
	private ArrayList<Comment> commentList = null;
	
	private PictCommentAdapter adapter = null;
	
	private ProgressDialog progressDialog = null;
	private boolean ifLoadFinish = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		this.pictureId = this.getActivity().getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.PICT_ID);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		
		if (application.getNetworkState()) {
			this.getLoaderManager().initLoader(0, null, this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_pict_comment, container, false);
		listView = (ListView) view.findViewById(R.id.pict_comment_list);
		progressBar = (ProgressBar) view.findViewById(R.id.pict_comment_progress_bar);
		pictCommentEditText = (EditText) view.findViewById(R.id.pict_comment_comment_edit);
		commentButton = (Button) view.findViewById(R.id.pict_comment_comment_button);
		
		commentButton.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//this.getLoaderManager().getLoader(0).stopLoading();
		if (this.adapter != null) {
			//this.adapter.stopLoad();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//this.getLoaderManager().getLoader(0).startLoading();
		if (this.adapter != null) {
			//this.adapter.resumeLoad();
		}
	}

	@Override
	public Loader<ArrayList<Comment>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new PictCommentLoader(this.getActivity(), this.pictureId);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Comment>> loader,
			ArrayList<Comment> data) {
		// TODO Auto-generated method stub
		
		// set the adapter
		this.commentList = data;
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		if (data != null) {
			if (ifLoadFinish == false) {
				this.adapter = new PictCommentAdapter(this.getActivity(), this.listView, data, application.getNetworkState());
				this.listView.setAdapter(adapter);
				ifLoadFinish = true;
			}
		}
		
		this.progressBar.setVisibility(View.INVISIBLE);
		this.listView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Comment>> arg0) {
		// TODO Auto-generated method stub
		
		// reset the adapter
		this.commentList = null;
		this.progressBar.setVisibility(View.VISIBLE);
		this.listView.setVisibility(View.INVISIBLE);
		this.ifLoadFinish = false;
	}
	
	private static class PictCommentLoader extends AsyncTaskLoader<ArrayList<Comment>> {

		private String pictId = null;
		private Context context = null;
		private ArrayList<Comment> data = null;
 		
		public PictCommentLoader(Context context, String pictId) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.pictId = pictId;
		}

		@Override
		public ArrayList<Comment> loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			PictCommentNetRenderer renderer = new PictCommentNetRenderer(this.pictId);
			
			try {
				this.data = renderer.renderToList();
				if (this.data != null) {
					return this.data;
				}
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
				this.forceLoad();
			}
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.pict_comment_comment_button) {
			if (this.pictCommentEditText.getText().toString().length() == 0) {
				Toast.makeText(this.getActivity(), getString(R.string.no_comment), Toast.LENGTH_LONG).show();
			}
			else {
				progressDialog = ProgressDialog.show(this.getActivity(), getString(R.string.waiting_title), "");
				final String pictCommentString = pictCommentEditText.getText().toString();
				
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						progressDialog.dismiss();
						if (msg.what == 1) {
							if ((Boolean)msg.obj) {
								Toast.makeText(PictCommentsFragment.this.getActivity(), 
										getString(R.string.comment_success), Toast.LENGTH_LONG).show();
								PictCommentsFragment.this.getLoaderManager().restartLoader(0, null, PictCommentsFragment.this);
							}
							else {
								Toast.makeText(PictCommentsFragment.this.getActivity(), 
										getString(R.string.comment_failed), Toast.LENGTH_LONG).show();
							}
						}
					}
					
				};
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean success = false;
						try {
							success = PictureComment.addPictComment(UserIdHandler.getUserId(PictCommentsFragment.this.getActivity()), 
									pictureId, ApiKeyHandler.getApiKey(PictCommentsFragment.this.getActivity()), 
									pictCommentString, 0);
						} catch (AuthFailException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (PictureOrUserNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;	
						}
						Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.obj = success;
						handler.sendMessage(msg);
					}
				}).start();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return false;
		switch (item.getItemId()) {
		case R.id.base_fragment_menu_reload:
			this.getLoaderManager().restartLoader(0, null, this);
			break;
		}
		return true;
	}
}
