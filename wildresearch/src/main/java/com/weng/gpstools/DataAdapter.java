/*
Copyright 2012 fangqing.fan@hotmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.weng.gpstools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import com.weng.R;

import java.util.List;

public class DataAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Gps> listGps;
	private TableRow descpit;
	private boolean dataSwitch;
	
	public DataAdapter(Context context, List<Gps> listGps,boolean dataSwitch) {
		this.mInflater = LayoutInflater.from(context);
		this.listGps = listGps;
		this.dataSwitch = dataSwitch;
	}
	
	public int getCount() {
		return listGps.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.data_list_item,null);
			descpit = (TableRow)convertView.findViewById(R.id.descript_row);
			viewHolder.lat = (TextView)convertView.findViewById(R.id.lat_value);
			viewHolder.lon = (TextView)convertView.findViewById(R.id.lon_value);
			viewHolder.high = (TextView)convertView.findViewById(R.id.high_value);
			viewHolder.direct = (TextView)convertView.findViewById(R.id.direct_value);
			viewHolder.speed = (TextView)convertView.findViewById(R.id.speed_value);
			viewHolder.time = (TextView)convertView.findViewById(R.id.time_value);
			viewHolder.descript = (TextView)convertView.findViewById(R.id.descript_value);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.lat.setText(String.format("% -11.8f",listGps.get(position).getLatitude()));
		viewHolder.lon.setText(String.format("% -12.8f",listGps.get(position).getLongitude()));
		viewHolder.high.setText(String.format("% -7.3f",listGps.get(position).getHigh()));
		viewHolder.direct.setText(String.format("% 5.2f",listGps.get(position).getDirect()));
		viewHolder.speed.setText(String.format("%-4.1f",listGps.get(position).getSpeed()));
		viewHolder.time.setText(listGps.get(position).getGpsTime());
		viewHolder.descript.setText(listGps.get(position).getDescription());
		if(dataSwitch){
			descpit.setVisibility(View.VISIBLE);
		}else{
			descpit.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public final class ViewHolder{  
		private TextView lat;//纬度
		private TextView lon;//经度
		private TextView high;//海拔
		private TextView direct;//方向
		private TextView speed;//速度        
		private TextView time;//时间
		private TextView descript;
	}
}
