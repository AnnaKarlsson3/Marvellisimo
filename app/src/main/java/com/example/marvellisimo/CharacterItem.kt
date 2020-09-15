

import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Character
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.character_recycle_row_layout.view.*

//adapterClass:
class CharacterItem(val character: RealmCharacterEntity) : Item<GroupieViewHolder>() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_charactername_row.text = character.name

        val imgsize = "/portrait_small"
        val img : String = renamePathHttps("${character.thumbnail}")

        Picasso.get().load(img).fit().into(viewHolder.itemView.imageView_character_row)

    }

    //renders out the rows in view:
    override fun getLayout(): Int {
        return R.layout.character_recycle_row_layout
    }
    fun renamePathHttps(path: String): String {
        return path.replace("http", "https")
    }
}