package com.bav.labdispositivosmovilesbav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController
import com.bav.labdispositivosmovilesbav.ui.theme.LABDispositivosMovilesBAVTheme
import com.bav.labdispositivosmovilesbav.utils.LanguageManager
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar LanguageManager
        LanguageManager.init(applicationContext)
        
        // Restaurar el idioma guardado
        val userPreferencesRepository = UserPreferencesRepository(applicationContext)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userPreferencesRepository.language.collect { language ->
                    LanguageManager.setLocale(this@MainActivity, language)
                }
            }
        }
        
        setContent {
            LABDispositivosMovilesBAVTheme {
                NavGraph()
            }
        }
    }
}

