package com.example.marvellisimo.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ComicDataWrapper(val data: ComicDataContainer) {

}

data class ComicDataContainer(val total: Int,
                              val count: Int,
                              val limit: Int,
                              val results: ArrayList<Comic>) {

}
@Parcelize
data class Comic(val id: Int,
                 val title: String,
                 val description: String,
                 val thumbnail: Image,
                 val urls: ArrayList<Url>):Parcelable
