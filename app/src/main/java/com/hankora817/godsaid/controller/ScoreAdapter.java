package com.hankora817.godsaid.controller;

import java.util.ArrayList;

import com.hankora817.godsaid.bean.ScoreBean;
import com.hankroa817.godsaid.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreAdapter extends BaseAdapter
{
	private ArrayList<ScoreBean> scoreArr;
	
	
	public ScoreAdapter(ArrayList<ScoreBean> scoreArr)
	{
		this.scoreArr = scoreArr;
	}
	
	
	@Override
	public int getCount()
	{
		return scoreArr.size();
	}
	
	
	@Override
	public ScoreBean getItem(int position)
	{
		return scoreArr.get(position);
	}
	
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	public class ViewHolder
	{
		private TextView textDate;
		private TextView textScore;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ScoreBean bean = getItem(position);
		ViewHolder holder;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_score_list_item, parent, false);
			holder.textScore = (TextView) convertView.findViewById(R.id.text_score);
			holder.textDate = (TextView) convertView.findViewById(R.id.text_date);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.textScore.setText(String.valueOf(bean.getSc_score()));
		holder.textDate.setText(bean.getSc_date());
		
		return convertView;
	}
}
