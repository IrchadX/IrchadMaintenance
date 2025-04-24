package com.example.irchadmaintenance.ui.components

import android.content.res.Resources
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.ui.screens.InterventionType


@Composable
fun InterventionTypeToggle(
    selectedType: InterventionType,
    onTypeSelected: (InterventionType) -> Unit
) {
    val toggleHeight = 48.dp
    val indicatorWidth = 0.46f

    val indicatorOffset by animateDpAsState(
        targetValue = if (selectedType == InterventionType.PREVENTIVE) 0.dp else with(LocalDensity.current) {
            (Resources.getSystem().displayMetrics.widthPixels.dp / density) * indicatorWidth
        },
        label = "IndicatorSlide"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(toggleHeight)
            .padding(horizontal = 22.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MediumGrayColor)
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(indicatorWidth)
                .offset(x = indicatorOffset)
                .clip(RoundedCornerShape(30.dp))
                .background(TealColor)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTypeSelected(InterventionType.PREVENTIVE) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Préventive",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (selectedType == InterventionType.PREVENTIVE) WhiteColor else GrayTextColor
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTypeSelected(InterventionType.CURATIVE) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Curative",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (selectedType == InterventionType.CURATIVE) WhiteColor else GrayTextColor
                )
            }
        }
    }
}
