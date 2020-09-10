package com.example.marvellisimo

import CharacterItem
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import com.example.marvellisimo.ViewModel.ViewModelComicCharacterPage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_characters_page.*
import kotlinx.android.synthetic.main.activity_comic_page.*



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

        PrintToRecycleView()

    }

    private fun PrintToRecycleView(){
        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()
        val model: ViewModelComicCharacterPage by viewModels()

        model.characterDataWrapper.observe(this, {
            it.data.results.forEach { character -> adapter.add(CharacterItem(character)) }
        })

        recycle_view_character.adapter = adapter
    }
}
