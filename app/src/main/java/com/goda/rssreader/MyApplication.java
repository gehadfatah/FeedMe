package com.goda.rssreader;

import android.app.Application;
import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.goda.rssreader.dagger.application.ApplicationComponent;
import com.goda.rssreader.dagger.application.DaggerApplicationComponent;

public class MyApplication extends Application {
    private ApplicationComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .build();

        scheduleJob();
    }

    public static MyApplication get(Context activity) {
        return (MyApplication) activity.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return component;
    }


    public void scheduleJob( ) {

        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = firebaseJobDispatcher.newJobBuilder()
                .setService(UpdateDatabaseService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                 .setTrigger(Trigger.executionWindow(0,10*60*60))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setTag("update_database")
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        firebaseJobDispatcher.schedule(myJob);
    }
}
