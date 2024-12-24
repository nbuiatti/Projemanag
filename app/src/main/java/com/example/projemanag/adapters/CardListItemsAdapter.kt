package com.example.projemanag.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemCardBinding
import com.example.projemanag.models.Card

open class CardListItemsAdapter(
    private var cardList: ArrayList<Card>

) : RecyclerView.Adapter<CardListItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListItemsViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CardListItemsViewHolder(binding)
    }

    override fun getItemCount(): Int = cardList.size

    override fun onBindViewHolder(holder: CardListItemsViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)


    }

}
