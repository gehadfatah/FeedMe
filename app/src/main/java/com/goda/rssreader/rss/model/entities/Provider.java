package com.goda.rssreader.rss.model.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.goda.rssreader.R;
import com.goda.rssreader.databinding.AppAdapterViewModel;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Provider implements AppAdapterViewModel {
    @PrimaryKey
    @NonNull
    private String rssLink;

    private String name;

    @Ignore
    private List<Article> articleList = new ArrayList<>();

    public Provider(@NonNull String rssLink, String name) {
        this.rssLink = rssLink;
        this.name = name;
    }

    @NonNull
    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(@NonNull String rssLink) {
        this.rssLink = rssLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Provider other = (Provider) obj;
        return rssLink.equals(other.rssLink);
    }

    @Override
    public int layoutId() {
        return R.layout.item_provider;
    }
}
