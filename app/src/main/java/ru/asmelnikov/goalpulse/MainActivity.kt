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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.asmelnikov.domain.repository.FootballRepository
import ru.asmelnikov.goalpulse.ui.theme.GoalPulseTheme
import org.koin.android.ext.android.get
import ru.asmelnikov.utils.Resource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val repository: FootballRepository = get()

        lifecycleScope.launch {
            when (val result = repository.getAllCompetitions()) {
                is Resource.Success -> {
                    Log.e("TEST", result.data?.joinToString() ?: "empty")
                }

                is Resource.Error -> {
                    Log.e(
                        "TEST",
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