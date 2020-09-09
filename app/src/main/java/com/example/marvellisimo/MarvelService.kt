@file:JvmName("MarvelServiceKt")

package com.example.marvellisimo

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

const val publicKey = "3573b53875a89b001a4e318271a7b005" // Replace this
const val privateKey = "78821bef882d707a3c579952f4ffaf71d37a8213" // Replace this
var ts = (Date().time).toString()

private fun String.md5(): String {
    try { // Create MD5 Hash
        val digest = MessageDigest
            .getInstance("MD5")
        digest.update(this.toByteArray())
        val messageDigest = digest.digest()
        // Create Hex String
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2) h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""

}

interface MarvelService {
    @GET("characters?")
    fun getAllCharacters(@Query("ts") timeSt: String = "$ts", @Query("apikey") apikey: String= "$publicKey", @Query("hash") hash: String = "$ts  $privateKey $publicKey".md5()): Call<CharacterDataWrapper>
}

