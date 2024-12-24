package com.example.projemanag.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projemanag.R
import com.example.projemanag.adapters.TasksAdapter
import com.example.projemanag.databinding.ActivityTaskListBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.models.Card
import com.example.projemanag.models.Task
import com.example.projemanag.utils.Constants

class TaskListActivity : BaseActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var mBoardDetails: Board
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

    override fun onCreateOptionsMenu(menu: Menu?):Boolean{
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_members -> {
                val intent = Intent(this, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
        val adapter = TasksAdapter(
            board.taskList,
            onDoneClickListener,
            onEditClickListener,
            onDeleteClickListener,
            onDoneCardClickListener
        )
        binding.rvTaskList.adapter = adapter
    }

    fun addUpdateTaskListSuccess() {
        FirestoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    private val onDoneClickListener: (listName: String) -> Unit = { listName ->
        if (listName.isNotEmpty()) {
            createTaskList(listName)
        } else {
            Toast.makeText(this, "Please enter a List Name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTaskList(taskListName: String) {
        val task = Task(taskListName, FirestoreClass().getCurrentUserId())
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    private val onEditClickListener: (position: Int, listName: String, model: Task) -> Unit =
        { position, listName, model ->
            if (listName.isNotEmpty()) {
                updateTaskList(position, listName, model)
            } else {
                Toast.makeText(this, "Please enter a List Name", Toast.LENGTH_SHORT).show()
            }
        }

    private fun updateTaskList(position: Int, listName: String, model: Task) {
        val task = Task(listName, model.createdBy)

        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    private val onDeleteClickListener: (position: Int, title: String) -> Unit =
        { position, title ->
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Alert")
            //set message for alert dialog
            builder.setMessage("Are you sure you want to delete $title?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                dialogInterface.dismiss() // Dialog will be dismissed
                deleteTaskList(position)

            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                dialogInterface.dismiss() // Dialog will be dismissed
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
            alertDialog.show()  // show the dialog to UI
        }

    private fun deleteTaskList(position: Int) {
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)

    }

    private val onDoneCardClickListener: (position: Int, cardName: String) -> Unit = { position, cardName ->
        if (cardName.isNotEmpty()) {
            addCardToTaskList(position, cardName)
        } else {
            Toast.makeText(this, "Please enter a Card Name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCardToTaskList(position: Int, cardName: String) {
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        val cardAssignedUsersList : ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(FirestoreClass().getCurrentUserId())

        val card = Card(cardName, FirestoreClass().getCurrentUserId(), cardAssignedUsersList)

        val cardList = mBoardDetails.taskList[position].cards
        cardList.add(card)

        val task = Task(
            mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardList
        )
        mBoardDetails.taskList[position] = task

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }
}