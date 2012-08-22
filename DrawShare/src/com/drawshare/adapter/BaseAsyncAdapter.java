package com.drawshare.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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
	protected int defaultImageViewId = -1;
	protected Context context = null;
	protected AsyncImageLoader.ImageLoadListener defaultListener = null;
	protected int layoutId = -1;
	protected AsyncImageLoader defaultImageLoader = null;
	private LayoutInflater inflater = null;
	
	protected View[] viewList = null;
	protected Bitmap[] defaultBitmapList = null;
	
	public BaseAsyncAdapter(final Context context, AbsListView view, final int defaultImageViewId, boolean netStatus, 
			int layoutId) {
		this.listOrGridView = view;
		this.defaultImageViewId = defaultImageViewId;
		this.context = context;
		this.layoutId = layoutId;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.defaultImageLoader = new AsyncImageLoader(netStatus);
		this.setDefaultListener();
		
		this.listOrGridView.setOnScrollListener(onScrollListener);
		
	}
	
	public BaseAsyncAdapter(final Context context, AbsListView view, ArrayList<T> dataSet, final int defaultImageViewId, 
			boolean netStatus, int layoutId) {
		this.listOrGridView = view;
		this.dataSet = dataSet;
		this.defaultImageViewId = defaultImageViewId;
		this.context = context;
		this.layoutId = layoutId;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.defaultImageLoader = new AsyncImageLoader(netStatus);
		setDefaultListener();
		
		this.viewList = new View[this.dataSet.size()];
		this.defaultBitmapList = new Bitmap[this.dataSet.size()];
		
		this.listOrGridView.setOnScrollListener(onScrollListener);
	}
	
	/**
	 * you can set more than one listener here
	 */
	public abstract void setListener();
	
	/**
	 * set the default listener, that is, the first listener
	 */
	protected void setDefaultListener() {
		this.defaultListener = new ImageLoadListener() {
				
				@Override
				public void onImageLoad(Integer rowNum, Bitmap bitmap) {
					// TODO Auto-generated method stub
					//Log.d(Constant.LOG_TAG, "on ImageLoad " + rowNum);
					View view = listOrGridView.findViewWithTag(rowNum);
					if (view != null) {
						ImageView imageView = (ImageView) view.findViewById(defaultImageViewId);
						imageView.setImageBitmap(bitmap);
						viewList[rowNum]  = view;
						
						if (defaultBitmapList[rowNum] == null) {
							defaultBitmapList[rowNum] = bitmap;
							Log.d(Constant.LOG_TAG, "set the defaultBitmapList " + rowNum);
						}
					}
				}
				
				@Override
				public void onError(Integer rowNum) {
					// TODO Auto-generated method stub
					View view = listOrGridView.findViewById(rowNum);
					if (view != null) {
						ImageView imageView = (ImageView) view.findViewById(defaultImageViewId);
						imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_pict_temp));
					}
				}
			};
	}
	
	/*
	 * 设置数据，需要在setAdapter之前调用
	 */
	public void setData(ArrayList<T> dataSet) {
		this.dataSet = dataSet;
		
		this.viewList = new View[this.dataSet.size()];
		defaultBitmapList = new Bitmap[this.dataSet.size()];
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
		if (this.viewList[position] != null) {
			//Log.d(Constant.LOG_TAG, "return from viewList");
			return viewList[position];
		}
		
		if (convertView == null) {
			convertView = inflater.inflate(this.layoutId, parent, false);
		}
		setViewTag(position, convertView);
		setImage(position, convertView);
		bindView(position, convertView);
		this.viewList[position] = convertView;
		return convertView;
	}
	
	private void setViewTag(int position, View convertView) {
		convertView.setTag(position);
	}
	
	public void setLoadImageLimit(){
		int start = this.listOrGridView.getFirstVisiblePosition();
		int end = this.listOrGridView.getLastVisiblePosition();
		if(end >= getCount()){
			end = getCount() -1;
		}
		this.defaultImageLoader.setLoadRowLimit(start, end);
		this.defaultImageLoader.unlock();
	}
	
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				defaultImageLoader.lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				setLoadImageLimit();
				//loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				defaultImageLoader.lock();
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
	
	public void stopLoad() {
		this.defaultImageLoader.lock();
	}
	
	public void resumeLoad() {
		this.defaultImageLoader.unlock();
	}
	

}
