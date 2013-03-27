package com.ptt.food.blog;


import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.kosbrother.tool.DetectScrollView;
import com.kosbrother.tool.DetectScrollView.DetectScrollViewListener;
import com.ptt.food.blog.api.DBAPI;
import com.ptt.food.blog.api.PttFoodAPI;
import com.ptt.food.blog.entity.Article;

public class ArticleActivity extends SherlockFragmentActivity implements DetectScrollViewListener{
	
	private static final int ID_WORD = 0;
    private static final int ID_SITE = 1;
    
	private TextView articleTextView;
	private TextView articleTextTitle;
	private TextView articleTextDate;
	private CheckBox checkboxFavorite;
	private DetectScrollView articleScrollView;
	private Button articleButtonUp;
	private Button articleButtonDown;
	private TextView articlePercent;
	private Article myAricle; // uset to get article text
	private Article theGottenArticle;
	private ArrayList<Article> favoriteArticles;
	private Bundle mBundle;
	private String articleTitle;
	private int articleId;
	private ActionBar ab;
	private LinearLayout layoutProgress;
	private MenuItem itemWord;
	private MenuItem itemSite;
	private LinearLayout layoutWord;
	private WebView webArticle;
	private boolean webBoolean = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article); 
        setViews();
        
        ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();       
        articleId = mBundle.getInt("ArticleId");
        articleTitle = mBundle.getString("ArticleTitle");
        
        ab.setDisplayHomeAsUpEnabled(true);
        
        new DownloadArticleTask().execute();
        
    }


	private void setViews() {		
		
		layoutProgress = (LinearLayout) findViewById (R.id.layout_progress);
		layoutWord = (LinearLayout) findViewById (R.id.layout_word);
		articleTextView = (TextView) findViewById (R.id.article_text);
		articleTextTitle = (TextView) findViewById (R.id.text_article_title);
		articleTextDate = (TextView) findViewById (R.id.text_article_date);
		checkboxFavorite = (CheckBox) findViewById (R.id.checkbox_article);
        articleScrollView = (DetectScrollView) findViewById (R.id.article_scrollview);
        articleButtonUp = (Button) findViewById (R.id.article_button_up);
        articleButtonDown = (Button) findViewById (R.id.article_button_down);
        articlePercent = (TextView) findViewById (R.id.article_percent);
        webArticle = (WebView) findViewById (R.id.web_article);
        webArticle.getSettings().setSupportZoom(true); 
        webArticle.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
        webArticle.setWebViewClient(new WebViewClient());
        webArticle.setVisibility(View.GONE);
        
        
        articleScrollView.setScrollViewListener(ArticleActivity.this);
        
        articleButtonUp.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
//				new GetPreviousArticleTask().execute();
			}
		});
        
        articleButtonDown.setOnClickListener(new OnClickListener() {			 
			@Override
			public void onClick(View arg0) {
//				new GetNextArticleTask().execute();
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
	
	
	public static int dip2px(Context context, float dpValue) {
  	  final float scale = context.getResources().getDisplayMetrics().density;
  	  return (int) (dpValue * scale + 0.5f);
  	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
		
		itemWord = menu.add(0, ID_WORD, 0, "文字版").setIcon(getResources().getDrawable(R.drawable.word_selected));
		itemWord.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		itemSite = menu.add(0, ID_SITE, 1, "網誌版").setIcon(getResources().getDrawable(R.drawable.site_black));
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
	    	itemWord.setIcon(getResources().getDrawable(R.drawable.word_selected));
	    	itemSite.setIcon(getResources().getDrawable(R.drawable.site_black));
	    	layoutWord.setVisibility(View.VISIBLE);
	    	webArticle.setVisibility(View.GONE);
	    	Toast.makeText(this, "文字版", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_SITE: // response
	    	if(webBoolean){
		    	itemWord.setIcon(getResources().getDrawable(R.drawable.word_black));
		    	itemSite.setIcon(getResources().getDrawable(R.drawable.site_selected));
		    	layoutWord.setVisibility(View.GONE);
		    	webArticle.setVisibility(View.VISIBLE);
		    	Toast.makeText(this, "網誌版", Toast.LENGTH_SHORT).show();
	    	}else{
	    		Toast.makeText(this, "本文章無網誌版", Toast.LENGTH_SHORT).show();
	    	}
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
        		theGottenArticle = PttFoodAPI.getArticle(articleId);
        		if (theGottenArticle != null){
        			myAricle = theGottenArticle;
        		}
        		favoriteArticles = DBAPI.getAllArticles(ArticleActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            
            if (myAricle!=null){
	            layoutProgress.setVisibility(View.GONE);
	            articleTextView.setText(myAricle.getContent());
	            
	            String text = "<font color=#cc0029>"+myAricle.getTitle()+"</font>"+ "<font color=#ffcc00>"+"by "+myAricle.getAuthor()+"</font>";
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
            
        }
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	 }
	
	
}