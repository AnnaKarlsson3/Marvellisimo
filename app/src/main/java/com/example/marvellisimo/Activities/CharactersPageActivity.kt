package com.example.marvellisimo

import CharacterItem
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import com.example.marvellisimo.Activities.CharacterDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.entity.UrlDb
import com.example.marvellisimo.ViewModel.ViewModelDataPopulator
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_characters_page.*


class CharactersPageActivity : AppCompatActivity() {
    val model: ViewModelDataPopulator by viewModels()
    var isClicked = false

    companion object{
        val CHAR_KEY = "CHAR_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)
        Realm.init(this)

        val configuration = RealmConfiguration.Builder()
            .name("characterDb")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)
        val realm = Realm.getDefaultInstance()

        dataCashing(realm)
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

        model.characterDataWrapper.observe(this, {
            it.data.results.forEach { character -> adapter.add(CharacterItem(character)) }
        })

        adapter.setOnItemClickListener { item, view ->
            val characterItem = item as CharacterItem
            val intent = Intent(this, CharacterDetailsActivity::class.java)
            intent.putExtra(CHAR_KEY, characterItem.character)
            startActivity(intent)
        }
        recycle_view_character.adapter = adapter
       
    }

    private fun dataCashing(realm: Realm) {
        model.characterDataWrapper.observe(this, {
            it.data.results.forEach { c ->
                realm.executeTransactionAsync(fun(realm: Realm) {
                    val character = RealmCharacterEntity().apply {
                        id = c.id
                        name = c.name
                        description = c.description
                        thumbnail = "${c.thumbnail.path}.${c.thumbnail.extension}"
                        urls?.addAll(c.urls.map {
                            UrlDb().apply {
                                type = "string"
                                url = it.toString()
                            }
                        })
                    }
                    realm.copyToRealmOrUpdate(character)
                }, fun() {
                    android.util.Log.d("database", "character uploaded")
                })
            }//foreach end
        })
    }

}
