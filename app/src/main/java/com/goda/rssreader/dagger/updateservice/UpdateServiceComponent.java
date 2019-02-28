package com.goda.rssreader.dagger.updateservice;

import com.goda.rssreader.UpdateDatabaseService;
import com.goda.rssreader.dagger.application.ApplicationComponent;
import com.goda.rssreader.dagger.updateservice.UpdateServiceModule;
import com.goda.rssreader.dagger.updateservice.UpdateServiceScope;

import dagger.Component;

@UpdateServiceScope
@Component(modules = UpdateServiceModule.class, dependencies = ApplicationComponent.class)
public interface UpdateServiceComponent {
    void inject(UpdateDatabaseService updateDatabaseService);
}
