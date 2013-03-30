package com.ptt.food.blog;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlManager;
import com.adwhirl.AdWhirlTargeting;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.costum.android.widget.LoadMoreListView;
import com.google.ads.AdView;
import com.ptt.food.blog.api.DBAPI;
import com.ptt.food.blog.entity.Article;
import com.taiwan.imageload.ListArticleAdapter;
import com.taiwan.imageload.ListNothingAdapter;
import com.taiwan.imageload.SubListAdapter;

public class FavoriteActivity extends SherlockActivity implements AdWhirlInterface {
	
	private static final int Contact_US = 0;
	private static final int ID_ABOUT_US = 1;
    private static final int ID_GRADE = 2;
    private static final int ID_OUR_APP = 3;
    private static final int ID_SETTING = 6;

	private ArrayList<Article> articleList = new ArrayList<Article>();
	private ListView myList;
	private LinearLayout progressLayout;
	private AlertDialog.Builder aboutUsDialog;
	
	private final String   adWhirlKey  = "5dc7684994954d51add2cd7b0768f564";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_favorite);
		myList = (ListView) findViewById(R.id.list_favorite);
		progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
		setAboutDialog();
		
		final ActionBar ab = getSupportActionBar();		     
        
        ab.setTitle(" 我的最愛");
        ab.setDisplayHomeAsUpEnabled(true);
       
        new SearchFavoriteTask().execute();
        
        try {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // deprecated
            int height = display.getHeight(); // deprecated

            if (width > 320) {
                setAdAdwhirl();
            }
        } catch (Exception e) {

        }
       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, ID_SETTING, 0, "閱讀設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, Contact_US, 0, "聯絡我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_ABOUT_US, 1, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_GRADE, 2, "給APP評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_OUR_APP, 3, "我們的APP").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        break;
	    case Contact_US:
	    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	    	emailIntent.setType("plain/text");
	    	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"brotherkos@gmail.com"});
	    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "聯絡我們 from Ptt美食部落");
	    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
	    	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	        break;
	    case ID_ABOUT_US:
	    	aboutUsDialog.show();
	        break;
	    case ID_GRADE:
	    	Intent gradeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.recommend_url)));
			startActivity(gradeIntent);
	        break;
	    case ID_OUR_APP:
	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.recommend_url)));
			startActivity(browserIntent);
	        break;
	    case ID_SETTING:
	    	Intent intentSetting = new Intent(FavoriteActivity.this, SettingActivity.class);
	    	startActivity(intentSetting);
	        break;
	    }
	    return true;
	}
	
   
	
	private class SearchFavoriteTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
        	// search db data
        	articleList = DBAPI.getAllArticles(FavoriteActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            if(articleList.size()!=0){
            	 ListArticleAdapter myListAdapter = new ListArticleAdapter(FavoriteActivity.this, articleList, true);
 		         myList.setAdapter(myListAdapter);
            }else{
            	ListNothingAdapter nothingAdapter = new ListNothingAdapter(FavoriteActivity.this);
	          	myList.setAdapter(nothingAdapter);
            }
            
        }
    }
	
	private void setAboutDialog() {
		// TODO Auto-generated method stub
    	aboutUsDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.about_us_string))
				.setIcon(R.drawable.app_icon)
				.setMessage(getResources().getString(R.string.about_us))
				.setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
					}
		});
	}
	
	private void setAdAdwhirl() {
        // TODO Auto-generated method stub
        AdWhirlManager.setConfigExpireTimeout(1000 * 60);
        AdWhirlTargeting.setAge(23);
        AdWhirlTargeting.setGender(AdWhirlTargeting.Gender.MALE);
        AdWhirlTargeting.setKeywords("online games gaming");
        AdWhirlTargeting.setPostalCode("94123");
        AdWhirlTargeting.setTestMode(false);

        AdWhirlLayout adwhirlLayout = new AdWhirlLayout(this, adWhirlKey);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.adonView);

        adwhirlLayout.setAdWhirlInterface(this);

        mainLayout.addView(adwhirlLayout);

        mainLayout.invalidate();
    }

    @Override
    public void adWhirlGeneric() {
        // TODO Auto-generated method stub

    }

    public void rotationHoriztion(int beganDegree, int endDegree, AdView view) {
        final float centerX = 320 / 2.0f;
        final float centerY = 48 / 2.0f;
        final float zDepth = -0.50f * view.getHeight();

        Rotate3dAnimation rotation = new Rotate3dAnimation(beganDegree, endDegree, centerX, centerY, zDepth, true);
        rotation.setDuration(1000);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(rotation);
    }

}