package com.hankora817.godsaid.activity;

import java.util.ArrayList;
import java.util.List;

import com.hankora817.godsaid.bean.ScoreBean;
import com.hankora817.godsaid.graph.LineGraph;
import com.hankora817.godsaid.graph.LineGraphVO;
import com.hankora817.godsaid.graph.LineGraphView;
import com.hankora817.godsaid.utils.DBaseUtil;
import com.hankroa817.godsaid.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GraphActivity extends AppCompatActivity
{
	private final int GRAPH_COUNT = 5;
	private Context mContext;
	private ViewGroup layoutGraphView;
	private TextView textIndex;
	private ArrayList<ScoreBean> mScoreArr;
	private int index, maxIndex;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.text_graph_talk_talk));
		
		mContext = this;
		layoutGraphView = (ViewGroup) findViewById(R.id.layout_graph);
		textIndex = (TextView) findViewById(R.id.text_index);
		Button btnNext = (Button) findViewById(R.id.btn_next);
		Button btnPre = (Button) findViewById(R.id.btn_pre);
		
		btnNext.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (index < maxIndex)
				{
					index++;
					setGraph();
				}
			}
		});
		
		btnPre.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (index > 1)
				{
					index--;
					setGraph();
				}
			}
		});
		
		mScoreArr = DBaseUtil.getGraphArr();
		if (mScoreArr.size() == 0)
		{
			findViewById(R.id.text_nodata).setVisibility(View.VISIBLE);
		}
		else
		{
			if (mScoreArr.size() % GRAPH_COUNT > 0)
				index = (mScoreArr.size() / GRAPH_COUNT) + 1;
			else
				index = mScoreArr.size() / GRAPH_COUNT;
			maxIndex = index;
			setGraph();
		}
	}
	
	
	private void setGraph()
	{
		LineGraphVO vo = makeLineGraphDefaultSetting();
		layoutGraphView.removeAllViews();
		layoutGraphView.addView(new LineGraphView(mContext, vo));
		textIndex.setText(String.valueOf(index));
	}
	
	
	private LineGraphVO makeLineGraphDefaultSetting()
	{
		ArrayList<String> titleList = new ArrayList<>();
		ArrayList<Integer> scoreList = new ArrayList<>();
		
		titleList.add("");
		if (index < 2)
			scoreList.add(0);
		else
			scoreList.add(mScoreArr.get((index * GRAPH_COUNT) - (GRAPH_COUNT + 1)).getSc_score());
		
		for (int i = (index * GRAPH_COUNT) - GRAPH_COUNT; i < (index * GRAPH_COUNT); i++)
		{
			Log.i("debug", "index=" + i);
			if (i < mScoreArr.size())
			{
				titleList.add(mScoreArr.get(i).getSc_date());
				scoreList.add(mScoreArr.get(i).getSc_score());
			}
		}
		
		String[] legendArr = titleList.toArray(new String[titleList.size()]);
		Integer[] graph1 = scoreList.toArray(new Integer[scoreList.size()]);
		
		List<LineGraph> arrGraph = new ArrayList<>();
		arrGraph.add(new LineGraph("android", Color.parseColor("#ff4081"), graph1));
		
		LineGraphVO vo = new LineGraphVO(legendArr, arrGraph);
		return vo;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
