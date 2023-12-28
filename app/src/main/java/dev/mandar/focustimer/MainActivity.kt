package dev.mandar.focustimer

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource

class MainActivity : ComponentActivity() {
  private lateinit var timerViewModel: TimerViewModel
  private lateinit var vibrator: Vibrator
  private lateinit var ringtone: Ringtone
  @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Initialize the vibrator and media player services
    vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    ringtone = RingtoneManager.getRingtone(applicationContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

    // Provide the ViewModel with the required services
    val factory = object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
          @Suppress("UNCHECKED_CAST")
          return TimerViewModel(application, vibrator, ringtone) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
      }
    }

    timerViewModel = ViewModelProvider(this, factory).get(TimerViewModel::class.java)

    // Rest of the onCreate implementation
    setContent {
      TimerApp(timerViewModel = timerViewModel)
    }
  }

}

@Composable
fun TimerApp(timerViewModel: TimerViewModel = viewModel()) {
  // Compose the TimerDisplay and ButtonsGrid here
  Container(timerViewModel.timeLeft) {timeInMillis -> timerViewModel.startTimer(timeInMillis)}
}

@Preview
@Composable
fun TimerAppPreview() {
  // Compose the TimerDisplay and ButtonsGrid here
  Container(100L) {}
}

@Composable
fun Container(timeLeft: Long, startTimer: StartTimer) {
  BoxWithConstraints(modifier = Modifier.fillMaxSize(1f)) {
    val constraints = constraints

    Column (modifier = Modifier.fillMaxHeight(1f)){
// Timer display at the top half
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        TimerDisplay(timeLeft, constraints.maxWidth)
      }

      // Button grid at the bottom half
      Column(
        modifier = Modifier
          .fillMaxWidth()
          //.weight(1f) // Takes the remaining half
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        ButtonsGrid(startTimer)
      }
    }
  }
}

