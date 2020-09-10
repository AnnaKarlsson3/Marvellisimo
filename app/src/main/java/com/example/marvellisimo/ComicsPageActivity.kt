package com.example.marvellisimo

import ComicItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_comic_page.*
import kotlinx.android.synthetic.main.character_recycle_row_layout.*
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*

class ComicsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)

        PrintToRecycleView()
    }

    private fun PrintToRecycleView(){
        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()
        val model: ViewModelCharacterPage by viewModels()

        model.characterDataWrapper.observe(this, {
            it.data.results.forEach { comic -> adapter.add(ComicItem(comic)) }
        })

        recycle_view_comic.adapter = adapter
    }
}


