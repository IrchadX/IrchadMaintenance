package com.example.irchadmaintenance

import android.content.Context
import com.example.irchadmaintenance.R

object ImageMapper {
    fun getDrawableResourceId(context: Context, imageName: String): Int {
        return try {

            val resourceName = imageName.substringBeforeLast('.')
            // Get the resource ID dynamically
            context.resources.getIdentifier(
                resourceName, // The name of the drawable (without extension)
                "drawable",  // The resource type
                context.packageName
            ).takeIf { it != 0 } ?: R.drawable.device// Fallback
        } catch (e: Exception) {
            R.drawable.device // Fallback in case of any error
        }
    }
}