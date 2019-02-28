package com.goda.rssreader.rss.model.local;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.goda.rssreader.rss.model.entities.Article;
import com.goda.rssreader.rss.model.entities.Provider;
import com.goda.rssreader.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Provider.class, Article.class}, version = 1)
public abstract class RSSDatabase extends RoomDatabase {

    private static RSSDatabase instance;

    public abstract ProvidersDao providersDao();

    public abstract ArticlesDao articlesDao();

    private Context mContext;

    public static synchronized RSSDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RSSDatabase.class, AppConstants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor()
                                    .execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<Long> list = getInstance(context).providersDao().saveProvidersList(populateData());
                                            Log.e("database","Size: "+ list.size()+"");
                                        }
                                    });
//                            Executors.newSingleThreadScheduledExecutor()
//                                    .execute(() -> getInstance(context).providersDao().saveProvidersList(populateData()));
                        }
                    })
                    .build();
        }
        return instance;
    }

    private static List<Provider> populateData() {
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider("http://rss.cnn.com/rss/edition.rss", "CNN"));
        providers.add(new Provider("http://feeds.mashable.com/Mashable", "Mashable"));
        providers.add(new Provider("https://gizmodo.com/rss", "Gizmodo"));
        return providers;
    }

}