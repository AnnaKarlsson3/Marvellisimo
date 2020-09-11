package com.example.marvellisimo

import CharacterItem
import com.example.marvellisimo.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ViewModel.ViewModelComicCharacterPage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_characters_page.*
import kotlinx.android.synthetic.main.character_recycle_row_layout.view.*


class CharactersPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)


        PrintToRecycleView()
        navButtons()
        setFavButton();


    }

    private fun navButtons(){
        val dis_button = findViewById<Button>(R.id.character_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.comic_btn)
        button.setOnClickListener{
            val intent = Intent(this, ComicsPageActivity::class.java)
            startActivity(intent)
        }

    }


    private fun setFavButton(){
        val favButton: ImageButton = findViewById(R.id.filter_fav_image_btn)
        var isClicked = false
        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                if(isClicked){
                    favButton.setImageResource(R.drawable.ic_star_solid)
                }else{
                    favButton.setImageResource(R.drawable.ic_star_regular)
                }
                isClicked = !isClicked;
            }
        })
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
