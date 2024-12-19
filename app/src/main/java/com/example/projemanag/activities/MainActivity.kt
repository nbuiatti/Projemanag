package com.example.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.adapters.BoardItemsAdapter
import com.example.projemanag.databinding.ActivityMainBinding
import com.example.projemanag.databinding.ContentMainBinding
import com.example.projemanag.databinding.NavHeaderMainBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }

    private lateinit var mUserName: String

    private lateinit var binding: ActivityMainBinding
    private lateinit var navBinding: NavHeaderMainBinding
    private lateinit var contMainBinding: ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        navBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        contMainBinding = binding.appBarView.contentMainView
        val view = binding.root
        setContentView(view)

        setupActionBar()

        binding.navView.setNavigationItemSelectedListener(this)
        FirestoreClass().loadUserData(this, true)

        binding.appBarView.fabCreateBoard.setOnClickListener {
            val intent = Intent(
                this,
                CreateBoardActivity::class.java
            )
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }

    }

    fun populateBoardListToUI(boardsList: ArrayList<Board>) {
        hideProgressDialog()

        if (boardsList.size > 0) {
            contMainBinding.rvBoardsList.apply {
                val boardItemsAdapter = BoardItemsAdapter(boardsList)

                adapter = boardItemsAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
                setHasFixedSize(true)
                visibility = View.VISIBLE
                boardItemsAdapter.setOnClickListener(object: BoardItemsAdapter.OnClickListener{
                    override fun onClick(position: Int, model: Board) {
                        val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                        intent.putExtra(Constants.DOCUMENT_ID,model.documentId)
                        startActivity(intent)
                    }
                })

            }

            contMainBinding.tvNoBoardsAvailable.visibility = View.GONE
        } else {
            contMainBinding.rvBoardsList.visibility = View.GONE
            contMainBinding.tvNoBoardsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.appBarView.toolbarMainActivity)
        binding.appBarView.toolbarMainActivity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        binding.appBarView.toolbarMainActivity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE
        ) {
            FirestoreClass().loadUserData(this)
        } else if (resultCode == Activity.RESULT_OK
            && requestCode == CREATE_BOARD_REQUEST_CODE
        ) {
            FirestoreClass().getBoardsList(this)

        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this, MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE
                )
            }

            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {

        mUserName = user.name

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navBinding.ivUserImage)

        navBinding.tvUsername.text = user.name

        if (readBoardsList) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardsList(this)
        }


    }

}