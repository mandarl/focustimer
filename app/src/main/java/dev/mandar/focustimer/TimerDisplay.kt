package dev.mandar.focustimer

import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.max

@Composable
fun TimerDisplay(timeLeft: Long, maxWidth: Int) {
  val minutes = (timeLeft / 60000).toInt()
  val seconds = ((timeLeft / 1000) % 60).toInt()
  val fontSize = max(24.sp, (maxWidth / 6).sp) // Adjust the font size based on the width

  Text(
    //text = String.format(Locale.ROOT,"%02d:%02d", minutes, seconds),
    text = buildAnnotatedString {
      withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
        append(String.format(Locale.ROOT, "%02d:%02d", minutes, seconds))
      }
    },
    //fontSize = fontSize,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start,
    modifier = Modifier
      .padding(16.dp),
    color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.background),
    fontSize = fontSize.sp,
    fontFamily = FontFamily(Typeface(android.graphics.Typeface.MONOSPACE))
  )
}

fun max(sp: TextUnit, sp1: TextUnit): Float {
  return max(sp.value, sp1.value)
}
