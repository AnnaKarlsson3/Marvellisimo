package com.example.marvellisimo.ViewModel

data class CharacterDataWrapper(val data: CharacterDataContainer) {

}

data class CharacterDataContainer(val results: ArrayList<Character>) {

}
data class Character(val id: Int,
                     val name: String,
                     val description: String,
                     val thumbnail: com.example.marvellisimo.ViewModel.Image
)

data class Image(val path: String, val extension: String)
