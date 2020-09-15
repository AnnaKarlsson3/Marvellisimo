package com.example.marvellisimo.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvellisimo.ApiService.MarvelService
import com.example.marvellisimo.ApiService.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelDataPopulator : ViewModel() {

    val comicDataWrapper: MutableLiveData<ComicDataWrapper> by lazy {
        val service = RetroInstance.getRetroInstance().create(MarvelService::class.java)
        val comicDataWrapper = MutableLiveData<ComicDataWrapper>()

        service.getAllComics().enqueue(object : Callback<ComicDataWrapper> {
            override fun onResponse(
                call: Call<ComicDataWrapper>,
                response: Response<ComicDataWrapper>
            ) {
                if (response.isSuccessful) {
                    comicDataWrapper.postValue(response.body())
                } else {
                    comicDataWrapper.postValue(null)
                }
            }
            override fun onFailure(call: Call<ComicDataWrapper>, t: Throwable) {
                comicDataWrapper.postValue(null)
            }
        })
        comicDataWrapper
    }

    val characterDataWrapper: MutableLiveData<CharacterDataWrapper> by lazy {
        val service = RetroInstance.getRetroInstance().create(MarvelService::class.java)
        val characterDataWrapper = MutableLiveData<CharacterDataWrapper>()

        service.getAllCharacters().enqueue(object : Callback<CharacterDataWrapper> {
            override fun onResponse(
                call: Call<CharacterDataWrapper>,
                response: Response<CharacterDataWrapper>
            ) {
                if (response.isSuccessful) {
                    characterDataWrapper.postValue(response.body())
                } else {
                    characterDataWrapper.postValue(null)
                }
            }
            override fun onFailure(call: Call<CharacterDataWrapper>, t: Throwable) {
                
                characterDataWrapper.postValue(null)
            }
        })
        characterDataWrapper
    }
}