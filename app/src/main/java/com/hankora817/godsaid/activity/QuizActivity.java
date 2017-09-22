package com.hankora817.godsaid.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.ads.AdView;
import com.hankora817.godsaid.bean.QuizBean;
import com.hankora817.godsaid.bean.ScoreBean;
import com.hankora817.godsaid.utils.DBaseUtil;
import com.hankora817.godsaid.utils.KaKaoUtil;
import com.hankora817.godsaid.utils.PreferenceUtil;
import com.hankroa817.godsaid.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuizActivity extends BaseActivity
{
	private Context mContext;
	private TextView textSaid, textWhich, textHint_1, textHint_2, textHint_3, textQuizCount;
	private ImageView imageHint1, imageHint2, imageHint3;
	private ArrayList<QuizBean> quizArray = new ArrayList<>();
	private ArrayList<Boolean> scoreArray = new ArrayList<>();
	private int quizIndex = 0;
	private int userSelect = 0;
	private int quizCorrect = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.text_quiz_talk_talk));
		
		mContext = this;
		
		textSaid = (TextView) findViewById(R.id.text_said);
		textWhich = (TextView) findViewById(R.id.text_which);
		textHint_1 = (TextView) findViewById(R.id.text_hint_1);
		textHint_2 = (TextView) findViewById(R.id.text_hint_2);
		textHint_3 = (TextView) findViewById(R.id.text_hint_3);
		textQuizCount = (TextView) findViewById(R.id.text_quiz_count);
		imageHint1 = (ImageView) findViewById(R.id.image_select_1);
		imageHint2 = (ImageView) findViewById(R.id.image_select_2);
		imageHint3 = (ImageView) findViewById(R.id.image_select_3);
		Button btnOk = (Button) findViewById(R.id.btn_ok);
		RelativeLayout layoutHint_1 = (RelativeLayout) findViewById(R.id.layout_hint_1);
		RelativeLayout layoutHint_2 = (RelativeLayout) findViewById(R.id.layout_hint_2);
		RelativeLayout layoutHint_3 = (RelativeLayout) findViewById(R.id.layout_hint_3);
		btnOk.setOnClickListener(clickListener);
		layoutHint_1.setOnClickListener(clickListener);
		layoutHint_2.setOnClickListener(clickListener);
		layoutHint_3.setOnClickListener(clickListener);
		
		createQuizList();
		
		setAnotherAd((AdView) findViewById(R.id.adview_banner));
	}
	
	
	private void setQuizView()
	{
		initImageHint();
		QuizBean bean = quizArray.get(quizIndex);
		userSelect = 0;
		quizCorrect = bean.getCorrect();
		String count = String.valueOf(quizIndex + 1) + "/10";
		textQuizCount.setText(count);
		
		textSaid.setText(bean.getSaid());
		textWhich.setText(bean.getWhich());
		textHint_1.setText(bean.getHint_1());
		textHint_2.setText(bean.getHint_2());
		textHint_3.setText(bean.getHint_3());
	}
	
	
	private void showResultPopup(boolean isCorrect)
	{
		QuizBean bean = quizArray.get(quizIndex);
		
		final Dialog resultDialog = new Dialog(mContext);
		resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		resultDialog.setContentView(R.layout.dialog_quiz_result);
		
		TextView textResult = (TextView) resultDialog.findViewById(R.id.text_result);
		TextView textQuiz = (TextView) resultDialog.findViewById(R.id.text_quiz);
		TextView textWhich2 = (TextView) resultDialog.findViewById(R.id.text_which);
		TextView textCorrect = (TextView) resultDialog.findViewById(R.id.text_correct);
		Button btnNext = (Button) resultDialog.findViewById(R.id.btn_next);
		
		if (isCorrect)
		{
			textResult.setText(getString(R.string.text_result_o));
			textResult.setTextColor(getResources().getColor(R.color.color_primary));
		}
		else
		{
			textResult.setText(getString(R.string.text_result_x));
			textResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
		}
		
		String correct = "";
		if (bean.getCorrect() == 1)
			correct = getString(R.string.text_correct) + bean.getHint_1();
		else if (bean.getCorrect() == 2)
			correct = getString(R.string.text_correct) + bean.getHint_2();
		else if (bean.getCorrect() == 3)
			correct = getString(R.string.text_correct) + bean.getHint_3();
		
		String quiz = getString(R.string.text_quiz) + bean.getSaid();
		textQuiz.setText(quiz);
		textWhich2.setText(bean.getWhich());
		textCorrect.setText(correct);
		
		btnNext.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				resultDialog.dismiss();
				if (quizIndex < 9)
				{
					quizIndex++;
					setQuizView();
				}
				else
				{
					showTotalScoreDialog();
				}
			}
		});
		
		resultDialog.show();
	}
	
	
	private void showTotalScoreDialog()
	{
		int totalScore = 0;
		for (int i = 0; i < scoreArray.size(); i++)
		{
			if (scoreArray.get(i))
				totalScore += 10;
		}
		
		Calendar cal = Calendar.getInstance();
		String today = cal.get(Calendar.MONTH) + getString(R.string.text_month) + " " + cal.get(Calendar.DATE) + getString(R.string.text_day);
		ScoreBean bean = new ScoreBean();
		bean.setSc_score(totalScore);
		bean.setSc_date(today);
		
		DBaseUtil.insertScore(bean);
		PreferenceUtil.setQuizDate(getIntent().getStringExtra("time"), mContext);
		
		final Dialog lastDialog = new Dialog(mContext);
		lastDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		lastDialog.setContentView(R.layout.dialog_your_score);
		TextView textScore = (TextView) lastDialog.findViewById(R.id.text_score);
		Button btnClose = (Button) lastDialog.findViewById(R.id.btn_close);
		ImageView btnShare = (ImageView) lastDialog.findViewById(R.id.btn_share);
		ImageView btnKakao = (ImageView) lastDialog.findViewById(R.id.btn_kakao);
		textScore.setText(String.valueOf(totalScore));
		
		final int myScore = totalScore;
		btnShare.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent msg = new Intent(Intent.ACTION_SEND);
				msg.addCategory(Intent.CATEGORY_DEFAULT);
				msg.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_share_score_front) + " " + String.valueOf(myScore)
						+ getString(R.string.text_share_score_end));
				msg.setType("text/plain");
				startActivity(Intent.createChooser(msg, getString(R.string.text_share)));
			}
		});
		
		btnKakao.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new KaKaoUtil().sendKakaoTalk(getString(R.string.text_share_score_front) + " " + String.valueOf(myScore)
						+ getString(R.string.text_share_score_end), mContext);
			}
		});
		btnClose.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				lastDialog.dismiss();
				finish();
			}
		});
		lastDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				finish();
			}
		});
		lastDialog.setCanceledOnTouchOutside(false);
		lastDialog.show();
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.btn_ok && userSelect != 0)
			{
				scoreArray.add(userSelect == quizCorrect);
				showResultPopup(userSelect == quizCorrect);
			}
			else if (v.getId() == R.id.layout_hint_1)
			{
				initImageHint();
				imageHint1.setVisibility(View.VISIBLE);
				userSelect = 1;
			}
			else if (v.getId() == R.id.layout_hint_2)
			{
				initImageHint();
				imageHint2.setVisibility(View.VISIBLE);
				userSelect = 2;
			}
			else if (v.getId() == R.id.layout_hint_3)
			{
				initImageHint();
				imageHint3.setVisibility(View.VISIBLE);
				userSelect = 3;
			}
		}
	};
	
	
	private void initImageHint()
	{
		imageHint1.setVisibility(View.INVISIBLE);
		imageHint2.setVisibility(View.INVISIBLE);
		imageHint3.setVisibility(View.INVISIBLE);
	}
	
	
	private void createQuizList()
	{
		AssetManager manager = getResources().getAssets();
		try
		{
			AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream) manager.open("GodQuiz.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(ais));
			
			StringBuilder sb = new StringBuilder();
			
			int bufferSize = 1024 * 1024;
			char readBuf[] = new char[bufferSize];
			int resultSize = 0;
			while ((resultSize = br.read(readBuf)) != -1)
			{
				if (resultSize == bufferSize)
				{
					sb.append(readBuf);
				}
				else
				{
					for (int i = 0; i < resultSize; i++)
					{
						sb.append(readBuf[i]);
					}
				}
			}
			String jString = sb.toString();
			JSONObject obj = new JSONObject(jString);
			JSONArray array = new JSONArray();
			
			if (obj.has("list"))
				array = obj.getJSONArray("list");
			
			ArrayList<QuizBean> list = new ArrayList<>();
			for (int j = 0; j < array.length(); j++)
			{
				QuizBean bean = new QuizBean();
				bean.setJSONObject(array.getJSONObject(j).toString());
				list.add(bean);
			}
			
			for (int i = 0; i < 10; i++)
			{
				int random = new Random().nextInt(list.size());
				quizArray.add(list.get(random));
				list.remove(random);
			}
			
			setQuizView();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
