package com.drawShare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

public class LineAction extends Action {
	
	private int color = 0;
	private Path path = null;
	private float size = 0;
	private float startX = 0;
	private float startY = 0;
	
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
		path.moveTo(startX, startY);
		path.lineTo(startX, startY);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(this.color);
		paint.setStrokeWidth(this.size);
		paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //Log.d("Ragnarok", "the color is " + this.color);
        canvas.drawPath(this.path, paint);
	}

	@Override
	public void move(float x, float y) {
		// TODO Auto-generated method stub
		//this.path.quadTo(x, y, (float)((x + startX) / 2.0), (float)((y + startY) / 2.0));
		this.path.lineTo(x, y);
	}
}
