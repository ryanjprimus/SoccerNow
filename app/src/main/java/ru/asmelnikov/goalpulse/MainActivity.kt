package ru.asmelnikov.goalpulse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.asmelnikov.goalpulse.ui.theme.GoalPulseTheme
import org.koin.android.ext.android.get
import ru.asmelnikov.utils.Resource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val repository: ru.asmelnikov.domain.repository.FootballRepository = get()

        lifecycleScope.launch {
            when (val result = repository.getAllCompetitionsFromRemoteToLocal()) {
                is Resource.Success -> {
                    Log.e("LOADING_DATA", "SUCCESS")
                }

                is Resource.Error -> {
                    Log.e(
                        "LOADING_DATA",
                        "ERROR -${result.httpErrors} - ${result.httpErrors?.code} - ${result.httpErrors?.errorMessage}"
                    )
                }
            }
        }

        super.onCreate(savedInstanceState)
        setContent {
            GoalPulseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    LaunchedEffect(key1 = Unit) {
                        lifecycleScope.launch {
                            repository.getAllCompetitionsFlowFromLocal().collect { comps ->
                                comps.forEach { comp ->
                                    Log.e("RESULT FROM LOCAL", comp.toString())
                                }
                            }
                        }
                    }

                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GoalPulseTheme {
        Greeting("Android")
    }
}