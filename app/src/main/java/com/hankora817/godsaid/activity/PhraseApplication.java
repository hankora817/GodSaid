package com.hankora817.godsaid.activity;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.TextView;

public class PhraseApplication extends Application
{
	private static PhraseApplication instance;
	private Typeface customFont;
	
	
	public static PhraseApplication getApplication()
	{
		if (instance == null)
			throw new IllegalStateException("this application does not inherit");
		return instance;
	}
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		customFont = Typeface.createFromAsset(getAssets(), "BMJUA_ttf.ttf");
	}
	
	
	public void setCustomFont(TextView... views)
	{
		for (TextView view : views)
			view.setTypeface(customFont);
	}
}
