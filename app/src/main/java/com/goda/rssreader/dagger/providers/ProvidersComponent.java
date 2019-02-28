package com.goda.rssreader.dagger.providers;

import com.goda.rssreader.dagger.application.ApplicationComponent;
import com.goda.rssreader.rss.view.ui.ProvidersFragment;

import dagger.Component;

@ProvidersFragmentScope
@Component(modules = ProvidersModule.class, dependencies = ApplicationComponent.class)
public interface ProvidersComponent {
    void inject(ProvidersFragment providersFragment);
}
