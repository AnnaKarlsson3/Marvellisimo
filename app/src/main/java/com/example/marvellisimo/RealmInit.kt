package com.example.marvellisimo

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmInit : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("comicDb")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)
    }




}