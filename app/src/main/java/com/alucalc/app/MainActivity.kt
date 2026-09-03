package com.alucalc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alucalc.app.data.remote.TokenStore
import com.alucalc.app.navigation.AluNavGraph
import com.alucalc.app.ui.theme.AluCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenStore = TokenStore(applicationContext)
        setContent {
            AluCalcTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AluNavGraph(tokenStore = tokenStore)
                }
            }
        }
    }
}
