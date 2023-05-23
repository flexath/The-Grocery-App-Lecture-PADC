package com.flexath.grocery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexath.grocery.R
import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.delegates.GroceryViewItemActionDelegate
import com.flexath.grocery.viewholders.GroceryViewHolder

class GroceryAdapter(private val delegate: GroceryViewItemActionDelegate,private val number:Long) : BaseRecyclerAdapter<GroceryViewHolder, GroceryVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        return if(number == 0L) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item,parent,false)
            GroceryViewHolder(view, delegate)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item_grid,parent,false)
            GroceryViewHolder(view, delegate)
        }

    }
}