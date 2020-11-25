package com.example.first;

import java.util.List;

public class News {
    private String id;
    private String title;
    private String author;
    private String publishTime;
    private int type;
    private String cover;
    private List<String> covers;

    public News() {
    }

    public News(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public int getType() {
        return type;
    }

    public String getCover() {
        return cover;
    }

    public List<String> getCovers() {
        return covers;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }
}
