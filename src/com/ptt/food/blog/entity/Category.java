package com.ptt.food.blog.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class Category {

    static String message     = "[{\"id\":15,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D9F3/index.html\",\"name\":\"\u4e2d\u5f0f\u9910\u5ef3\"},{\"id\":16,\"link\":\"http://www.ptt.cc/man/Food/DB9A/DD88/index.html\",\"name\":\"\u897f\u5f0f\u9910\u5ef3\"},{\"id\":17,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D3E/index.html\",\"name\":\"\u65e5\u97d3\u6599\u7406\"},{\"id\":18,\"link\":\"http://www.ptt.cc/man/Food/DB9A/DDDF/index.html\",\"name\":\"\u5357\u6d0b\u5370\u5ea6\"},{\"id\":19,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D62D/index.html\",\"name\":\"\u5927\u98ef\u5e97\u5462\"},{\"id\":20,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D88B/index.html\",\"name\":\"\u5403\u5230\u98fd\u5594\"},{\"id\":21,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D593/index.html\",\"name\":\"\u7279\u6b8a\u9910\u5ef3\"},{\"id\":22,\"link\":\"http://www.ptt.cc/man/Food/DB9A/DBEF/index.html\",\"name\":\"\u7d20\u98df\u9910\u5ef3\"},{\"id\":23,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D274/index.html\",\"name\":\"\u300c\u934b\u300d\"},{\"id\":24,\"link\":\"http://www.ptt.cc/man/Food/DB9A/DFB/index.html\",\"name\":\"\u591c\u5e02\"},{\"id\":25,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D76B/index.html\",\"name\":\"\u65e9\u9910\u5e97\"},{\"id\":26,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D2C/index.html\",\"name\":\"\u767e\u8ca8\u516c\u53f8\"},{\"id\":27,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D2B9/index.html\",\"name\":\"\u5564\u9152\u5c4b\u3001PUB\"},{\"id\":28,\"link\":\"http://www.ptt.cc/man/Food/DB9A/DF71/index.html\",\"name\":\"\u901f\u98df\u5e97\u3001\u8d85\u5546\"},{\"id\":29,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D560/index.html\",\"name\":\"\u7c21\u9910\u3001\u5496\u5561\u3001\u4e0b\u5348\u8336\"},{\"id\":30,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D50/index.html\",\"name\":\"\u7db2\u8cfc\u3001\u5408\u8cfc\u5e97\u5bb6\u5c08\u5340\"},{\"id\":31,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D1B0/index.html\",\"name\":\"\u6524\u8ca9\u5c0f\u5403\u3001\u5404\u5730\u7279\u8272\u7f8e\u98df\"},{\"id\":32,\"link\":\"http://www.ptt.cc/man/Food/DB9A/D871/index.html\",\"name\":\"\u751c\u9ede\u3001\u86cb\u7cd5\u3001\u9eb5\u5305\u3001\u51b0\u54c1\u3001\u98f2\u6599\"}]";
    static String areaMessage = "[{\"id\":1,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DD1C/index.html\",\"name\":\"\u53f0\u5317\u5e02\"},{\"id\":2,\"link\":\"http://www.ptt.cc/man/Food/D9DA/D6B5/index.html\",\"name\":\"\u65b0\u5317\u5e02\"},{\"id\":3,\"link\":\"http://www.ptt.cc/man/Food/D9DA/D33B/index.html\",\"name\":\"\u57fa\u9686\u5e02\"},{\"id\":4,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DEB7/index.html\",\"name\":\"\u6843\u5712\u4e2d\u58e2\"},{\"id\":5,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DFE4/index.html\",\"name\":\"\u65b0\u7af9\u82d7\u6817\"},{\"id\":6,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DB8E/index.html\",\"name\":\"\u5b9c\u862d\u82b1\u6771\"},{\"id\":7,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DFF0/index.html\",\"name\":\"\u53f0\u4e2d\u7e23\u5e02\"},{\"id\":8,\"link\":\"http://www.ptt.cc/man/Food/D9DA/D50E/index.html\",\"name\":\"\u5f70\u5316\u5357\u6295\"},{\"id\":9,\"link\":\"http://www.ptt.cc/man/Food/D9DA/D14D/index.html\",\"name\":\"\u5609\u7fa9\u96f2\u6797\"},{\"id\":10,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DFFB/index.html\",\"name\":\"\u53f0\u5357\u7e23\u5e02\"},{\"id\":11,\"link\":\"http://www.ptt.cc/man/Food/D9DA/D6/index.html\",\"name\":\"\u9ad8\u96c4\u5c4f\u6771\"},{\"id\":12,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DE33/index.html\",\"name\":\"\u6f8e\u6e56\u7fa4\u5cf6\"},{\"id\":13,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DB35/index.html\",\"name\":\"\u91d1\u9580\u99ac\u7956\"},{\"id\":14,\"link\":\"http://www.ptt.cc/man/Food/D9DA/DC11/index.html\",\"name\":\"\u5403\u5728\u7570\u570b\"}]";

    int           id;
    String        name;

    public Category() {
        this(-1, "");
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCateName() {
        return name;
    }

    public void setCateName(String name) {
        this.name = name;
    }

    public static ArrayList<Category> getCategories() {
        ArrayList<Category> cateogries = new ArrayList<Category>();
        JSONArray categoryArray;
        try {
            categoryArray = new JSONArray(message.toString());
            for (int i = 0; i < categoryArray.length(); i++) {
                int category_id = categoryArray.getJSONObject(i).getInt("id");
                String name = categoryArray.getJSONObject(i).getString("name");
                Category cat = new Category(category_id, name);
                cateogries.add(cat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cateogries;
    }

    public static ArrayList<Category> getAreas() {
        ArrayList<Category> cateogries = new ArrayList<Category>();
        JSONArray categoryArray;
        try {
            categoryArray = new JSONArray(areaMessage.toString());
            for (int i = 0; i < categoryArray.length(); i++) {
                int category_id = categoryArray.getJSONObject(i).getInt("id");
                String name = categoryArray.getJSONObject(i).getString("name");
                Category cat = new Category(category_id, name);
                cateogries.add(cat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cateogries;
    }

}
