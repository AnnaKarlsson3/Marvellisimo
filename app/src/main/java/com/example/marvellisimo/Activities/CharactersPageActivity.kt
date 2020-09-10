package com.example.marvellisimo.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.example.marvellisimo.R


class CharactersPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)


        val dis_button = findViewById<Button>(R.id.character_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.comic_btn)
        button.setOnClickListener{
            val intent = Intent(this, ComicsPageActivity::class.java)
            startActivity(intent)
        }


    }
}