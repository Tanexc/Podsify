package com.tanexc.podsify.widgets.presentation


import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.podsify.widgets.presentation.componet.cards.BaseDeviceConnectionCard
import com.tanexc.podsify.widgets.presentation.componet.cards.NoConnectionCard


@Composable
fun ConnectionWidgetContent(
    modifier: GlanceModifier = GlanceModifier,
    state: ConnectionState
) {
    when (state) {
        is ConnectionState.Connected -> {
            val device = state.device
            BaseDeviceConnectionCard(device)
        }

        else -> NoConnectionCard()
    }
}