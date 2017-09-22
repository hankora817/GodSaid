package com.hankora817.godsaid.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hankora817.godsaid.bean.GodBean;
import com.hankora817.godsaid.utils.PreferenceUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashAcitivty extends Activity
{
	private final String FILE_NAME = "Godsaid.db";
	private final String SCORE_FILE_NAME = "scoreDB.db";
	private final String FILE_PATH = "/data/data/com.hankora817.godsaid/db";
	private int update_check = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		checkFileProcess();
	}
	
	
	private void checkFileProcess()
	{
		File directory = new File(Environment.getDataDirectory() + "/data/com.hankora817.godsaid/db");
		
		if (!directory.exists())
			directory.mkdir();
		
		File file = new File(FILE_PATH + "/" + FILE_NAME);
		File file2 = new File(FILE_PATH + "/" + SCORE_FILE_NAME);
		
		Log.i("debug", "파일 존재 여부: " + file.exists());
		if (!file.exists())
		{
			copyDB(this, FILE_NAME);
			insertDB();
		}
		else if (PreferenceUtil.getUpgradeCheck(this) < update_check)
			upgradeDB();
		
		if (!file2.exists())
		{
			copyDB(this, SCORE_FILE_NAME);
		}
		
		handler.sendEmptyMessageDelayed(0, 2000);
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Intent intent = new Intent(SplashAcitivty.this, PhraseActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	
	private void insertDB()
	{
		AssetManager manager = getResources().getAssets();
		try
		{
			AssetInputStream ais = (AssetInputStream) manager.open("Godsaid.json");
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
			
			for (int j = 0; j < array.length(); j++)
			{
				GodBean bean = new GodBean();
				bean.setJSONObject(array.getJSONObject(j).toString());
				
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
				//Insert
				ContentValues newValues = new ContentValues();
				newValues.put("number", j);
				newValues.put("said", bean.getSaid());
				newValues.put("which", bean.getWhich());
				newValues.put("open", 0);
				db.insert("user_table", null, newValues);
				db.close();
			}
			
			PreferenceUtil.setTotalSaid(array.length(), this);
			PreferenceUtil.setUpdateCheck(update_check, this);
			Log.i("Insert TotalSaid", Integer.toString(array.length()));
		}
		catch (Exception e)
		{
			Log.w("execption", "파일이 없나봐용", e);
		}
	}
	
	
	private void upgradeDB()
	{
		int totalSaid = PreferenceUtil.getTotalSaid(this);
		AssetManager manager = getResources().getAssets();
		try
		{
			AssetInputStream ais = (AssetInputStream) manager.open("Godsaid.json");
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
			
			// 기존 db 변경
			for (int j = 0; j < totalSaid; j++)
			{
				GodBean bean = new GodBean();
				bean.setJSONObject(array.getJSONObject(j).toString());
				
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
				ContentValues newValues = new ContentValues();
				newValues.put("number", j);
				newValues.put("said", bean.getSaid());
				newValues.put("which", bean.getWhich());
				db.update("user_table", newValues, "number = " + j, null);
				db.close();
			}
			
			if (totalSaid < array.length()) // 새로 추가된것 db에 저장
			{
				for (int j = totalSaid; j < array.length(); j++)
				{
					GodBean bean = new GodBean();
					bean.setJSONObject(array.getJSONObject(j).toString());
					
					SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
					//Insert
					ContentValues newValues = new ContentValues();
					newValues.put("number", j);
					newValues.put("said", bean.getSaid());
					newValues.put("which", bean.getWhich());
					newValues.put("open", 0);
					db.insert("user_table", null, newValues);
					db.close();
				}
			}
			
			PreferenceUtil.setTotalSaid(array.length(), this);
			PreferenceUtil.setUpdateCheck(update_check, this);
			Log.i("Upgrade TotalSaid", Integer.toString(array.length()));
		}
		catch (Exception e)
		{
			Log.w("execption", "파일이 없나봐용", e);
		}
	}
	
	
	private void copyDB(Context mContext, String fileName)
	{
		AssetManager manager = mContext.getAssets();
		File folder = new File(FILE_PATH);
		File file = new File(FILE_PATH + "/" + fileName);
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try
		{
			InputStream is = manager.open(fileName);
			BufferedInputStream bis = new BufferedInputStream(is);
			
			if (folder.exists())
			{
			}
			else
			{
				folder.mkdirs();
			}
			
			if (file.exists())
			{
				file.delete();
				file.createNewFile();
			}
			
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int read = -1;
			byte[] buffer = new byte[1024];
			while ((read = bis.read(buffer, 0, 1024)) != -1)
			{
				bos.write(buffer, 0, read);
			}
			
			bos.flush();
			
			bos.close();
			fos.close();
			bis.close();
			is.close();
			
		}
		catch (IOException e)
		{
			Log.e("ErrorMessage : ", e.getMessage());
		}
	}
}
