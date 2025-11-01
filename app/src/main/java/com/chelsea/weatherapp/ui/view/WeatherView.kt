package com.chelsea.panpanweatherapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chelsea.panpanweatherapp.ui.model.WeatherResponse
import com.chelsea.panpanweatherapp.ui.viewmodel.WeatherUiState
import com.chelsea.panpanweatherapp.ui.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherScreen(
    vm: WeatherViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            SearchBar(
                kota = vm.kota,
                onKotaChanged = { vm.kota = it },
                onSearchClicked = {
                    vm.getCuaca()
                    keyboardController?.hide()
                },
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    when (val s = vm.uiState) {
                        is WeatherUiState.Initial -> InitialState()
                        is WeatherUiState.Loading -> LoadingState()
                        is WeatherUiState.Error -> ErrorState(message = s.message)
                        is WeatherUiState.Success -> SuccessState(weather = s.weather)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    kota: String,
    onKotaChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = kota,
            onValueChange = onKotaChanged,
            label = { Text("Enter city name", color = Color.White.copy(alpha = 0.7f)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                disabledContainerColor = Color.White.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.White.copy(alpha = 0.7f),
                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClicked() }
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSearchClicked,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search Button", tint = Color.White)
            Text("Search", color = Color.White, modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@Composable
fun InitialState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Search for a city to get started",
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            Icons.Default.Warning,
            contentDescription = "Error Icon",
            tint = Color.Red,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Oops! Something went wrong",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = message,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun SuccessState(weather: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "location icon",
                tint = Color.White
            )
            Text(
                text = weather.name,
                fontSize = 18.sp,
                color = Color.White
            )
        }
        Text(
            text = formatDateTime(weather.dt),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Updated as of ${formatTime(weather.dt)}",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        MainWeatherInfo(weather)

        Spacer(modifier = Modifier.height(24.dp))
        WeatherDetailsGrid(weather)

        SunriseSunsetInfo(weather)
    }
}

@Composable
fun MainWeatherInfo(weather: WeatherResponse) {
    val weatherCondition = weather.weather.firstOrNull()?.main ?: "Clear"

    val pandaImageRes = when (weatherCondition.lowercase()) {
        "clouds" -> R.drawable.panda_awan
        "mist" -> R.drawable.panda_awan
        "smoke" -> R.drawable.panda_awan
        "haze" -> R.drawable.panda_awan
        "fog" -> R.drawable.panda_awan
        "thunderstorm" -> R.drawable.panda_hujan
        "drizzle" -> R.drawable.panda_hujan
        "rain" -> R.drawable.panda_hujan
        "snow" -> R.drawable.panda_hujan
        "tornado" -> R.drawable.panda_hujan
        "clear" -> R.drawable.panda_matahari
        else -> R.drawable.panda_matahari
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Left side: API icon, condition, temp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weather.weather.firstOrNull()?.iconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = weather.weather.firstOrNull()?.description,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = weatherCondition,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = "${weather.main.temp.toInt()}°C",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Image(
            painter = painterResource(id = pandaImageRes),
            contentDescription = "Panda $weatherCondition",
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun WeatherDetailsGrid(weather: WeatherResponse) {
    Column(modifier = Modifier.padding(vertical = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            DetailBox("HUMIDITY", "${weather.main.humidity}%")
            DetailBox("WIND", "${weather.wind.speed} km/h")
            DetailBox("FEELS LIKE", "${weather.main.feelsLike.toInt()}°")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            DetailBox("RAINFALL", "0.0 mm")
            DetailBox("PRESSURE", "${weather.main.pressure} hPa")
            DetailBox("CLOUDS", "${weather.clouds.all}%")
        }
    }
}

@Composable
fun RowScope.DetailBox(title: String, value: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = getDetailIcon(title)),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SunriseSunsetInfo(weather: WeatherResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise",
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
            Text("SUNRISE", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
            Text(
                formatTime(weather.sys.sunrise, weather.dt),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "Sunset",
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
            Text("SUNSET", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
            Text(
                formatTime(weather.sys.sunset, weather.dt),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

private fun getDetailIcon(title: String): Int {
    val key = title.uppercase(Locale.getDefault())
    return when {
        "HUMID" in key -> R.drawable.air
        "RAIN" in key -> R.drawable.payung
        "WIND" in key -> R.drawable.wind
        "PRESSURE" in key -> R.drawable.devices
        "FEELS" in key -> R.drawable.temp
        "CLOUD" in key -> R.drawable.cloud
        else -> R.drawable.air
    }
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault())
    val date = Date(timestamp * 1000)
    return sdf.format(date)
}

private fun formatTime(timestamp: Long, dt: Long? = null): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date = Date(timestamp * 1000)
    return sdf.format(date)
}
