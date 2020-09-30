package com.example.marvellisimo.items

import android.view.View
import android.widget.ImageButton
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.realm.Realm
import kotlinx.android.synthetic.main.character_recycle_row_layout.view.*

class CharacterItem(val character: RealmCharacterEntity) : Item<GroupieViewHolder>() {

    companion object {
        val realm = Realm.getDefaultInstance()
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_charactername_row.text = character.name
        var favorite = character.favorite

        val img : String = "${character.thumbnail}"
        Picasso.get().load(img).resize(100, 75)
            .centerCrop().into(viewHolder.itemView.imageView_character_row)

        var fav_ListButton: ImageButton =
            viewHolder.itemView.findViewById(R.id.image_Fav_Button_character)

        if (favorite == true) {
            fav_ListButton.setImageResource(R.drawable.ic_star_solid)
        } else {
            fav_ListButton.setImageResource(R.drawable.ic_star_regular)
        }

        fav_ListButton.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                if (favorite == false) {
                    realm.executeTransaction {
                        character.favorite = true
                        realm.copyToRealmOrUpdate(character)
                    }
                } else {
                    realm.executeTransaction {
                        character.favorite = false
                        realm.copyToRealmOrUpdate(character)
                    }
                }
            }
        });
    }

    //renders out the rows in view:
    override fun getLayout(): Int {
        return R.layout.character_recycle_row_layout
    }
}