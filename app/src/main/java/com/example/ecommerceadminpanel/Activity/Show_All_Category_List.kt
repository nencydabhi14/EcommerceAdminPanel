package com.example.ecommerceadminpanel.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceadminpanel.Adaptor.ShowCategoryAdaptor
import com.example.ecommerceadminpanel.ModelClass.ModelCategoryDelete
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.databinding.ActivityShowAllCategoryListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Show_All_Category_List : AppCompatActivity() {

   lateinit var binding: ActivityShowAllCategoryListBinding
    var list1 = arrayListOf<ModelCategoryDelete>()
    var stringList = arrayListOf<String>()
    var data = arrayOf<String>()
    lateinit var category : String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShowAllCategoryListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.rama)


        binding.backCategory.setOnClickListener {
            onBackPressed()
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
                    var key = x.key.toString()

                    var mc1 = ModelCategoryDelete(id, category,key)

                    list1.add(mc1)
                    data += x.child("category").getValue().toString()

                    setUpCategory(list1)

                    Log.e("TAG", "onDataChangeCategory: ${mc1.category}")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Show_All_Category_List, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setUpCategory(list1: ArrayList<ModelCategoryDelete>) {
        var adaptor = ShowCategoryAdaptor(this,list1)
        var lm = GridLayoutManager(this,2)
        binding.allCategoriesRecycler.adapter = adaptor
        binding.allCategoriesRecycler.layoutManager = lm
    }

    override fun onResume() {
        super.onResume()
        readData()
    }
}