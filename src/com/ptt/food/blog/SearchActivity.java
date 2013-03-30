package com.ptt.food.blog;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdView;
import com.ptt.food.blog.api.PttFoodAPI;
import com.ptt.food.blog.entity.Article;
import com.ptt.food.fragment.NewsFragment;
import com.taiwan.imageload.ImageLoader;
import com.taiwan.imageload.ListArticleAdapter;

public class SearchActivity extends SherlockListActivity {

	private static final int Contact_US = 0;
	private static final int ID_ABOUT_US = 1;
    private static final int ID_GRADE = 2;
    private static final int ID_OUR_APP = 3;
    private static final int ID_FAVORITE = 4;
    private static final int ID_SEARCH = 5;

    private Bundle              mBundle;
    private String              keyword;
    private ArrayList<Article>  articles;
    private ListView            articleListView;
    private MenuItem            item;

    private LinearLayout        layoutNoSearch;
    private AlertDialog.Builder aboutUsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        layoutNoSearch = (LinearLayout) findViewById(R.id.layout_no_search);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        
        mBundle = this.getIntent().getExtras();
        keyword = mBundle.getString("SearchKeyword");
        articleListView = this.getListView();

        articleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Article movie = articles.get(position);
                Intent newAct = new Intent();
                Bundle bundle = new Bundle();
                bundle.putIntArray("ArticleIds", new int[] {movie.getId()});
                bundle.putInt("ArticlePosition", 0);
                newAct.putExtras(bundle);
                newAct.setClass(SearchActivity.this, ArticleActivity.class);
                startActivity(newAct);
            }

        });

        setAboutDialog();        
        new LoadDataTask().execute();
    }

   

//    public class SearchAdapter extends BaseAdapter {
//
//        private final Context          mContext;
//        private final ArrayList<Article> articles;
//        private final ImageLoader      imageLoader;
//
//        public SearchAdapter(Context mContext, ArrayList<Article> articles) {
//            this.articles = articles;
//            this.mContext = mContext;
//            imageLoader = new ImageLoader(mContext);
//        }
//
//        @Override
//        public int getCount() {
//            return articles.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater myInflater = LayoutInflater.from(mContext);
//            View converView = myInflater.inflate(R.layout.item_novel_search, null);
//            ImageView pic = (ImageView) converView.findViewById(R.id.grid_item_image);
//            TextView name = (TextView) converView.findViewById(R.id.grid_item_name);
//            TextView author = (TextView) converView.findViewById(R.id.grid_item_author);
//            TextView articleNum = (TextView) converView.findViewById(R.id.grid_item_counts);
//            TextView textFinish = (TextView) converView.findViewById(R.id.grid_item_finish);
//            TextView textSerialize = (TextView) converView.findViewById(R.id.serializing);
//
//            imageLoader.DisplayImage(novels.get(position).getPic(), pic);
//            name.setText(novels.get(position).getName());
//            author.setText(novels.get(position).getAuthor());
//            articleNum.setText(novels.get(position).getArticleNum());
//            textFinish.setText(novels.get(position).getLastUpdate());
//
//            if (novels.get(position).isSerializing()) {
//                textSerialize.setText("連載中...");
//            } else {
//                textSerialize.setText("全本");
//            }
//
//            return converView;
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

    	menu.add(0, Contact_US, 0, "聯絡我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_ABOUT_US, 1, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_GRADE, 2, "給APP評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_OUR_APP, 3, "我們的APP").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	menu.add(0, ID_FAVORITE, 5, "最愛").setIcon(R.drawable.icon_heart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        item = menu.add(0, ID_SEARCH, 4, "search").setIcon(R.drawable.ic_search_inverse)
                .setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
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
                                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                    keyword = v.getText().toString();
                                    new LoadDataTask().execute();

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
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	    	finish();
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
	    	Intent intent = new Intent(SearchActivity.this, FavoriteActivity.class);
	    	startActivity(intent);
	        break;
	    case ID_SEARCH:
	        break;
	    }
	    return true;
	}

    class LoadDataTask extends AsyncTask<Integer, Integer, String> {

        private ProgressDialog         progressdialogInit;
        private final OnCancelListener cancelListener = new OnCancelListener() {
                                                          public void onCancel(DialogInterface arg0) {
                                                              LoadDataTask.this.cancel(true);
                                                              finish();
                                                          }
                                                      };

        @Override
        protected void onPreExecute() {
            progressdialogInit = ProgressDialog.show(SearchActivity.this, "Load", "Loading…");
            progressdialogInit.setTitle("Load");
            progressdialogInit.setMessage("Loading…");
            progressdialogInit.setOnCancelListener(cancelListener);
            progressdialogInit.setCanceledOnTouchOutside(false);
            progressdialogInit.setCancelable(true);
            progressdialogInit.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            fetchData();
            return "progress end";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            progressdialogInit.dismiss();
            if (articles != null && articles.size() != 0) {
//                articleListView.setAdapter(new SearchAdapter(SearchActivity.this, articles));
            	articleListView.setAdapter(new ListArticleAdapter(SearchActivity.this, articles, true));
            } else {
                layoutNoSearch.setVisibility(View.VISIBLE);
            }
            try {
                item.expandActionView();
                EditText search = (EditText) item.getActionView();
                search.setText(keyword);
            } catch (Exception e) {

            }
        }

    }
    
    private void fetchData() {
        articles = PttFoodAPI.searchArticles(keyword, 1);
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


}
