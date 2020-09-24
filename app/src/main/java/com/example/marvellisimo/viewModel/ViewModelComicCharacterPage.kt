package com.example.marvellisimo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.UserItem
import com.example.marvellisimo.dao.MarvelHandler
import com.example.marvellisimo.dao.asLiveData
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.example.marvellisimo.entity.RealmComicEntity
import com.example.marvellisimo.entity.User
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class ViewModelComicCharacterPage : ViewModel() {


    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }
    val api= MarvelHandler(realm)

    val comicResults:LiveData<RealmResults<RealmComicEntity>> by lazy {
        realm.where(RealmComicEntity::class.java).sort("title", Sort.ASCENDING).findAllAsync().asLiveData()
    }
    fun getComicData(offset: Int): LiveData<RealmResults<RealmComicEntity>> {
        api.fetchComicsToRealm(offset)
        return comicResults
    }

    val characterResults:LiveData<RealmResults<RealmCharacterEntity>> by lazy {
        realm.where(RealmCharacterEntity::class.java).sort("name", Sort.ASCENDING).findAllAsync().asLiveData()
    }

    fun getCharacterData(offset : Int): LiveData<RealmResults<RealmCharacterEntity>> {
        api.fetchCharactersToRealm(offset)

        return characterResults
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

    fun getSearchComicData(searchText: String): LiveData<RealmResults<RealmComicEntity>> {
        api.fetchSearchedComic(searchText)
        return realm.where(RealmComicEntity::class.java)
            .beginsWith("title","${searchText}", Case.INSENSITIVE)
            .sort("title",Sort.ASCENDING)
            .findAllAsync().asLiveData()

    }

    fun getSearchCharacterData(searchText: String): LiveData<RealmResults<RealmCharacterEntity>> {
        api.fetchSearchedCharacter(searchText)
        return realm.where(RealmCharacterEntity::class.java)
                .beginsWith("name","${searchText}", Case.INSENSITIVE)
                .sort("name",Sort.ASCENDING)
                .findAllAsync().asLiveData()

    }
    fun getTotalComicCount():Int{
        return api.totalComicCount
    }

    fun getTotalCharacterCount():Int{
        return api.totalCharacterCount
    }

    fun getComicLimit():Int{
        return api.comicLimit
    }

    fun getCharacterLimit():Int{
        return api.characterLimit
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }


}


