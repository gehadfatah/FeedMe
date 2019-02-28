package com.goda.rssreader.dagger.updateservice;

import android.content.Context;

import com.goda.rssreader.UpdateDatabaseService;
import com.goda.rssreader.rss.model.local.ArticlesDao;
import com.goda.rssreader.rss.model.local.ProvidersDao;
import com.goda.rssreader.rss.model.local.RSSDatabase;
import com.goda.rssreader.rss.model.remote.RssService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class UpdateServiceModule {
    private final Context mContext;

    public UpdateServiceModule(UpdateDatabaseService mContext) {
        this.mContext = mContext;
    }

    @Provides
    @UpdateServiceScope
    public Context context() {
        return mContext;
    }

    @Provides
    @UpdateServiceScope
    public RssService rssService(Retrofit retrofit) {
        return retrofit.create(RssService.class);
    }

    @Provides
    @UpdateServiceScope
    public ArticlesDao articlesDao(Context context) {
        return RSSDatabase.getInstance(context).articlesDao();
    }

    @Provides
    @UpdateServiceScope
    public ProvidersDao providersDao(Context context){
        return RSSDatabase.getInstance(context).providersDao();
    }
}
