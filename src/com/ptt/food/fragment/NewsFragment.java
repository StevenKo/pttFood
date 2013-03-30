package com.ptt.food.fragment;

import java.util.ArrayList;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.ptt.food.blog.R;
import com.ptt.food.blog.api.PttFoodAPI;
import com.ptt.food.blog.entity.Article;
import com.taiwan.imageload.ListArticleAdapter;
import com.taiwan.imageload.ListNothingAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public final class NewsFragment extends Fragment {
    
	public static int myPage = 1;
	private Boolean checkLoad = true;
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private LoadMoreListView myList;
	private ArrayList<Article> newArticles = new ArrayList<Article>();
	private ArrayList<Article> moreArticles = new ArrayList<Article>();
	private ListArticleAdapter myListAdapter;
	private Button buttonReload;
	
    public static NewsFragment newInstance() {     
   	 
  	  NewsFragment fragment = new NewsFragment(); 	    
      return fragment;
        
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	View myFragmentView = inflater.inflate(R.layout.loadmore, container, false);
    	progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    	reloadLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
    	buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
    	myList = (LoadMoreListView) myFragmentView.findViewById(R.id.news_list);
        myList.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list				
				if(checkLoad){
					myPage = myPage +1;
					new LoadMoreTask().execute();
				}else{
					myList.onLoadMoreComplete();
				}
			}
		});
        
        buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });
        
        if (myListAdapter != null) {
            progressLayout.setVisibility(View.GONE);
            myList.setAdapter(myListAdapter);
        } else {
            new DownloadChannelsTask().execute();
        }
        
        return myFragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       
    }
    
    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	
        	ArrayList<Article> gottenArticles = PttFoodAPI.getNewArticles(myPage);
        	
        	if(gottenArticles != null && gottenArticles.size()!=0){
	        	for (int i=0; i< gottenArticles.size();i++){
	        		String title = gottenArticles.get(i).getTitle();
	        		if(title != null && title.indexOf("刪除")==-1 && !title.equals("")){
	        			newArticles.add(gottenArticles.get(i));
	        		}
	        	}
        	}
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            
            myListAdapter = new ListArticleAdapter(getActivity(), newArticles, false);
	        myList.setAdapter(myListAdapter);
            
//            if(newArticles !=null && newArticles.size()!=0){
//          	  try{
//          		  myListAdapter = new ListArticleAdapter(getActivity(), newArticles, false);
//  		          myList.setAdapter(myListAdapter);
//          	  }catch(Exception e){
//          		 
//          	  }
//            }else{
//            	reloadLayout.setVisibility(View.VISIBLE);
//            }

        }
    }
    
    
    private class LoadMoreTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	
        	moreArticles.clear();
        	moreArticles = PttFoodAPI.getNewArticles(myPage); 
        	if(moreArticles!= null){
	        	for(int i=0; i<moreArticles.size();i++){
	        		String title = moreArticles.get(i).getTitle();
	        		if(!title.equals("") && title.indexOf("刪除")==-1){
	        			newArticles.add(moreArticles.get(i));
	        		}
	            }
        	}
        	
        	
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            
            if(moreArticles!= null){
            	myListAdapter.notifyDataSetChanged();	                
            }else{
                checkLoad= false;
                Toast.makeText(getActivity(), "no more data", Toast.LENGTH_SHORT).show();            	
            }       
          	myList.onLoadMoreComplete();
          	
          	
        }
    }
    
}
