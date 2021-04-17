package com.at.plantsiveaggresive

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.at.plantsiveaggresive.databinding.ActivityMainBinding
import com.google.android.gms.common.internal.StringResourceValueReader
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import java.lang.reflect.Array
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var messages: kotlin.Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        Get the messages from the list
        messages = resources.getStringArray(R.array.tempe_high)
        val index = Random.nextInt(0, messages.size)

        binding.tvTitle.text = messages[index]

        FirebaseApp.initializeApp(this)
        database = Firebase.database.reference

        val databaseValuesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val liveData = snapshot.getValue<ServerData>()
                if (liveData != null) {
                    binding.tvDisplayTemp.text = liveData.currentTemp.toString() + " ℃"
                    binding.tvDisplayHumid.text = liveData.currentHumid.toString() + " %"

                    setPassiveAggressiveMessage()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("onCancelled", error.toString())
            }
        }

        database.child("liveData").addValueEventListener(databaseValuesListener)

        val tempSeekBar: SeekBar = binding.sbInsertTemp
        val humidSeekBar: SeekBar = binding.sbInsertHumid

        binding.tvDisplayTemp.text = tempSeekBar.progress.toString() + " ℃"
        binding.tvDisplayHumid.text = humidSeekBar.progress.toString() + " %"

        database.child("liveData").child("currentTemp").get().addOnSuccessListener {
            binding.tvSeekbarTemp.text = it.value.toString() + " ℃"
        }

        database.child("liveData").child("currentHumid").get().addOnSuccessListener {
            binding.tvSeekbarHumid.text = it.value.toString() + " %"
        }

        tempSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                binding.tvSeekbarTemp.text = progress.toString() + " ℃"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        humidSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                binding.tvSeekbarHumid.text = progress.toString() + " %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.btnSaveThreshold.setOnClickListener {
            val thresholdHumid: Int = humidSeekBar.progress
            Log.d("Threshold Humidity", thresholdHumid.toString())
            database.child("liveData").child("thresholdHumid").setValue(thresholdHumid)

            val thresholdTemp: Int = tempSeekBar.progress
            Log.d("Threshold Temperature", thresholdTemp.toString())
            database.child("liveData").child("thresholdTemp").setValue(thresholdTemp)
        }
    }

    private fun setPassiveAggressiveMessage() {
    }


    data class ServerData(
        val currentHumid: Int,
        val currentTemp: Int,
        val flag: String,
        val thresholdTemp: Int,
        val thresholdHumid: Int
    ) {
        constructor() : this(0, 0, "", 0, 0)
    }
}