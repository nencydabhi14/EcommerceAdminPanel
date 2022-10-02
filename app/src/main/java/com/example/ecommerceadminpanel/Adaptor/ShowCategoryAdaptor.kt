package com.example.ecommerceadminpanel.Adaptor

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceadminpanel.ModelClass.ModelCategoryDelete
import com.example.ecommerceadminpanel.ModelClass.ModelCategoy
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.Activity.Show_All_Category_List
import com.google.firebase.database.FirebaseDatabase

class ShowCategoryAdaptor(
    val showAllCategoryList: Show_All_Category_List,
    val list1: ArrayList<ModelCategoryDelete>
) : RecyclerView.Adapter<ShowCategoryAdaptor.ViewData>() {

    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id_txt = itemView.findViewById<TextView>(R.id.id_txt)
        var category_txt = itemView.findViewById<TextView>(R.id.category_txt)
        var update_category = itemView.findViewById<ImageView>(R.id.update_category)
        var delete_category = itemView.findViewById<ImageView>(R.id.delete_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
        var view =
            LayoutInflater.from(showAllCategoryList).inflate(R.layout.all_category, parent, false)
        return ViewData(view)
    }

    override fun onBindViewHolder(holder: ViewData, position: Int) {
        holder.id_txt.text = list1[position].id
        holder.category_txt.text = list1[position].category

        holder.delete_category.setOnClickListener {
            var dialog = Dialog(showAllCategoryList)
            dialog.setContentView(R.layout.delete_category)
            dialog.show()

            var firebaseDatabase = FirebaseDatabase.getInstance()
            var fireref = firebaseDatabase.reference

            var yes_delete_category = dialog.findViewById<TextView>(R.id.yes_delete_category)
            var cancel_delete_category = dialog.findViewById<TextView>(R.id.cancel_delete_category)
            var catgory_txt = dialog.findViewById<TextView>(R.id.catgory_txt)

            catgory_txt.text = list1[position].category

            yes_delete_category.setOnClickListener {
                fireref.child("category").child(list1[position].key).removeValue()

                list1.clear()
                dialog.dismiss()
            }

            cancel_delete_category.setOnClickListener {
                dialog.dismiss()
            }

        }
        holder.update_category.setOnClickListener {
            updateCategory(position)
        }
    }

    override fun getItemCount(): Int {
        return list1.size
    }

    fun updateCategory(position: Int) {
        var dialogup = Dialog(showAllCategoryList)
        dialogup.setContentView(R.layout.update_category)
        dialogup.show()

        var id_category_Edt_D = dialogup.findViewById<EditText>(R.id.id_category_Edt_D)
        var category_Edt_D = dialogup.findViewById<EditText>(R.id.category_Edt_D)
        var done_btn_category_D = dialogup.findViewById<TextView>(R.id.done_btn_category_D)
        var cancle_btn_D = dialogup.findViewById<TextView>(R.id.cancle_btn_D)

        id_category_Edt_D.setText(list1[position].id)
        category_Edt_D.setText(list1[position].category)

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var fireref = firebaseDatabase.reference

        done_btn_category_D.setOnClickListener {

            var m1 = ModelCategoy(id_category_Edt_D.text.toString(), category_Edt_D.text.toString())

            fireref.child("category").child("${list1[position].key}").setValue(m1)
            dialogup.dismiss()
            list1.clear()
        }

        cancle_btn_D.setOnClickListener {
            dialogup.dismiss()
        }
    }
}