package com.ptt.food.blog.api;

import java.util.ArrayList;
import com.ptt.food.blog.entity.Article;
import com.ptt.food.db.SQLiteFavoriteArticle;

import android.content.Context;


public class DBAPI {
	
	public static ArrayList<Article> getAllArticles(Context context) {
        SQLiteFavoriteArticle db = new SQLiteFavoriteArticle(context);
        return db.getAllArticles();
    }
	
	public static boolean deleteArticle(Article article, Context context) {
		SQLiteFavoriteArticle db = new SQLiteFavoriteArticle(context);
        db.deleteArticle(article);
        return true;
    }
	
	public static boolean insertArticle(Article Article, Context context) {
		SQLiteFavoriteArticle db = new SQLiteFavoriteArticle(context);
        int id = (int) db.insertArticle(Article);
        return true;
    }

}
