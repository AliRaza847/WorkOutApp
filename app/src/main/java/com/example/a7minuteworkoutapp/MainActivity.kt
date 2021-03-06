package com.example.a7minuteworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ll_start.setOnClickListener {
            Toast.makeText(this, "Start is click", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
        llBMI.setOnClickListener {
            val intent = Intent(this,ActivityBMI::class.java)
            startActivity(intent)
        }
        llCalculateHistory.setOnClickListener {
            val intent = Intent(this,HistroyActivity::class.java)
            startActivity(intent)
        }
    }
}