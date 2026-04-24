package com.example.assignement2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat


data class Flashcard(val statement: String, val isHack: Boolean, val explanation: String)

class MainActivity : AppCompatActivity() {

    private val tag = "MythBusterLog"

    private val quizStatements = listOf(
        Flashcard("Charging your smartphone overnight will ruin its battery.", false, "Modern phones include circuits that stop charging when full. Just avoid extreme heat."),
        Flashcard("Putting a wet phone in a bowl of dry rice helps to remove moisture.", false, "Rice can block airflow and leave starch inside. Use silica gel packets or open-air drying instead."),
        Flashcard("Closing all background apps saves smartphone battery life.", false, "Android is optimized to manage background tasks. Re-launching apps uses more power than leaving them suspended."),
        Flashcard("You can speed up phone charging by switching to Airplane Mode.", true, "Airplane mode shuts off power-hungry antennae, allowing a slight increase in charging speed."),
        Flashcard("A lower-megapixel camera always takes worse photos than a high-megapixel one.", false, "Sensor size, lens quality, and software processing are often more important than megapixel count alone.")
    )

    private var currentIndex = 0
    private var correctAnswersScore = 0

    private lateinit var flipper: ViewFlipper
    private lateinit var txtQuestion: TextView
    private lateinit var txtFeedback: TextView
    private lateinit var btnNext: Button
    private lateinit var btnHack: Button
    private lateinit var btnMyth: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flipper = findViewById(R.id.viewFlipper)
        txtQuestion = findViewById(R.id.txtQuestion)
        txtFeedback = findViewById(R.id.txtFeedback)
        btnNext = findViewById(R.id.btnNext)
        btnHack = findViewById(R.id.btnHack)
        btnMyth = findViewById(R.id.btnMyth)

        Log.d(tag, "Application started. Currently on Welcome Screen (Index 0).")


        findViewById<Button>(R.id.btnStart).setOnClickListener {
            Log.d(tag, "Start button clicked. Navigating to Quiz screen (Index 1).")
            resetQuizState()
            flipper.displayedChild = 1
            loadNextFlashcard()
        }

        btnHack.setOnClickListener { processAnswerSubmission(true) }
        btnMyth.setOnClickListener { processAnswerSubmission(false) }

        btnNext.setOnClickListener {
            Log.d(tag, "Next button clicked. Incrementing index from $currentIndex.")
            currentIndex++
            if (currentIndex < quizStatements.size) {

                loadNextFlashcard()
            } else {

                transitionToScoreScreen()
            }
        }

        findViewById<Button>(R.id.btnReview).setOnClickListener {
            Log.d(tag, "Review button clicked. Entering Review mode (Index 3).")
            generateReviewScreenData() // Build the review text
            flipper.displayedChild = 3 // Show Review screen
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            Log.d(tag, "Reset button clicked. Returning to Welcome screen (Index 0).")
            flipper.displayedChild = 0 // Return to welcome
        }


        findViewById<Button>(R.id.btnReviewBack).setOnClickListener {
            Log.d(tag, "Review Back button clicked. Returning to Score screen (Index 2).")
            flipper.displayedChild = 2
        }
    }


    private fun resetQuizState() {
        Log.d(tag, "Resetting quiz progress and score.")
        currentIndex = 0
        correctAnswersScore = 0
    }


    private fun loadNextFlashcard() {
        Log.d(tag, "Loading flashcard at index: $currentIndex.")

        // Load the current statement
        txtQuestion.text = quizStatements[currentIndex].statement

        // Set initial UI state for a new question
        txtFeedback.visibility = View.GONE
        btnNext.visibility = View.INVISIBLE
        setAnswerButtonsEnabled(true)
    }

    private fun processAnswerSubmission(userSelection: Boolean) {
        setAnswerButtonsEnabled(false) // Prevent multiple submissions

        val currentFlashcard = quizStatements[currentIndex]
        txtFeedback.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE // Allow user to proceed

        if (userSelection == currentFlashcard.isHack) {
            // Correct answer
            correctAnswersScore++
            Log.d(tag, "Correct! Current total score is now $correctAnswersScore.")
            txtFeedback.setText(R.string.correct_feedback)
            txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.correctFeedback))
        } else {
            // Incorrect answer
            Log.d(tag, "Incorrect. Current total score remains $correctAnswersScore.")
            txtFeedback.setText(R.string.incorrect_feedback)
            txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.incorrectFeedback))
        }
    }


    private fun transitionToScoreScreen() {
        Log.d(tag, "All statements complete. Finalizing score of $correctAnswersScore.")
        flipper.displayedChild = 2 // Transition to Score screen

        val finalScoreText = "$correctAnswersScore / ${quizStatements.size}"
        findViewById<TextView>(R.id.txtScore).text = finalScoreText

        // Generate personalized feedback based on percentage
        val scorePercent = (correctAnswersScore.toDouble() / quizStatements.size.toDouble()) * 100
        val personalFeedbackTxt = findViewById<TextView>(R.id.txtPersonalFeedback)

        if (scorePercent >= 80) {
            personalFeedbackTxt.setText(R.string.feedback_high)
        } else if (scorePercent >= 50) {
            personalFeedbackTxt.setText(R.string.feedback_medium)
        } else {
            personalFeedbackTxt.setText(R.string.feedback_low)
        }
    }


    private fun generateReviewScreenData() {
        Log.d(tag, "Building review summary.")
        val reviewTxtView = findViewById<TextView>(R.id.txtReview)


        val reviewBuilder = StringBuilder()
        for (i in quizStatements.indices) {
            val fc = quizStatements[i]
            reviewBuilder.append("${i + 1}. ${fc.statement}\n")
            reviewBuilder.append("Answer: ${if (fc.isHack) "Hack (True)" else "Myth (False)"}\n")
            reviewBuilder.append("Explanation: ${fc.explanation}\n\n")
        }
        reviewTxtView.text = reviewBuilder.toString()
    }


    private fun setAnswerButtonsEnabled(isEnabled: Boolean) {
        btnHack.isEnabled = isEnabled
        btnMyth.isEnabled = isEnabled
    }
}

/*
Website:Chatgpt
author: Chatgpt (Open Ai)
date published : November 30, 2022
date accessed : 23 april 2026
url:https://chatgpt.com/
 */

/*website: geeksforgeeks
author: GeeksforGeeks
date published: 23 july 2025
date accessed : 23 april 2026
url: https://www.geeksforgeeks.org/kotlin/viewflipper-in-android-with-kotlin/
*/

/* website: w3schools
author: w3schools
 url: https://developer.android.com/codelabs/basic-android-kotlin-compose-add-images#0
 date accessed : 23 april 2026*/