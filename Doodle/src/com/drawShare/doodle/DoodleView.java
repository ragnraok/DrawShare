package com.drawShare.doodle;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.drawShare.doodle.action.Action;
import com.drawShare.doodle.action.CircleAction;
import com.drawShare.doodle.action.DeepEraserAction;
import com.drawShare.doodle.action.EraserAction;
import com.drawShare.doodle.action.LineAction;
import com.drawShare.doodle.action.RectAction;

public class DoodleView extends SurfaceView implements Runnable, Callback {
	
	public abstract class Mode {
		public static final int NONE_MODE = 0;
		public static final int LINE_MODE = 1;
		public static final int RECT_MODE = 2;
		public static final int CIRCLE_MODE = 3;
		public static final int ERASER_MODE = 4;
		public static final int DEEP_ERASER_MODE = 5;
	}
	
	private Bitmap background = null; // 背景图片
	private Bitmap surfaceBitmap = null; // 实际绘画的图片
	
	private boolean ifLoop = true; // 绘画线程结束标志位
	private LinkedList<Action> actionList = null; // 绘画动作的列表
	private Action curAction = null;
	private SurfaceHolder holder = null;
	//private Paint paint = null;
	private int curMode = 0;
	private int curIndex = 0; // actionList的最大绘画index，用于前进后退
	private int color = Color.BLUE; // 画笔的颜色
	private float size = 5; // 画笔的大小
	private int backgroundColor = Color.WHITE;
	
	// 放大缩小使用的变量
	private int multiMode = 0;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private PointF startPoint = new PointF();
	private PointF endPoint = new PointF();
	private PointF midPoint = new PointF();
	private float oldDis = 0;
	private float newDis = 0;
	private boolean ifEnableDragAndZoom = false;
	private static final float POINTER_DIS_LIMIT_MIN = 230;
	private static final float POINTER_DIS_LIMIT_MAX = 280;
	
	private float scale = 1;
	private float totalScale = 1;
	private Matrix tempMatrix = new Matrix();
	private Matrix saveMatrix = new Matrix();
	private float xTranslateDis = 0;
	private float yTranslateDis = 0;
	private float xMoveDis = 0;
	private float yMoveDis = 0;
	
	//private static final int MAX_SIZE = 10;
	private static final String LOG_TAG = "Ragnarok";
	
	private int backgroundOriginWidth = 0;
	private int backgroundOriginHeight = 0;
 
	public DoodleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DoodleView(Context context, Bitmap background) {
		super(context);
		this.background = background;
		//this.paint = new Paint();
		actionList = new LinkedList<Action>();
		this.curIndex = actionList.size();
		holder = this.getHolder();
		holder.addCallback(this);
		this.setFocusable(true);
		ifLoop = true;
		ifEnableDragAndZoom = false;
		scale = 1;
		surfaceBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Bitmap.Config.ARGB_8888);
		this.backgroundOriginHeight = background.getHeight();
		this.backgroundOriginWidth = background.getWidth();
		
		new Thread(this).start();
	}
	
	public void setMode(int mode) {
		this.curMode = mode;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setEnableDragAndZoom(boolean ifEnableDragAndZoom) {
		this.ifEnableDragAndZoom = ifEnableDragAndZoom;
	}
	
	public void setBrushSize(float size) {
		this.size = size;
	}
	
	public void setBackground(Bitmap background) {
		this.background = background;
	}
	
	public void clear() {
		this.actionList.clear();
		this.curIndex = this.actionList.size();
	}
	
	private void addAction(Action action) {
		actionList.add(action);
		curIndex = curIndex + 1 > actionList.size() ? actionList.size() : curIndex + 1;
	}
	
	public void rollBack() {
		curIndex = curIndex - 1 < 0 ? 0 : curIndex - 1;
		if (actionList.size() > 0)
			actionList.removeLast();
	}
	
	public boolean ifClear() {
		return actionList.size() > 0 ? false : true;
	}
	
	public void goForward() {
		curIndex = curIndex + 1 > actionList.size() ? actionList.size() : curIndex + 1;
	}
	
	public void Draw() {
		Canvas canvas = holder.lockCanvas();
		if (canvas == null || holder == null)
			return;
		
		canvas.drawColor(backgroundColor);
		canvas.setMatrix(saveMatrix);
		
		canvas.drawBitmap(background, 0, 0, null);
		
		this.surfaceBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas surfaceCanvas = new Canvas(surfaceBitmap);

		surfaceCanvas.drawColor(Color.TRANSPARENT);
		
		for (int i = 0; i < this.curIndex; i++) {
			this.actionList.get(i).draw(surfaceCanvas);
		}
		
		if (this.curAction != null) 
			curAction.draw(surfaceCanvas);
		
		canvas.drawBitmap(surfaceBitmap, 0, 0, null);
		
		holder.unlockCanvasAndPost(canvas);
	}
	
	private void setCurAction(float x, float y, float size, int color) {
		switch (this.curMode) {
		case DoodleView.Mode.CIRCLE_MODE:
			curAction = new CircleAction(x, y, size, color);
			break;
		case DoodleView.Mode.LINE_MODE:
			curAction = new LineAction(x, y, size, color);
			Log.d(LOG_TAG, "curAction is line mode");
			break;
		case DoodleView.Mode.RECT_MODE:
			curAction = new RectAction(x, y, size, color);
			break;
		case DoodleView.Mode.ERASER_MODE:
			curAction = new EraserAction(x, y, size, color);
			break;
		case DoodleView.Mode.DEEP_ERASER_MODE:
			curAction = new DeepEraserAction(x, y, size, this.backgroundColor);
			break;
		default:
			curAction = null;
			Log.d(LOG_TAG, "curAction is null");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if (action == MotionEvent.ACTION_CANCEL) {
			return false;
		}
		float x = event.getX();
		float y = event.getY();
		
		PointF drawPoint = getDrawPoint(x, y, xMoveDis, yMoveDis, midPoint, totalScale);
		float drawX = drawPoint.x;
		float drawY = drawPoint.y;
		
		Log.d(LOG_TAG, "drawX = " + drawX + ", drawY = " + drawY);
		
		if (event.getPointerCount() == 1) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				this.setCurAction(drawX, drawY, this.size, this.color);
				Log.d(LOG_TAG, "action down, pointer count is " + event.getPointerCount());
				if (this.ifEnableDragAndZoom) {
					this.multiMode = DRAG;
					startPoint.set(x, y);
					tempMatrix.set(saveMatrix);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (curAction != null) {
					curAction.move(drawX, drawY);
					//Log.d(LOG_TAG, "move to " + x + ", " + y);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (curAction != null) {
					curAction.move(drawX, drawY);
					//Log.d(LOG_TAG, "move to " + x + ", " + y);
					this.addAction(curAction);
					curAction = null;
				}
				break;
			}
			return true;
		}
		else if (event.getPointerCount() >= 2 && ifEnableDragAndZoom) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (this.multiMode == DRAG) {
					xMoveDis = xMoveDis + xTranslateDis;
					yMoveDis = yMoveDis + yTranslateDis;
					Log.d(LOG_TAG, "xMoveDis = " + xMoveDis + ", yMoveDis = " + yMoveDis);
				}
				else if (this.multiMode == ZOOM) {
					totalScale = totalScale * scale;
					Log.d(LOG_TAG, "totalScale = " + totalScale);
				}
				
				this.multiMode = NONE;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				Log.d(LOG_TAG, "action_pointer_down");
				oldDis = getDistance(new PointF(event.getX(0), event.getY(0)), new PointF(event.getX(1), event.getY(1)));
				Log.d(LOG_TAG, "oldDis is " + oldDis);
				if (POINTER_DIS_LIMIT_MIN < oldDis && oldDis > POINTER_DIS_LIMIT_MAX) {
					this.multiMode = ZOOM;
					Log.d(LOG_TAG, "set to zoom mode");
					tempMatrix.set(saveMatrix);
					midPoint = getMidPoint(midPoint, new PointF(x, y));
				}
				break;
			case MotionEvent.ACTION_MOVE:
					if (this.multiMode == DRAG) {
						//Log.d(LOG_TAG, "drag the view");
						xTranslateDis = x - startPoint.x;
						yTranslateDis = y - startPoint.y;
						Log.d(LOG_TAG, "xTranslateDis=" + xTranslateDis + ", yTranslateDis=" + yTranslateDis);
						
						saveMatrix.set(tempMatrix);						
						saveMatrix.postTranslate(x - startPoint.x, y - startPoint.y);
					}
					else if (this.multiMode == ZOOM) {
						newDis = getDistance(new PointF(event.getX(0), event.getY(0)),
								new PointF(event.getX(1), event.getY(1))
								);
						Log.d(LOG_TAG, "newDis is " + newDis);
						if (POINTER_DIS_LIMIT_MIN < newDis && newDis > POINTER_DIS_LIMIT_MAX) {
							scale = newDis / oldDis; 
							saveMatrix.set(tempMatrix);
							saveMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
							Log.d(LOG_TAG, "scale is " + scale);
							
						}
						
					}
				break;
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private PointF getDrawPoint(float x, float y, float xMoveDis, float yMoveDis, PointF zoomMidPoint, float scale) {
		float drawX = x - xMoveDis;
		float drawY = y - yMoveDis;
		
		float dis = getDistance(new PointF(x, y), zoomMidPoint);
		Log.d("dis", "dis = " + dis);
		float scaleDis = dis * scale;
		Log.d("dis", "scaleDis = " + scaleDis);
		
		//if (scale != 1) {
		//	drawX = drawX + scaleDis;
		//	drawY = drawY + scaleDis;
		//}
		Log.d("dis", "zoomMidPoint.x = " + zoomMidPoint.x + ", zoomMidPoint.y = " + zoomMidPoint.y);
		return new PointF(drawX, drawY);
	}
	
	private float getDistance(PointF p1, PointF p2) {
		float xDis = Math.abs(p1.x - p2.x);
		float yDis = Math.abs(p2.y - p1.y);
		
		return (float) Math.sqrt(xDis * xDis + yDis * yDis);
	}
	
	private PointF getMidPoint(PointF p1, PointF p2) {
		return new PointF((p1.x + p2.x) / 2f, (p1.y + p2.y) / 2f);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// while the thread run, draw the view infinitely
		while (this.ifLoop) {
			try {
				Thread.sleep(10); // 控制刷新频率
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (this.holder) {
				this.Draw();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		this.ifLoop = false;
	}
	
	

}
