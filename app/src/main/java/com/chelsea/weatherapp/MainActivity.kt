package com.chelsea.panpanweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chelsea.panpanweatherapp.data.container.AppKontainer
import com.chelsea.panpanweatherapp.ui.view.WeatherScreen
import com.chelsea.panpanweatherapp.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: WeatherViewModel = viewModel(
                initializer = {
                    WeatherViewModel(AppKontainer.repo)
                }
            )

            WeatherScreen(vm = vm)
        }
    }
}
