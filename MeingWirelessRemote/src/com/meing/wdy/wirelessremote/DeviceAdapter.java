package com.meing.wdy.wirelessremote;

import java.util.List;

import zxing.toptech.utils.DeviceInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter {

	private Context context;
	private List<DeviceInfo> lists;
	// private NameListAdapter nameAdapter;

	public DeviceAdapter(Context cxt, List<DeviceInfo> lists) {
		context = cxt;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.search_item, null);
			holder.username = (TextView) convertView
					.findViewById(R.id.search_name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DeviceInfo deviceinfo = lists.get(position);
		holder.username.setText(deviceinfo.getDevicename());
		return convertView;
	}

	class ViewHolder {
		private TextView username;
	}

}
