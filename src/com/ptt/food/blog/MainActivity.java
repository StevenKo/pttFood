package com.ptt.food.blog;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlManager;
import com.adwhirl.AdWhirlTargeting;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.google.ads.AdView;
import com.ptt.food.blog.R;
import com.ptt.food.fragment.NewsFragment;
import com.ptt.food.fragment.SiteFragment;
import com.ptt.food.fragment.TypeFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.Display;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends SherlockFragmentActivity implements AdWhirlInterface {
	
	private static final int Contact_US = 0;
	private static final int ID_ABOUT_US = 1;
    private static final int ID_GRADE = 2;
    private static final int ID_OUR_APP = 3;
    private static final int ID_FAVORITE = 4;
    private static final int ID_SEARCH = 5;
    private static final int ID_SETTING = 6;
	
    private AlertDialog.Builder aboutUsDialog;
	private String[] CONTENT;
	private MenuItem  itemSearch;
	private ViewPager pager;
	
	private final String   adWhirlKey  = "5dc7684994954d51add2cd7b0768f564";

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);
        setAboutDialog();
        
        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.tabs);
        
        FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
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


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	
    	menu.add(0, ID_SETTING, 0, "閱讀設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
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
                          Bundle bundle = new Bundle();
                          bundle.putString("SearchKeyword", v.getText().toString());
                          Intent intent = new Intent();
                          intent.setClass(MainActivity.this, SearchActivity.class);
                          intent.putExtras(bundle);
                          startActivity(intent);
                          itemSearch.collapseActionView();
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
	    	Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
	    	startActivity(intent);
	        break;
	    case ID_SETTING:
	    	Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
	    	startActivity(intentSetting);
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
    
    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() ==0) {
            finish();
        } else {
            pager.setCurrentItem(0, true);
        }
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
