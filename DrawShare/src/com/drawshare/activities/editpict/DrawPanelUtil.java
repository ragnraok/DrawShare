package com.drawshare.activities.editpict;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.drawshare.R;
import com.drawshare.doodle.view.DoodleView;

public class DrawPanelUtil {
	public static LinearLayout getPenWidthLayout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.draw_panel_pen_width_layout, null);
		
		return layout;
	}
	
	public static Map<Float, Button> getAllPenWidthButton(LinearLayout penWidthLayout) {
		Map<Float, Button> buttonMap = new HashMap<Float, Button>();
		buttonMap.put(5f, (Button) penWidthLayout.findViewById(R.id.draw_panel_pen_width_one_button));
		buttonMap.put(10f, (Button) penWidthLayout.findViewById(R.id.draw_panel_pen_width_two_button));
		buttonMap.put(15f, (Button) penWidthLayout.findViewById(R.id.draw_panel_pen_width_three_button));
		buttonMap.put(20f, (Button) penWidthLayout.findViewById(R.id.draw_panel_pen_width_four_button));
		
		return buttonMap;
	}
	
	public static void setPenWidthButtonActions(Map<Float, Button> buttonMap, final DoodleView doodleView,
			final PopupWindow[] windows) {
		Iterator<Entry<Float, Button>> iter = buttonMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Float, Button> entry = (Map.Entry<Float, Button>) iter.next();
			final float penWidth = entry.getKey();
			Button button = entry.getValue();
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doodleView.setBrushSize(penWidth);
					for (PopupWindow window : windows) {
						window.dismiss();
					}
				}
			});
		}
	}
	
	public static RelativeLayout getColorPanelLayout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.draw_panel_pen_color_layout, null);
		return layout;
	}
	
	public static Map<Integer, Button> getAllColorButton(RelativeLayout colorPanelLayout) {
		Map<Integer, Button> buttonMap = new HashMap<Integer, Button>();
		buttonMap.put(Color.rgb(230, 0, 18), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_red_button));
		buttonMap.put(Color.rgb(248, 182, 45), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_croci_button));
		buttonMap.put(Color.rgb(250, 230, 0), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_yellow_button));
		buttonMap.put(Color.rgb(0, 154, 62), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_green_button));
		buttonMap.put(Color.rgb(46, 167, 224), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_blue_button));
		buttonMap.put(Color.rgb(126, 49, 142), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_purple_button));
		buttonMap.put(Color.rgb(181, 181, 182), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_gray_button));
		buttonMap.put(Color.rgb(255, 255, 255), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_white_button));
		buttonMap.put(Color.rgb(0, 0, 0), (Button) colorPanelLayout.findViewById(R.id.draw_panel_pen_color_black_button));
		return buttonMap;
	}
	
	public static void setColorButtonActions(Map<Integer, Button> buttonMap, final DoodleView doodleView,
			final PopupWindow[] windows) {
		Iterator<Entry<Integer, Button>> iter = buttonMap.entrySet().iterator();
		while (iter.hasNext()) {
			while (iter.hasNext()) {
				Map.Entry<Integer, Button> entry = (Map.Entry<Integer, Button>) iter.next();
				final int color = entry.getKey();
				Button button = entry.getValue();
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doodleView.setColor(color);
						for (PopupWindow popupWindow : windows) 
							popupWindow.dismiss();
					}
				});
			}
		}
	}
	
	public static LinearLayout getEditPanelLayout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.draw_panel_edit_layout, null);
		return layout;
	}
	
	public static void doodleAddText(final Context context, final DoodleView doodleView) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("请输入文字");
		final EditText editText = new EditText(context);
		builder.setView(editText);
		builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = editText.getText().toString();
				doodleView.setMode( DoodleView.Mode.TEXT_MODE);
				doodleView.addTextAction(text);
				Toast.makeText(context, context.getString(R.string.set_doodle_font_size_and_position), Toast.LENGTH_LONG).show();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}
}
