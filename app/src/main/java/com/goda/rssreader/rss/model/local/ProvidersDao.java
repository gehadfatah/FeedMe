package com.goda.rssreader.rss.model.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.goda.rssreader.rss.model.entities.Provider;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProvidersDao {

    @Insert(onConflict = REPLACE)
    long saveProvider(Provider provider);

    @Insert(onConflict = REPLACE)
    List<Long> saveProvidersList(List<Provider> providers);

    @Delete
    int deleteProvider(Provider provider);

    @Query("SELECT * FROM Provider")
    Flowable<List<Provider>> getProviders();
}