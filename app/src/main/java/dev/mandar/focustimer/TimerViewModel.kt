package dev.mandar.focustimer

import android.app.Application
import android.media.Ringtone
import android.os.Build
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val FIVE_MIN_IN_MILLIS = 5L * 60L * 1000L

class TimerViewModel(
  application: Application,
  private val vibrator: Vibrator,
  private val ringtone: Ringtone
) : AndroidViewModel(application) {
  private val powerManager = getApplication<Application>().getSystemService<PowerManager>()
  private val dimWakeLock = powerManager?.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
    "FocusTimer::DimWakelockTag")
  private val brightWakeLock = powerManager?.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
    "FocusTimer::BrightWakelockTag")
  private var timerJob: Job? = null
  var timeLeft by mutableLongStateOf(0L) // Time left in milliseconds

  // Start the timer with a specified time
  fun startTimer(timeInMillis: Long) {
    // Cancel any existing job
    timerJob?.cancel()
    timeLeft = timeInMillis
    dimWakeLock?.acquire(timeInMillis + 1000L) // Wait one second to give time to acquire bright wake lock

    timerJob = viewModelScope.launch {
      while (timeLeft > 0) {
        delay(1000) // Wait for a second
        timeLeft -= 1000 // Decrease the time left by one second
      }
      onTimerFinished()
    }
  }

  // Called when the timer reaches zero
  private fun onTimerFinished() {
    playSound()
    vibratePhone()
    brightWakeLock?.acquire(FIVE_MIN_IN_MILLIS)
  }

  // Stop the timer
  fun stopTimer() {
    timerJob?.cancel()
  }

  override fun onCleared() {
    super.onCleared()
    timerJob?.cancel() // Cancel the job when the ViewModel is cleared
    //exoPlayer.release()
  }
  private fun vibratePhone() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
      vibrator.vibrate(500) // Deprecated in API 26
    }
  }

  private fun playSound() {
    // Check if mediaPlayer is initialized and not null

    try {
      if (!ringtone.isPlaying) {
        ringtone.play()
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error in playing alert", e)
    }
  }

  companion object {
    const val TAG = "TimerViewModel"
  }

}
