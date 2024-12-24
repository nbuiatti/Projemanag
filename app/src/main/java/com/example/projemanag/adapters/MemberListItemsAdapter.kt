package com.example.projemanag.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemMemberBinding
import com.example.projemanag.models.User

open class MemberListItemsAdapter(
    private val userList: ArrayList<User>
) : RecyclerView.Adapter<MemberListItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberListItemsViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberListItemsViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size


    override fun onBindViewHolder(holder: MemberListItemsViewHolder, position: Int) {
        val member = userList[position]
        holder.bind(member)
    }
}