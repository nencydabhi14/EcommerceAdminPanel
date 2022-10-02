package com.example.ecommerceadminpanel.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceadminpanel.Adaptor.AllOfferAdaptor
import com.example.ecommerceadminpanel.Adaptor.AllProductAdaptor
import com.example.ecommerceadminpanel.ModelClass.ModelCategoy
import com.example.ecommerceadminpanel.ModelClass.ModelDataRead
import com.example.ecommerceadminpanel.ModelClass.ModelOfferRead
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.databinding.ActivityUploadProductScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Upload_Product_Screen : AppCompatActivity() {

    lateinit var binding: ActivityUploadProductScreenBinding
    var list = arrayListOf<ModelDataRead>()
    var listoffer = arrayListOf<ModelOfferRead>()
    var list1 = arrayListOf<ModelCategoy>()
    var stringList = arrayListOf<String>()
    var data = arrayOf<String>("select")

    var pcategory: String? = null
    var categoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadProductScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.rama)

        binding.uploadProduct.setOnClickListener {
            var intent = Intent(this, Product_Upload_Form::class.java)
            startActivity(intent)
        }

        binding.categoryEnter.setOnClickListener {
            var dialogupdate = Dialog(this)
            dialogupdate.setContentView(R.layout.dialog_category)
            dialogupdate.show()

            var id_category_Edt = dialogupdate.findViewById<EditText>(R.id.id_category_Edt)
            var category_Edt = dialogupdate.findViewById<EditText>(R.id.category_Edt)
            var done_btn_category = dialogupdate.findViewById<TextView>(R.id.done_btn_category)
            var cancel_btn_category = dialogupdate.findViewById<TextView>(R.id.cancel_btn_category)
            var show_category_list = dialogupdate.findViewById<TextView>(R.id.show_category_list)

            done_btn_category.setOnClickListener {
                if (id_category_Edt.text.toString().equals("") && category_Edt.text.toString().equals("")) {
                    id_category_Edt.setError("Enter Id")
                    category_Edt.setError("Enter category")
                }
                else {
                    var mc1 = ModelCategoy(
                        id_category_Edt.text.toString(),
                        category_Edt.text.toString()
                    )
                    list1.clear()
                    stringList.clear()

                    InsertData(mc1)

                    Toast.makeText(
                        this,
                        "your categoryId is ${id_category_Edt.text}\n your Category is ${category_Edt.text}\n SuccessFully Created",
                        Toast.LENGTH_LONG
                    ).show()
//
                    dialogupdate.dismiss()
                }


//                ReadCategotyID(id_category_Edt.text.toString())
//                if () {
//                    Toast.makeText(
//                        this@Upload_Product_Screen,
//                        "This Category and CategoryID is already exist",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            }

            cancel_btn_category.setOnClickListener {
                dialogupdate.dismiss()
            }

            show_category_list.setOnClickListener {
                var intent = Intent(this, Show_All_Category_List::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onStart() {
        super.onStart()

        ReadData()
        ReadOffer()
    }

    fun ReadData() {

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var reference = firebaseDatabase.reference

        reference.child("Admin").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (x in snapshot.children) {
                    var pname = x.child("pname").getValue().toString()
                    var pprice = x.child("pprice").getValue().toString()
                    pcategory = x.child("pcategory").getValue().toString()
                    var pdiscription = x.child("pdiscription").getValue().toString()
                    var poffer = x.child("poffer").getValue().toString()
                    var image = x.child("image").getValue().toString()
                    var offer = x.child("offer").getValue().toString()
                    categoryId = x.child("categoryId").getValue().toString()
                    var key = x.key.toString()

                    var m1 = ModelDataRead(
                        pname,
                        pprice,
                        pcategory!!,
                        pdiscription,
                        poffer,
                        image,
                        key, offer,
                        categoryId!!
                    )
                    list.add(m1)

                    Log.e(
                        "TAG",
                        "onDataChange========================================================: $pname $pprice $pcategory $pdiscription $poffer $key $image ,$offer"
                    )
                }
                binding.loadingRecycler.isVisible = false

                binding.allproductTxt.isVisible = true

                if (list.size.equals(0)){
                    binding.notYetAvailble.isVisible = true
                    binding.allproductTxt.isVisible = false
                }else{
                    binding.notYetAvailble.isVisible = false
                    binding.allproductTxt.isVisible = true
                }

                binding.loadingRecycler.isVisible = false

                setUpRV(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "onCancelled: error")
            }
        })

    }


    fun ReadOffer() {
        listoffer.clear()

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var reference = firebaseDatabase.reference

        reference.child("offer").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (x in snapshot.children) {
                    var pname = x.child("pname").getValue().toString()
                    var pprice = x.child("pprice").getValue().toString()
                    pcategory = x.child("category").getValue().toString()
                    var pdiscription = x.child("discription").getValue().toString()
                    var poffer = x.child("pofferEdt").getValue().toString()
                    var image = x.child("image").getValue().toString()
                    categoryId = x.child("categoryId").getValue().toString()
                    var key = x.key.toString()
                    val totaloffer = x.child("totaloffer").getValue().toString()

                    var m1 = ModelOfferRead(
                        pname,
                        pprice,
                        pcategory!!,
                        pdiscription,
                        poffer,
                        image,
                        key,
                        categoryId!!,totaloffer
                    )

                    listoffer.add(m1)

                    Log.e(
                        "TAG",
                        "onDataChange====: $pname $pprice $pcategory $pdiscription $poffer $key $image"
                    )
                }
                binding.loadingRecycler.isVisible = false

                binding.offerTxt.isVisible = true

                if (listoffer.size.equals(0)){
                    binding.notYetAvailble.isVisible = true
                    binding.offerTxt.isVisible = false
                }
                else{
                    binding.notYetAvailble.isVisible = false
                    binding.offerTxt.isVisible = true
                }

                setUpoffer(listoffer)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "onCancelled: error")
            }
        })

    }


    fun setUpRV(l1: ArrayList<ModelDataRead>) {

        var allProductAdaptor = AllProductAdaptor(this@Upload_Product_Screen, l1)
        var lm = LinearLayoutManager(this)
        binding.recyclerAllProduct.layoutManager = lm
        binding.recyclerAllProduct.adapter = allProductAdaptor
    }

    fun setUpoffer(listoffer: ArrayList<ModelOfferRead>) {
        var allOfferAdaptor = AllOfferAdaptor(this@Upload_Product_Screen, listoffer)
        var lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerAllOffers.layoutManager = lm
        binding.recyclerAllOffers.adapter = allOfferAdaptor
    }

    fun InsertData(m1: ModelCategoy) {

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var reference = firebaseDatabase.reference

        reference.child("category").push().setValue(m1)
    }
}