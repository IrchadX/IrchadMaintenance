package com.example.irchadmaintenance.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

val TealColor = Color(0xFF3AAFA9)
val DarkTealColor = Color(0xFF004D40)
val WhiteColor = Color.White
val LightTealColor = Color(0xFFE0F2F1)
val DarkTealTextColor = Color(0xFF00766C)
val LightGrayBgColor = Color(0xFFF5F5F5)
val GrayTextColor = Color(0xFF616161)
val MediumGrayColor = Color(0xFFE0E0E0)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InterventionCalendar(
    currentYearMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = TealColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousMonthClick) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous month",
                        tint = Color(0xFF292D32)
                    )
                }

                Text(
                    text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()}, ${currentYearMonth.year}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF292D32)
                )

                IconButton(onClick = onNextMonthClick) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next month",
                        tint = Color(0xFF292D32)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                val daysOfWeek = listOf("S", "D", "L", "M", "M", "J", "V")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = WhiteColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            val firstDayOfMonth = currentYearMonth.atDay(1)
            val dayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
            val daysInMonth = currentYearMonth.lengthOfMonth()

            val rows = (daysInMonth + dayOfWeek + 6) / 7

            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (column in 0 until 7) {
                        val day = row * 7 + column - dayOfWeek + 1

                        if (day in 1..daysInMonth) {
                            val date = currentYearMonth.atDay(day)
                            val isSelected = date == selectedDate

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) WhiteColor
                                        else TealColor
                                    )
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = (
                                            if (isSelected) TealColor
                                            else WhiteColor
                                            ),
                                    fontSize = 14.sp
                                )
                            }
                        } else {

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}