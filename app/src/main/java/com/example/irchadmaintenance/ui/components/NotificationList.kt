package com.example.irchadmaintenance.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.irchadmaintenance.data.Notification
import com.example.irchadmaintenance.data.NotificationSeverity

@Composable
fun NotificationsList(
    notifications: List<Notification>,
    showOnlyUnread: Boolean,
    onNotificationClick: (Notification) -> Unit
) {
    val filteredNotifications = if (showOnlyUnread) {
        notifications.filter { !it.isRead }
    } else {
        notifications
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(filteredNotifications) { notification ->
            NotificationCard(
                notification = notification,
                onClick = { onNotificationClick(notification) }
            )
            HorizontalDivider(
                color = Color(0xFFE2E4E8),
                thickness = 1.dp
            )
        }
    }
}

