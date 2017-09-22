package com.hankora817.godsaid.controller;

import java.util.ArrayList;

import com.hankroa817.godsaid.R;
import com.hankora817.godsaid.bean.GodBean;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PhraseAdpater extends BaseAdapter
{
	private ArrayList<GodBean> mListModels;
	
	
	public PhraseAdpater(ArrayList<GodBean> list)
	{
		this.mListModels = list;
	}
	
	
	@Override
	public int getCount()
	{
		return mListModels.size();
	}
	
	
	@Override
	public GodBean getItem(int position)
	{
		return mListModels.get(position);
	}
	
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	private class ViewHolder
	{
		private TextView textSaid;
		private TextView textWhich;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		GodBean bean = getItem(position);
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_phrase_list_view, parent, false);
			holder.textSaid = (TextView) convertView.findViewById(R.id.text_said);
			holder.textWhich = (TextView) convertView.findViewById(R.id.text_which);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.textSaid.setText(bean.getSaid());
		holder.textWhich.setText(bean.getWhich());
		
		return convertView;
	}
	
}
