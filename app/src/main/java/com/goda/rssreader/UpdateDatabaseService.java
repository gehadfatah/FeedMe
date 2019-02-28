package com.goda.rssreader;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.goda.rssreader.dagger.updateservice.DaggerUpdateServiceComponent;
import com.goda.rssreader.dagger.updateservice.UpdateServiceComponent;
import com.goda.rssreader.dagger.updateservice.UpdateServiceModule;
import com.goda.rssreader.rss.model.ArticlesRepo;
import com.goda.rssreader.rss.model.ProvidersRepo;
import com.goda.rssreader.rss.model.entities.Provider;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UpdateDatabaseService extends JobService {

    @Inject
    ArticlesRepo articlesRepo;
    @Inject
    ProvidersRepo providersRepo;

    private Subscription subscription;

    private List<Provider> providerList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        UpdateServiceComponent component = DaggerUpdateServiceComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .updateServiceModule(new UpdateServiceModule(this))
                .build();

        component.inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        providersRepo.getProviders()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<List<Provider>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        UpdateDatabaseService.this.subscription = s;
                    }

                    @Override
                    public void onNext(List<Provider> providers) {
                        if (providers != null) {
                            providerList.addAll(providers);
                            Single.just(providerList).flattenAsObservable(list -> providerList).doOnNext(provider -> articlesRepo.getArticles(provider.getRssLink()))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (subscription != null) {
            subscription.cancel();
        }
        return false;
    }
}
