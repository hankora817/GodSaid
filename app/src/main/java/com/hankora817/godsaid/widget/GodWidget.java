package com.hankora817.godsaid.widget;

import java.util.ArrayList;

import com.hankroa817.godsaid.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

public class GodWidget extends AppWidgetProvider
{
	ArrayList<String> todaySaid;
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++)
		{
			updateWidget(context, appWidgetManager, appWidgetIds[i]);
		}
		
	}
	
	
	public void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetIds)
	{
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.godwidget);
		
		Intent homeEventIntent = new Intent(context, GodWidget.class);
		PendingIntent pendingIntentOfHomeEvent = PendingIntent.getActivity(context, 0, homeEventIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.widget_title, pendingIntentOfHomeEvent);
		
		SharedPreferences pref = context.getSharedPreferences("TodaySaid", Context.MODE_PRIVATE);
		int number = pref.getInt("number", 0);
		loadSaidDB(number);
		
		remoteViews.setTextViewText(R.id.widget_test_said, todaySaid.get(0).toString());
		remoteViews.setTextViewText(R.id.widget_test_which, todaySaid.get(1).toString());
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		
		if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
		{
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			this.updateWidget(context, appWidgetManager, appWidgetId);
		}
	}
	
	
	private ArrayList<String> loadSaidDB(int number)
	{
		todaySaid = new ArrayList<>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.hankora817.godsaid/db/Godsaid.db", null);
		
		Cursor cr = db.rawQuery("SELECT ifnull(said,'')said, ifnull(which,'')which FROM user_table where number = " + number, null);
		cr.moveToFirst();
		while (!cr.isAfterLast())
		{
			todaySaid.add(cr.getString(0));
			todaySaid.add(cr.getString(1));
			cr.moveToNext();
		}
		cr.close();
		db.close();
		
		return todaySaid;
	}
}
