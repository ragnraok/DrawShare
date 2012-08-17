package com.drawshare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.drawshare.R;
import com.drawshare.Request.Constant;
import com.drawshare.asyncloader.AsyncImageLoader;
import com.drawshare.asyncloader.AsyncImageLoader.ImageLoadListener;

public abstract class BaseAsyncAdapter<T> extends BaseAdapter{

	protected AbsListView listOrGridView = null;
	protected ArrayList<T> dataSet = null; // this data set not contain the url
	protected int imageViewId = -1;
	protected Context context = null;
	protected AsyncImageLoader.ImageLoadListener listener = null;
	protected int layoutId = -1;
	protected AsyncImageLoader imageLoader = null;
	private LayoutInflater inflater = null;
	
	public BaseAsyncAdapter(final Context context, AbsListView view, ArrayList<T> dataSet, final int imageViewId, 
			boolean netStatus, int layoutId) {
		this.listOrGridView = view;
		this.dataSet = dataSet;
		this.imageViewId = imageViewId;
		this.context = context;
		this.layoutId = layoutId;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = new AsyncImageLoader(netStatus);
		this.listener = new ImageLoadListener() {
			
			@Override
			public void onImageLoad(Integer rowNum, Bitmap bitmap) {
				// TODO Auto-generated method stub
				View view = listOrGridView.findViewWithTag(rowNum);
				if (view != null) {
					ImageView imageView = (ImageView) view.findViewById(imageViewId);
					imageView.setImageBitmap(bitmap);
				}
			}
			
			@Override
			public void onError(Integer rowNum) {
				// TODO Auto-generated method stub
				View view = listOrGridView.findViewById(rowNum);
				if (view != null) {
					ImageView imageView = (ImageView) view.findViewById(imageViewId);
					imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_pict_temp));
				}
			}
		};
		
		this.listOrGridView.setOnScrollListener(onScrollListener);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { // called 16 times..
		// TODO Auto-generated method stub
		//Log.d(Constant.LOG_TAG, "in getView");
		if (convertView == null) {
			convertView = inflater.inflate(this.layoutId, parent, false);
		}
		setViewTag(position, convertView);
		setImage(position, convertView);
		bindView(position, convertView);
		return convertView;
	}
	
	private void setViewTag(int position, View convertView) {
		convertView.setTag(position);
	}
	
	public void loadImage(){
		int start = this.listOrGridView.getFirstVisiblePosition();
		int end = this.listOrGridView.getLastVisiblePosition();
		if(end >= getCount()){
			end = getCount() -1;
		}
		this.imageLoader.setLoadRowLimit(start, end);
		this.imageLoader.unlock();
	}
	
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				imageLoader.lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				loadImage();
				//loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				imageLoader.lock();
				break;

			default:
				break;
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * 设置其他view的子组件
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	protected abstract void bindView(int position, View convertView);
	
	/**
	 * 用imageLoader来加载图片
	 * @param position
	 * @param convertView
	 */
	protected abstract void setImage(int position, View convertView);
	

}
