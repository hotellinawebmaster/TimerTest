package com.example.timertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock.elapsedRealtime
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.Math.round



class MainActivity : AppCompatActivity() {

    lateinit var handler : Handler
    lateinit var runnable : Runnable
    lateinit var timeTextView : TextView
    var cicle : Int = 0
    var is_running :Boolean = false
    var paused : Boolean =false
    var delay : Long = 25
    lateinit var runBtn : Button
    lateinit var pauseBtn : Button
    lateinit var stopBtn : Button

    var time_operation_start : Long =0
    var time_total : Long = 0
    var time_diff : Long = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.time_xml) as TextView;
        runBtn = findViewById(R.id.run_xml) as Button;
        pauseBtn = findViewById(R.id.pause_xml) as Button;
        stopBtn = findViewById(R.id.stop_xml) as Button;

        cicle = 0
        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run() {
                operation()
                handler.postDelayed(this, delay)
            }
        }
        pauseBtn.isEnabled = false

    } // OnCreate END


    // http://www.unitarium.com/time-calculator
    fun operation(){
        time_total = elapsedRealtime() - time_operation_start
       // time_total = time_diff
        Log.d("TAG1", cicle.toString() +   "**time_total= " + time_total )
        timeTextView.text = (time_total / 10 ).toString()
        cicle++
    }



    fun runMethod(v : View) {
        if (! is_running and ! paused) {
            time_operation_start = elapsedRealtime()
            is_running = true
            Log.d("TAG1", "runMethod:NOT RUNNING" + this.toString() )
            handler.post(runnable)
            runBtn.isEnabled = false
            pauseBtn.isEnabled = true
        }
    }

    fun pauseMethod(v : View) {
        if (!paused) {               // it goes into a PAUSE state
            handler.removeCallbacks(runnable)
            paused = true
            time_total = elapsedRealtime() - time_operation_start
            //time_total = time_diff
            //Log.d("TAG1", "PAUSED NOW " + " time_diff=" + time_diff + " time_total= " + time_total)
            timeTextView.text = (time_total / 10).toString()
            pauseBtn.setText("RESUME")
        }
        else
        if ( paused and is_running ) { // it should restart from a PAUSED state
            Log.d("TAG1", "pauseMethod " +  " ***time_total="+ time_total  +" roundedTo=" + time_total )
            time_operation_start = elapsedRealtime() - ((time_total))
            paused = false
            Log.d("TAG1", "RESUMING FROM PAUSE " + cicle.toString() + " time_diff=" +  time_diff + "  time_total= " + time_total)
            handler.post(runnable)
            pauseBtn.setText("PAUSE")
        }
    }

    fun stopMethod(v : View) {
        handler.removeCallbacks(runnable)
        time_total = 0
        cicle = 0
        timeTextView.text = "0"
        Log.d("TAG1","stopMethod:this=" + this.toString())
        is_running = false
        paused = false
        runBtn.isEnabled = true
        pauseBtn.isEnabled = false
        pauseBtn.setText("PAUSE")
    }
}