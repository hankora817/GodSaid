package com.hankora817.godsaid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil
{
	private static final String TOTAL_SAID = "total_said";
	private static final String UPDATE_CHECK = "upgrade_check";
	private static final String TODAY_DATE = "today_date";
	private static final String PICK_NUMBER = "pick_number";
	private static final String QUIZ_DATE = "quiz_date";
	
	private static SharedPreferences _preference;
	
	
	public static SharedPreferences instance(Context context)
	{
		if (_preference == null)
			_preference = PreferenceManager.getDefaultSharedPreferences(context);
		return _preference;
	}
	
	
	public static void setUpdateCheck(int version, Context context)
	{
		put(UPDATE_CHECK, version, context);
	}
	
	
	public static Integer getUpgradeCheck(Context context)
	{
		return get(UPDATE_CHECK, 0, context);
	}
	
	
	public static void setTodayDate(String date, Context context)
	{
		put(TODAY_DATE, date, context);
	}
	
	
	public static String getTodayDate(Context context)
	{
		return getWithNullToBlank(TODAY_DATE, context);
	}
	
	
	public static void setQuizDate(String date, Context context)
	{
		put(QUIZ_DATE, date, context);
	}
	
	
	public static String getQuizDate(Context context)
	{
		return getWithNullToBlank(QUIZ_DATE, context);
	}
	
	
	public static void setPickNumber(int number, Context context)
	{
		put(PICK_NUMBER, number, context);
	}
	
	
	public static Integer getPickNumber(Context context)
	{
		return get(PICK_NUMBER, 1, context);
	}
	
	
	public static void setTotalSaid(int count, Context context)
	{
		put(TOTAL_SAID, count, context);
	}
	
	
	public static Integer getTotalSaid(Context context)
	{
		return get(TOTAL_SAID, 0, context);
	}
	
	
	/**
	 * key 수동 설정
	 * 
	 * @param key
	 *            키 값
	 * @param value
	 *            내용
	 */
	public static void put(String key, String value, Context context)
	{
		SharedPreferences p = instance(context);
		SharedPreferences.Editor editor = p.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	
	/**
	 * String 값 가져오기
	 * 
	 * @param key
	 *            키 값
	 * @return String (기본값 null)
	 */
	public static String get(String key, Context context)
	{
		SharedPreferences p = instance(context);
		return p.getString(key, null);
	}
	
	
	/**
	 * String 값 가져오기
	 * 
	 * @param key
	 *            키 값
	 * @return String (기본값 "")
	 */
	public static String getWithNullToBlank(String key, Context context)
	{
		SharedPreferences p = instance(context);
		return p.getString(key, "");
	}
	
	
	/**
	 * key 설정
	 * 
	 * @param key
	 *            키 값
	 * @param value
	 *            내용
	 */
	public static void put(String key, boolean value, Context context)
	{
		SharedPreferences p = instance(context);
		SharedPreferences.Editor editor = p.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	
	/**
	 * Boolean 값 가져오기
	 * 
	 * @param key
	 *            키 값
	 * @return Boolean
	 */
	public static boolean get(String key, boolean defaultValue, Context context)
	{
		SharedPreferences p = instance(context);
		return p.getBoolean(key, defaultValue);
	}
	
	
	/**
	 * key 설정
	 * 
	 * @param key
	 *            키 값
	 * @param value
	 *            내용
	 */
	public static void put(String key, int value, Context context)
	{
		SharedPreferences p = instance(context);
		SharedPreferences.Editor editor = p.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	
	/**
	 * int 값 가져오기
	 * 
	 * @param key
	 *            키 값
	 * @return int
	 */
	public static int get(String key, int defaultValue, Context context)
	{
		SharedPreferences p = instance(context);
		return p.getInt(key, defaultValue);
	}
}
