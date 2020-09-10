import com.example.marvellisimo.Character
import com.example.marvellisimo.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.character_recycle_row_layout.*
import kotlinx.android.synthetic.main.character_recycle_row_layout.view.*
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*

//adapterClass:
class ComicItem(val character: Character) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_comicname_row.text = character.name
        //put image in: (use picasso lib, 3e party: https://github.com/square/picasso)

    }

    //renders out the rows in view:
    override fun getLayout(): Int {
        return R.layout.comic_recycle_row_layout
    }
}

