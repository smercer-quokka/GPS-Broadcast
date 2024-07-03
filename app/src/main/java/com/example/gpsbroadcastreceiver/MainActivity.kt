package com.example.gpsbroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GPSBroadcastReceiver"
    }

    private lateinit var tvBroadcastData: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the TextView
        tvBroadcastData = findViewById(R.id.tvBroadcastData)

        registerGpsBroadcasts(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerGpsBroadcasts(context: Context) {
        val intent: Intent? = context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent == null) {
                    Log.d(TAG, "intent received is null")
                    return
                }
                if ("newPosition" == intent.action) {
                    Log.d(TAG, "intent=$intent")
                    val bundle = intent.extras
                    if (bundle == null) {
                        Log.d(TAG, "bundle is null")
                        return
                    }
                    val keys = bundle.keySet()
                    val stringBuilder = StringBuilder()
                    for (key in keys) {
                        val value = bundle[key]
                        if (value != null) {
                            Log.d(TAG, "key=$key, value=$value")
                            stringBuilder.append("key=$key, value=$value\n")
                        } else {
                            Log.d(TAG, "key=$key, value is null")
                            stringBuilder.append("key=$key, value is null\n")
                        }
                    }
                    // Update the TextView with the broadcast data
                    tvBroadcastData.text = stringBuilder.toString()
                }
            }
        }, IntentFilter("newPosition"), RECEIVER_EXPORTED)
        Log.d(TAG, "registered receiver for newPosition")
        if (intent != null) {
            Log.d(TAG, "sticky intent=$intent")
        }
    }
}