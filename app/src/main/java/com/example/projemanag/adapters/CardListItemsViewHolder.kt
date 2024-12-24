package com.example.projemanag.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemCardBinding
import com.example.projemanag.models.Card

class CardListItemsViewHolder(
    private val binding: ItemCardBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(card: Card) = with(binding) {
        binding.tvCardName.text = card.name
    }
}