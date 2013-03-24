package com.ptt.food.blog.entity;

public class Article {

    int    id;
    String author;
    String title;
    String release_time;
    String content;
    String link;
    int    category_id;

    public Article() {
        this(1, "", "", "", "", "", 1);
    }

    public Article(int id, String author, String title, String release_time, String content, String link, int category_id) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.release_time = release_time;
        this.content = content;
        this.link = link;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getReleaseTime() {
        return release_time;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public int getCategoryId() {
        return category_id;
    }

}
