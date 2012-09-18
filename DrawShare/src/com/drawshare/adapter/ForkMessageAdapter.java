package com.drawshare.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.drawshare.R;
import com.drawshare.render.object.ForkMessage;
import com.drawshare.util.DrawShareConstant;

public class ForkMessageAdapter extends BaseAsyncAdapter<ForkMessage> {

	public ForkMessageAdapter(Context context, AbsListView view,
			ArrayList<ForkMessage> dataSet,
			boolean netStatus) {
		super(context, view, dataSet, R.id.fork_message_grid_avatar_image, netStatus, R.layout.fork_message_grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindView(int position, View convertView) {
		// TODO Auto-generated method stub
		ForkMessage forkMessage = this.dataSet.get(position);
		String sendDate = new SimpleDateFormat("yyyy-MM-dd").format(forkMessage.sendDate);
		String originPictTitle = forkMessage.originPictTitle;
		String senderName = forkMessage.senderName;
		
		TextView promoteTextView = (TextView) convertView.findViewById(R.id.fork_message_grid_change_promote_text);
		TextView dateTextView = (TextView) convertView.findViewById(R.id.fork_message_grid_fork_date_text);
		TextView titleTextView = (TextView) convertView.findViewById(R.id.fork_message_grid_origin_pict_title_text);
		
		promoteTextView.setText("被" + senderName + "修改了");
		dateTextView.setText(sendDate);
		titleTextView.setText(originPictTitle);
	}

	@Override
	protected void setImage(int position, View convertView) {
		// TODO Auto-generated method stub
		ForkMessage message = this.dataSet.get(position);
		this.defaultImageLoader.loadImage(position, message.originPictURL, this.defaultListener, 
				DrawShareConstant.MESSAGE_AVATAR_SIZE);
	}

}
