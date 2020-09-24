@file:JvmName("MarvelServiceKt")

package com.example.marvellisimo.apiService

import com.example.marvellisimo.entity.CharacterDataWrapper
import com.example.marvellisimo.entity.ComicDataWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest

const val apiKey = "16534c1cb23bb6f87e5211bbad399f32" // Replace this
const val privateKey = "f4f723c0307fc2ff44a15ecc4323087f78189925" // Replace this



fun getMD5 (timestamp:String): String{
    return "${timestamp}$privateKey$apiKey".md5()
}

fun String.md5(): String{
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString (""){"%02x".format(it)  }
}


interface MarvelService {

    @GET("characters?apikey=$apiKey&orderBy=name")
    fun getAllCharacters(@Query("offset")offset :Int = 0,
                         @Query("limit") limit : Int = 100,
                         @Query("ts") ts:String=System.currentTimeMillis().toString(),
                         @Query("hash") hash: String = getMD5(ts)): Call<CharacterDataWrapper>

    @GET("comics?apikey=$apiKey&orderBy=title")
    fun getAllComics(@Query("offset")offset :Int = 0,
                     @Query("limit") limit : Int = 100,
                     @Query("ts") ts:String=System.currentTimeMillis().toString(),
                     @Query("hash") hash: String = getMD5(ts)): Call<ComicDataWrapper>

    @GET("characters?apikey=$apiKey")
    fun getSearchedCharacters(
        @Query("nameStartsWith") nameStartsWith:String="",
                         @Query("ts") ts:String=System.currentTimeMillis().toString(),
                         @Query("hash") hash: String = getMD5(ts)): Call<CharacterDataWrapper>

    @GET("comics?apikey=$apiKey")
    fun getSearchedComics(
        @Query("titleStartsWith")titleStartsWith:String="",
                     @Query("ts") ts:String=System.currentTimeMillis().toString(),
                     @Query("hash") hash: String = getMD5(ts)): Call<ComicDataWrapper>


}

