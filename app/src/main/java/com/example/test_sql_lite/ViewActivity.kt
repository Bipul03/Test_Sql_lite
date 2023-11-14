package com.example.test_sql_lite

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class ViewActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var arrayAdapter: UserAdapter
    lateinit var arrayList: ArrayList<UserModel>
    lateinit var dbHelper : DatabaseHelper
    lateinit var fileUri : Uri
   lateinit var edit:ImageView
   lateinit var images:ImageView
    lateinit var galleryImage:ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        recyclerView = findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        arrayList = ArrayList()

        dbHelper = DatabaseHelper(this)
        val  userData = dbHelper.getAllData()

        arrayAdapter= UserAdapter(userData,this,this::onDeleteClick,this::onUpdateClick)

        recyclerView.adapter = arrayAdapter

        galleryImage = registerForActivityResult(ActivityResultContracts.GetContent())
        {
            images.setImageURI(it)
            if (it != null) {
                fileUri = it
            }
        }

    }


    fun onDeleteClick(id:Int) {
        val deleteRow = dbHelper.deleteData(id)
        if (deleteRow > 0) {
            val newData = dbHelper.getAllData()
            arrayAdapter = UserAdapter(newData,  this,this::onDeleteClick,this::onUpdateClick)
            recyclerView.adapter = arrayAdapter
            Toast.makeText(this, "Data delete successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onUpdateClick(dataItem: UserModel){
        showUpdateDialog(dataItem)
    }


    @SuppressLint("MissingInflatedId")
    private fun showUpdateDialog(dataItem: UserModel) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.update_dialog, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.updateNameEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.updateDescriptionEditText)
         images = dialogView.findViewById<ImageView>(R.id.images)
         edit=dialogView.findViewById(R.id.edit)

        edit.setOnClickListener {
            galleryImage.launch("image/*")
        }

        nameEditText.setText(dataItem.name)
        descriptionEditText.setText(dataItem.email)
        images.setImageBitmap(dataItem.image)



        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Update Data")
            .setPositiveButton("Update") { dialog, _ ->
                val newName = nameEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()
                val image = readBytes(this,fileUri)

                val updatedRows =
                    image?.let { dbHelper.updateData(dataItem.id, newName, newDescription, it) }
                if (updatedRows != null) {
                    if (updatedRows > 0) {
                        arrayAdapter = UserAdapter(
                            dbHelper.getAllData(),
                            this,
                            this::onDeleteClick,
                            this::showUpdateDialog
                        )
                        recyclerView.adapter = arrayAdapter
                        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
            //                    } else {
            //                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
            //                    }
            //                }

                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()


    }
    private fun readBytes (context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream (uri)?.use { it.readBytes () }
}