package com.example.projemanag.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemTaskBinding
import com.example.projemanag.models.Task

class TasksAdapter(
    private var tasksList: List<Task>,
    private val onDoneClickListener: (listName: String) -> Unit,
    private val onEditClickListener: (position: Int, listName: String, model: Task) -> Unit,
    private val onDeleteClickListener: (position: Int, title: String) -> Unit,
    private val onDoneCardClickListener: (position: Int, cardName: String) -> Unit,

    ) : RecyclerView.Adapter<TasksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.toDp().toPx()), 0, (40.toDp()).toPx(), 0)
        binding.root.layoutParams = layoutParams


        binding.rvCardList.layoutManager =
            LinearLayoutManager(parent.context, LinearLayoutManager.VERTICAL, false)
        binding.rvCardList.setHasFixedSize(true)

        return TasksViewHolder(
            binding,
            onDoneClickListener,
            onEditClickListener,
            onDeleteClickListener,
            onDoneCardClickListener
        )
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {

        val task = tasksList[position]
        val isLastItem = position == tasksList.size - 1

        holder.bind(task, isLastItem, position)
    }
}

private fun Int.toDp(): Int =
    (this / Resources.getSystem().displayMetrics.density).toInt()

private fun Int.toPx(): Int =
    (this * Resources.getSystem().displayMetrics.density).toInt()

