package com.drawShare.doodle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class DoodleActivity extends Activity {
	
	DoodleView doodleView = null;
	Bitmap background = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_doodle);
        int width = this.getWindowManager().getDefaultDisplay().getWidth();
        int height = this.getWindowManager().getDefaultDisplay().getHeight();
        //background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //Canvas canvas = new Canvas(background);
        //Paint paint = new Paint();
        //paint.setColor(Color.WHITE);
        //canvas.drawColor(Color.WHITE);
        //canvas.drawBitmap(background, 0, 0, paint);
        
        background = BitmapFactory.decodeResource(this.getResources(), R.drawable.background);
        doodleView = new DoodleView(this, background);
        doodleView.setColor(Color.YELLOW);
        doodleView.setBrushSize(10f);
        this.setContentView(doodleView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doodle, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_line_mode:
			doodleView.setEnableDragAndZoom(false);
			doodleView.setMode(DoodleView.Mode.LINE_MODE);
			break;
		case R.id.menu_circle_mode:
			doodleView.setEnableDragAndZoom(false);
			doodleView.setMode(DoodleView.Mode.CIRCLE_MODE);
			break;
		case R.id.menu_eraser_mode:
			doodleView.setEnableDragAndZoom(false);
			doodleView.setMode(DoodleView.Mode.ERASER_MODE);
			break;
		case R.id.menu_rect_mode:
			doodleView.setEnableDragAndZoom(false);
			doodleView.setMode(DoodleView.Mode.RECT_MODE);
			break;
		case R.id.menu_roll_back:
			doodleView.setEnableDragAndZoom(false);
			doodleView.rollBack();
			break;
		case R.id.menu_clear_screen:
			//doodleView.setEnableDragAndZoom(false);
			doodleView.clear();
			break;
		case R.id.menu_enable_drag_and_zoom:
			doodleView.setEnableDragAndZoom(true);
			doodleView.setMode(DoodleView.Mode.NONE_MODE);
			break;
		case R.id.menu_deep_eraser_mode:
			doodleView.setEnableDragAndZoom(false);
			doodleView.setMode(DoodleView.Mode.DEEP_ERASER_MODE);
			break;
	
		}
		return super.onOptionsItemSelected(item);
	}
}
