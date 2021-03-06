package com.adwhirl.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlLayout.ViewAdRunnable;
import com.adwhirl.adapters.AdWhirlAdapter;
import com.adwhirl.obj.Ration;
import com.adwhirl.util.AdWhirlUtil;
import com.vpon.adon.android.AdListener;
import com.vpon.adon.android.AdOnPlatform;
import com.vpon.adon.android.AdView;

public class AdOnCNAdapter extends AdWhirlAdapter implements AdListener {

	public AdOnCNAdapter(AdWhirlLayout adWhirlLayout, Ration ration) {
		super(adWhirlLayout, ration);
	}

	@Override
	public void handle() {
		AdWhirlLayout adWhirlLayout = adWhirlLayoutReference.get();
		if (adWhirlLayout == null) {
			return;
		}

		Activity activity = adWhirlLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		int width=0;
		int height=0;
		   DisplayMetrics dm = new DisplayMetrics();
		   activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		   if (320 >dm.widthPixels)
		   {
		   Log.i("Ad Status", "目前Vpon不提供320以下的广告");
		   }
		   else if(320<=dm.widthPixels&&dm.widthPixels<480)
		   {
		   width=320;
		   height=48;
		   }
		   else if(480<=dm.widthPixels&&dm.widthPixels<720)
		   {
		   width=480;
		   height=72;
		   }
		   else
		   {
		   width=720;
		   height=108;
		   }

		try {
			AdView adView = new AdView(activity, width, height);
//			AdView adView = new AdView(activity);
			//如果對於填寫adWidth和adHeight有任何疑問，請到wiki.vpon.com查詢。
	        //If there is any question about adWidth and adHeight, please check wiki.vpon.com.
			boolean autoRefreshAd = false;
			adView.setLicenseKey(ration.key, AdOnPlatform.CN, autoRefreshAd);
			adView.setAdListener(this);
			
			
//			 adWhirlLayout.addView(adView, new LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			 
			
			Log.v("SDK", "Handling handle function");
			
			//adWhirlLayout.addView(adView, new LayoutParams(
			//		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
		} catch (IllegalArgumentException e) {
			adWhirlLayout.rollover();
			return;
		}
	}
/*
	public void onRecevieAd(AdView adView) {
		Log.d(AdWhirlUtil.ADWHIRL, "AdOnCNAdapter success");

		AdWhirlLayout adWhirlLayout = adWhirlLayoutReference.get();
		if (adWhirlLayout == null) {
			return;
		}

		adWhirlLayout.adWhirlManager.resetRollover();
		adWhirlLayout.handler.post(new ViewAdRunnable(adWhirlLayout, adView));
		adWhirlLayout.rotateThreadedDelayed();
	}

	public void onFailedToRecevieAd(AdView adView) {
		Log.d(AdWhirlUtil.ADWHIRL, "AdOnCNAdapter failure");

		adView.setAdListener(null);

		AdWhirlLayout adWhirlLayout = adWhirlLayoutReference.get();
		if (adWhirlLayout == null) {
			return;
		}

		adWhirlLayout.rollover();
	}
	*/
	public void onRecevieAd(AdView adView) {
		Log.d(AdWhirlUtil.ADWHIRL, "AdOnCNAdapter success");

		AdWhirlLayout adWhirlLayout = adWhirlLayoutReference.get();
		if (adWhirlLayout == null) {
			return;
		}

		adWhirlLayout.adWhirlManager.resetRollover();
		adWhirlLayout.handler.post(new ViewAdRunnable(adWhirlLayout, adView));
		adWhirlLayout.rotateThreadedDelayed();
	}

	public void onFailedToRecevieAd(AdView adView) {
		Log.d(AdWhirlUtil.ADWHIRL, "AdOnCNAdapter failure");

		adView.setAdListener(null);

		AdWhirlLayout adWhirlLayout = adWhirlLayoutReference.get();
		if (adWhirlLayout == null) {
			return;
		}

		adWhirlLayout.rollover();
	}
}
