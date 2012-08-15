package com.drawShare.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class EraserAction extends Action {
	
	private Path path = null;
	private float size;
	private float startX = 0;
	private float startY = 0;
	private float endX = 0;
	private float endY = 0;
	private int color = 0;

	public EraserAction(float startX, float startY, float size, int color) {
		//super(startX, startY, size, color);
		// TODO Auto-generated constructor stub
		this.color = color;
		this.size = size;
		this.path = new Path();
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		path.moveTo(startX, startY);
		path.quadTo(startX, startY, (startX + endX) / 2, (startY + endY) / 2);
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
		paint.setStrokeCap(Paint.Cap.SQUARE);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		canvas.drawPath(this.path, paint);
	}

	@Override
	public void move(float x, float y) {
		// TODO Auto-generated method stub
		super.move(x, y);
		this.path.quadTo(x, y, (startX + x) / 2, (startY + y) / 2);
	}

}
