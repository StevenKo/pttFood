package com.ptt.food.blog.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ptt.food.blog.entity.Article;
import com.ptt.food.blog.entity.Category;

public class PttFoodAPI {
    final static String         HOST  = "http://106.187.52.8";
    public static final String  TAG   = "PttFoodAPI";
    public static final boolean DEBUG = true;

    public static ArrayList<Category> getMainCategories() {
        return Category.getCategories();
    }

    public static ArrayList<Category> getAreaCateogories() {
        return Category.getAreas();
    }

    public static ArrayList<Category> getSubcategories(int category_id) {
        ArrayList<Category> subCategories = new ArrayList<Category>();
        String message = getMessageFromServer("GET", "/api/v1/categories/" + category_id + "/subcagtegory.json", null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONArray jsonArray;
                jsonArray = new JSONArray(message.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    Category c = new Category(id, name);
                    subCategories.add(c);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return subCategories;
    }

    public static ArrayList<Article> getNewArticles(int page) {
        ArrayList<Article> articles = new ArrayList<Article>();
        String message = getMessageFromServer("GET", "/api/v1/articles/new_articles.json?page=" + page, null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONArray jsonArray;
                jsonArray = new JSONArray(message.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String author = jsonArray.getJSONObject(i).getString("author");
                    String release_time = jsonArray.getJSONObject(i).getString("release_time");
                    String title = jsonArray.getJSONObject(i).getString("title");
                    Article a = new Article(id, author, title, release_time, "", "", 0);
                    articles.add(a);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return articles;
    }

    public static ArrayList<Article> getCategoryArticles(int category_id) {
        ArrayList<Article> articles = new ArrayList<Article>();
        String message = getMessageFromServer("GET", "/api/v1/articles.json?category_id=" + category_id, null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONArray jsonArray;
                jsonArray = new JSONArray(message.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String author = jsonArray.getJSONObject(i).getString("author");
                    String release_time = jsonArray.getJSONObject(i).getString("release_time");
                    String title = jsonArray.getJSONObject(i).getString("title");
                    Article a = new Article(id, author, title, release_time, "", "", category_id);
                    articles.add(a);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return articles;
    }

    public static ArrayList<Article> searchArticles(String keyword, int page) {
        String query;
        try {
            query = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }

        ArrayList<Article> articles = new ArrayList<Article>();
        String message = getMessageFromServer("GET", "/api/v1/articles/search.json?keyword=" + query + "&page=" + page, null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONArray jsonArray;
                jsonArray = new JSONArray(message.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String author = jsonArray.getJSONObject(i).getString("author");
                    String release_time = jsonArray.getJSONObject(i).getString("release_time");
                    String title = jsonArray.getJSONObject(i).getString("title");
                    String content = jsonArray.getJSONObject(i).getString("content");
                    String link = jsonArray.getJSONObject(i).getString("link");
                    // int category_id = jsonArray.getJSONObject(i).getInt("");
                    Article a = new Article(id, author, title, release_time, content, "", 0);
                    articles.add(a);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return articles;
    }

    public static Article getArticle(int article_id) {
        String message = getMessageFromServer("GET", "/api/v1/articles/" + article_id + ".json", null);

        if (message == null) {
            return null;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                String author = nObject.getString("author");
                String content = nObject.getString("content");
                String link = nObject.getString("link");
                String release_time = nObject.getString("release_time");
                String title = nObject.getString("title");
                int category_id = 0;
                if (!nObject.isNull("category_id"))
                    category_id = nObject.getInt("category_id");

                return new Article(article_id, author, title, release_time, content, link, category_id);

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String getMessageFromServer(String requestMethod, String apiPath, JSONObject json) {
        URL url;
        try {
            url = new URL(HOST + apiPath);
            if (DEBUG)
                Log.d(TAG, "URL: " + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);

            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (requestMethod.equalsIgnoreCase("POST"))
                connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            if (requestMethod.equalsIgnoreCase("POST")) {
                OutputStream outputStream;

                outputStream = connection.getOutputStream();
                if (DEBUG)
                    Log.d("post message", json.toString());

                outputStream.write(json.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder lines = new StringBuilder();
            ;
            String tempStr;

            while ((tempStr = reader.readLine()) != null) {
                lines = lines.append(tempStr);
            }
            if (DEBUG)
                Log.d("MOVIE_API", lines.toString());

            reader.close();
            connection.disconnect();

            return lines.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
