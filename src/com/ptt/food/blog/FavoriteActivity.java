package com.ptt.food.blog;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.costum.android.widget.LoadMoreListView;
import com.ptt.food.blog.entity.Article;
import com.taiwan.imageload.ListNothingAdapter;
import com.taiwan.imageload.SubListAdapter;

public class FavoriteActivity extends SherlockActivity {
	

	private ArrayList<Article> articleList = new ArrayList<Article>();
	private SubListAdapter categoryAdapter;
	private ListView myList;
	private LinearLayout progressLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_favorite);
		myList = (ListView) findViewById(R.id.list_favorite);
		progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
		
		final ActionBar ab = getSupportActionBar();		     
        
        ab.setTitle(" 我的最愛");
        ab.setDisplayHomeAsUpEnabled(true);
       
        new SearchFavoriteTask().execute();
       
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
	
   
	
	private class SearchFavoriteTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
        	// search db data
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            if(articleList.size()!=0){
            	
            }else{
            	ListNothingAdapter nothingAdapter = new ListNothingAdapter(FavoriteActivity.this);
	          	myList.setAdapter(nothingAdapter);
            }
            
        }
    }	

}