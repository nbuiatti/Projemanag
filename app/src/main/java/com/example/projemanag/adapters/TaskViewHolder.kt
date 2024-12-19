package com.example.projemanag.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemTaskBinding
import com.example.projemanag.models.Task

class TasksViewHolder(private val binding: ItemTaskBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task, isLastItem: Boolean) = with(binding) {
        binding.tvTaskListTitle.text = task.title

        if (isLastItem) {
            tvAddTaskList.visibility = View.VISIBLE
            llTaskItem.visibility = View.GONE
        } else {
            tvAddTaskList.visibility = View.GONE
            llTaskItem.visibility = View.VISIBLE
        }
        binding.tvTaskListTitle.text = task.title
        binding.tvAddTaskList.setOnClickListener {
            binding.tvTaskListTitle.visibility = View.GONE
            binding.cvAddTaskListName.visibility = View.VISIBLE
        }
        binding.ibCloseListName.setOnClickListener {
            binding.tvTaskListTitle.visibility = View.VISIBLE
            binding.cvAddTaskListName.visibility = View.GONE
        }
        binding.ibDoneListName.setOnClickListener {
            //TODO create entry in DB and display the task list
        }

    }
}
