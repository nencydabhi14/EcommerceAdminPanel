package com.example.ecommerceadminpanel.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.ecommerceadminpanel.ModelClass.ModelCategoy
import com.example.ecommerceadminpanel.ModelClass.ModelData
import com.example.ecommerceadminpanel.ModelClass.ModelOffer
import com.example.ecommerceadminpanel.Notification.Notification
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.databinding.ActivityProductUploadFormBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Product_Upload_Form : AppCompatActivity() {

    var downloadUri: Uri? = null
    var category: String? = null
    var categoryId: String? = null
    var categoryps: String? = null
    lateinit var binding: ActivityProductUploadFormBinding
    lateinit var uri: Uri
    var list1 = arrayListOf<ModelCategoy>()
    var stringList = arrayListOf<String>()
    var data = arrayOf<String>()

    lateinit var offer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductUploadFormBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this)
            .load("https://images.pexels.com/photos/8540989/pexels-photo-8540989.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
            .into(binding.backgroundImage)

        window.statusBarColor = ContextCompat.getColor(this, R.color.rama)

        offer = "No Available Offer"

        binding.available.setOnClickListener {
            binding.offerCare.isVisible = true
            binding.totalOffer.isVisible = true
            binding.textOffer.isVisible = true

            offer = binding.pofferEdt.text.toString()
        }

        binding.notAvailable.setOnClickListener {
            binding.offerCare.isVisible = false
            binding.totalOffer.isVisible = false
            binding.textOffer.isVisible = false

            offer = "No Available Offer"
            binding.pofferEdt.setText("")
        }

        binding.doneProduct.setOnClickListener {

            var pname = binding.pnameEdt.text.toString()
            var description = binding.pdiscriptionEdt.text.toString()

            if (offer.equals("No Available Offer")) {
                var m1 = ModelData(
                    binding.pnameEdt.text.toString(),
                    binding.ppriceEdt.text.toString(),
                    category!!,
                    binding.pdiscriptionEdt.text.toString(),
                    binding.pofferEdt.text.toString(),
                    downloadUri.toString(),
                    offer,
                    categoryId!!
                )
                InsertDataWithoutoffer(m1)
                finish()
            } else {
                var offer = binding.pofferEdt.text.toString()
                var price = binding.ppriceEdt.text.toString()

                var totaloffer = (offer.toInt() * price.toInt()) / 100

                var om1 = ModelOffer(
                    binding.pnameEdt.text.toString(),
                    binding.ppriceEdt.text.toString(),
                    category!!,
                    binding.pdiscriptionEdt.text.toString(),
                    binding.pofferEdt.text.toString(),
                    downloadUri.toString(),
                    categoryId!!,
                    totaloffer
                )
                insertofferData(om1)

                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                    binding.offerProgressBar.isVisible = true
                }, 2000)
            }

            Notification.exampleNotification(this, pname, description)

        }

        binding.pofferEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.totalOffer.setText(p0)
                Log.e("TAG", "onTextChanged: $p0")
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


        binding.backForm.setOnClickListener {
            onBackPressed()
        }

        binding.uploadImageBtn.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)

            binding.progressImage.isVisible = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            uri = data?.data!!
            binding.productImage.setImageURI(uri)
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
        }.addOnFailureListener { error ->
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "uploadImage: ${error.message}")
        }
    }

    fun InsertDataWithoutoffer(m1: ModelData) {

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var reference = firebaseDatabase.reference

        reference.child("Admin").push().setValue(m1)
    }

    fun insertofferData(om1: ModelOffer) {
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var reference = firebaseDatabase.reference

        reference.child("offer").push().setValue(om1)
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

                    Log.e("TAG", "onDataChangeCategory: ${mc1.category}")

                    binding.spinner.onItemSelectedListener =
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
                Toast.makeText(this@Product_Upload_Form, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setupSpinner(data: Array<String>) {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data)
        binding.spinner.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        readData()
    }
}