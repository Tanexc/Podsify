package com.tanexc.podsify.presentation.screen.main.component

sealed interface PermissionState {
    object Granted: PermissionState
    object Unknown: PermissionState

    data class Denied(
        val permissions: Set<String>
    ): PermissionState
}