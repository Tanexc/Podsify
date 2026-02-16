package com.tanexc.bluetoothtool.domain.model

import androidx.compose.runtime.Stable


@Stable
data class Device(
    val name: String = "",
    val address: String = "",
    val manufacturer: Manufacturer = Manufacturer.Unknown,
    val battery: Battery = Battery.Undefined
)
