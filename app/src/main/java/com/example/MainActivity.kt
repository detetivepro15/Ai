package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.PacificationScreen
import com.example.ui.PacificationViewModel
import com.example.ui.theme.PaciFacilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaciFacilTheme {
                val viewModel: PacificationViewModel = viewModel()
                PacificationScreen(viewModel = viewModel)
            }
        }
    }
}
