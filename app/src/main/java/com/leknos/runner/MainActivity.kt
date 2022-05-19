package com.leknos.runner

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.leknos.runner.Utils.timeWithNull
import java.util.*


class MainActivity : AppCompatActivity() {

    private var timeToSleep: TextView? = null
    private var timeSleep: TextView? = null
    private var button: ImageButton? = null

    companion object{
        const val SLEEP_HOUR_TIME = "hour_time"
        const val SLEEP_MINUTE_TIME = "minute_time"
        const val TAG = "MainActivity"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private var sleepTimer: SleepTimer? = null

    private var sleepHour = 0
    private var sleepMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeToSleep = findViewById(R.id.time_to_sleep)
        timeSleep = findViewById(R.id.time_sleep)
        button = findViewById(R.id.button)

        sharedPreferences = getSharedPreferences("my_setting", MODE_PRIVATE)

        if (sharedPreferences.contains(SLEEP_HOUR_TIME) && sharedPreferences.contains(
                SLEEP_MINUTE_TIME
            )
        ) {
            sleepHour = sharedPreferences.getInt(SLEEP_HOUR_TIME, 23)
            sleepMinute = sharedPreferences.getInt(SLEEP_MINUTE_TIME, 15)
        } else {
            sleepHour = 23
            sleepMinute = 15
        }
        //init timer

        sleepTimer = SleepTimer(this, sleepHour, sleepMinute)

        //default by first run
        timeSleep?.text =
            getString(R.string.sleep_time, timeWithNull(sleepHour), timeWithNull(sleepMinute))

        timeSleep?.setOnClickListener {
            val dateAndTime: Calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    sleepHour = hourOfDay
                    sleepMinute = minute
                    sleepTimer?.setSleepHour(sleepHour)
                    sleepTimer?.setSleepMinute(sleepMinute)
                    timeSleep?.text = getString(R.string.sleep_time, timeWithNull(sleepHour), timeWithNull(sleepMinute))
                    Toast.makeText(this, "You successfully choose sleep time!", Toast.LENGTH_SHORT).show()
                    if(sleepTimer?.isTimerRunning == true){
                        sleepTimer?.reloadTimer()
                    }
                },
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE),
                true)
                .show()
        }

        button?.setOnClickListener {
            if (sleepTimer!!.isTimerRunning) {
                sleepTimer!!.stopTimer()
            } else {
                sleepTimer!!.startTimer()
            }
            changeButtonBackground(sleepTimer!!.isTimerRunning)
        }
        Log.d(TAG, "onCreate")
    }

    override fun onStop() {
        super.onStop()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(SLEEP_HOUR_TIME, sleepHour)
        editor.putInt(SLEEP_MINUTE_TIME, sleepMinute)
        editor.apply()
        Log.d(TAG, "onStop")
    }

    fun changeButtonBackground(isTimerRunning: Boolean) {
        if (isTimerRunning) {
            button!!.background = ContextCompat.getDrawable(this, R.drawable.ic_stop)
        } else {
            button!!.background = ContextCompat.getDrawable(this, R.drawable.ic_start)
        }
    }

    fun changeSleepTimerText(text: String?) {
        timeToSleep?.text = text
    }

}