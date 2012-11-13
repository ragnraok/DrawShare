package com.drawshare.activities.pictinfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.picture.PictEdit;
import com.drawshare.activities.base.BaseFragment;
import com.drawshare.application.DrawShareApplication;
import com.drawshare.util.DrawShareConstant;

public class PictShowFragment extends BaseFragment implements LoaderCallbacks<Bitmap>{

	private String pictId = null;
	private ImageView pictImageView = null;
	private ProgressBar progressBar = null;
	private TextView pictTitleTextView = null;
	private TextView pictDateTextView = null;
	
	private Bitmap pict = null;
	private static String title = null;
	private static String date = null;
	 
	private boolean netStatus = false;
	private boolean ifLoadFinish = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		this.pictId = this.getActivity().getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.PICT_ID);
		
		DrawShareApplication application = (DrawShareApplication) this.getActivity().getApplication();
		this.netStatus = application.getNetworkState();
		
		if (netStatus) {
			this.getLoaderManager().initLoader(0, null, this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_pict_show	, container, false);
		this.progressBar = (ProgressBar) view.findViewById(R.id.pict_show_progress_bar);
		this.pictImageView = (ImageView) view.findViewById(R.id.pict_show_pict_image);
		this.pictDateTextView = (TextView) view.findViewById(R.id.pict_show_pict_date_text);
		this.pictTitleTextView = (TextView) view.findViewById(R.id.pict_show_pict_name_text);
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (netStatus) {
			//if (this.pict != null) {
			//	this.pictImageView.setImageBitmap(this.pict);
			//}
			//else {
			//	this.getLoaderManager().getLoader(0).stopLoading();
			//}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (netStatus) {
			//if (this.pict != null) {
			//	this.pictImageView.setImageBitmap(this.pict);
			//}
			//else {
			//	this.getLoaderManager().getLoader(0).startLoading();
			//}
		}
	}

	@Override
	public Loader<Bitmap> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new PictLoader(this.getActivity(), this.pictId);
	}

	@Override
	public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
		// TODO Auto-generated method stub
		//if (ifLoadFinish == false) {
			this.pict = data;
			this.pictImageView.setImageBitmap(this.pict);
			//Log.d(Constant.LOG_TAG, "set the pictBitmap");
			if (this.date != null)
				this.pictDateTextView.setText(date);
			
			if (this.title != null)
				this.pictTitleTextView.setText(title);
			
			this.progressBar.setVisibility(View.INVISIBLE);
			this.pictImageView.setVisibility(View.VISIBLE);
			ifLoadFinish = true;
		//}
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> arg0) {
		// TODO Auto-generated method stub
		this.pict = null;
		
		this.progressBar.setVisibility(View.VISIBLE);
		this.pictImageView.setVisibility(View.INVISIBLE);
		ifLoadFinish = false;
	}
	
	private static class PictLoader extends AsyncTaskLoader<Bitmap> {

		private Context context = null;
		private String pictId = null;
		private String pictureUrl = null;
		private Bitmap pictBitmap = null;
		
		public PictLoader(Context context, String pictId) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.pictId = pictId;
		}

		@Override
		public Bitmap loadInBackground() {
			// TODO Auto-generated method stub
			//return null;
			this.pictureUrl = getPictURL();
			
			this.pictBitmap = Util.urlToBitmap(pictureUrl, DrawShareConstant.DEFAULT_IMAGE_SIZE);
			
			if (pictBitmap != null) {
				Log.d(Constant.LOG_TAG, "get the pictBitmap");
				return pictBitmap;
			}
			else {
				pictBitmap = null;
				return null;
			}
		}
		
		
		@Override
		protected void onReset() {
			// TODO Auto-generated method stub
			super.onReset();
			this.pictBitmap = null;
			this.pictureUrl = null;
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (this.pictBitmap != null) {
				this.deliverResult(pictBitmap);
			}
			else {
				this.forceLoad();
			}
		}
		
	
		@Override
		protected void onStopLoading() {
			// TODO Auto-generated method stub
			super.onStopLoading();
		}

		private String getPictURL() {
			try {
				JSONObject jsonObject = PictEdit.getPictInfo(this.pictId);
				String pictUrl = jsonObject.getString("picture_url");
				title = jsonObject.getString("title");
				date = jsonObject.getString("picture_create_date");
				return pictUrl;
			} catch (PictureNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.base_fragment_menu_reload:
			this.getLoaderManager().restartLoader(0, null, this);
			break;
		}
		return true;
	}
}
