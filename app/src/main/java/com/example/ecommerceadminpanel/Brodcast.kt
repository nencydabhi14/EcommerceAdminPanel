package com.example.ecommerceadminpanel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast

class Brodcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {
        var status = isNetworkAvailable(context!!)

        if (status == true) {
            Log.e("TAG", "onReceive: net is On")
        } else {
            Toast.makeText(
                context,
                "Please On Your Mobile Data\n Restart the Application",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}