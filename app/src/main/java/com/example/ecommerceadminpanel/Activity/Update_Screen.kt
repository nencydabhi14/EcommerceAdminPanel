package com.example.ecommerceadminpanel.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.ecommerceadminpanel.ModelClass.ModelCategoy
import com.example.ecommerceadminpanel.ModelClass.ModelDataRead
import com.example.ecommerceadminpanel.ModelClass.ModelOfferRead
import com.example.ecommerceadminpanel.ModelClass.ModelUpdateo
import com.example.ecommerceadminpanel.ModelClass.ModelUpdateClass
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.databinding.ActivityUpdateScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Update_Screen : AppCompatActivity() {

    lateinit var key: String
    lateinit var binding: ActivityUpdateScreenBinding

    var list = arrayListOf<ModelDataRead>()
    var listo = arrayListOf<ModelOfferRead>()

    lateinit var uri: Uri
    var downloadUri: Uri? = null
    var list1 = arrayListOf<ModelCategoy>()
    var stringList = arrayListOf<String>()
    var data = arrayOf<String>()
    var category: String? = null
    var categoryId: String? = null
    var categoryps: String? = null
    var catID: String? = null
    var id1: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.rama)

        var pname = intent.getStringExtra("pname")
        var pprice = intent.getStringExtra("pprice")
        var pcategory = intent.getStringExtra("pcategory")
        var pdiscription = intent.getStringExtra("pdiscription")
        var poffer = intent.getStringExtra("poffer")
        var pimg = intent.getStringExtra("pimg")
        key = intent.getStringExtra("key").toString()
        var offer = intent.getStringExtra("offer")
        var position = intent.getIntExtra("position", 0)
        categoryId = intent.getStringExtra("categoryID")

//        Toast.makeText(this, "$categoryId", Toast.LENGTH_SHORT).show()


        id1 = categoryId!!.toInt()

        binding.pnameEdtUpdate.setText(pname)
        binding.ppriceEdtUpdate.setText(pprice)
        binding.pdiscriptionEdtUpdate.setText(pdiscription)
        binding.pofferEdtUpdate.setText(poffer)

        if (downloadUri == null) {
            downloadUri = pimg!!.toUri()
        }

        if (offer == "No Available Offer") {
            binding.notAvailable.isChecked = true

        } else {
            binding.available.isChecked = true

            binding.offerCare.isVisible = true
            binding.pofferEdtUpdate.setText(poffer)
        }


        binding.available.setOnClickListener {
            binding.offerCare.isVisible = true

            offer = binding.pofferEdtUpdate.text.toString()
        }

        binding.notAvailable.setOnClickListener {
            binding.offerCare.isVisible = false

            offer = "No Available Offer"
            binding.pofferEdtUpdate.setText("")
        }

        Glide.with(this).load(pimg).into(binding.productImageUpdate)

        Glide.with(this)
            .load("https://images.pexels.com/photos/8540989/pexels-photo-8540989.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
            .into(binding.backgroundImage)

        binding.uploadImageBtnUpdate.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)

            binding.progressImage.isVisible = true
        }

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var fireref = firebaseDatabase.reference

        binding.doneProductUpdate.setOnClickListener {

            if (offer.equals("No Available Offer")) {
                var m1 = ModelUpdateClass(
                    binding.pnameEdtUpdate.text.toString(),
                    binding.ppriceEdtUpdate.text.toString(),
                    pcategory.toString(),
                    binding.pdiscriptionEdtUpdate.text.toString(),
                    binding.pofferEdtUpdate.text.toString(), downloadUri.toString(), offer!!
                )

                fireref.child("Admin").child("${key}").setValue(m1)
                list.clear()

                finish()
            } else {
                var m1 = ModelUpdateo(
                    binding.pnameEdtUpdate.text.toString(),
                    binding.ppriceEdtUpdate.text.toString(),
                    pcategory.toString(),
                    binding.pdiscriptionEdtUpdate.text.toString(),
                    binding.pofferEdtUpdate.text.toString(), downloadUri.toString()
                )

                fireref.child("offer").child("${key}").setValue(m1)
                listo.clear()

                fireref.child("Admin").child(key).removeValue()
                list.clear()

                finish()
            }


        }

        binding.backUpdate.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            uri = data?.data!!
            binding.productImageUpdate.setImageURI(uri)

            uploadImage()
        }
    }

    fun uploadImage() {

        var file = File(uri.toString())
        var storage = Firebase.storage

        var storageReference = storage.reference.child(file.name)
        var UploadTask = storageReference.putFile(uri!!)

        UploadTask.addOnSuccessListener { res ->

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                if (uri != null) {
                    downloadUri = uri
                }

            }

            binding.progressImage.isVisible = false
            Toast.makeText(this, "SuccessFully Uploaded", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener { error ->
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "uploadImage: ${error.message}")
            }
    }

    private fun readData() {
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseReference = firebaseDatabase.reference

        databaseReference.child("category").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list1.clear()
                stringList.clear()
                data = emptyArray()
                for (x in snapshot.children) {

                    var id = x.child("id").getValue().toString()
                    category = x.child("category").getValue().toString()

                    var mc1 = ModelCategoy(id, category!!)

                    list1.add(mc1)
                    data += x.child("category").getValue().toString()

                    setupSpinner(data)
                    binding.spinnerUpdate.setSelection(id1!! - 1)

                    binding.spinnerUpdate.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                            override fun onItemSelected(
                                parent: AdapterView<*>?, view: View?, position: Int, id: Long
                            ) {
                                category = list1[position].category
                                categoryId = list1[position].id
                                categoryps = position.toString()
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Update_Screen, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun setupSpinner(data: Array<String>) {
        val arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            this.data
        )
        binding.spinnerUpdate.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
//        binding.spinnerUpdate.setSelection(id1!! - 1)
    }

    override fun onResume() {
        super.onResume()

        readData()
    }
}