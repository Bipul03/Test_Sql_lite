package com.example.test_sql_lite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.example.test_sql_lite.R

class MainActivity : AppCompatActivity() {
   lateinit var fileUri : Uri
    lateinit var imageView: ImageView
    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextName = findViewById<EditText>(R.id.name)
        val editTextEmail = findViewById<EditText>(R.id.email)
        val buttonInsert = findViewById<Button>(R.id.insert)
        imageView = findViewById(R.id.image)
        var camera=findViewById<ImageView>(R.id.camera)

        var galleryImage = registerForActivityResult(ActivityResultContracts.GetContent())

        {
            imageView.setImageURI(it)
            if (it != null) {
                fileUri = it
            }
        }
        val gallerBtn = findViewById<ImageView>(R.id.image)
        camera.setOnClickListener {
            galleryImage.launch("image/*")
        }

        buttonInsert.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val image = readBytes(this,fileUri)

            if (name.isEmpty()) {
                editTextName.error = "Enter Name"
            }
           else if (email.isEmpty()) {
                    editTextName.error = "Enter Email"
                } else {
                    val dbHelper = DatabaseHelper(this)
                    val result = image?.let { it1 -> dbHelper.insertData(name, email, it1) }
                    if (result != -1L) {
                        val intent = Intent(this, ViewActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Insert", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                    }
                    dbHelper.close()
                }
            }
        }


    private fun readBytes (context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream (uri)?.use { it.readBytes () }
}

