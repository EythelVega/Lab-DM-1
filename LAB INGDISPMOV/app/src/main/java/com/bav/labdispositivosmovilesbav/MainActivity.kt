package com.bav.labdispositivosmovilesbav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bav.labdispositivosmovilesbav.ui.theme.LABDispositivosMovilesBAVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            LABDispositivosMovilesBAVTheme {
                var showSuccess by remember { mutableStateOf(false) }
                
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (showSuccess) {
                        SuccessScreen {
                            showSuccess = false
                        }
                    } else {
                        RegisterScreen(
                            onRegisterSuccess = {
                                showSuccess = true
                            }
                        )
                    }
                }
            }
        }
    }
}