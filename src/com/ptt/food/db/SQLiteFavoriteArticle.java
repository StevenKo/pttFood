package com.ptt.food.db;

import java.util.ArrayList;

import com.ptt.food.blog.entity.Article;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteFavoriteArticle extends SQLiteOpenHelper {
	
	private static final String  DB_NAME          = "kosfood.sqlite"; // 資料庫名稱
    private static final int     DATABASE_VERSION = 1;                // 資料庫版本
    private final SQLiteDatabase db;
    private final Context        ctx;
    
    // Define database schema
    public interface FoodSchema {
        String TABLE_NAME     = "foodarticles";
        String ID             = "id"; //primaryId
        String ARTICLE_ID     = "article_id";
        String AUTHOR         = "author";
        String TITLE          = "title";        
        String RELEASE_TIME   = "release_time";
        String CONTENT        = "content";
        String LINK           = "link";
        String CATEGORY_ID    = "category_id";
    }
    
    public SQLiteFavoriteArticle(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        ctx = context;
        db = this.getWritableDatabase();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FoodSchema.TABLE_NAME + " (" 
        		+ FoodSchema.ID + " INTEGER PRIMARY KEY" + ","     + FoodSchema.ARTICLE_ID + " INTEGER NOT NULL" + ","
        		+ FoodSchema.AUTHOR + " TEXT NOT NULL" + ","   	   + FoodSchema.TITLE + " TEXT NOT NULL" + "," 
        		+ FoodSchema.RELEASE_TIME + " TEXT NOT NULL" + "," + FoodSchema.CONTENT + " TEXT NOT NULL" + "," 
        		+ FoodSchema.LINK + " TEXT NOT NULL" + "," + FoodSchema.CATEGORY_ID + " INTEGER NOT NULL" + ");");
    }
    
//     "DELETE FROM articles WHERE `articles`.`id` = ?"
    
    public boolean deleteArticle(Article article) {
        Cursor cursor = db.rawQuery("DELETE FROM "+FoodSchema.TABLE_NAME+" WHERE article_id = ?", new String[] { article.getId() + "" });
        cursor.moveToFirst();
        cursor.close();
        return true;
    }
    
    public long insertArticle(Article article) {
        ContentValues args = new ContentValues();
        args.put(FoodSchema.ARTICLE_ID, article.getId());
        args.put(FoodSchema.AUTHOR, article.getAuthor());
        args.put(FoodSchema.TITLE, article.getTitle());
        args.put(FoodSchema.RELEASE_TIME, article.getReleaseTime());
        args.put(FoodSchema.CONTENT, article.getContent());
        args.put(FoodSchema.LINK, article.getLink());
        args.put(FoodSchema.CATEGORY_ID, article.getCategoryId());
        return db.insert(FoodSchema.TABLE_NAME, null, args);
    }
    
    public ArrayList<Article> getAllArticles() {
        Cursor cursor = null;
        ArrayList<Article> articles = new ArrayList<Article>();
        cursor = db.rawQuery("SELECT * FROM " + FoodSchema.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0); //primary_id
            int ARTICLE_ID = cursor.getInt(1);
            String AUTHOR = cursor.getString(2);
            String TITLE = cursor.getString(3);
            String RELEASE_TIME = cursor.getString(4);
            String CONTENT = cursor.getString(5);
            String LINK = cursor.getString(6);
            int CATEGORY_ID = cursor.getInt(7);
            
            Article article = new Article(ARTICLE_ID, AUTHOR, TITLE, RELEASE_TIME, CONTENT, LINK, CATEGORY_ID);
            articles.add(article);
        }
        return articles;
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
