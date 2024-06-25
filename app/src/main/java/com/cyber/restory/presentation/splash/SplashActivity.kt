package com.cyber.restory.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cyber.restory.databinding.ActivitySplashBinding
import com.cyber.restory.presentation.MainActivity

class SplashActivity : AppCompatActivity() {
    private val binding : ActivitySplashBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 스플래시 화면 관련 로직 구현
        setupSplashScreen()
    }

    private fun setupSplashScreen() {
        // 스플래시 화면 표시 후 메인 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DURATION)
    }

    companion object {
        const val SPLASH_DURATION = 2000L
    }
}