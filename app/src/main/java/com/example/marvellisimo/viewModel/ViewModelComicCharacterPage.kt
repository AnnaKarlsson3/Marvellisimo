package com.example.marvellisimo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.dao.MarvelHandler
import com.example.marvellisimo.dao.asLiveData
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.example.marvellisimo.entity.RealmComicEntity
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class ViewModelComicCharacterPage : ViewModel() {
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }
    val api= MarvelHandler(realm)

    fun getComicData(): LiveData<RealmResults<RealmComicEntity>> {
        api.fetchComicsToRealm()
        return realm.where(RealmComicEntity::class.java).sort("title", Sort.ASCENDING).findAllAsync().asLiveData()
    }

    fun getCharacterData(): LiveData<RealmResults<RealmCharacterEntity>> {
        api.fetchCharactersToRealm()
        return realm.where(RealmCharacterEntity::class.java).sort("name", Sort.ASCENDING).findAllAsync().asLiveData()
    }

    fun getSearchComicData(searchText: String): LiveData<RealmResults<RealmComicEntity>> {
        return realm.where(RealmComicEntity::class.java)
                .beginsWith("title","${searchText}", Case.INSENSITIVE)
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