package com.example.marvellisimo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ComicsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)

        /*val model: ViewModelCharacterPage by viewModelStore.

        val res = listOf<CharacterDataContainer>()
        for(c in res){
            Log.d("result", "Characters: $c")
        }*/
        val model: ViewModelCharacterPage by viewModels()

        model.characterDataWrapper.observe(this, {
            Log.i("tag", "reponame:${it}")

        })
    }
}