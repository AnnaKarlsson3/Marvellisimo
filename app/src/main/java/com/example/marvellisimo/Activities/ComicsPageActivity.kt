package com.example.marvellisimo.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        val dis_button = findViewById<Button>(R.id.comic_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.character_btn)
        button.setOnClickListener{
            val intent = Intent(this, CharactersPageActivity::class.java)
            startActivity(intent)
        }
    }
}