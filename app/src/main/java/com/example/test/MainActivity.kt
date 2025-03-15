package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {
    private lateinit var appName:TextView
    private lateinit var lottie:LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appName=findViewById(R.id.appName)
        lottie =findViewById(R.id.lottie)


     val intent = Intent(this, secondactivity::class.java)
        appName.animate()
            .translationY(-1000f) // Moves up
            .setDuration(2300) // Duration in milliseconds
            .setStartDelay(0) // Start immediately
            .withEndAction {
                // After the vertical animation ends, animate appName to the left
                appName.animate()
                    .translationX(-1000f) // Moves left
                    .setDuration(3000)
                    .setStartDelay(0)
                    .start()
            }
            .start()

        lottie.animate().translationX(2000f).setDuration(2000).setStartDelay(2900)

        android.os.Handler().postDelayed({
            startActivity(intent)
            finish()  // اختياري: لإغلاق النشاط الحالي بعد التحويل
        }, 4000) // 5000 ميلي ثانية = 5 ثواني


    }
}