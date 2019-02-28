package com.goda.rssreader.rss.model.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.goda.rssreader.rss.model.entities.Article;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ArticlesDao {

    @Insert(onConflict = REPLACE)
    void saveArticle(Article article);

    @Insert(onConflict = REPLACE)
    void saveArticle(List<Article> articles);

    @Query("SELECT * FROM article  WHERE article.providerLink = :providerLink")
    Single<List<Article>> getArticles(String providerLink);

    @Query("DELETE FROM article WHERE article.providerLink = :providerLink")
    void deleteByProvider(String providerLink);

}
