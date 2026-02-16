package com.tanexc.bluetoothtool.domain

import androidx.compose.runtime.Stable
import com.tanexc.bluetoothtool.domain.model.Device

@Stable
sealed interface ConnectionState {
    object NoConnection : ConnectionState

    data class Connected(
        val device: Device
    ) : ConnectionState
}