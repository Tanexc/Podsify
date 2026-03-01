package com.tanexc.podsify.presentation.screen.main

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.tanexc.podsify.presentation.components.card.ConnectionCard
import com.tanexc.podsify.presentation.components.card.GrantPermissionsCard
import com.tanexc.podsify.presentation.components.card.HorizontalConnectionCard
import com.tanexc.podsify.presentation.screen.main.component.MainScreenComponent
import com.tanexc.podsify.presentation.screen.main.component.PermissionState
import com.tanexc.podsify.util.checkNecessaryPermissions

@Composable
fun MainScreen(component: MainScreenComponent) {
    val context = LocalContext.current

    val permissionState by component
        .permissionState
        .collectAsState()

    val connectionState by component
        .connectionState
        .collectAsState()

    LaunchedEffect(Unit) {
        val permissions = context.checkNecessaryPermissions()
        if (permissions.isNotEmpty()) component.updatePermissionsState(
            PermissionState.Denied(
                permissions
            )
        )
        else {
            component.updatePermissionsState(PermissionState.Granted)
            component.startBluetoothTool()
        }
    }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val deniedPermissions = permissions.filter { (_, isGranted) -> !isGranted }.keys
        if (deniedPermissions.isEmpty()) {
            component.updatePermissionsState(PermissionState.Granted)
        } else {
            component.updatePermissionsState(PermissionState.Denied(deniedPermissions))
        }
    }

    Column {
        GrantPermissionsCard(
            permissionState = permissionState,
            onGrantPermissions = { permissions ->
                requestPermissionsLauncher.launch(permissions)
            }
        )

        val orientation = LocalConfiguration.current.orientation
        AnimatedVisibility(
            visible = permissionState is PermissionState.Granted,
            enter = fadeIn()
        ) {
            when (orientation) {
                ORIENTATION_LANDSCAPE -> HorizontalConnectionCard(connectionState)
                else ->ConnectionCard(connectionState)
            }

        }
    }
}