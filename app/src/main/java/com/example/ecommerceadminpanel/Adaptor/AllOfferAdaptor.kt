package com.example.ecommerceadminpanel.Adaptor

import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceadminpanel.Activity.Update_Screen
import com.example.ecommerceadminpanel.ModelClass.ModelOfferRead
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.Activity.Upload_Product_Screen
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase

class AllOfferAdaptor(
    val uploadProductScreen: Upload_Product_Screen,
    val listoffer: ArrayList<ModelOfferRead>
) : RecyclerView.Adapter<AllOfferAdaptor.ViewData>() {

    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var product_image_R = itemView.findViewById<ImageView>(R.id.product_image_R)
        var product_name_txt = itemView.findViewById<TextView>(R.id.product_name_txt)
        var product_price_txt = itemView.findViewById<TextView>(R.id.product_price_txt)
        var product_description_txt = itemView.findViewById<TextView>(R.id.product_description_txt)
        var product_offer_txt = itemView.findViewById<TextView>(R.id.product_offer_txt)
        var product_totaloffer_txt = itemView.findViewById<TextView>(R.id.product_totaloffer_txt)

        var updel_img = itemView.findViewById<ImageView>(R.id.updel_img)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
        var view = LayoutInflater.from(uploadProductScreen)
            .inflate(R.layout.alloffer_item_design, parent, false)

        return ViewData(view)
    }

    override fun onBindViewHolder(holder: ViewData, position: Int) {
        holder.product_name_txt.text = listoffer[position].pname
        holder.product_price_txt.text = listoffer[position].pprice
        holder.product_description_txt.text = listoffer[position].discription
        holder.product_offer_txt.text = listoffer[position].pofferEdt
        holder.product_totaloffer_txt.text = listoffer[position].totaloffer

        Glide.with(uploadProductScreen).load(listoffer[position].image)
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

                pname_txt.text = listoffer[position].pname

                yes_delete.setOnClickListener {
                    fireref.child("Admin").child(listoffer[position].key).removeValue()
                    dialogdelete.dismiss()
                }

                cancel_delete.setOnClickListener {
                    dialogdelete.dismiss()
                }
                dialog.dismiss()
            }

            update_item!!.setOnClickListener {
                var intent = Intent(uploadProductScreen, Update_Screen::class.java)
                intent.putExtra("pname", listoffer[position].pname)
                intent.putExtra("pprice", listoffer[position].pprice)
                intent.putExtra("pcategory", listoffer[position].category)
                intent.putExtra("pdiscription", listoffer[position].discription)
                intent.putExtra("poffer", listoffer[position].pofferEdt)
                intent.putExtra("pimg", listoffer[position].image)
                intent.putExtra("offer", listoffer[position].pofferEdt)
                intent.putExtra("key", listoffer[position].key)
                intent.putExtra("categoryID", listoffer[position].categoryId)
                intent.putExtra("position", position)
                uploadProductScreen.startActivity(intent)
                dialog.dismiss()

            }
        }
    }

    override fun getItemCount(): Int {
        return listoffer.size
    }
}