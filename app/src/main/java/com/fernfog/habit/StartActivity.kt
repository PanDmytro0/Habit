package com.fernfog.habit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        findViewById<MaterialButton>(R.id.startButton).setOnClickListener {
            finish()
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.privacyPolicyButton).setOnClickListener {
            //startActivity(Intent(this@StartActivity, MainActivity::class.java))
        }
    }
}