package com.example.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, secondactivity::class.java)
        // supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.Actionbar)))

        android.os.Handler().postDelayed({
            startActivity(intent)
            finish()  // اختياري: لإغلاق النشاط الحالي بعد التحويل
        }, 2000) // 5000 ميلي ثانية = 5 ثواني


    }
}