package com.example.test_sql_lite


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    val userData: List<UserModel>,
    val context: Context,
    private val onDeleteClick: (Int) ->Unit,
    private val onUpdateClick :(UserModel) ->Unit
)
    :RecyclerView.Adapter<UserAdapter.MyHoldeView>() {
    class MyHoldeView(itemView: View):RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)
        val email = itemView.findViewById<TextView>(R.id.email)
        val id = itemView.findViewById<TextView>(R.id.id)
        val delete = itemView.findViewById<TextView>(R.id.delete)
        val update = itemView.findViewById<TextView>(R.id.update)
        var image=itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoldeView {
        val layout = LayoutInflater.from(context).inflate(R.layout.user_list_viee,parent,false)
        return MyHoldeView(layout)
    }

    override fun getItemCount(): Int {
        return userData.size
    }

    override fun onBindViewHolder(holder: MyHoldeView, position: Int) {
        holder.name.text ="Name:- "+ userData[position].name
        holder.email.text = "Email:- "+userData[position].email
        holder.id.text ="Id:-"+ userData[position].id.toString()
        holder.image.setImageBitmap(userData[position].image)
        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)


            builder.setTitle("Delete")
            builder.setMessage("Are you scure")

            builder.setPositiveButton("Yes") { dialog, which ->
                onDeleteClick(userData[position].id)
                dialog.dismiss()
            }
builder.setNegativeButton("Cancel") { dialog, which ->
    dialog.dismiss()
}
            // Create and show the dialog
            val dialog = builder.create()
            dialog.show()



        }

        holder.update.setOnClickListener {

            onUpdateClick(userData[position])

        }

    }

    fun updateData(newData: List<UserModel>) {
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}