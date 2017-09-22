package com.hankora817.godsaid.activity;

import java.util.ArrayList;

import com.google.android.gms.ads.AdView;
import com.hankora817.godsaid.bean.GodBean;
import com.hankora817.godsaid.controller.PhraseAdpater;
import com.hankora817.godsaid.utils.DBaseUtil;
import com.hankora817.godsaid.utils.KaKaoUtil;
import com.hankroa817.godsaid.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PhraseListActivity extends BaseActivity
{
	private Context mContext;
	private ArrayList<GodBean> mSaidList;
	private TextView text_pro;
	private ProgressBar progressBar;
	private PhraseAdpater mAdapter;
	private int totalSaid;
	private int progress = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phrase_list_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.text_my_talk_talk));
		
		mContext = this;
		
		totalSaid = DBaseUtil.getTotalSaidCount();
		mSaidList = DBaseUtil.getOpenSaids();
		
		if (totalSaid != 0)
			progress = 100 * mSaidList.size() / totalSaid;
		
		text_pro = (TextView) findViewById(R.id.text_progress);
		progressBar = (ProgressBar) findViewById(R.id.progress_progress);
		
		ListView listView = (ListView) findViewById(R.id.phrase_listview);
		mAdapter = new PhraseAdpater(mSaidList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				GodBean bean = mAdapter.getItem(position);
				showPhrasePopup(bean.getSaid(), bean.getWhich());
			}
		});
		
		//프로그레스바에 진행률 표시
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(1500);
					int i = 0;
					while (i < progress)
					{
						Message msg = handler.obtainMessage();
						msg.arg1 = i;
						handler.sendMessage(msg);
						Thread.sleep(200);
						i += 1;
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
		
		setAnotherAd((AdView) findViewById(R.id.adview_banner));
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			text_pro.setText(Integer.toString(msg.arg1) + " %");
			progressBar.setProgress(msg.arg1);
		}
	};
	
	
	private void showPhrasePopup(final String said, final String which)
	{
		final Dialog popupDialog = new Dialog(mContext);
		popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popupDialog.setContentView(R.layout.phrase_popup);
		
		ImageView pop_kakao = (ImageView) popupDialog.findViewById(R.id.image_kakao);
		ImageView pop_share = (ImageView) popupDialog.findViewById(R.id.image_share);
		TextView pop_said = (TextView) popupDialog.findViewById(R.id.text_said);
		TextView pop_which = (TextView) popupDialog.findViewById(R.id.text_which);
		
		PhraseApplication.getApplication().setCustomFont(pop_said, pop_which);
		
		pop_said.setText(said);
		pop_which.setText(which);
		
		pop_kakao.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new KaKaoUtil().sendKakaoTalk(said + "\n[" + which + "]", mContext);
			}
		});
		pop_share.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				shareMsg(said, which);
			}
		});
		popupDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				popupDialog.dismiss();
			}
		});
		
		popupDialog.show();
	}
	
	
	public void shareMsg(String said, String which)
	{
		Intent msg = new Intent(Intent.ACTION_SEND);
		msg.addCategory(Intent.CATEGORY_DEFAULT);
		msg.putExtra(Intent.EXTRA_TEXT, said + "\n[" + which + "]");
		msg.setType("text/plain");
		startActivity(Intent.createChooser(msg, getString(R.string.text_share)));
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
