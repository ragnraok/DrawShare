package com.drawshare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class DeepEraserAction extends Action {
	
	private Path path = null;
	private float size;
	private int color = 0;
	
	public DeepEraserAction(float startX, float startY, float size, int color) {
		//super(startX, startY, size, color);
		// TODO Auto-generated constructor stub
		this.color = color;
		this.startX = startX;
		this.startY = startY;
		this.size = size;
		this.endX = startX;
		this.endY = startY;
		
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(this.size);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(this.color);
		
		this.path = new Path();
		path.moveTo(startX, startY);
		path.lineTo(startX, startY);
	}
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawPath(this.path, mPaint);
	}

	@Override
	public void move(float x, float y) {
		// TODO Auto-generated method stub
		super.move(x, y);
		//path.lineTo(x, y);
		this.path.quadTo(startX, startY, (startX + x) / 2, (startY + y) / 2);
		this.startX = x;
		this.startY = y;
	}
	
}
