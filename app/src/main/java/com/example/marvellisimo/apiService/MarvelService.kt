@file:JvmName("MarvelServiceKt")

package com.example.marvellisimo.apiService

import com.example.marvellisimo.entity.CharacterDataWrapper
import com.example.marvellisimo.entity.ComicDataWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest

const val apiKey = "3573b53875a89b001a4e318271a7b005" // Replace this
const val privateKey = "78821bef882d707a3c579952f4ffaf71d37a8213" // Replace this



fun getMD5 (timestamp:String): String{
    return "${timestamp}$privateKey$apiKey".md5()
}

fun String.md5(): String{
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString (""){"%02x".format(it)  }
}


interface MarvelService {

    @GET("characters?apikey=$apiKey")
    fun getAllCharacters(@Query("offset")offset :Int = 0, @Query("limit") limit : Int = 100, @Query("ts") ts:String=System.currentTimeMillis().toString(), @Query("hash") hash: String = getMD5(ts)): Call<CharacterDataWrapper>

    @GET("comics?apikey=$apiKey")
    fun getAllComics(@Query("offset")offset :Int = 0, @Query("limit") limit : Int = 100,@Query("ts") ts:String=System.currentTimeMillis().toString(),  @Query("hash") hash: String = getMD5(ts)): Call<ComicDataWrapper>
}

