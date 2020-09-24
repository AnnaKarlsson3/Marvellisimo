package com.example.marvellisimo.dao

import android.util.Log
import com.example.marvellisimo.apiService.MarvelService
import com.example.marvellisimo.apiService.RetroInstance
import com.example.marvellisimo.entity.CharacterDataWrapper
import com.example.marvellisimo.entity.ComicDataWrapper
import com.example.marvellisimo.entity.*
import io.realm.Realm
import io.realm.kotlin.where
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarvelHandler(val realm: Realm) {
    val service = RetroInstance.getRetroInstance().create(MarvelService::class.java)
    var totalComicCount: Int = 0
    var totalCharacterCount: Int = 0
    var comicLimit =0
    var characterLimit =0

    companion object {
        val realm = Realm.getDefaultInstance()
    }

    //Get Comic-data from API
    fun fetchComicsToRealm(offset: Int) {

        service.getAllComics(offset).enqueue(object : Callback<ComicDataWrapper> {
            override fun onResponse(
                call: Call<ComicDataWrapper>,
                response: Response<ComicDataWrapper>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        totalComicCount = response.body()!!.data.total
                        comicLimit = response.body()!!.data.limit
                        saveComicsToRealm(response.body()!!)
                        Log.d("loaded", "comics : ${response.body()!!.data.results.size}")
                    }


                } else {
                    Log.d("loaded", "comics : not loaded")
                }
            }
            override fun onFailure(call: Call<ComicDataWrapper>, t: Throwable) {
            }
        })
        Log.d("loaded", "offset : ${offset}")
    }

    //set ComicData from API to Realm
    private fun saveComicsToRealm(comicdatawrapper: ComicDataWrapper) {
        //realm.deleteAll()
        realm.executeTransactionAsync(fun(realm: Realm) {

            comicdatawrapper.data.results.forEach { c ->
                val comicFromDatabase = realm.where(RealmComicEntity::class.java)
                    .equalTo("id", c.id)
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
            android.util.Log.d("loaded", "uploaded")
        })
    }

    //Get Character-data from API
    fun fetchCharactersToRealm(offset: Int) {
        service.getAllCharacters(offset).enqueue(object : Callback<CharacterDataWrapper> {
            override fun onResponse(
                call: Call<CharacterDataWrapper>,
                response: Response<CharacterDataWrapper>
            ) {
                if (response.isSuccessful && response.body()!=null) {
                    characterLimit = response.body()!!.data.limit
                    totalCharacterCount = response.body()!!.data.total
                    saveCharactersToRealm(response.body()!!)

                    Log.d("loaded", "character : ${response.body()!!.data.results.size}")

                } else {
                    Log.d("loaded", "characters : not loaded")
                }
            }

            override fun onFailure(call: Call<CharacterDataWrapper>, t: Throwable) {
            }
        })
        Log.d("loaded", "offset : ${offset}")
    }

    //set CharacterData from API to Realm
    private fun saveCharactersToRealm(characterDataWrapper: CharacterDataWrapper) {
        realm.executeTransactionAsync(fun(realm: Realm) {
            //realm.deleteAll()
            characterDataWrapper.data.results.forEach { c ->
                val characterFromDatabase = realm.where(RealmCharacterEntity::class.java)
                    .equalTo("id", c.id)
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
            android.util.Log.d("loaded", "character uploaded")
        })
    }

    fun fetchSearchedComic(title: String) {
        service.getSearchedComics(titleStartsWith = title)
            .enqueue(object : Callback<ComicDataWrapper> {
                override fun onResponse(
                    call: Call<ComicDataWrapper>,
                    response: Response<ComicDataWrapper>
                ) {
                    Log.d(
                        "searched",
                        "comics start with: ${title} : ${response.body()?.data?.results?.size}"
                    )
                    if (response.body() != null) {
                        saveComicsToRealm(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ComicDataWrapper>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun fetchSearchedCharacter(name: String) {
        service.getSearchedCharacters(nameStartsWith = name)
            .enqueue(object : Callback<CharacterDataWrapper> {
                override fun onResponse(
                    call: Call<CharacterDataWrapper>,
                    response: Response<CharacterDataWrapper>
                ) {
                    Log.d(
                        "searched",
                        "comics starrt with: ${name} : ${response.body()?.data?.results?.size}"
                    )
                    if (response.body() != null) {
                        saveCharactersToRealm(response.body()!!)
                    }
                }


                override fun onFailure(call: Call<CharacterDataWrapper>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }


}//class end