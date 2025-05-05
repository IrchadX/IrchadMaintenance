package com.example.irchadmaintenance.ui.components

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.R

@Composable
fun DiagnosticInfo(
    batteryValue: String,
    tempValue: String,
    signalValue: String,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            var isRotating by remember { mutableStateOf(false) }
            val rotation by animateFloatAsState(
                targetValue = if (isRotating) 360f else 0f,
                animationSpec = tween(durationMillis = 1000),
                label = "rotation"
            )

            IconButton(
                onClick = {
                    isRotating = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        isRotating = false
                        onRefresh()
                    }, 1000)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.rotate(rotation),
                    tint = Color(0xFF2B7A78)
                )
            }
        }


        DiagnosticItem("Batterie", batteryValue, evaluateBattery(batteryValue))
        DiagnosticItem("Température", tempValue, evaluateTemperature(tempValue))
        DiagnosticItem("Connectivité", signalValue, evaluateConnectivity(signalValue))
    }
}

data class DiagnosticEvaluation(
    val borderColor: Color,
    val iconResId: Int
)

@Composable
fun DiagnosticItem(
    label: String,
    value: String,
    evaluation: DiagnosticEvaluation
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF1F8F7), shape = RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = evaluation.borderColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 33.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFF2B7A78)
        )

        Text(
            text = label,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            color = Color.Black
        )

        Text(
            text = value,
            color = Color.Black,
            fontSize = 14.sp
        )

        Image(
            painter = painterResource(id = evaluation.iconResId),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
    }
}

fun evaluateBattery(value: String): DiagnosticEvaluation {
    val percent = value.replace("%", "").trim().toIntOrNull() ?: return redEval()
    return when {
        percent < 30 -> redEval()
        percent in 30..59 -> yellowEval()
        else -> greenEval()
    }
}

fun evaluateTemperature(value: String): DiagnosticEvaluation {
    val temp = value.replace("°C", "").replace("C", "").trim().toIntOrNull() ?: return redEval()
    return when {
        temp > 50 -> redEval()
        temp in 35..50 -> yellowEval()
        else -> greenEval()
    }
}

fun evaluateConnectivity(value: String): DiagnosticEvaluation {
    return when (value.trim().lowercase()) {
        "faible signal" -> redEval()
        "signal moyen" -> yellowEval()
        "bon réseau" -> greenEval()
        else -> redEval()
    }
}

fun redEval() = DiagnosticEvaluation(Color(0xFFCC2222), R.drawable.bad)
fun yellowEval() = DiagnosticEvaluation(Color(0xFFFFB800), R.drawable.warning)
fun greenEval() = DiagnosticEvaluation(Color(0xFF008D38), R.drawable.good)
