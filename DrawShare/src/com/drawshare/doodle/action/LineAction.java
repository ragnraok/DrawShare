package com.drawshare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

public class LineAction extends Action {
	
	private int color = 0;
	private Path path = null;
	private float size = 0;
	
	public LineAction(int color) {
		//super(color);
		this.color = color;
		this.path = new Path();
	}
	
	public LineAction(float startX, float startY, float size, int color) {
		//super(color);
		this.color = color;
		this.path = new Path();
		this.size = size;
		this.startX = startX;
		this.startY = startY;
		
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(this.color);
		mPaint.setStrokeWidth(this.size);
		mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
		
		path.moveTo(startX, startY);
		path.lineTo(startX, startY);
	}

	@Override
	public void draw(Canvas canvas) {
        //Log.d("Ragnarok", "the color is " + this.color);
        canvas.drawPath(this.path, mPaint);
	}

	@Override
	public void move(float x, float y) {
		this.path.quadTo(startX, startY, (startX + x) / 2, (startY + y) / 2);
		this.startX = x;
		this.startY = y;
		//this.path.lineTo(x, y);
	}
}
