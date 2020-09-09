package com.example.marvellisimo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCharacterPage : ViewModel() {
//    lateinit var characterDataWrapper : MutableLiveData<CharacterDataWrapper>
//    init {
//        characterDataWrapper = MutableLiveData()
//    }
//    fun getCharacterDataWrapperObserver(): MutableLiveData<CharacterDataWrapper>{
//        return characterDataWrapper
//    }

    val characterDataWrapper: MutableLiveData<CharacterDataWrapper> by lazy {
        val service = RetroInstance.getRetroInstance().create(MarvelService::class.java)
        val characterDataWrapper = MutableLiveData<CharacterDataWrapper>()

        service.getAllCharacters("snehal").enqueue(object : Callback<CharacterDataWrapper> {
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