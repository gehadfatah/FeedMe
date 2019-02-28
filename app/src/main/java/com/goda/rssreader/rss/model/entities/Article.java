package com.goda.rssreader.rss.model.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.goda.rssreader.R;
import com.goda.rssreader.databinding.AppAdapterViewModel;
import me.toptas.rssconverter.RssItem;

@Entity(tableName = "article", foreignKeys = @ForeignKey(entity = Provider.class,
        parentColumns = "rssLink",
        childColumns = "providerLink", onDelete = ForeignKey.CASCADE))
public class Article implements AppAdapterViewModel {

    public Article() {
    }

    public Article(RssItem rssItem, String providerLink) {

        this.link = rssItem.getLink();
        this.description = rssItem.getDescription();
        this.image = rssItem.getImage();
        this.publishDate = rssItem.getPublishDate();
        this.title = rssItem.getTitle();
        this.providerLink = providerLink;

    }

    private String description;

    private String image;
    @PrimaryKey
    @NonNull
    private String link;

    private String publishDate;

    private String title;

    private String providerLink;

    public String getDescription() {
        return TextUtils.isEmpty(description)?"":description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return TextUtils.isEmpty(image)?"":image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return TextUtils.isEmpty(link)?"": link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishDate() {
        return TextUtils.isEmpty(publishDate)?"":publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return TextUtils.isEmpty(title)?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProviderLink() {
        return providerLink;
    }

    public void setProviderLink(String providerLink) {
        this.providerLink = providerLink;
    }

    @Ignore
    @Override
    public String toString() {
        return "link: " + link + " title: " + title + " provider Link: " + providerLink;
    }

    @Ignore
    @Override
    public int layoutId() {
        return R.layout.item_article;
    }
}
