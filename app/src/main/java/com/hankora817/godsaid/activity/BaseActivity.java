package com.hankora817.godsaid.activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	
	public void setAnotherAd(AdView adView)
	{
		/**
		 * load ad type banner
		 */
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
														.addTestDevice("E17EC991224CCDFD5354CDDF63EFB93B")
														.addTestDevice("988546D21CD9B0023C4B0C2FB4CF0310")
														.build();
		adView.loadAd(adRequest);
	}
}
