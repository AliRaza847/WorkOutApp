package com.example.a7minuteworkoutapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.custom_dialog.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(){
    private var restTimer: CountDownTimer ? = null
    private var restProgressBar = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech?= null
    private var exerciseAdapter: ExerciseStatusAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            setCustomDialog()
        }
        exerciseList  = Constants.defaultExerciseList()
        setUpRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgressBar = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        if (tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    private fun setUpProgress(){
        progressBar.progress = restProgressBar
        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(p0: Long) {
                restProgressBar++
                progressBar.progress = 10-restProgressBar
                tvTimer.text = (10-restProgressBar).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }
        }.start()
    }
    private fun setUpExerciseProgress(){
        exerciseProgressBar.progress = exerciseProgress
        
        exerciseTimer = object : CountDownTimer(30000,1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                exerciseProgressBar.progress = 30-exerciseProgress
                tvExerciseTimer.text = (30-exerciseProgress).toString()
            }
            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()
                if (currentExercisePosition < 10) {
                    setUpRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }
    private fun setUpExerciseView(){
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        if (exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        setUpExerciseProgress()
    }
    private fun setUpRestView(){
        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE
        if (restTimer!=null){
            restTimer!!.cancel()
            restProgressBar = 0
        }
        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()
        setUpProgress()
    }
    private fun setupExerciseStatusRecyclerView() {

        // Defining a layout manager for the recycle view
        // Here we have used a LinearLayout Manager with horizontal scroll.
        rvExerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // As the adapter expects the exercises list and context so initialize it passing it.
        exerciseAdapter = ExerciseStatusAdapter(this, exerciseList!!)

        // Adapter class is attached to recycler view
        rvExerciseStatus.adapter = exerciseAdapter
    }
    private fun setCustomDialog(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.custom_dialog)
        customDialog.btn_yes.setOnClickListener {
            customDialog.dismiss()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        customDialog.btn_no.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}