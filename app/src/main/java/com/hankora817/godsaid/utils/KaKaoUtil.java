package com.hankora817.godsaid.utils;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import android.content.Context;

public class KaKaoUtil
{
	public void sendKakaoTalk(String msg, Context context)
	{
		try
		{
			KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
			KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
			kakaoTalkLinkMessageBuilder.addAppLink(msg, new AppActionBuilder().setUrl("market://details?id=com.hankora817.godsaid")
																				.build());
			kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, context);
		}
		catch (KakaoParameterException e)
		{
			e.printStackTrace();
		}
	}
}
