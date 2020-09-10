package com.example.marvellisimo

import CharacterItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_characters_page.*
import kotlinx.android.synthetic.main.activity_comic_page.*

class CharactersPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)

        PrintToRecycleView()

    }

    private fun PrintToRecycleView(){
        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()
        val model: ViewModelCharacterPage by viewModels()

        model.characterDataWrapper.observe(this, {
            it.data.results.forEach { character -> adapter.add(CharacterItem(character)) }
        })

        recycle_view_character.adapter = adapter
    }
}
