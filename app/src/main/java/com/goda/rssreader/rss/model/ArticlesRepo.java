package com.goda.rssreader.rss.model;

import android.support.annotation.Nullable;

import com.goda.rssreader.rss.model.entities.Article;
import com.goda.rssreader.rss.model.local.ArticlesDao;
import com.goda.rssreader.rss.model.remote.RssService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssItem;

public class ArticlesRepo {
    private RssService rssService;
    private ArticlesDao articlesDao;
    private Executor executor;

    @Inject
    public ArticlesRepo(RssService rssService, @Nullable ArticlesDao articlesDao, Executor executor) {
        this.rssService = rssService;
        this.articlesDao = articlesDao;
        this.executor = executor;

    }


    public Flowable<List<Article>> getArticles(String providerLink) {

        return Single.mergeDelayError(rssService.getRss(providerLink)
                        .map(rssFeed -> {
                            List<Article> articles = new ArrayList<>();
                            for (RssItem rssItem : rssFeed.getItems()) {
                                Article article = new Article(rssItem, providerLink);
                                articles.add(article);
                            }
                            return articles;
                        })
                        .doOnSuccess(new Consumer<List<Article>>() {
                            @Override
                            public void accept(List<Article> articles) throws Exception {
                                executor.execute(() -> articlesDao.saveArticle(articles));
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                articlesDao.getArticles(providerLink).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        );
    }
}