package com.drawshare.activities.userprofile;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.drawshare.R;
import com.drawshare.activities.base.BaseFragmentActivity;
import com.drawshare.adapter.TabsAdapter;

public class UserMessageActivity extends BaseFragmentActivity implements OnTabChangeListener {

	private ViewPager viewPager = null;
	private TabHost tabHost = null;
	private TabsAdapter tabsAdapter = null;
	
	private ArrayList<Drawable> notSeletDrawables = new ArrayList<Drawable>();
	private ArrayList<Drawable> selectDrawables = new ArrayList<Drawable>()	;
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<String> tabTagList = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        
        findAllView();
        tabHost.setup();
        tabsAdapter = new TabsAdapter(this, tabHost, viewPager);
        initDrawableList();
        initTagList();
        
        tabsAdapter.addTab(tabHost.newTabSpec(tabTagList.get(0)).setIndicator(generateIndicatorView(0)), 
        		FollowMessageFragment.class, null);
        tabsAdapter.addTab(tabHost.newTabSpec(tabTagList.get(1)).setIndicator(generateIndicatorView(1)), 
        		ForkMessageFragment.class, null);
        
        this.viewList.get(0).setBackgroundDrawable(selectDrawables.get(0));
        this.tabHost.setCurrentTab(0);
        
        tabHost.setOnTabChangedListener(this);
    }
    
    private void findAllView() {
    	tabHost = (TabHost) findViewById(android.R.id.tabhost);   
        viewPager = (ViewPager) findViewById(R.id.user_message_view_pager);
    }
    
    private void initDrawableList() {
    	Resources resources = getResources();
    	notSeletDrawables.add(resources.getDrawable(R.drawable.follow_message_1));
    	notSeletDrawables.add(resources.getDrawable(R.drawable.fork_message_1));
    	selectDrawables.add(resources.getDrawable(R.drawable.follow_message_2));
    	selectDrawables.add(resources.getDrawable(R.drawable.fork_message_2));
    }
    
    private void initTagList() {
    	tabTagList.add("follow_message");
    	tabTagList.add("fork_message");
    }
    
    private View generateIndicatorView(int index) {
    	LayoutInflater inflater = this.getLayoutInflater();
    	View view = inflater.inflate(R.layout.tab_indicator_view, null, false);
    	view.setBackgroundDrawable(notSeletDrawables.get(index));
    	
    	viewList.add(view);
    	
    	return view;
    }

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int index = this.tabTagList.indexOf(tabId);
		
		for (int i = 0; i < viewList.size(); i++) {
			this.viewList.get(i).setBackgroundDrawable(this.notSeletDrawables.get(i));
		}
		this.viewList.get(index).setBackgroundDrawable(this.selectDrawables.get(index));
		
		this.viewPager.setCurrentItem(index);
	}
    
}
