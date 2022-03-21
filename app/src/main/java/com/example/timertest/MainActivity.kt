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
    var delay : Long = 1000 // update UI every 1 second
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

    // check for time on Intenet: resources
    // http://www.unitarium.com/time-calculator
    // https://www.daftlogic.com/projects-convert-milliseconds-to-hours-minutes-seconds.htm
    fun operation(){
        time_total = elapsedRealtime() - time_operation_start

        val hours =  ( time_total / 1000 / 60 / 60 )
        val minutes = (time_total / 1000 / 60) % 60
        val seconds = (time_total / 1000) % 60

       // Log.d("TAG1",   " time_initialTEST= " + time_total + "   "  + hours+ ":" + minutes + ": " + seconds + " secs")

        Log.d("TAG1", cicle.toString() +   " time_total= " + time_total + "   " +hours+ ":" + minutes + ":" + seconds)
        timeTextView.text =  hours.toString() + ":" + minutes.toString() + ":" + seconds.toString() + " secs"
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
            val hours =  ( time_total / 1000 / 60 / 60 )
            val minutes = (time_total / 1000 / 60) % 60
            val seconds = (time_total / 1000) % 60
            timeTextView.text =  hours.toString() + ":" + minutes.toString() + ":" + seconds.toString() + " secs"
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
        timeTextView.text = "0:0:0 secs"
        Log.d("TAG1","stopMethod:this=" + this.toString())
        is_running = false
        paused = false
        runBtn.isEnabled = true
        pauseBtn.isEnabled = false
        pauseBtn.setText("PAUSE")
    }
}