package com.goda.rssreader.rss.model;

import com.goda.rssreader.rss.model.entities.Provider;
import com.goda.rssreader.rss.model.local.ProvidersDao;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProvidersRepo {
    private ProvidersDao providersDao;
    private Executor executor;

    @Inject
    public ProvidersRepo(ProvidersDao providersDao, Executor executor) {
        this.providersDao = providersDao;
        this.executor = executor;

    }

    public Flowable<List<Provider>> getProviders() {
        return providersDao.getProviders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> addProvider(Provider provider) {
        return Single.fromCallable(() -> providersDao.saveProvider(provider)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> deleteProvider(Provider provider) {
        return Single.fromCallable(() -> providersDao.deleteProvider(provider)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
