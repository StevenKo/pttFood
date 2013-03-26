package com.ptt.food.fragment;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.ptt.food.blog.R;
import com.ptt.food.blog.api.PttFoodAPI;
import com.ptt.food.blog.entity.Article;
import com.ptt.food.blog.entity.Category;
import com.taiwan.imageload.ListAdapter;

public class SiteFragment extends Fragment {
    
	private LinearLayout progressLayout;
	private LoadMoreListView myList;
	private ArrayList<Category> categoryList;
	private ListAdapter categoryAdapter;
	
    public static SiteFragment newInstance() {     
   	 
      SiteFragment fragment = new SiteFragment(); 	    
      return fragment;
        
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        new DownloadChannelsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	View myFragmentView = inflater.inflate(R.layout.loadmore, container, false);
    	progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    	myList = (LoadMoreListView) myFragmentView.findViewById(R.id.news_list);
        myList.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				myList.onLoadMoreComplete();
			}
		});
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
        	categoryList = PttFoodAPI.getAreaCateogories();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            
            if(categoryList !=null){
          	  try{
          		categoryAdapter = new ListAdapter(getActivity(), categoryList);
  		        myList.setAdapter(categoryAdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
          	   
            }

        }
    }
    
    
//    public boolean isOnline() {
//	    ConnectivityManager cm =
//	        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
//	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//	        return true;
//	    }
//	    return false;
//	}
}
