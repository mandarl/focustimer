package dev.mandar.focustimer

import android.media.Ringtone
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class TimerViewModel(
  private val vibrator: Vibrator,
  private val ringtone: Ringtone
) : ViewModel() {
  var timeLeft by mutableLongStateOf(0L) // Time left in milliseconds
  private var timerJob: Job? = null

  // Start the timer with a specified time
  fun startTimer(timeInMillis: Long) {
    // Cancel any existing job
    timerJob?.cancel()
    timeLeft = timeInMillis

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
