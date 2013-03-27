package com.taiwan.imageload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ptt.food.blog.ArticleActivity;
import com.ptt.food.blog.R;
import com.ptt.food.blog.api.DBAPI;
import com.ptt.food.blog.entity.Article;

public class ListArticleAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<Article> data;
    private ArrayList<Article> dataFavorite;
    private static LayoutInflater inflater=null;
   
    
    public ListArticleAdapter(Activity a, ArrayList<Article> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataFavorite = DBAPI.getAllArticles(activity);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.item_list_article, null);
	        TextView text=(TextView)vi.findViewById(R.id.text_article_title);
	        TextView textDate=(TextView)vi.findViewById(R.id.text_article_date);
	        Button button = (Button)vi.findViewById(R.id.button_item_category);
	        final CheckBox checkboxFavorite = (CheckBox)vi.findViewById(R.id.checkbox_article);
	        text.setText(data.get(position).getTitle());
	        
	        String titleString = "<font color=#cc0029>"+data.get(position).getTitle()+"</font>"+ "<font color=#ffcc00>"+"by "+data.get(position).getAuthor()+"</font>";
	        text.setText(Html.fromHtml(titleString));
	        
	        try{
		        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
		        String dateString = formatter.format(data.get(position).getReleaseTime()); 
		        textDate.setText(dateString);
	        }catch(Exception e){
	        	textDate.setVisibility(View.GONE);
	        }
	        
	
	        for(int i =0; i<dataFavorite.size();i++){
				if(dataFavorite.get(i).getId() == data.get(position).getId()){
					checkboxFavorite.setChecked(true);
					break;
				}else{
					checkboxFavorite.setChecked(false);
				}
			}
	        
	        checkboxFavorite.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
	            	if(checkboxFavorite.isChecked()){
						DBAPI.insertArticle(data.get(position), activity);
						dataFavorite.add(data.get(position));
						Toast.makeText(activity, "加入我的最愛", Toast.LENGTH_SHORT).show();
					}else{
						DBAPI.deleteArticle(data.get(position), activity);
						for(int i =0; i<dataFavorite.size();i++){
							if(dataFavorite.get(i).getId() == data.get(position).getId()){
								dataFavorite.remove(i);
							}
						}
						Toast.makeText(activity, "從我的最愛移除", Toast.LENGTH_SHORT).show();
					}
	            }
	        });
	        
	        vi.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
	            	Intent intent = new Intent(activity, ArticleActivity.class);
	                Bundle bundle = new Bundle();
	                bundle.putInt("ArticleId", data.get(position).getId());
	                bundle.putString("ArticleTitle", data.get(position).getTitle());
	                intent.putExtras(bundle);
	                activity.startActivity(intent);
	            }

	        });
	        
        return vi;
    }
}
