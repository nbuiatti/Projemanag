package com.example.projemanag.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemTaskBinding
import com.example.projemanag.models.Task

class TasksViewHolder(
    private val binding: ItemTaskBinding,
    private val onCreateClickListener: (listName: String) -> Unit,
    private val onEditClickListener: (position: Int, listName: String, model: Task) -> Unit,
    private val onDeleteClickListener: (position: Int, title: String) -> Unit,

    private val onCreateCardClickListener: (position: Int, cardName: String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task, isLastItem: Boolean, position: Int) = with(binding) {
        tvTaskListTitle.text = task.title

        if (isLastItem) {
            tvAddTaskList.visibility = View.VISIBLE
            llTaskItem.visibility = View.GONE
        } else {
            tvAddTaskList.visibility = View.GONE
            llTaskItem.visibility = View.VISIBLE
        }
        tvTaskListTitle.text = task.title
        tvAddTaskList.setOnClickListener {
            tvTaskListTitle.visibility = View.GONE
            cvAddTaskListName.visibility = View.VISIBLE
        }
        ibCloseListName.setOnClickListener {
            tvTaskListTitle.visibility = View.VISIBLE
            cvAddTaskListName.visibility = View.GONE
        }
        ibDoneListName.setOnClickListener {
            val listName = etTaskListName.text.toString()
            onCreateClickListener(listName)
        }
        ibEditListName.setOnClickListener {
            etTaskListName.setText(task.title)
            llTitleView.visibility = View.GONE
            cvEditTaskListName.visibility = View.VISIBLE
        }
        ibCloseEditableView.setOnClickListener {
            llTitleView.visibility = View.VISIBLE
            cvEditTaskListName.visibility = View.GONE
        }
        ibDoneEditListName.setOnClickListener {
            val listName = etEditTaskListName.text.toString()
            onEditClickListener(position, listName, task)
        }
        ibDeleteList.setOnClickListener {
            onDeleteClickListener(position, task.title)
        }

        tvAddCard.setOnClickListener {
            tvAddCard.visibility = View.GONE
            cvAddCard.visibility = View.VISIBLE
        }
        ibCloseCardName.setOnClickListener {
            tvAddCard.visibility = View.VISIBLE
            cvAddCard.visibility = View.GONE
        }
        ibDoneCardName.setOnClickListener {
            val cardName = etCardName.text.toString()
            onCreateCardClickListener(position, cardName)
        }
        val adapter = CardListItemsAdapter(task.cards)
        rvCardList.adapter = adapter
    }


}
