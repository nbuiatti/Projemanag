package com.example.projemanag.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projemanag.R
import com.example.projemanag.adapters.TasksAdapter
import com.example.projemanag.databinding.ActivityTaskListBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.models.Task
import com.example.projemanag.utils.Constants

class TaskListActivity : BaseActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var  mBoardDetails : Board
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var boardDocument = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            boardDocument = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, boardDocument)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.name
        }

        binding.toolbarTaskListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun boardDetails(board: Board) {
        mBoardDetails = board
        hideProgressDialog()
        setupActionBar()

        val addTaskList = Task(resources.getString(R.string.add_list))
        board.taskList.add(addTaskList)
        binding.rvTaskList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTaskList.setHasFixedSize(true)
        val adapter = TasksAdapter(board.taskList)
        binding.rvTaskList.adapter =adapter
    }
    fun addUpdateTaskListSuccess(){

        FirestoreClass().getBoardDetails(this,mBoardDetails.documentId)
    }
}