package com.drawshare.activities.editpict;

import java.io.File;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureOrUserNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.picture.PictEdit;
import com.drawshare.Request.picture.PictEditHistory;
import com.drawshare.activities.base.BaseActivity;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.doodle.view.DoodleView;
import com.drawshare.util.DrawShareConstant;
import com.drawshare.util.DrawShareUtil;

public class DrawActivity extends BaseActivity implements OnClickListener {

	/**
	 * basic activity components
	 */
	private DoodleView doodleView = null;
	private Button showPanelButton = null;
	
	/**
	 * the buttons for basic draw panel
	 */
	private Button drawRectButton = null;
	private Button drawPenWidthButton = null;
	private Button drawColorButton = null;
	private Button drawPenButton = null;
	private Button drawEraserButton = null;
	private Button drawDeepEraserButton = null;
	private Button drawBackgroundButton = null;
	private Button drawEditButton = null;
	private Button drawRoundButton = null;
	private Button drawTextButton = null;
	
	/**
	 * buttons for pen width
	 */
	Map<Float, Button> penWidtButtonMap = null;
	
	/**
	 * buttons for pen color
	 */
	Map<Integer, Button> penColorButtonMap = null;
	
	/**
	 * buttons for edit panel
	 */
	Button rollbackButton = null;
	Button clearButton = null;
	Button publishButton = null;
	Button exitButton = null;
	
	/**
	 * layout for activity and panels
	 */
	private RelativeLayout drawLayout = null;
	private LinearLayout leftDrawPanelLayout = null;
	private LinearLayout rightDrawPanLayout = null;
	private LinearLayout drawPenWidthLayout = null;
	private RelativeLayout drawPenColorLayout = null;
	private LinearLayout drawEditLayout = null;
	
	private PopupWindow leftDrawPanelWindow = null;
	private PopupWindow rightDrawPanelWindow = null;
	private PopupWindow penColorWindow = null;
	private PopupWindow penWidthWindow = null;
	private PopupWindow drawEditWindow = null;
	
	private File tempFile = null;
	
	// variable for create or fork
	private boolean ifFork = false;
	private String sourcePictURL = null;
	private String sourcePictId = null;
	private String newPictTitle = null;
	
	private boolean ifPublish = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_draw);
        setUpView();
        setLeftPanelWindow();
        setRightPanelWindow();
        setPenColorWindow();
        setPenWidthWindow();
        setPenEditWindow();
        setViewAction();
        getExtraData();
        loadBackgroundBitmap();
    }
    

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		//super.setUpView();
		drawLayout = new RelativeLayout(this);
		drawLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.doodleView = new DoodleView(this);
		this.doodleView.setColor(Color.BLACK);
		this.doodleView.setBrushSize(10f);
		drawLayout.addView(doodleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.showPanelButton = new Button(this);
		this.showPanelButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.show_draw_panel_selector));
		RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		buttonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		buttonLayoutParams.setMargins(0, 0, 20, 20);
		drawLayout.addView(this.showPanelButton, buttonLayoutParams);
		
		this.setContentView(drawLayout);
	}
	
	private void getExtraData() {
		this.ifFork = getIntent().getBooleanExtra(DrawShareConstant.EXTRA_KEY.IF_FORK, false);
		if (ifFork) { // fork a new picture
			this.sourcePictId = getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.PICT_ID);
			this.sourcePictURL = getIntent().getStringExtra(DrawShareConstant.EXTRA_KEY.PICT_URL);
		}
	}
	
	/**
	 * load the background bitmap if it is fork a picture
	 */
	private void loadBackgroundBitmap() {
		if (this.ifFork) {
			// load the bitmap
			if (this.application.getNetworkState()) {
				final ProgressDialog progressDialog = DrawShareUtil.getWaitProgressDialog(this);
				progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == event.KEYCODE_BACK) {
							progressDialog.dismiss();
							finish();
							return true;
						}
						return false;
					}
				});
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						if (msg.what == 1) {
							progressDialog.dismiss();
							Bitmap bitmap = (Bitmap) msg.obj;
							if (bitmap != null) {
								doodleView.setBackground(bitmap);
								doodleView.postInvalidate();
							}
							else {
								Toast.makeText(DrawActivity.this, getString(R.string.load_image_failed), Toast.LENGTH_LONG).show();
							}
						}
					}
				};
				new Thread(new Runnable() {
				
					@Override
					public void run() {
						// TODO Auto-generated method stub
					Bitmap dumybitmap = Util.urlToBitmap(sourcePictURL, DrawShareConstant.PICT_DRAW_SIZE);
					WindowManager windowManager = getWindowManager();
					Bitmap bitmap = Bitmap.createScaledBitmap(dumybitmap, windowManager.getDefaultDisplay().getWidth(), 
							windowManager.getDefaultDisplay().getHeight(), false);
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}
				}).start();
			}
			else {
				Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void setLeftPanelWindow() {
		leftDrawPanelLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.left_draw_panel_layout, null);
		leftDrawPanelWindow = new PopupWindow(leftDrawPanelLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		leftDrawPanelWindow.setFocusable(true);
		leftDrawPanelWindow.setOutsideTouchable(true);
		leftDrawPanelWindow.setBackgroundDrawable(new BitmapDrawable());
		leftDrawPanelWindow.setAnimationStyle(R.style.left_draw_panel_popup_style);
		
		// find the buttons
		drawRectButton = (Button) leftDrawPanelLayout.findViewById(R.id.draw_panel_rect_button);
		drawRoundButton = (Button) leftDrawPanelLayout.findViewById(R.id.draw_panel_round_button);
		drawPenWidthButton = (Button) leftDrawPanelLayout.findViewById(R.id.draw_panel_width_button);
		drawBackgroundButton = (Button) leftDrawPanelLayout.findViewById(R.id.draw_panel_background_button);
		drawEditButton = (Button) leftDrawPanelLayout.findViewById(R.id.draw_panel_edit_button);
	}
	
	private void setRightPanelWindow() {
		rightDrawPanLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.right_draw_panel_layout, null);
		rightDrawPanelWindow = new PopupWindow(rightDrawPanLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		rightDrawPanelWindow.setFocusable(true);
		rightDrawPanelWindow.setBackgroundDrawable(new BitmapDrawable());
		rightDrawPanelWindow.setOutsideTouchable(true);
		rightDrawPanelWindow.setAnimationStyle(R.style.right_draw_panel_popup_style);
		
		// find the buttons
		drawColorButton = (Button) rightDrawPanLayout.findViewById(R.id.draw_panel_color_button);
		drawPenButton = (Button) rightDrawPanLayout.findViewById(R.id.draw_panel_pen_button);
		drawEraserButton = (Button) rightDrawPanLayout.findViewById(R.id.draw_panel_eraser_button);
		drawDeepEraserButton = (Button) rightDrawPanLayout.findViewById(R.id.draw_panel_deep_eraser_button);
		drawTextButton = (Button) rightDrawPanLayout.findViewById(R.id.draw_panel_pen_text_button);
	}
	
	private void setPenWidthWindow() {
		drawPenWidthLayout = DrawPanelUtil.getPenWidthLayout(this);
		penWidthWindow = new PopupWindow(drawPenWidthLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		penWidthWindow.setFocusable(true);
		penWidthWindow.setBackgroundDrawable(new BitmapDrawable());
		penWidthWindow.setOutsideTouchable(true);
		penWidthWindow.setAnimationStyle(R.style.left_draw_panel_popup_style);
		
		penWidtButtonMap = DrawPanelUtil.getAllPenWidthButton(drawPenWidthLayout);
	}
	
	private void setPenColorWindow() {
		drawPenColorLayout = DrawPanelUtil.getColorPanelLayout(this);
		penColorWindow = new PopupWindow(drawPenColorLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		penColorWindow.setFocusable(true);
		penColorWindow.setBackgroundDrawable(new BitmapDrawable());
		penColorWindow.setOutsideTouchable(true);
		penColorWindow.setAnimationStyle(R.style.right_draw_panel_popup_style);
		
		penColorButtonMap = DrawPanelUtil.getAllColorButton(drawPenColorLayout);
	}
	
	private void setPenEditWindow() {
		drawEditLayout = DrawPanelUtil.getEditPanelLayout(this);
		drawEditWindow = new PopupWindow(drawEditLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		drawEditWindow.setFocusable(true);
		drawEditWindow.setBackgroundDrawable(new BitmapDrawable());
		drawEditWindow.setOutsideTouchable(true);
		drawEditWindow.setAnimationStyle(R.style.left_draw_panel_popup_style);
		
		rollbackButton = (Button) drawEditLayout.findViewById(R.id.draw_panel_edit_rollback_button);
		clearButton = (Button) drawEditLayout.findViewById(R.id.draw_panel_edit_clear_button);
		publishButton = (Button) drawEditLayout.findViewById(R.id.draw_panel_edit_publish_button);
		exitButton = (Button) drawEditLayout.findViewById(R.id.draw_panel_edit_exit_button);
	}

	@Override
	protected void setViewAction() {
		// TODO Auto-generated method stub
		super.setViewAction();
		// set up the panel actions
		this.drawRectButton.setOnClickListener(this);
		this.drawPenWidthButton.setOnClickListener(this);
		this.drawColorButton.setOnClickListener(this);
		this.drawPenButton.setOnClickListener(this);
		this.drawEraserButton.setOnClickListener(this);
		this.drawDeepEraserButton.setOnClickListener(this);
		this.drawBackgroundButton.setOnClickListener(this);
		this.drawEditButton.setOnClickListener(this);
		this.drawRoundButton.setOnClickListener(this);
		this.drawTextButton.setOnClickListener(this);
		this.rollbackButton.setOnClickListener(this);
		this.clearButton.setOnClickListener(this);
		this.publishButton.setOnClickListener(this);
		this.exitButton.setOnClickListener(this);
		
		DrawPanelUtil.setPenWidthButtonActions(penWidtButtonMap, doodleView,  new PopupWindow[] {
				
		});
		DrawPanelUtil.setColorButtonActions(penColorButtonMap, doodleView, new PopupWindow[] {
				
		});
		
		this.showPanelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftDrawPanelWindow.showAtLocation(drawLayout, Gravity.LEFT, 0, 0);
				rightDrawPanelWindow.showAtLocation(drawLayout, Gravity.RIGHT, 0, 0);
			}
		});
	}

	
	private void setDoodleBackground() {
		tempFile = tempFile=new File("/sdcard/a.jpg");
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true");

		// 宽高比例
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 4);
		
		WindowManager windowManager = getWindowManager();
		// 宽高
		intent.putExtra("outputX", windowManager.getDefaultDisplay().getWidth());
		intent.putExtra("outputY", windowManager.getDefaultDisplay().getHeight());
		

		intent.putExtra("output", Uri.fromFile(tempFile));
		intent.putExtra("outputFormat", "JPEG");

		startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				// button.setText(tempFile.exists() + "");
				final Bitmap background = BitmapFactory.decodeFile(tempFile
						.getAbsolutePath());
				this.doodleView.setBackground(background);
			}
		}
	}
	
	private void publishDoodleWithDialog() {
		final EditText editText = new EditText(this);
		new AlertDialog.Builder(this).setTitle(R.string.publish_pict_title).setView(editText)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (editText.getText().length() == 0) {
						Toast.makeText(DrawActivity.this, getString(R.string.please_enter_pict_title), Toast.LENGTH_LONG).show();
					}
					else {
						publishDoodle(editText.getText().toString());
					}
				}
			}).setNegativeButton(R.string.cancel, null).show();
	}
	
	private void publishDoodle(final String title) {
		// first get the draw doodle
		WindowManager windowManager = this.getWindowManager();
		final Bitmap bitmap = Bitmap.createBitmap(windowManager.getDefaultDisplay().getWidth(), 
				windowManager.getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		doodleView.draw(canvas);
		
		if (this.application.getNetworkState()) {
			final ProgressDialog progressDialog = DrawShareUtil.getWaitProgressDialog(this);
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (msg.what == 2 || msg.what == 3) {
						progressDialog.dismiss();
						if ((Boolean) msg.obj) {
							Toast.makeText(DrawActivity.this, getString(R.string.publish_success), Toast.LENGTH_LONG).show();
							ifPublish = true;
						}
						else {
							Toast.makeText(DrawActivity.this, getString(R.string.publish_failed), Toast.LENGTH_LONG).show();
						}
					}
				}
			};
			if (ifFork) {
				// fork a picture
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean success = false;
						try {
							success = PictEditHistory.forkPicture(UserIdHandler.getUserId(DrawActivity.this), 
									sourcePictId, ApiKeyHandler.getApiKey(DrawActivity.this), 
									title, "nothing", bitmap);
						} catch (AuthFailException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (PictureOrUserNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (Exception e) {
							success = false;
						}
						Message msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = success;
						handler.sendMessage(msg);
					}
				}).start();
			}
			else {
				// create a picture
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean success = false;
						try {
							success = PictEdit.addNewPict(UserIdHandler.getUserId(DrawActivity.this), 
									ApiKeyHandler.getApiKey(DrawActivity.this), bitmap, title, "nothing");
						} catch (UserNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (AuthFailException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							success = false;
						} catch (Exception e) {
							success = false;
						}
						Message msg = handler.obtainMessage();
						msg.what = 3;
						msg.obj = success;
						handler.sendMessage(msg);
					}
				}).start();
			}
		} else {
			Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
		}
	}
	
	private void exitDoodle() {
		if (this.ifPublish == false) {
			new AlertDialog.Builder(this).setTitle(getString(R.string.confirm_exit)).setMessage(R.string.not_save_confirm)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).setNegativeButton(R.string.cancel, null).show();
		}
		else {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.draw_panel_rect_button:
			this.doodleView.setMode(DoodleView.Mode.RECT_MODE);
			break;
		case R.id.draw_panel_round_button:
			this.doodleView.setMode(DoodleView.Mode.CIRCLE_MODE);
			break;
		case R.id.draw_panel_width_button:
			//Toast.makeText(this, "width", Toast.LENGTH_LONG).show();
			penWidthWindow.showAtLocation(drawLayout, Gravity.LEFT, drawPenWidthButton.getWidth() + 10, 0);
			break;
		case R.id.draw_panel_background_button:
			setDoodleBackground();
			break;
		case R.id.draw_panel_edit_button:
			//Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
			drawEditWindow.showAtLocation(drawLayout, Gravity.LEFT, drawEditButton.getWidth() + 10, drawEditButton.getHeight() * 2);
			break;
		case R.id.draw_panel_color_button:
			///Toast.makeText(this, "color", Toast.LENGTH_LONG).show();
			int yOff = -(drawColorButton.getHeight() * 2);
			penColorWindow.showAtLocation(drawLayout, Gravity.RIGHT, drawColorButton.getWidth() + 10, 
					yOff);
			break;
		case R.id.draw_panel_pen_text_button:
			leftDrawPanelWindow.dismiss();
			rightDrawPanelWindow.dismiss();
			DrawPanelUtil.doodleAddText(this, doodleView);
			break;
		case R.id.draw_panel_pen_button:
			this.doodleView.setMode(DoodleView.Mode.LINE_MODE);
			break;
		case R.id.draw_panel_eraser_button:
			this.doodleView.setMode(DoodleView.Mode.ERASER_MODE);
			break;
		case R.id.draw_panel_deep_eraser_button:
			this.doodleView.setMode(DoodleView.Mode.DEEP_ERASER_MODE);
			break;
		case R.id.draw_panel_edit_rollback_button:
			this.doodleView.rollBack();
			break;
		case R.id.draw_panel_edit_clear_button:
			this.doodleView.clear();
			break;
		case R.id.draw_panel_edit_exit_button:
			exitDoodle();
			break;
		case R.id.draw_panel_edit_publish_button:
			publishDoodleWithDialog();
			break;
		}
	}
	


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//doodleView.rollBack();
			if (doodleView.ifClear()) {
				finish();
			}
			else {
				doodleView.rollBack();
			}
		}
		return true;
	}	
}
