package com.tanexc.podsify.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tanexc.podsify.R
import com.tanexc.podsify.presentation.screen.main.component.PermissionState

@Composable
fun GrantPermissionsCard(
    modifier: Modifier = Modifier,
    permissionState: PermissionState,
    onGrantPermissions: (Array<String>) -> Unit,
) {
    AnimatedVisibility(
        visible = permissionState is PermissionState.Denied,
        exit = slideOutVertically() + shrinkVertically(),
        enter = slideInVertically()
    ) {
        val permissions = (permissionState as? PermissionState.Denied)?.permissions?.toTypedArray()
            ?: emptyArray()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(
                modifier = Modifier.padding(22.dp)
            ) {
                Text(
                    text = stringResource(R.string.main_screen_grant_necessary_permissions),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(text = stringResource(R.string.main_screen_grant_permissions_text))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onGrantPermissions(permissions) }) {
                        Text(stringResource(R.string.grant_permissions_card_grant))
                    }
                }
            }
        }
    }
}