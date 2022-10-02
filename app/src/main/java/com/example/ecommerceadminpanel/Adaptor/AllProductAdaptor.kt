package com.example.ecommerceadminpanel.Adaptor

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceadminpanel.ModelClass.ModelDataRead
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.Activity.Update_Screen
import com.example.ecommerceadminpanel.Activity.Upload_Product_Screen
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase

class AllProductAdaptor(
    var uploadProductScreen: Upload_Product_Screen,
    var l1: ArrayList<ModelDataRead>
) : RecyclerView.Adapter<AllProductAdaptor.ViewData>() {

    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var updel_img = itemView.findViewById<ImageView>(R.id.updel_img)

        var product_image_R = itemView.findViewById<ImageView>(R.id.product_image_R)
        var product_name_txt = itemView.findViewById<TextView>(R.id.product_name_txt)
        var product_price_txt = itemView.findViewById<TextView>(R.id.product_price_txt)
        var product_description_txt = itemView.findViewById<TextView>(R.id.product_description_txt)
        var available_not = itemView.findViewById<TextView>(R.id.available_not)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
        var view = LayoutInflater.from(uploadProductScreen)
            .inflate(R.layout.allptoduct_item_design, parent, false)

        return ViewData(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewData, position: Int) {

        holder.product_name_txt.text = l1[position].pname
        holder.product_price_txt.text = l1[position].pprice
        holder.product_description_txt.text = l1[position].pdiscription
        holder.available_not.text = l1[position].offer

        Glide.with(uploadProductScreen).load(l1[position].image)
            .placeholder(R.drawable.ic_launcher_background).into(holder.product_image_R)

        holder.updel_img.setOnClickListener {
            var dialog = BottomSheetDialog(uploadProductScreen)
            dialog.setContentView(R.layout.dialog_delet_update)
            dialog.show()

            var firebaseDatabase = FirebaseDatabase.getInstance()
            var fireref = firebaseDatabase.reference

            var delete_item = dialog.findViewById<RelativeLayout>(R.id.delete_item)
            var update_item = dialog.findViewById<RelativeLayout>(R.id.update_item)

            delete_item!!.setOnClickListener {
                var dialogdelete = Dialog(uploadProductScreen)
                dialogdelete.setContentView(R.layout.dialog_delet)
                dialogdelete.show()

                var yes_delete = dialogdelete.findViewById<TextView>(R.id.yes_delete)
                var pname_txt = dialogdelete.findViewById<TextView>(R.id.pname_txt)
                var cancel_delete = dialogdelete.findViewById<TextView>(R.id.cancel_delete)

                pname_txt.text = l1[position].pname

                yes_delete.setOnClickListener {
                    fireref.child("Admin").child(l1[position].key).removeValue()
                    dialogdelete.dismiss()
                }

                cancel_delete.setOnClickListener {
                    dialogdelete.dismiss()
                }
                dialog.dismiss()
            }

            update_item!!.setOnClickListener {
                var intent = Intent(uploadProductScreen, Update_Screen::class.java)
                intent.putExtra("pname", l1[position].pname)
                intent.putExtra("pprice", l1[position].pprice)
                intent.putExtra("pcategory", l1[position].pcategory)
                intent.putExtra("pdiscription", l1[position].pdiscription)
                intent.putExtra("poffer", l1[position].poffer)
                intent.putExtra("pimg", l1[position].image)
                intent.putExtra("offer", l1[position].offer)
                intent.putExtra("key", l1[position].key)
                intent.putExtra("categoryID", l1[position].categoryId)
                intent.putExtra("position", position)
                uploadProductScreen.startActivity(intent)
                dialog.dismiss()

            }
        }
    }

    override fun getItemCount(): Int {
        return l1.size
    }
}

