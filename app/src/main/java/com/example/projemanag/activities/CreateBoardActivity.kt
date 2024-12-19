package com.example.projemanag.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityCreateBoardBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var mSelectedImageFileUri: Uri? = null

    private lateinit var binding: ActivityCreateBoardBinding

    private lateinit var mUserName : String

    private var mBoardImageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        if(intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME)!!
        }

        binding.ivBoardImage.setOnClickListener {

            val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            if (ContextCompat.checkSelfPermission(
                    this, permissionsList.toString()
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    permissionsList,
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }

        }

        binding.btnCreate.setOnClickListener {
            if(mSelectedImageFileUri != null){
                uploadBoardImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard()
            }
        }
    }

    private fun createBoard(){
        val assignedUserArrayList : ArrayList<String> = ArrayList()
        assignedUserArrayList.add(getCurrentUserID())

        var board = Board(
        binding.etBoardName.text.toString(),
        mBoardImageURL,
        mUserName,
        assignedUserArrayList
        )
        FirestoreClass().createBoard(this,board)
    }

    private fun uploadBoardImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "BOARD_IMAGE" + System.currentTimeMillis()
                        + "." + Constants.getFileExtension(this, mSelectedImageFileUri)
            )
        sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
            Log.i(
                "Board Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.i(
                    "Downloadable Image URL", uri.toString()
                )
                mBoardImageURL = uri.toString()
                hideProgressDialog()
                createBoard()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this,
                exception.message,
                Toast.LENGTH_LONG
            ).show()
            hideProgressDialog()
        }

    }

    fun boardCreatedSuccessfully(){
        hideProgressDialog()

        setResult(Activity.RESULT_OK)

        finish()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCreateBoardActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.create_board_title)
        }
        binding.toolbarCreateBoardActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    "Denied permissions", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(binding.ivBoardImage)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}