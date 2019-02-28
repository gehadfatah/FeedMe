package com.goda.rssreader.rss.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.goda.rssreader.rss.model.ProvidersRepo;
import com.goda.rssreader.rss.model.entities.Provider;
import com.goda.rssreader.base.BaseViewModel;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;

public class ProvidersViewModel extends BaseViewModel {
    private ProvidersRepo repo;
    private MutableLiveData<List<Provider>> providersList = new MutableLiveData<>();

    ProvidersViewModel(ProvidersRepo repo) {
        this.repo = repo;
    }


    public LiveData<List<Provider>> getProviders() {
        setIsLoading(true);
        getCompositeDisposable().add(repo.getProviders().subscribeWith(new DisposableSubscriber<List<Provider>>() {
            @Override
            public void onNext(List<Provider> providers) {
                setIsLoading(false);
                providersList.setValue(providers);
            }

            @Override
            public void onError(Throwable t) {
                setIsLoading(false);
                setErrorMessage(t.getMessage()+"");
            }

            @Override
            public void onComplete() {
                setIsLoading(false);
            }
        }));
        return providersList;
    }

    public void deleteProvider(Provider provider) {
        setIsLoading(true);
        getCompositeDisposable().add(repo.deleteProvider(provider).subscribeWith(new DisposableSingleObserver<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                setIsLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                setErrorMessage(e.getMessage()+"");
                setIsLoading(false);
            }
        }));
    }


    public void addProvider(Provider provider) {
        setIsLoading(true);
        getCompositeDisposable().add(repo.addProvider(provider).subscribeWith(new DisposableSingleObserver<Long>() {
            @Override
            public void onSuccess(Long i) {
                setIsLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                setErrorMessage(e.getMessage()+"");
                setIsLoading(false);
            }
        }));
    }
}
