package com.goda.rssreader.rss.view.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.goda.rssreader.R;
import com.goda.rssreader.databinding.ActivityMainBinding;
import com.goda.rssreader.rss.model.entities.Provider;
import com.goda.rssreader.rss.view.callback.Navigator;
import com.goda.rssreader.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements Navigator {

    private ProvidersFragment providersFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providersFragment = new ProvidersFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentRss, providersFragment, ProvidersFragment.class.getName()).commit();

    }


    @Override
    public void openArticlesFragment(Provider provider) {
        ArticlesFragment articlesFragment = ArticlesFragment.newInstance(provider.getRssLink());
        if (!articlesFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(ArticlesFragment.class.getName())
                    .add(R.id.fragmentRss,
                            articlesFragment).commit();
        }
    }
}
