package com.hankora817.godsaid.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hankora817.godsaid.bean.GodBean;
import com.hankora817.godsaid.utils.DBaseUtil;
import com.hankora817.godsaid.utils.KaKaoUtil;
import com.hankora817.godsaid.utils.PreferenceUtil;
import com.hankora817.godsaid.widget.GodWidget;
import com.hankroa817.godsaid.R;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhraseActivity extends AppCompatActivity
{
	private TextView textSaid, textWhich;
	private ImageView btnStart;
	private String mTime;
	
	private InterstitialAd interstitialAd;
	private String[] menuTitles;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private Context mContext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phrase_activity);
		
		mContext = this;
		
		textSaid = (TextView) findViewById(R.id.text_said);
		textWhich = (TextView) findViewById(R.id.text_which);
		btnStart = (ImageView) findViewById(R.id.btn_start);
		
		PhraseApplication.getApplication().setCustomFont(textSaid, textWhich);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
		setSupportActionBar(toolbar);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
		{
			@Override
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
			}
			
			
			@Override
			public void onDrawerClosed(View drawerView)
			{
				super.onDrawerClosed(drawerView);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		menuTitles = new String[] { getString(R.string.text_my_talk_talk), getString(R.string.text_quiz_talk_talk),
				getString(R.string.text_score_talk_talk) };
		drawerList = (ListView) findViewById(R.id.drawer_menu_list);
		drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuTitles));
		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (position == 0)
					startActivity(new Intent(mContext, PhraseListActivity.class));
				else if (position == 1)
				{
					long now = System.currentTimeMillis();
					SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
					Date currentTime = new Date(now);
					String quizTime = mSimpleDateFormat.format(currentTime);
					
					String todayDate = PreferenceUtil.getQuizDate(mContext);
					
					if (quizTime.equals(todayDate))
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setMessage(getString(R.string.text_quiz_oneday));
						builder.setPositiveButton(getString(android.R.string.ok), null);
						builder.show();
					}
					else
						startActivity(new Intent(mContext, QuizActivity.class).putExtra("time", quizTime));
				}
				else if (position == 2)
				{
					startActivity(new Intent(mContext, ScoreActivity.class));
				}
				
				if (drawerLayout.isDrawerOpen(GravityCompat.START))
					drawerLayout.closeDrawers();
			}
		});
		
		btnStart.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				btnStart.setClickable(false);
				getRandomSaid();
			}
		});
		
		loadPreferences();
		setMainAd();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_option, menu);
		
		return true;
	}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
			return true;
		
		if (item.getItemId() == R.id.menu_link)
		{
			if (textSaid.getText().length() > 0)
				shareMsg();
			else
				showNoneSaid();
		}
		
		if (item.getItemId() == R.id.menu_kakao)
		{
			if (textSaid.getText().length() > 0)
				new KaKaoUtil().sendKakaoTalk(textSaid.getText().toString() + "\n[" + textWhich.getText().toString() + "]", mContext);
			else
				showNoneSaid();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	private void showNoneSaid()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(getString(R.string.text_today_said_none));
		builder.setPositiveButton(getString(android.R.string.ok), null);
		builder.show();
	}
	
	
	private void loadPreferences()
	{
		long now = System.currentTimeMillis();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
		Date currentTime = new Date(now);
		mTime = mSimpleDateFormat.format(currentTime);
		
		String todayDate = PreferenceUtil.getTodayDate(mContext);
		int number = PreferenceUtil.getPickNumber(mContext);
		
		if (mTime.equals(todayDate))
		{
			final GodBean bean = DBaseUtil.getSaid(number);
			new Handler()
			{
				public void handleMessage(Message msg)
				{
					startAnimation(false, bean.getSaid(), bean.getWhich());
				}
			}.sendEmptyMessageDelayed(2000, 0);
		}
		else
		{
			btnStart.setVisibility(View.VISIBLE);
		}
	}
	
	
	private void getRandomSaid()
	{
		GodBean bean = DBaseUtil.getRandomSaid();
		saveText(bean.getNumber());
		DBaseUtil.updateSaidOpen(bean.getNumber());
		startAnimation(true, bean.getSaid(), bean.getWhich());
	}
	
	
	public void shareMsg()
	{
		Intent msg = new Intent(Intent.ACTION_SEND);
		msg.addCategory(Intent.CATEGORY_DEFAULT);
		msg.putExtra(Intent.EXTRA_TEXT, textSaid.getText().toString() + "\n[" + textWhich.getText().toString() + "]");
		msg.setType("text/plain");
		startActivity(Intent.createChooser(msg, getString(R.string.text_share)));
		
	}
	
	
	private void startAnimation(boolean first, final String said, final String which)
	{
		ObjectAnimator buttonAni = ObjectAnimator.ofFloat(btnStart, "translationX", 15, -15, 15, -15, 15, -15, 15, -15, 15, -15, 15, -15, 20, -20,
				20, -20, 20, -20, 30, -30, 30, -30, 30, -30).setDuration(3000);
		ObjectAnimator buttonAni2 = ObjectAnimator.ofFloat(btnStart, "rotation", 5, -5, 5, 5, -5, 5, 5, -5, 5, 5, -5, 5, 5, -5, 5, 5, -5, 5, 5, -5,
				5, 5, -5, 5, 5, -5, 5, 5, -5, 5).setDuration(3000);
		ObjectAnimator buttonAni3 = ObjectAnimator.ofFloat(btnStart, "alpha", 1, 0).setDuration(500);
		
		buttonAni3.addListener(new AnimatorListener()
		{
			
			@Override
			public void onAnimationStart(Animator animation)
			{
				textSaid.setText(said);
				ObjectAnimator ani2 = ObjectAnimator.ofFloat(textSaid, "rotationY", -90, -1).setDuration(1000);
				ani2.addListener(new AnimatorListener()
				{
					
					@Override
					public void onAnimationStart(Animator animation)
					{
						
					}
					
					
					@Override
					public void onAnimationRepeat(Animator animation)
					{
						
					}
					
					
					@Override
					public void onAnimationEnd(Animator animation)
					{
						textWhich.setText(which);
						ObjectAnimator ani3 = ObjectAnimator.ofFloat(textWhich, "translationX", -400, 0).setDuration(1000);
						ani3.start();
					}
					
					
					@Override
					public void onAnimationCancel(Animator animation)
					{
						
					}
				});
				ani2.start();
			}
			
			
			@Override
			public void onAnimationRepeat(Animator animation)
			{
				
			}
			
			
			@Override
			public void onAnimationEnd(Animator animation)
			{
				
			}
			
			
			@Override
			public void onAnimationCancel(Animator animation)
			{
				
			}
		});
		
		if (first)
		{
			buttonAni3.setStartDelay(2500);
			buttonAni.start();
			buttonAni2.start();
			buttonAni3.start();
		}
		else
			buttonAni3.start();
		
	}
	
	
	private void saveText(int number)
	{
		PreferenceUtil.setTodayDate(mTime, mContext);
		PreferenceUtil.setPickNumber(number, mContext);
		
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		update.setClass(this, GodWidget.class);
		update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(this, GodWidget.class)));
		this.sendBroadcast(update);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				if (drawerLayout.isDrawerOpen(GravityCompat.START))
					drawerLayout.closeDrawers();
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setTitle(getString(R.string.app_name));
					builder.setMessage(getString(R.string.text_app_end_msg));
					builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							finish();
							android.os.Process.killProcess(Process.myPid());
						}
					});
					builder.setNegativeButton(android.R.string.no, null);
					builder.show();
				}
				break;
		}
		return true;
	}
	
	
	public void setMainAd()
	{
		/**
		 * load ad type banner
		 */
		AdView adView = (AdView) findViewById(R.id.adview_banner);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
														.addTestDevice("988546D21CD9B0023C4B0C2FB4CF0310")
														.build();
		adView.loadAd(adRequest);
		
		/**
		 * load ad type front
		 */
		interstitialAd = new InterstitialAd(mContext);
		interstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id_front));
		adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("988546D21CD9B0023C4B0C2FB4CF0310").build();
		interstitialAd.setAdListener(new AdListener()
		{
			@Override
			public void onAdLoaded()
			{
				super.onAdLoaded();
				if (interstitialAd.isLoaded())
					interstitialAd.show();
			}
		});
		interstitialAd.loadAd(adRequest);
	}
}
