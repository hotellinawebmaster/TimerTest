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

// states of the stopwatch: stopped, running, paused

class MainActivity : AppCompatActivity() {

    lateinit var handler : Handler
    lateinit var runnable : Runnable
    lateinit var timeTextView : TextView
    //var cicle : Int = 0
    var is_running :Boolean = false
    var paused : Boolean =false
    var delay : Long = 1000 // update UI every 1 second
    lateinit var runBtn : Button
    lateinit var stopBtn : Button

    var time_operation_start : Long =0
    var time_total : Long = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.time_xml) as TextView;
        runBtn = findViewById(R.id.run_xml) as Button;
        stopBtn = findViewById(R.id.stop_xml) as Button;

        //cicle = 0
        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run() {
                operation()
                handler.postDelayed(this, delay)
            }
        }
    } // OnCreate END

    // check for time on Intenet: resources
    // http://www.unitarium.com/time-calculator
    // https://www.daftlogic.com/projects-convert-milliseconds-to-hours-minutes-seconds.htm
    fun operation(){
        time_total = elapsedRealtime() - time_operation_start

        val hours =  ( time_total / 1000 / 60 / 60 )
        val minutes = (time_total / 1000 / 60) % 60
        val seconds = (time_total / 1000) % 60
        Log.d("TAG1",    " time_total= " + time_total + "   " +hours+ ":" + minutes + ":" + seconds)

        timeTextView.text =  hours.toString() + ":" + minutes.toString() + ":" + seconds.toString() + " secs"
        //cicle++ activate to DEBUG
    }

    fun runMethod(v : View) {
        if (! is_running) { // it was stopped and it is now going to run
            time_operation_start = elapsedRealtime()
            is_running = true
            Log.d("TAG1", "runMethod:NOT RUNNING" + this.toString())
            handler.post(runnable)
            runBtn.text = "PAUSE"
        }
        else {
            if (!paused) {  // it goes into a PAUSE state now
                handler.removeCallbacks(runnable)
                paused = true
                time_total = elapsedRealtime() - time_operation_start
                val hours = (time_total / 1000 / 60 / 60)
                val minutes = (time_total / 1000 / 60) % 60
                val seconds = (time_total / 1000) % 60
                Log.d("TAG1","PAUSED AT cycle" + "  Time total in ms= " + time_total  )
                timeTextView.text = hours.toString() + ":" + minutes.toString() + ":" + seconds.toString() + " secs"
                runBtn.setText("RESUME")
            } else
                 { // it was paused, so going to resume now
                    time_operation_start = elapsedRealtime() - ((time_total))
                    paused = false
                    Log.d("TAG1","RESUMING FROM PAUSE cycle --- Time total in ms= " + time_total  )
                    handler.post(runnable)
                    runBtn.setText("PAUSE")
                }
        }
    }



    fun stopMethod(v : View) {
        handler.removeCallbacks(runnable)
        time_total = 0
        //cicle = 0
        timeTextView.text = "0:0:0 secs"
        Log.d("TAG1","stopMethod:this=" + this.toString())
        is_running = false
        paused = false
        runBtn.setText("RUN")
    }
}