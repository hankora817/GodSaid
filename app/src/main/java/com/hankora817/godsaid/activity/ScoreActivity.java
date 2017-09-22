package com.hankora817.godsaid.activity;

import java.util.ArrayList;

import com.google.android.gms.ads.AdView;
import com.hankora817.godsaid.bean.ScoreBean;
import com.hankora817.godsaid.controller.ScoreAdapter;
import com.hankora817.godsaid.utils.DBaseUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.hankroa817.godsaid.R;

public class ScoreActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.text_score_talk_talk));
		
		ListView listScoreView = (ListView) findViewById(R.id.list_score);
		ArrayList<ScoreBean> scoreArr = DBaseUtil.getScoreArr();
		
		if (scoreArr.size() > 0)
		{
			ScoreAdapter adapter = new ScoreAdapter(scoreArr);
			listScoreView.setAdapter(adapter);
		}
		else
			findViewById(R.id.text_nodata).setVisibility(View.VISIBLE);
		
		setAnotherAd((AdView) findViewById(R.id.adview_banner));
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_graph, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
				
			case R.id.menu_graph:
				startActivity(new Intent(ScoreActivity.this, GraphActivity.class));
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
