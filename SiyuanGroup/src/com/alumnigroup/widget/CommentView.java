package com.alumnigroup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class CommentView extends LinearLayout {
	private BaseAdapter mBaseAdapter;
	public CommentView(Context context) {
		super(context);
	}

	public CommentView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private void init(){
		this.setOrientation(LinearLayout.VERTICAL);
		if(mBaseAdapter==null)throw new IllegalArgumentException("you have to set a adapter for this widget!");
		this.removeAllViews();
		for(int i=0;i<mBaseAdapter.getCount();i++){
			View convertView = null;
			this.addView(mBaseAdapter.getView(i, convertView, this), i);
		}
	 	requestLayout();
		invalidate();
	}
	
	public void setAdapter(BaseAdapter adapter){
		this.mBaseAdapter = adapter;
		init();
	}

}
