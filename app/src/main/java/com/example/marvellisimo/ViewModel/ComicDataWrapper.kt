package com.example.marvellisimo.ViewModel

data class ComicDataWrapper(val data: ComicDataContainer) {

}

data class ComicDataContainer(val results: ArrayList<Comic>) {

}
data class Comic(val id: Int,
                     val title: String,
                     val description: String,
                     val thumbnail:  com.example.marvellisimo.ViewModel.ImageComic)

data class ImageComic(val path: String, val extension: String)