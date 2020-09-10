package com.example.marvellisimo.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_details.*
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*

class CharacterDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        val character = intent.getParcelableExtra<Character>(CharactersPageActivity.CHAR_KEY)
        supportActionBar?.title = character?.name

        character_name.text = character?.name
        character_info.text = character?.description

        val img : String = "${character?.thumbnail?.path}.${character?.thumbnail?.extension}".replace("http","https")

        Picasso.get().load(img).fit().into(character_image)


    }
}