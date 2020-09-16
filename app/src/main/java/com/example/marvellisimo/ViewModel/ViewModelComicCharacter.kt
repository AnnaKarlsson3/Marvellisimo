package com.example.marvellisimo.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.example.marvellisimo.entity.RealmComicEntity
import io.realm.*
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
            .beginsWith("title","${searchText}",Case.INSENSITIVE)
            .findAllAsync().asLiveData()

    }

    fun getFavoriteComic(): LiveData<RealmResults<RealmComicEntity>> {
        return realm.where(RealmComicEntity::class.java)
            .equalTo("favorite",true)
            .findAllAsync().asLiveData()
    }

    fun getFavoriteCharacter(): LiveData<RealmResults<RealmCharacterEntity>> {
        return realm.where(RealmCharacterEntity::class.java)
            .equalTo("favorite",true)
            .findAllAsync().asLiveData()
    }

    fun getSearchCharacterData(searchText: String): LiveData<RealmResults<RealmCharacterEntity>> {
        return realm.where(RealmCharacterEntity::class.java)
            .beginsWith("name","${searchText}", Case.INSENSITIVE)
            .findAllAsync().asLiveData()

    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

}


    fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLivedata<T>(this)




