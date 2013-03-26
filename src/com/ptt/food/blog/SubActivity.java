package com.ptt.food.blog;


import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.ptt.food.blog.api.PttFoodAPI;
import com.ptt.food.blog.entity.Article;
import com.ptt.food.blog.entity.Category;
import com.taiwan.imageload.ListNothingAdapter;
import com.taiwan.imageload.SubListAdapter;


public class SubActivity extends SherlockActivity {
	
	private Bundle mBundle;
	private String categoryTitle;
	private LoadMoreListView  myList;
	private LinearLayout progressLayout;
	private ArrayList<Category> categoryList;
	private ArrayList<Article> articleList;
	private int categoryId;
	private SubListAdapter categoryAdapter;
	private int categoryLength;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadmore);
		myList = (LoadMoreListView) findViewById(R.id.news_list);
		progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
		
		
		final ActionBar ab = getSupportActionBar();		     
        mBundle = this.getIntent().getExtras();
        categoryTitle = mBundle.getString("CategoryTitle");
        categoryId = mBundle.getInt("CategoryId");
        
        ab.setTitle(categoryTitle);
        ab.setDisplayHomeAsUpEnabled(true);
       
        new DownloadChannelsTask().execute();
       
        myList.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				myList.onLoadMoreComplete();
//				if(checkLoad){
//					myPage = myPage +1;
//				}else{
//					myList.onLoadMoreComplete();
//				}
			}
		});
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        return true;
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    }
	    return true;
	}
	
   
	
	private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {

        	categoryList = PttFoodAPI.getSubcategories(categoryId);
        	articleList = PttFoodAPI.getCategoryArticles(categoryId);
        	categoryLength = categoryList.size();
        	
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            
            if(categoryList!=null){
            	if(articleList!=null && articleList.size()!=0){
            		for(int i=0; i<articleList.size(); i++){
            			categoryList.add(new Category(articleList.get(i).getId(), articleList.get(i).getTitle()));
            		}
            	}
            	
            }
            
            if(categoryList.size()!=0){
            	categoryAdapter = new SubListAdapter(SubActivity.this, categoryList,categoryLength);
            	myList.setAdapter(categoryAdapter);
            }else{
            	ListNothingAdapter nothingAdapter = new ListNothingAdapter(SubActivity.this);
            	myList.setAdapter(nothingAdapter);
            }
            
        }
    }	

}
