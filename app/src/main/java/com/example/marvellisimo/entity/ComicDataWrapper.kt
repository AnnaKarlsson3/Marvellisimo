package com.example.marvellisimo.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ComicDataWrapper(val data: ComicDataContainer) {

}

data class ComicDataContainer(val results: ArrayList<Comic>) {

}
@Parcelize
data class Comic(val id: Int,
                 val title: String,
                 val description: String,
                 val thumbnail: Image,
                 val urls: ArrayList<Url>):Parcelable

//@Parcelize
//data class ImageComic(val path: String, val extension: String):Parcelable