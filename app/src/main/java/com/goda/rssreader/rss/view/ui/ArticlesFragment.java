package com.goda.rssreader.rss.view.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.goda.rssreader.MyApplication;
import com.goda.rssreader.R;
import com.goda.rssreader.dagger.articles.ArticlesComponent;
import com.goda.rssreader.dagger.articles.ArticlesModule;
import com.goda.rssreader.dagger.articles.DaggerArticlesComponent;
import com.goda.rssreader.databinding.AppAdapter;
import com.goda.rssreader.databinding.FragmentArticlesBinding;
import com.goda.rssreader.rss.model.entities.Article;
import com.goda.rssreader.rss.view.callback.ArticlesCallback;
import com.goda.rssreader.rss.viewmodel.ArticlesViewModel;
import com.goda.rssreader.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArticlesFragment extends BaseFragment<FragmentArticlesBinding> implements ArticlesCallback {

    @Inject
    ArticlesViewModel viewModel;
    private ArrayList<Article> articlesList;
    private AppAdapter<Article> mAdapter;

    private final static String PROVIDER_KEY = "provider_key";
    private String providerLink;

    public ArticlesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            providerLink = getArguments().getString(PROVIDER_KEY);
            getArticles();
        }
        observeErrorMessage();
        observeLoading();
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                if(!TextUtils.isEmpty(message)){
                    showSnakeBar(message);
                }
            }
        });
    }

    private void observeLoading() {
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null && isLoading) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });
    }

    public static ArticlesFragment newInstance(String providerLink) {
        ArticlesFragment articlesFragment = new ArticlesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PROVIDER_KEY, providerLink);
        articlesFragment.setArguments(bundle);
        return articlesFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_articles;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ArticlesComponent component = DaggerArticlesComponent.builder()
                .articlesModule(new ArticlesModule(this))
                .applicationComponent(MyApplication.get(context).getComponent())
                .build();

        component.inject(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private DiffUtil.ItemCallback<Article> diffCallback = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldArticle, @NonNull Article newArticle) {
            return oldArticle.getLink().equals(newArticle.getLink());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldArticle, @NonNull Article newArticle) {
            return oldArticle.getLink().equals(newArticle.getLink())
                    && oldArticle.getDescription().equals(newArticle.getDescription())
                    && oldArticle.getImage().equals(newArticle.getImage())
                    && oldArticle.getTitle().equals(newArticle.getTitle())
                    && oldArticle.getPublishDate().equals(newArticle.getPublishDate());
        }
    };


    private void setUpRecyclerView() {
        RecyclerView recyclerView = getViewDataBinding().recyclerViewArticles;
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        articlesList = new ArrayList<>();
        mAdapter = new AppAdapter<>(this, diffCallback);
        recyclerView.setAdapter(mAdapter);
        getViewDataBinding().swipe.setOnRefreshListener(() -> viewModel.getArticles(providerLink));
    }

    Observer<List<Article>> articlesListObserver = articles -> {
        if (articles != null && !articles.isEmpty()) {
            if (getViewDataBinding().swipe.isRefreshing()) {
                getViewDataBinding().swipe.setRefreshing(false);
            }
            articlesList.clear();
            articlesList.addAll(articles);
            mAdapter.submitList(articles);
        }
    };

    private void getArticles() {
        viewModel.getArticles(providerLink).observe(this, articlesListObserver);
    }

    @Override
    public void onArticleClicked(Article article) {
        if (getActivity() != null && isAdded()) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary))
                    .addDefaultShareMenuItem()
                    .setShowTitle(true);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse(article.getLink()));
        }
    }
}
