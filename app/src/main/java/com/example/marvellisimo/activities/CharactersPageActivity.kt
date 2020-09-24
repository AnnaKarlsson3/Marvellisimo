package com.example.marvellisimo

import CharacterItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Button


import android.widget.ImageButton
import android.widget.SearchView

import androidx.activity.viewModels
import com.example.marvellisimo.activities.CharacterDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.viewModel.ViewModelComicCharacterPage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_characters_page.*
import kotlinx.android.synthetic.main.activity_comic_page.*


class CharactersPageActivity : AppCompatActivity() {

    val modelCharacter: ViewModelComicCharacterPage by viewModels()
    var isClicked = false
    val activity = this
    val adapter = GroupAdapter<GroupieViewHolder>()
    val manager = LinearLayoutManager(this)
    var isScrolling = false
    var current = 0
    var total = 0
    var scrolledOut = 0
    var offset = 0
    var totalCharacter = 0

    companion object {
        val CHAR_ID = "CHAR_ID"
        val CHAR_KEY = "CHAR_KEY"
        val CHAR_NAME = "CHAR_NAME"
        val CHAR_INFO = "CHAR_INFO"
        val CHAR_URL = "CHAR_URL"
        val CHAR_IMAGE = "CHAR_IMAGE"
        val CHAR_FAVORITE = "CHAR_FAVORITE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        filterCharacter()
        PrintToRecycleView()
        navButtons()
        setFavButton();
        clickOnRecycleView()
        onScrolling()
    }

    private fun navButtons() {
        val dis_button = findViewById<Button>(R.id.character_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.comic_btn)
        button.setOnClickListener {
            val intent = Intent(this, ComicsPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFavButton() {
        val favButton: ImageButton = findViewById(R.id.filter_fav_character_btn)

        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                isClicked = !isClicked;
                if (isClicked) {
                    modelCharacter.getFavoriteCharacter().observe(activity, {
                        adapter.clear()
                        it.forEach { character ->
                            adapter.add(CharacterItem(character))
                        }
                    })

                    favButton.setImageResource(R.drawable.ic_star_solid)
                } else {
                    favButton.setImageResource(R.drawable.ic_star_regular)
                    PrintToRecycleView()
                }

            }
        })
    }

    private fun PrintToRecycleView() {

        //3party adapter https://github.com/lisawray/groupie ..
        modelCharacter.getCharacterData(offset).observe(this, {
            adapter.clear()
            it.forEach { character ->
                adapter.add(CharacterItem(character))
                Log.d("characterResult", "${character.name}")
            }
        })

        recycle_view_character.adapter = adapter


    }

    private fun onScrolling() {
        recycle_view_character.layoutManager = manager

        recycle_view_character.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                current = manager.childCount
                total = manager.itemCount
                scrolledOut = manager.findFirstVisibleItemPosition()

                totalCharacter = modelCharacter.getTotalCharacterCount()
                val limit = modelCharacter.getCharacterLimit()
                Log.d("C", "character limit:${limit}")
                Log.d("C", "character offset: ${offset}")
                Log.d("C", "character total in Api: ${totalCharacter}")

                println("character--- total in recycleview: ${total}, current:${current}, scrolled${scrolledOut}")

                if (isScrolling && scrolledOut + current == (offset + limit) && offset < totalCharacter) {
                    offset += limit
                    modelCharacter.getCharacterData(offset)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun clickOnRecycleView() {
        adapter.setOnItemClickListener { item, view ->
            val characterItem = item as CharacterItem
            val intent = Intent(this, CharacterDetailsActivity::class.java)

            //intent.putExtra(CHAR_KEY, characterItem.character)
            intent.putExtra(CHAR_ID, characterItem.character.id)
            intent.putExtra(CHAR_NAME, characterItem.character.name)
            intent.putExtra(CHAR_IMAGE, characterItem.character.thumbnail)
            intent.putExtra(CHAR_INFO, characterItem.character.description)
            intent.putExtra(CHAR_FAVORITE, characterItem.character.favorite)
            intent.putExtra(CHAR_URL, characterItem.character.urls?.get(0)?.url)
            startActivity(intent)
        }
    }

    private fun filterCharacter() {
        search_bar_character.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                modelCharacter.getSearchCharacterData(newText).observe(activity, {
                    adapter.clear()
                    it.forEach { character ->
                        adapter.add(CharacterItem(character))
                        Log.d("filterdCharacter", "${character.name}")
                    }
                })
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })
    }

}
