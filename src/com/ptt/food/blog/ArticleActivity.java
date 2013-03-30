package com.ptt.food.blog;



import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlManager;
import com.adwhirl.AdWhirlTargeting;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.google.ads.AdView;
import com.kosbrother.tool.DetectScrollView;
import com.kosbrother.tool.DetectScrollView.DetectScrollViewListener;
import com.ptt.food.blog.api.DBAPI;
import com.ptt.food.blog.api.PttFoodAPI;
import com.ptt.food.blog.api.Setting;
import com.ptt.food.blog.entity.Article;


public class ArticleActivity extends SherlockFragmentActivity implements DetectScrollViewListener,AdWhirlInterface{
	
	private static final int ID_WORD = 7;
    private static final int ID_SITE = 8;
    
    private static final int Contact_US = 0;
	private static final int ID_ABOUT_US = 1;
    private static final int ID_GRADE = 2;
    private static final int ID_OUR_APP = 3;
    private static final int ID_FAVORITE = 4;
    private static final int ID_SETTING = 6;
    
	private TextView articleTextView;
	private TextView articleTextTitle;
	private TextView articleTextDate;
	private TextView articlePercent;
	private CheckBox checkboxFavorite;
	private DetectScrollView articleScrollView;
	private Button articleButtonUp;
	private Button articleButtonDown;
	private Button buttonReload;
	
	private Article myAricle; // uset to get article text
//	private Article theGottenArticle;
	private ArrayList<Article> favoriteArticles;
	private Bundle mBundle;
//	private String articleTitle;
//	private int articleId;
	private ActionBar ab;
	private LinearLayout layoutProgress;
	private LinearLayout layoutReload;
	private MenuItem itemWord;
	private MenuItem itemSite;
	private LinearLayout layoutWord;
	private WebView webArticle;
	private boolean webBoolean = true;
	
	private int[] articleIDs;
	private ArrayList<Integer> articleIDsArray = new ArrayList<Integer>();
	private int articlePosition;
	private Boolean LoadOrNot = true;
	private Boolean LoadingOrNot = false; //when false, can load from server; when true, can't load from server
	private int pageNum = 0;
	private ArrayList<Article> newArticles = new ArrayList<Article>();
	
	private int textTitleSize;
	private int textContentSize;
	private AlertDialog.Builder aboutUsDialog;
	
	private final String   adWhirlKey  = "5dc7684994954d51add2cd7b0768f564";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article); 
       
        restorePreValues();
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        
        mBundle = this.getIntent().getExtras();       
//      articleId = mBundle.getInt("ArticleId");
//      articleTitle = mBundle.getString("ArticleTitle");
        articleIDs = mBundle.getIntArray("ArticleIds");
        articlePosition = mBundle.getInt("ArticlePosition");
        pageNum = mBundle.getInt("PageNum");
        
        if(pageNum == -1){
        	LoadOrNot = false;
        }
        
        pourArticleIDs(); 
        setViews();
        
        new DownloadArticleTask().execute();
        
        setAboutDialog();
        
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


	private void restorePreValues() {
		textTitleSize = Setting.getSetting(Setting.keyTextTitleSize, ArticleActivity.this);
		textContentSize = Setting.getSetting(Setting.keyTextContentSize, ArticleActivity.this);
		
	}


	private void pourArticleIDs() {
		for(int i=0; i< articleIDs.length;i++){
			articleIDsArray.add(articleIDs[i]);
		}
	}


	private void setViews() {		
		
		layoutProgress = (LinearLayout) findViewById (R.id.layout_progress);
		layoutReload = (LinearLayout) findViewById (R.id.layout_reload);
		layoutWord = (LinearLayout) findViewById (R.id.layout_word);
		articleTextView = (TextView) findViewById (R.id.article_text);
		articleTextTitle = (TextView) findViewById (R.id.text_article_title);
		articleTextDate = (TextView) findViewById (R.id.text_article_date);
		checkboxFavorite = (CheckBox) findViewById (R.id.checkbox_article);
        articleScrollView = (DetectScrollView) findViewById (R.id.article_scrollview);
        articleButtonUp = (Button) findViewById (R.id.article_button_up);
        articleButtonDown = (Button) findViewById (R.id.article_button_down);
        buttonReload = (Button) findViewById (R.id.button_reload);
        articlePercent = (TextView) findViewById (R.id.article_percent);
        
        articleTextTitle.setTextSize(textTitleSize);
        articleTextDate.setTextSize(textTitleSize-3);
        articleTextView.setTextSize(textContentSize);
        
        webArticle = (WebView) findViewById (R.id.web_article);
        webArticle.getSettings().setSupportZoom(true); 
        webArticle.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
        webArticle.setWebViewClient(new WebViewClient());
        webArticle.setVisibility(View.GONE);
        
        
        articleScrollView.setScrollViewListener(ArticleActivity.this);
        
        if(articleIDs.length ==1){
        	articleButtonUp.setVisibility(View.GONE);
        	articleButtonDown.setVisibility(View.GONE);
        }
        
        
        articleButtonUp.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				if(isOnline()){          	
	            	
		            if(articlePosition == 0){
		            	Toast.makeText(getApplicationContext(), "無上一則", Toast.LENGTH_SHORT).show();
		            }else{
		            	layoutProgress.setVisibility(View.VISIBLE);
		            	articlePosition = articlePosition - 1;
			            new UpdateNewsTask().execute();
		            }
	            	
            	}else{
            		Toast.makeText(getApplicationContext(), "無網路連線", Toast.LENGTH_SHORT).show();
                	finish();
            	}
			}

			
		});
        
        articleButtonDown.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				if(isOnline()){
	           
		            if(articlePosition + 1 == articleIDsArray.size()){
		            		Toast.makeText(getApplicationContext(), "無下一則", Toast.LENGTH_SHORT).show();
		            }else{
		            	layoutProgress.setVisibility(View.VISIBLE);
		            	articlePosition = articlePosition + 1;
			            new UpdateNewsTask().execute();
		            }
	            		            	
	            	if(LoadOrNot && articleIDsArray.size()- articlePosition < 5){
	            		new loadNewsIDsTask().execute();
	            	}
            	}else{
            		Toast.makeText(getApplicationContext(), "無網路連線", Toast.LENGTH_SHORT).show();
                	finish();
            	}
			}
		});
        
        buttonReload.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				layoutProgress.setVisibility(View.VISIBLE);
				layoutReload.setVisibility(View.GONE);
				new UpdateNewsTask().execute();
			}
		});
        
        checkboxFavorite.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
				if(checkboxFavorite.isChecked()){
					DBAPI.insertArticle(myAricle, ArticleActivity.this);
					Toast.makeText(ArticleActivity.this, "加入我的最愛", Toast.LENGTH_SHORT).show();
				}else{					
					DBAPI.deleteArticle(myAricle, ArticleActivity.this);
					Toast.makeText(ArticleActivity.this, "從我的最愛移除", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
	}
	
	@Override
	public void onScrollChanged(DetectScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		int kk = articleScrollView.getHeight();
		int tt = articleTextView.getHeight();
		int xx = (int)(((double)(y+kk)/(double)(tt))*100);
		String yPositon = Integer.toString(xx);
		articlePercent.setText(yPositon+"%");
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		menu.add(0, ID_SETTING, 0, "閱讀設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, Contact_US, 0, "聯絡我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_ABOUT_US, 1, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_GRADE, 2, "給APP評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_OUR_APP, 3, "我們的APP").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_FAVORITE, 7, "最愛").setIcon(R.drawable.icon_heart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		itemWord = menu.add(0, ID_WORD, 5, "文字版");
		itemWord.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		itemSite = menu.add(0, ID_SITE, 6, "網誌版");
		itemSite.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
        return true;
    }
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    case ID_WORD: // setting
	    	layoutWord.setVisibility(View.VISIBLE);
	    	webArticle.setVisibility(View.GONE);
	    	articlePercent.setVisibility(View.VISIBLE);
	    	Toast.makeText(this, "文字版", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_SITE: // response
	    	if(myAricle == null){
	    		Toast.makeText(this, "資料下載中,請稍候", Toast.LENGTH_SHORT).show();	    	
	    	}else{
	    		if(webBoolean){
	    			articlePercent.setVisibility(View.GONE);
			    	layoutWord.setVisibility(View.GONE);
			    	webArticle.setVisibility(View.VISIBLE);
			    	Toast.makeText(this, "網誌版", Toast.LENGTH_SHORT).show();
		    	}else{
		    		Toast.makeText(this, "本文章無網誌版", Toast.LENGTH_SHORT).show();
		    	}
	    	}
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
	    case ID_FAVORITE:
	    	Intent intent = new Intent(ArticleActivity.this, FavoriteActivity.class);
	    	startActivity(intent);
	        break;
	    case ID_SETTING:
	    	Intent intentSetting = new Intent(ArticleActivity.this, SettingActivity.class);
	    	startActivity(intentSetting);
	        break;
	    }
	    return true;
	}
	
	private class DownloadArticleTask extends AsyncTask {
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	    }
		
        @Override
        protected Object doInBackground(Object... params) {        	
        	myAricle = PttFoodAPI.getArticle(articleIDsArray.get(articlePosition));
        	favoriteArticles = DBAPI.getAllArticles(ArticleActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            
            layoutProgress.setVisibility(View.GONE);
            
            if (myAricle!=null){	            
            	setUIAfterLoading();           	
            }else{
            	// 重試
            	layoutReload.setVisibility(View.VISIBLE);
            	Toast.makeText(getApplicationContext(), "無資料,請重試！", Toast.LENGTH_SHORT).show();
            }           
        }
	}
	
	private class loadNewsIDsTask extends AsyncTask<Void, Void, Void> {
	  	
		@Override
		protected Void doInBackground(Void... params) {
			if(!LoadingOrNot){
				LoadingOrNot = true;
				pageNum = pageNum+1;			
				newArticles = PttFoodAPI.getNewArticles(pageNum);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(LoadingOrNot){
				if (newArticles == null){
					LoadOrNot = false;
				}else{					
					for(int i= 0 ; i< newArticles.size(); i++){
						articleIDsArray.add(newArticles.get(i).getId()); //if newsIDsArray more than one, do normal
					}					
				}
				LoadingOrNot = false;
			}
			
			super.onPostExecute(result);			
		}
	}
	
	
	private class UpdateNewsTask extends AsyncTask<Void, Void, Void> {
    	 	
		@Override
		protected Void doInBackground(Void... params) {			
			myAricle = PttFoodAPI.getArticle(articleIDsArray.get(articlePosition));		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			layoutProgress.setVisibility(View.GONE);
			
			if(myAricle!= null){
				layoutWord.setVisibility(View.VISIBLE);
		    	webArticle.setVisibility(View.GONE);
				articlePercent.setVisibility(View.VISIBLE);
				articlePercent.setText("0%");
				articleScrollView.fullScroll(ScrollView.FOCUS_UP);
				setUIAfterLoading();
			}else{
				layoutReload.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "無資料,請重試！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void setUIAfterLoading() {
			
        articleTextView.setText(myAricle.getContent());
        
        String text = "<font color=#9E1919>"+myAricle.getTitle()+"</font>"+ "<font color=#AD8440>"+" by "+myAricle.getAuthor()+"</font>";
        articleTextTitle.setText(Html.fromHtml(text));
        
        articleTextDate.setText(myAricle.getReleaseTime());
        if(myAricle.getLink()!=null && !myAricle.getLink().equals("null")){
        	webBoolean = true;
        	webArticle.loadUrl(myAricle.getLink());            	
        }else{
        	webBoolean = false;
        }
        
        // set checkbox
        for(int i =0; i<favoriteArticles.size();i++){
			if(favoriteArticles.get(i).getId() == myAricle.getId()){
				checkboxFavorite.setChecked(true);
				break;
			}else{
				checkboxFavorite.setChecked(false);
			}
	    }
	}
	
	
	@Override
    protected void onResume() {
        super.onResume();
        restorePreValues();
        articleTextTitle.setTextSize(textTitleSize);
        articleTextDate.setTextSize(textTitleSize-3);
        articleTextView.setTextSize(textContentSize);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	 }
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
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