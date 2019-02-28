package com.goda.rssreader.rss.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.goda.rssreader.rss.model.ProvidersRepo;

import javax.inject.Inject;

public class ProvidersViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final ProvidersRepo repo;

    @Inject
    public ProvidersViewModelFactory(@NonNull ProvidersRepo repo) {
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ProvidersViewModel(repo);
    }

}