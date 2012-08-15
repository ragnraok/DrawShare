package com.drawShare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleAction extends Action {
	
	private float startX = 0;
	private float startY = 0;
	private float endX = 0;
	private float endY = 0;
	private float radius = 0;
	private float size = 0;
	private int color = 0;
	
	public CircleAction(int color) {
		//super(color);
		this.color = color;
	}

	public CircleAction(float startX, float startY, float size, int color) {
		//super(startX, startY, size, color);
		// TODO Auto-generated constructor stub
		this.color = color;
		this.startX = startX;
		this.startY = startY;
		this.size = size;
		this.endX = startX;
		this.endY = startY;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE); // 空心
		paint.setColor(this.color);
		paint.setStrokeWidth(this.size);
		
		canvas.drawCircle((this.startX + this.endX) / 2, (this.startY + this.endY) / 2, this.radius, paint);
	}

	@Override
	public void move(float x, float y) {
		// TODO Auto-generated method stub
		super.move(x, y);
		this.endX = x;
		this.endY = y;
		this.radius = (float)((Math.sqrt((x - startX) * (x - startX) + (y - startY) * (y - startY))) / 2.0);
	}
	
	

}
