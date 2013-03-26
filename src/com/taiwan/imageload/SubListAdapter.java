package com.taiwan.imageload;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptt.food.blog.ArticleActivity;
import com.ptt.food.blog.R;
import com.ptt.food.blog.SubActivity;
import com.ptt.food.blog.entity.Category;

public class SubListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<Category> data;
    private static LayoutInflater inflater=null;
    private int categorySize;
   
    
    public SubListAdapter(Activity a, ArrayList<Category> d, int categoryLength) {
        activity = a;
        data=d;
        categorySize = categoryLength;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
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
            vi = inflater.inflate(R.layout.item_list, null);
	        TextView text=(TextView)vi.findViewById(R.id.text_category_name);
	        TextView testId = (TextView)vi.findViewById(R.id.text_category_id);
	        ImageView image = (ImageView)vi.findViewById(R.id.image_type);
	        text.setText(data.get(position).getCateName());
	        testId.setText(Integer.toString(data.get(position).getId()));
	        
	        if(position < categorySize){
	        	image.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.icon_folder));
		        vi.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                // Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
		                Intent intent = new Intent(activity, SubActivity.class);
		                Bundle bundle = new Bundle();
		                bundle.putInt("CategoryId", data.get(position).getId());
		                bundle.putString("CategoryTitle", data.get(position).getCateName());
		                intent.putExtras(bundle);
		                activity.startActivity(intent);
		            }
	
		        });
	        }else{	        	
	        	image.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.icon_article));
	        	vi.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                // Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
		                Intent intent = new Intent(activity, ArticleActivity.class);
		                Bundle bundle = new Bundle();
		                bundle.putInt("ArticleId", data.get(position).getId());
		                bundle.putString("ArticleTitle", data.get(position).getCateName());
		                intent.putExtras(bundle);
		                activity.startActivity(intent);
		            }
		        });
	        }
	        
        return vi;
    }
}
