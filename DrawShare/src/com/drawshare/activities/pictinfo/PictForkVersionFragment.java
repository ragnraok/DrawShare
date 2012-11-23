package com.drawshare.activities.pictinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.activities.userprofile.OtherUserIndexActivity;
import com.drawshare.adapter.PictForkAdapter;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.asyncloader.AsyncImageLoader;
import com.drawshare.render.netRenderer.PictForkVersionNetRenderer;
import com.drawshare.render.object.Picture;
import com.drawshare.util.DrawShareConstant;

public class PictForkVersionFragment extends BaseFragment 
	implements OnClickListener, LoaderCallbacks<ArrayList<Picture>> {

	private ImageView sourceImageView = null;
	private ImageView sourceAvatarImageView = null;
	private TextView sourceUsernameTextView = null;
	private TextView sourceTitleTextView = null;
	private TextView sourceCreateDateTextView = null;
	
	private GridView forkGridView = null;
	private ProgressBar forkProgressBar = null;
	
	private String pictureId = null;
	private ArrayList<Picture> forkPictList = null;
	
	private static Picture sourcePict = null;
	
	private PictForkAdapter adapter = null;
	
	private AsyncImageLoader sourceImageLoader = null;
	private AsyncImageLoader sourceUserAvatarLoader = null;
	
	private boolean ifLoadFinish = false;
	private boolean ifLoadSourceFinish = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		this.pictureId = getActivity().getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.PICT_ID);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		if (application.getNetworkState()) {
			this.getLoaderManager().initLoader(0, null, this);
			//this.getLoaderManager().initLoader(1, null, this);
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_pict_fork, container, false);
		this.sourceImageView = (ImageView) view.findViewById(R.id.pict_fork_source_image);
		this.sourceAvatarImageView = (ImageView) view.findViewById(R.id.pict_fork_source_user_avatar_image);
		this.sourceTitleTextView = (TextView) view.findViewById(R.id.pict_fork_source_title_text);
		this.sourceCreateDateTextView = (TextView) view.findViewById(R.id.pict_fork_source_date_text);
		this.sourceUsernameTextView = (TextView) view.findViewById(R.id.pict_fork_source_username_text);
		this.forkGridView = (GridView) view.findViewById(R.id.pict_fork_grid);
		this.forkProgressBar = (ProgressBar) view.findViewById(R.id.pict_fork_progress_bar);
		
		sourceAvatarImageView.setOnClickListener(this);
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		this.sourceImageLoader = new AsyncImageLoader(application.getNetworkState());
		this.sourceUserAvatarLoader = new AsyncImageLoader(application.getNetworkState());
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.getLoaderManager().getLoader(0).stopLoading();
		if (this.adapter != null) {
			//adapter.stopLoad();
		}
		if (this.sourceImageLoader != null) {
			//sourceImageLoader.lock();
		}
		if (this.sourceUserAvatarLoader != null) {
			//sourceUserAvatarLoader.lock();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.getLoaderManager().getLoader(0).startLoading();
		if (this.adapter != null) {
			//adapter.resumeLoad();
		}
		if (this.sourceImageLoader != null) {
			//sourceImageLoader.unlock();
		}
		if (this.sourceUserAvatarLoader != null) {
		//	sourceUserAvatarLoader.unlock();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (sourcePict != null) {
			if (v.getId() == R.id.pict_fork_source_user_avatar_image) {
				Intent intent = new Intent(this.getActivity(), OtherUserIndexActivity.class);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.USER_ID, sourcePict.createUserId);
				//Log.d(Constant.LOG_TAG, "createUserId = " + sourcePict.createUserId);
				intent.putExtra(DrawShareConstant.EXTRA_KEY.IF_MYSELF, false);
				startActivity(intent);
			}
		}
	}

	@Override
	public Loader<ArrayList<Picture>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new PictForkVersionLoader(this.getActivity(), this.pictureId);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Picture>> loader,
			ArrayList<Picture> data) {
		// TODO Auto-generated method stub
		this.forkPictList = data;
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		if (data != null) {
			if (ifLoadFinish == false) {
				adapter = new PictForkAdapter(this.getActivity(), this.forkGridView, data, application.getNetworkState());
				ifLoadFinish = true;
			}
			this.forkGridView.setAdapter(adapter);
			this.forkGridView.setVisibility(View.VISIBLE);
			this.forkProgressBar.setVisibility(View.INVISIBLE);
		}
		else {
			Toast.makeText(this.getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
		if (sourcePict != null) {
				String createDateString = new SimpleDateFormat("yyyy-MM-dd").format(sourcePict.createDate);
				this.sourceCreateDateTextView.setText(createDateString);
				this.sourceTitleTextView.setText(sourcePict.title);
				this.sourceUsernameTextView.setText(sourcePict.creatUserName);
				
				sourceImageLoader.loadImage(0, sourcePict.pictURL, new AsyncImageLoader.ImageLoadListener() {
					
					@Override
					public void onImageLoad(Integer rowNum, Bitmap bitmap) {
						// TODO Auto-generated method stub
						sourceImageView.setImageBitmap(bitmap);
					}
					
					@Override
					public void onError(Integer rowNum) {
						// TODO Auto-generated method stub
						sourceImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_pict_temp));
						
					}
				}, DrawShareConstant.PICT_FROK_SOURCE_IMAGE_SIZE);
				
				this.sourceUserAvatarLoader.loadImage(0, sourcePict.createUserAvatarUrl, new AsyncImageLoader.ImageLoadListener() {
					
					@Override
					public void onImageLoad(Integer rowNum, Bitmap bitmap) {
						// TODO Auto-generated method stub
						sourceAvatarImageView.setImageBitmap(bitmap);
					}
					
					@Override
					public void onError(Integer rowNum) {
						// TODO Auto-generated method stub
						sourceAvatarImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_pict_temp));
					}
				}, DrawShareConstant.PICT_FORK_SOURCE_AVATAR_SIZE);		
		}
		else {
			Toast.makeText(this.getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Picture>> arg0) {
		// TODO Auto-generated method stub
		this.forkPictList = null;
		sourcePict = null;
		ifLoadFinish = false;
		ifLoadSourceFinish = false;
	}
	
	public void resetLoader() {
		this.forkPictList = null;
		sourcePict = null;
		ifLoadFinish = false;
		ifLoadSourceFinish = false;
		this.forkGridView.setVisibility(View.INVISIBLE);
		this.forkProgressBar.setVisibility(View.VISIBLE);
	}
	
	private static class PictForkVersionLoader extends AsyncTaskLoader<ArrayList<Picture>> {

		private Context context = null;
		private ArrayList<Picture> data = null;
		private String pictureId = null;
		
		public PictForkVersionLoader(Context context, String pictureId) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.pictureId = pictureId;
		}

		@Override
		public ArrayList<Picture> loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			PictForkVersionNetRenderer renderer = new PictForkVersionNetRenderer(pictureId);
			
			try {
				this.data = renderer.renderToList();
				sourcePict = renderer.renderToObject();
			} catch (AuthFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.data = null;
				sourcePict = null;
			} catch (UserNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.data = null;
				sourcePict = null;
			} catch (PictureNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.data = null;
				sourcePict = null;
			}
			return this.data;
		}

		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.data = null;
			//sourcePict = null;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.base_fragment_menu_reload:
			this.getLoaderManager().getLoader(0).reset();
			this.resetLoader();
			this.getLoaderManager().getLoader(0).startLoading();
			break;
		}
		return true;
	}
}
