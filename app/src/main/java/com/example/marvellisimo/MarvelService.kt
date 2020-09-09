@file:JvmName("MarvelServiceKt")

package com.example.marvellisimo

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val apiKey = "3573b53875a89b001a4e318271a7b005" // Replace this
const val privateKey = "78821bef882d707a3c579952f4ffaf71d37a8213" // Replace this
const val ts = "1"
val HASH = (ts + privateKey + apiKey).md5()

interface MarvelService {
    @GET("characters?ts=$ts&apikey=$apiKey")
    fun getAllCharacters(@Query("hash") hash: String = HASH): Single<CharacterDataWrapper>
}
