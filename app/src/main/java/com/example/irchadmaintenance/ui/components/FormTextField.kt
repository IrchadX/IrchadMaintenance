package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable ((isFocused: Boolean) -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        var isFocused by remember { mutableStateOf(false) }

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isFocused) TealColor else Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            shape = RoundedCornerShape(6.dp),
            leadingIcon = leadingIcon?.let { { it(isFocused) } },
            singleLine = singleLine,
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealColor,
                unfocusedBorderColor = Color.Black
            )
        )
    }
}

@Composable
fun LocationIcon(isFocused: Boolean = false) {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = "Location",
        tint = if (isFocused) TealColor else Color.Black
    )
}