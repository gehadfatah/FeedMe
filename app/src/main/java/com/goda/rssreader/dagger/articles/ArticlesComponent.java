package com.goda.rssreader.dagger.articles;

import com.goda.rssreader.dagger.application.ApplicationComponent;
import com.goda.rssreader.rss.view.ui.ArticlesFragment;

import dagger.Component;

@ArticlesFragmentScope
@Component(modules = ArticlesModule.class, dependencies = ApplicationComponent.class)
public interface ArticlesComponent {
    void inject(ArticlesFragment articlesFragment);
}
