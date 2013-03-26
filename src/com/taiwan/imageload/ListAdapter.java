package com.taiwan.imageload;

import java.util.ArrayList;
import com.ptt.food.blog.R;
import com.ptt.food.blog.SubActivity;
import com.ptt.food.blog.entity.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<Category> data;
    private static LayoutInflater inflater=null;
   
    
    public ListAdapter(Activity a, ArrayList<Category> d) {
        activity = a;
        data=d;
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
	        text.setText(data.get(position).getCateName());
	        testId.setText(Integer.toString(data.get(position).getId()));
	        
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
	        
        return vi;
    }
}