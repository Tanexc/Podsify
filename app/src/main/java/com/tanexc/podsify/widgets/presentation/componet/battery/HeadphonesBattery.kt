package com.tanexc.podsify.widgets.presentation.componet.battery

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Spacer
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

@Composable
fun HeadphonesBattery(
    modifier: GlanceModifier,
    level: Int,
    @DrawableRes iconResource: Int
) {
    Box(
        modifier = GlanceModifier
            .cornerRadius(8.dp)
            .background(GlanceTheme.colors.primary)
    ) {
        Image(
            provider = ImageProvider(iconResource),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                GlanceTheme.colors.onPrimary
            ),
            modifier = GlanceModifier.padding(6.dp)
                .size(36.dp)
        )
    }
    Spacer(GlanceModifier.size(16.dp))
    Text(
        text = "$level %",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = GlanceTheme.colors.onSurface
        )
    )
}