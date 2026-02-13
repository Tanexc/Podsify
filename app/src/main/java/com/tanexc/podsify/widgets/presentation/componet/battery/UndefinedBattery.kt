package com.tanexc.podsify.widgets.presentation.componet.battery

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.tanexc.podsify.R
import com.tanexc.podsify.widgets.presentation.util.stringResource

@Composable
fun UndefinedBattery() {
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(R.drawable.battery_unknown),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                GlanceTheme.colors.primary
            ),
            modifier = GlanceModifier.size(32.dp)
        )
        Text(
            text = stringResource(R.string.no_battery_info),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.primary
            )
        )
    }
}
