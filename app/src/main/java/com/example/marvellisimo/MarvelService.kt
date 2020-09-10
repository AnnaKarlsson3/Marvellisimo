@file:JvmName("MarvelServiceKt")

package com.example.marvellisimo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest

const val apiKey = "3573b53875a89b001a4e318271a7b005" // Replace this
const val privateKey = "78821bef882d707a3c579952f4ffaf71d37a8213" // Replace this
//var ts = System.currentTimeMillis()

fun getMD5 (timestamp:String): String{
    return "${timestamp}$privateKey$apiKey".md5()
}

fun String.md5(): String{
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString (""){"%02x".format(it)  }
}

interface MarvelService {
    @GET("characters?apikey=$apiKey")
    fun getAllCharacters(@Query("ts") ts:String=System.currentTimeMillis().toString(),  @Query("hash") hash: String = getMD5(ts)): Call<CharacterDataWrapper>
}

