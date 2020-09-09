package com.example.marvellisimo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_comic_page.*
import kotlinx.android.synthetic.main.activity_comic_page.view.*
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*

class ComicsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)

        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()

        //adapter.add()
        recycle_view_comic.adapter = adapter

    }
}

//adapterClass:
class ComicItem(val character: Character): Item<GroupieViewHolder>(){

    //get the name and pic from viewModel-object: will be called in our list for each user object:
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_charactername_row.text = character.name
        // viewHolder.itemView.textView_username_new_message.text= user.username
        //put image in: (use picasso lib, 3e party: https://github.com/square/picasso)

    }

    //renders out the rows in view:
    override fun getLayout(): Int {
        return R.layout.comic_recycle_row_layout
    }
}
