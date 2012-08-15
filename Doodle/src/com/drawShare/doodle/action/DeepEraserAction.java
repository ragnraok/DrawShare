package com.drawShare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class DeepEraserAction extends Action {
	
	private Path path = null;
	private float size;
	private float startX = 0;
	private float startY = 0;
	private float endX = 0;
	private float endY = 0;
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
		
		this.path = new Path();
		path.moveTo(startX, startY);
		path.lineTo(startX, startY);
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(this.size);
		paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setColor(this.color);
		canvas.drawPath(this.path, paint);
	}

	@Override
	public void move(float x, float y) {
		// TODO Auto-generated method stub
		super.move(x, y);
		path.lineTo(x, y);
	}
	
}
