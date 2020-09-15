package com.example.marvellisimo.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.ApiService.MarvelService
import com.example.marvellisimo.ApiService.RetroInstance
import com.example.marvellisimo.entity.RealmComicEntity
import com.example.marvellisimo.entity.RealmLiveData
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.where


class ViewModelComicCharacter() : ViewModel() {

//    val allComicResults : MutableLiveData<RealmResults<RealmComicEntity>> by lazy {
//
//        val allComicResults = MutableLiveData<RealmResults<RealmComicEntity>>
//        allComicResults = realm.where<RealmComicEntity>().findAllAsync()
//        allComicResults

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }
    fun getComics(): RealmLiveData<RealmComicEntity> {
        return realm.where(RealmComicEntity::class.java).findAllAsync().asLiveData()
    }
}