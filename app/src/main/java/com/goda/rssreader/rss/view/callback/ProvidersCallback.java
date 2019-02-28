package com.goda.rssreader.rss.view.callback;

import com.goda.rssreader.databinding.ClickCallback;
import com.goda.rssreader.rss.model.entities.Provider;

public interface ProvidersCallback extends ClickCallback {
    void onDeleteProviderClicked(Provider provider);
    void onProviderClicked(Provider provider);
}
