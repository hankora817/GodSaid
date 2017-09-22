package com.hankora817.godsaid.utils;

import java.util.ArrayList;
import java.util.Random;

import com.hankora817.godsaid.bean.GodBean;
import com.hankora817.godsaid.bean.ScoreBean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBaseUtil
{
	private static final String DB_PATH = "/data/data/com.hankora817.godsaid/db/Godsaid.db";
	private static final String SCORE_PATH = "/data/data/com.hankora817.godsaid/db/scoreDB.db";
	
	
	/**
	 * open이 0인것만 구해서 그 중에서 선택해야
	 */
	public static GodBean getRandomSaid()
	{
		GodBean bean = new GodBean();
		ArrayList<Integer> notOpenArr = new ArrayList<>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
		Cursor cr = db.rawQuery("select number from user_table where open = 0", null);
		cr.moveToFirst();
		
		while (!cr.isAfterLast())
		{
			notOpenArr.add(cr.getInt(0));
			cr.moveToNext();
		}
		cr.close();
		
		Random random = new Random();
		int i = random.nextInt(notOpenArr.size());
		
		cr = db.rawQuery("select number, said, which, open from user_table where open = 0 and number = " + notOpenArr.get(i), null);
		cr.moveToFirst();
		
		while (!cr.isAfterLast())
		{
			bean.setNumber(cr.getInt(0));
			bean.setSaid(cr.getString(1));
			bean.setWhich(cr.getString(2));
			
			cr.moveToNext();
		}
		
		cr.close();
		db.close();
		
		return bean;
	}
	
	
	public static GodBean getSaid(int number)
	{
		GodBean bean = new GodBean();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
		
		Cursor cr = db.rawQuery("SELECT ifnull(said,'')said, ifnull(which,'')which FROM user_table where number = " + number, null);
		cr.moveToFirst();
		while (!cr.isAfterLast())
		{
			bean.setSaid(cr.getString(0));
			bean.setWhich(cr.getString(1));
			cr.moveToNext();
		}
		cr.close();
		db.close();
		
		return bean;
	}
	
	
	public static ArrayList<GodBean> getOpenSaids()
	{
		ArrayList<GodBean> list = new ArrayList<>();
		
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
		Cursor cr = db.rawQuery("select number, said, which, open from user_table where open = 1", null);
		cr.moveToFirst();
		
		while (!cr.isAfterLast())
		{
			GodBean bean = new GodBean();
			bean.setNumber(cr.getInt(0));
			bean.setSaid(cr.getString(1));
			bean.setWhich(cr.getString(2));
			
			list.add(bean);
			
			cr.moveToNext();
		}
		
		cr.close();
		db.close();
		
		return list;
	}
	
	
	public static Integer getTotalSaidCount()
	{
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
		Cursor cr = db.rawQuery("select count(number) as number from user_table", null);
		cr.moveToFirst();
		int totalCount = cr.getInt(0);
		cr.close();
		db.close();
		return totalCount;
	}
	
	
	public static void updateSaidOpen(int number)
	{
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
		db.execSQL("update user_table set open = 1 where number = " + number);
		db.close();
	}
	
	
	public static void insertScore(ScoreBean bean)
	{
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(SCORE_PATH, null);
		db.execSQL("insert into score_table (sc_score, sc_date) values (" + bean.getSc_score() + ", " + "\"" + bean.getSc_date() + "\"" + ")");
		db.close();
	}
	
	
	public static ArrayList<ScoreBean> getScoreArr()
	{
		ArrayList<ScoreBean> list = new ArrayList<>();
		
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(SCORE_PATH, null);
		Cursor cr = db.rawQuery("select sc_idx, sc_score, sc_date from score_table order by sc_idx desc", null);
		cr.moveToFirst();
		
		while (!cr.isAfterLast())
		{
			ScoreBean bean = new ScoreBean();
			bean.setSc_idx(cr.getInt(0));
			bean.setSc_score(cr.getInt(1));
			bean.setSc_date(cr.getString(2));
			
			list.add(bean);
			
			cr.moveToNext();
		}
		
		cr.close();
		db.close();
		
		return list;
	}

	public static ArrayList<ScoreBean> getGraphArr()
	{
		ArrayList<ScoreBean> list = new ArrayList<>();

		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(SCORE_PATH, null);
		Cursor cr = db.rawQuery("select sc_idx, sc_score, sc_date from score_table order by sc_idx", null);
		cr.moveToFirst();

		while (!cr.isAfterLast())
		{
			ScoreBean bean = new ScoreBean();
			bean.setSc_idx(cr.getInt(0));
			bean.setSc_score(cr.getInt(1));
			bean.setSc_date(cr.getString(2));

			list.add(bean);

			cr.moveToNext();
		}

		cr.close();
		db.close();

		return list;
	}
	
}
