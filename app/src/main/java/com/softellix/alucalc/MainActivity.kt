package com.softellix.alucalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.softellix.alucalc.navigation.AppNavigation
import com.softellix.alucalc.ui.theme.AluCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AluCalcTheme {
                AppNavigation()
            }
        }
    }
}