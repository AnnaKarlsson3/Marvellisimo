package com.example.marvellisimo.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.example.marvellisimo.entity.RealmComicEntity
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.where


class ViewModelComicCharacter() : ViewModel(){


    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getComicData(): LiveData<RealmResults<RealmComicEntity>> {
        return realm.where(RealmComicEntity::class.java).findAllAsync().asLiveData()
    }

    fun getCharacterData(): LiveData<RealmResults<RealmCharacterEntity>> {
        return realm.where(RealmCharacterEntity::class.java).findAllAsync().asLiveData()
    }

    fun getSearchComicData(searchText: String): LiveData<RealmResults<RealmComicEntity>> {
        return realm.where(RealmComicEntity::class.java)
            .beginsWith("title","${searchText}")
            .findAllAsync().asLiveData()

    }

    fun getSearchCharacterData(searchText: String): LiveData<RealmResults<RealmCharacterEntity>> {
        return realm.where(RealmCharacterEntity::class.java)
            .beginsWith("title","${searchText}")
            .findAllAsync().asLiveData()

    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

}


    fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLivedata<T>(this)




