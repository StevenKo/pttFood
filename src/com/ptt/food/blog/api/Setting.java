package com.ptt.food.blog.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting {
//	public static int textSize;
//	public static int textLanguage; // 0 for 繁體, 1 for 簡體
//	public static int readingDirection; // 0 for 直向, 1 for 橫向
//	public static int clickToNextPage; // 0 for yes, 1 for no
//	public static int stopSleeping;  // 0 for yes, 1 for no
	
	public final static String keyRemindLeaving = "Leaving";
	public final static Boolean initialReindLeaving = true;
	
	public final static String keyPref = "pref";
	public final static String keyTextTitleSize = "TextTitleSize";
	public final static String keyTextContentSize = "TextContentSize";
	
	public final static int initialTextTitleSize = 20; // textsize in pixel
	public final static int initialTextContentSize = 20;

	
	private static final HashMap<String,Integer> initMap = new HashMap<String,Integer>(){
		{
			put(keyTextTitleSize,initialTextTitleSize);
			put(keyTextContentSize,initialTextContentSize);
		}
	};
	
	
	public static int getSetting(String settingKey,Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(keyPref, 0);
		int settingValue = sharePreference.getInt(settingKey, initMap.get(settingKey));
		return settingValue;
	}
	
	public static void saveSetting(String settingKey,int settingValue, Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(keyPref, 0);
		sharePreference.edit().putInt(settingKey, settingValue).commit();
	}
	
}
