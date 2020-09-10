package com.example.marvellisimo.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.ViewModelComicCharacterPage

class ComicsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)


        val model: ViewModelComicCharacterPage by viewModels()


        model.characterDataWrapper.observe(this, {
            Log.i("tag", "character:${it.data.results}")
        })
        model.comicDataWrapper.observe(this, {
            it.data.results.forEach{d ->  Log.i("tag", "comic:${d.title}")}

        })
    }
}