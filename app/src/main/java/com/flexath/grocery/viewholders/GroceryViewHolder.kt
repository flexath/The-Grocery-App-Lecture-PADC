package com.flexath.grocery.viewholders

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.databinding.ViewHolderGroceryItemBinding
import com.flexath.grocery.delegates.GroceryViewItemActionDelegate

class GroceryViewHolder(itemView: View,private val delegate: GroceryViewItemActionDelegate) : BaseViewHolder<GroceryVO>(itemView) {

    private var binding:ViewHolderGroceryItemBinding

    init {
        binding = ViewHolderGroceryItemBinding.bind(itemView)
    }

    override fun bindData(data: GroceryVO) {
        binding.tvTitle.text = data.name
        binding.tvDescription.text = data.description
        binding.tvCount.text = "x ${data.amount.toString()}"

        binding.btnDelete.setOnClickListener {
            delegate.onTapDeleteGrocery(data.name ?: "")
        }

        binding.btnEdit.setOnClickListener {
            delegate.onTapEditGrocery(data.name ?: "",data.description ?: "",data.amount ?: 0)
        }

        binding.btnFileUpload.setOnClickListener {
            delegate.onTapFileUpload(data)
        }

        Glide.with(itemView.context)
            .load(data.image)
            .into(binding.ivGroceryImage)
    }
}