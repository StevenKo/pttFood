package com.ptt.food.blog;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ptt.food.blog.R;
import com.ptt.food.fragment.NewsFragment;
import com.ptt.food.fragment.SiteFragment;
import com.ptt.food.fragment.TypeFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity {
	
	private static final int Contact_US = 0;
	private static final int ID_ABOUT_US = 1;
    private static final int ID_GRADE = 2;
    private static final int ID_OUR_APP = 3;
    private static final int ID_FAVORITE = 4;
    private static final int ID_SEARCH = 5;
	
	private String[] CONTENT;
	private MenuItem  itemSearch;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);
        
        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.tabs);
        
        FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, Contact_US, 0, "聯絡我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_ABOUT_US, 1, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_GRADE, 2, "給APP評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_OUR_APP, 3, "我們的APP").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_FAVORITE, 5, "最愛").setIcon(R.drawable.icon_heart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	
    	itemSearch = menu.add(0, ID_SEARCH, 4, "search").setIcon(R.drawable.ic_search_inverse).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            private EditText search;

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                search = (EditText) item.getActionView();
                search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                search.setInputType(InputType.TYPE_CLASS_TEXT);
                search.requestFocus();
                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    	if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {                    	  
//                          Bundle bundle = new Bundle();
//                          bundle.putString("SearchKeyword", v.getText().toString());
//                          Intent intent = new Intent();
//                          intent.setClass(MainActivity.this, SearchActivity.class);
//                          intent.putExtras(bundle);
//                          startActivity(intent);
//                          itemSearch.collapseActionView();
                          return true;
                      }
                        return false;
                    }
                });
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // TODO Auto-generated method stub
                search.setText("");
                return true;
            }
        }).setActionView(R.layout.collapsible_edittext);
		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    	
        return true;
	}
    
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    case Contact_US:
	         Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_ABOUT_US:
	         Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_GRADE:
	         Toast.makeText(this, "Grade Us", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_OUR_APP:
	         Toast.makeText(this, "Our App", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_FAVORITE:
	         Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
	        break;
	    case ID_SEARCH:
	        break;
	    }
	    return true;
	}
    
    class GoogleMusicAdapter extends FragmentStatePagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	Fragment kk = new Fragment();        	
        	if(position==0){
            	kk = NewsFragment.newInstance();
        	}else if(position == 1){
        		kk = SiteFragment.newInstance();
        	}else if(position == 2){
        		kk = TypeFragment.newInstance();
        	}
            return kk;
        }
       

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

}
