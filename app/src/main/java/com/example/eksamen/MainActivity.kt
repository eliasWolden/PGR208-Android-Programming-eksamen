package com.example.eksamen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eksamen.ui.theme.EksamenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EksamenTheme {
                ProductRepository.initialize(this)
                ProductRepository.getDatabaseInstance()
                Nav()
            }
        }
    }
}
