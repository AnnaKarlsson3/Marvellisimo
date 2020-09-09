package com.example.marvellisimo

import android.media.Image

data class CharacterDataWrapper(val data: CharacterDataContainer) {

}

data class CharacterDataContainer(val results: ArrayList<Character>) {

}
data class Character(val id: Int,
                     val name: String,
                     val description: String,
                     val thumbnail: com.example.marvellisimo.Image)

data class Image(val path: String, val extension: String)
