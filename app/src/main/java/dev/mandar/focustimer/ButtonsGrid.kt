package dev.mandar.focustimer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import java.util.concurrent.TimeUnit

// Define a list of pairs where the first value is the time and the second value is the unit.
val timeButtons = listOf(
  Pair(5, TimeUnit.SECONDS),
  Pair(30, TimeUnit.SECONDS),
  Pair(5, TimeUnit.MINUTES),
  Pair(10, TimeUnit.MINUTES),
  Pair(15, TimeUnit.MINUTES),
  Pair(30, TimeUnit.MINUTES),
  Pair(45, TimeUnit.MINUTES),
)

typealias StartTimer = (Long) -> Unit

// In your ButtonsGrid composable
@Composable
fun ButtonsGrid(startTimer: StartTimer) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    timeButtons.forEach { (value, unit) ->
      val timeInMillis = unit.toMillis(value.toLong())
      Button(
        onClick = { startTimer(timeInMillis) },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        contentPadding = PaddingValues(all = 8.dp)
      ) {
        // Display the time on the button
        Text(text = formatTime(value, unit))
      }
    }
  }
}

// Helper function to format the time for the button text
fun formatTime(value: Int, unit: TimeUnit): String {
  return when (unit) {
    TimeUnit.MINUTES -> "$value:00"
    TimeUnit.SECONDS -> "0:${value.toString().padStart(2, '0')}"
    else -> "$value" // Default, for other cases
  }
}
