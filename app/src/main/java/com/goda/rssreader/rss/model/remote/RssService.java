package com.goda.rssreader.rss.model.remote;

import io.reactivex.Single;
import me.toptas.rssconverter.RssFeed;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssService {
    @GET
    Single<RssFeed> getRss(@Url String url);
}
