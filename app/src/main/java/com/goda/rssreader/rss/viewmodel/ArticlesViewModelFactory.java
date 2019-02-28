package com.goda.rssreader.rss.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.goda.rssreader.rss.model.ArticlesRepo;

import javax.inject.Inject;

public class ArticlesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final ArticlesRepo repo;

    @Inject
    public ArticlesViewModelFactory(@NonNull ArticlesRepo repo) {
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ArticlesViewModel(repo);
    }

}