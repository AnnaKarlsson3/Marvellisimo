package com.example.marvellisimo.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CharacterDataWrapper(val data: CharacterDataContainer) {

}

data class CharacterDataContainer(val total:Int,
                                  val count:Int,
                                  val limit:Int,
                                  val results: ArrayList<Character>) {

}
@Parcelize
data class Character(val id: Int,
                     val name: String,
                     val description: String,
                     val thumbnail: Image,
                     val urls: ArrayList<Url> ): Parcelable{

}

@Parcelize
data class Image(val path: String, val extension: String):Parcelable

@Parcelize
data class Url(val type:String,val url:String):Parcelable