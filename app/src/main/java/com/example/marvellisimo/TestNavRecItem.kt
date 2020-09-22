package com.example.marvellisimo

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class TestNavRecItem(): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {


    }

    override fun getLayout(): Int {
        return R.layout.navigation_row_layout
    }
}