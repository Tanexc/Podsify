package com.tanexc.bluetoothtool.domain

import androidx.compose.runtime.Stable

@Stable
sealed interface ConnectionState {
    object NoConnection : ConnectionState

    data class Connected(
        val device: Device
    ) : ConnectionState
}