package com.tausif2606.timefighter.tausif2606

import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT

class MainActivity : AppCompatActivity() {

    /**Things i did not make here
     * 1. menu button
     * 2. about dialog
     * */

    internal var score = 0;

    internal lateinit var tapmeButton: Button
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView


    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 6000

    companion object {
        private val TAG = MainActivity::class.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is: $score")
        tapmeButton = findViewById(R.id.button)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)



        tapmeButton.setOnClickListener{ view->
            incrementScore()

        }

        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }



    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }




    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    private fun resetGame(){
        score = 0

        gameScoreTextView.text = getString(R.string.your_score, score)

        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.time_left, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left, timeLeft)

            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false

    }

    private fun restoreGame(){
        gameScoreTextView.text = getString(R.string.your_score,score)

        val restoredTime = timeLeftOnTimer /1000
        timeLeftTextView.text = getString(R.string.time_left, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished

                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }
    private fun incrementScore() {
        if(!gameStarted)
        {
            startGame()
        }

        score +=1
        val newScore = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore
    }

    private fun startGame(){
        countDownTimer.start()
        gameStarted = true

    }

    private fun endGame(){
        Toast.makeText(this,getString(R.string.gameOverMessage,score), LENGTH_SHORT).show()
        Log.d("MainActivity", getString(R.string.gameOverMessage,score))
        resetGame()

    }


}