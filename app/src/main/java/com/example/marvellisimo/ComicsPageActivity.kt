package com.example.marvellisimo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ComicsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)



        val model: ViewModelCharacterPage by viewModels()

        model.characterDataWrapper.observe(this, {
            Log.i("tag", "reponame:${it.data.results[0].name}")

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