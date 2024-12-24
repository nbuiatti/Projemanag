package com.example.projemanag.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projemanag.databinding.ItemMemberBinding
import com.example.projemanag.models.User

class MemberListItemsViewHolder(
    private val binding: ItemMemberBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) = with(binding) {

        tvMemberName.text = user.name
        tvMemberEmail.text = user.email
        Glide.with(root.context)
            .load(user.image)
            .centerCrop()
            .placeholder(com.example.projemanag.R.drawable.ic_board_place_holder)
            .into(ivMemberImage)

    }
}
