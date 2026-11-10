package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.AdhkariTheme
import com.example.ui.viewmodel.AdhkariViewModel
import com.example.ui.viewmodel.ThabatViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: AdhkariViewModel by viewModels()
    private val thabatViewModel: ThabatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settings.collectAsState()
            AdhkariTheme(darkTheme = settings.isDarkMode ?: isSystemInDarkTheme()) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(Modifier.fillMaxSize()) {
                        AppNavigation(rememberNavController(), viewModel, thabatViewModel)
                    }
                }
            }
        }
    }
}
