package com.example.marvellisimo.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.entity.RealmComicEntity
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.kotlin.where


class ViewModelComicCharacter(val realm: Realm = Realm.getDefaultInstance()) : ViewModel(){

     var allComicResults : RealmResults<RealmComicEntity>? = null
     fun comicList(){
        allComicResults = realm.where<RealmComicEntity>().findAllAsync()

        /*allComicResult = realm.where<RealmComicEntity>()
            .findAll()
            Log.d("viewListModel", "comicList: $allComicResult")*/


    }
}