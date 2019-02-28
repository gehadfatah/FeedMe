package com.goda.rssreader.rss.view.callback;

import com.goda.rssreader.databinding.ClickCallback;
import com.goda.rssreader.rss.model.entities.Article;

public interface ArticlesCallback extends ClickCallback {
    void onArticleClicked(Article article);
}
