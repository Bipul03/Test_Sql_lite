package com.example.test_sql_lite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "YourDatabaseName"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "your_table_name"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_EMAIL = "email"
        private const val COL_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_EMAIL TEXT ,$COL_IMAGE BLOB)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(name: String, email: String,image:ByteArray): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_EMAIL, email)
        contentValues.put(COL_IMAGE, image)
        return db.insert(TABLE_NAME, null, contentValues)
    }
    // Inside your DatabaseHelper class
    @SuppressLint("Range")
    fun getAllData(): List<UserModel> {
        val dataModel = mutableListOf<UserModel>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val image = cursor.getBlob(cursor.getColumnIndex("image"))
                val imageBitmap = BitmapFactory.decodeByteArray(image,0,image.size)

                val userModel = UserModel(name, email,id,imageBitmap)
                dataModel.add(userModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataModel
//        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun deleteData(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME,"$COL_ID=?", arrayOf(id.toString()))
    }

    fun updateData(id: Int, newName: String, newEmail: String,image: ByteArray): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME,newName)
        contentValues.put(COL_EMAIL,newEmail)
        contentValues.put(COL_IMAGE, image)
        return db.update(TABLE_NAME, contentValues, "$COL_ID=?", arrayOf(id.toString()))

    }




}