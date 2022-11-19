package com.grishko.anecs;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class JokeApp extends Application {
    private Realm realmInstance;
    public Realm getRealmInstance() { return realmInstance; }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .name("jokes.realm")
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        realmInstance = Realm.getDefaultInstance();
    }

    @Override
    public void onTerminate() {
        realmInstance.close();

        super.onTerminate();
    }
}
