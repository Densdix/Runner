package com.leknos.runner;

import android.content.Intent;
import android.os.CountDownTimer;

import static com.leknos.runner.Utils.timeWithNull;

public class SleepTimer {
    private CountDownTimer countDownTimer;
    private MainActivity activity;
    private Calculation calculation;
    private long countDownInterval = 1000;
    private boolean isTimerRunning;
    public static final String TAG = "MySleepTimer";
    private int sleepHour;
    private int sleepMinute;


    public SleepTimer(final MainActivity activity, int sleepHour, int sleepMinute){
        this.activity = activity;
        this.sleepHour = sleepHour;
        this.sleepMinute = sleepMinute;
        this.calculation = new Calculation();
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(calculation.calcTimeToSleep(
                sleepHour, sleepMinute),
                countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);

                String timer = activity.getString(R.string.time_to_sleep, timeWithNull(hours), timeWithNull(minutes), timeWithNull(seconds));
                activity.changeSleepTimerText(timer);
            }

            @Override
            public void onFinish() {
                stopTimer();
                activity.changeButtonBackground(isTimerRunning);
                activity.changeSleepTimerText(activity.getString(R.string.empty_timer));
                Intent intentVibrate = new Intent(activity.getApplicationContext(),VibrateService.class);
                activity.startService(intentVibrate);

            }
        };
        countDownTimer.start();
        isTimerRunning = true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        isTimerRunning = false;
    }

    public void reloadTimer(){
        stopTimer();
        startTimer();
    }

    public void setSleepHour(int sleepHour) {
        this.sleepHour = sleepHour;
    }

    public void setSleepMinute(int sleepMinute) {
        this.sleepMinute = sleepMinute;
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }
}