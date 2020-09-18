package com.example.marvellisimo.dao

import com.example.marvellisimo.apiService.MarvelService
import com.example.marvellisimo.apiService.RetroInstance
import com.example.marvellisimo.entity.CharacterDataWrapper
import com.example.marvellisimo.entity.ComicDataWrapper
import com.example.marvellisimo.entity.*
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarvelHandler(val realm: Realm) {

    companion object {
        val realm = Realm.getDefaultInstance()
    }

    //Get Comic-data from API
    fun fetchComicsToRealm(){
        val service = RetroInstance.getRetroInstance().create(MarvelService::class.java)
        service.getAllComics().enqueue(object : Callback<ComicDataWrapper> {
            override fun onResponse(
                call: Call<ComicDataWrapper>,
                response: Response<ComicDataWrapper>
            ) {
                if (response.isSuccessful) {
                    saveComicsToRealm(response.body()!!)
                } else {

                }
            }
            override fun onFailure(call: Call<ComicDataWrapper>, t: Throwable) {
            }
        })
    }


    //set ComicData from API to Realm
    private fun saveComicsToRealm(comicdatawrapper: ComicDataWrapper) {
        realm.executeTransactionAsync(fun(realm: Realm) {
            comicdatawrapper.data.results.forEach { c ->
                val comicFromDatabase = realm.where(RealmComicEntity::class.java)
                    .equalTo("id",c.id)
                    .findFirst()

                val comic = RealmComicEntity().apply {
                    id = c.id
                    title = c.title
                    description = c.description
                    thumbnail = "${c.thumbnail.path}.${c.thumbnail.extension}"
                    if (comicFromDatabase != null) {
                        favorite = comicFromDatabase.favorite
                    }
                    urls?.addAll(c.urls.map {
                        UrlDb().apply {
                            type = it.type
                            url = it.url
                        }
                    })
                }
                realm.insertOrUpdate(comic)
            }
        }, fun() {
            android.util.Log.d("database", "uploaded")
        })
    }

    //Get Character-data from API
    fun fetchCharactersToRealm(){
        val service = RetroInstance.getRetroInstance().create(MarvelService::class.java)
        service.getAllCharacters().enqueue(object : Callback<CharacterDataWrapper> {
            override fun onResponse(
                call: Call<CharacterDataWrapper>,
                response: Response<CharacterDataWrapper>
            ) {
                if (response.isSuccessful) {
                    saveCharactersToRealm(response.body()!!)
                } else {

                }
            }
            override fun onFailure(call: Call<CharacterDataWrapper>, t: Throwable) {
            }
        })
    }

    //set CharacterData from API to Realm
    private fun saveCharactersToRealm(characterDataWrapper: CharacterDataWrapper) {
        realm.executeTransactionAsync(fun(realm: Realm) {
            characterDataWrapper.data.results.forEach { c ->
                val characterFromDatabase = realm.where(RealmCharacterEntity::class.java)
                    .equalTo("id",c.id)
                    .findFirst()
                val character = RealmCharacterEntity().apply {
                    id = c.id
                    name = c.name
                    description = c.description
                    thumbnail = "${c.thumbnail.path}.${c.thumbnail.extension}"
                    if (characterFromDatabase != null) {
                        favorite = characterFromDatabase.favorite
                    }
                    urls?.addAll(c.urls.map {
                        UrlDb().apply {
                            type = it.type
                            url = it.url
                        }
                    })
                }
                realm.insertOrUpdate(character)
            }
        }, fun() {
            android.util.Log.d("database", "characcter uploaded")
        })
    }


}//class end