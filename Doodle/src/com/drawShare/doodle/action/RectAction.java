package com.drawShare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class RectAction extends Action{
	
	private float startX = 0;
	private float startY = 0;
	private float endX = 0;
	private float endY = 0;
	private float size;
	private int color = 0;
	
	public RectAction(int color) {
		//super(color);
		this.color = color;
	}

	public RectAction(float startX, float startY, float size, int color) {
		//super(startX, startY, size, color);
		// TODO Auto-generated constructor stub
		this.color = color;
		this.startX = startX;
		this.startY = startY;
		this.endX = startX;
		this.endY = startY;
		this.size = size;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(this.color);
		paint.setStrokeWidth(this.size);
		canvas.drawRect(startX, startY, endX, endY, paint);
	}

	@Override
	public void move(float x, float y) {
		// TODO Auto-generated method stub
		super.move(x, y);
		this.endX = x;
		this.endY = y;
	}

}
