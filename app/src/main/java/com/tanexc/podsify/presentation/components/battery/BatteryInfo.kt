package com.tanexc.podsify.presentation.components.battery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tanexc.bluetoothtool.domain.model.Battery
import com.tanexc.podsify.R

@Composable
fun BatteryInfo(
    modifier: Modifier = Modifier,
    battery: Battery
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 22.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            when (battery) {
                is Battery.Undefined -> {}

                is Battery.EarBudsBattery -> {
                    SingleBatteryLevel(
                        level = battery.leftBatteryLevel,
                        label = stringResource(R.string.left_label),
                        modifier = Modifier
                            .weight(1f, false),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    SingleBatteryLevel(
                        level = battery.caseBatteryLevel,
                        label = stringResource(R.string.case_label),
                        modifier = Modifier
                            .weight(1f, false),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    SingleBatteryLevel(
                        level = battery.rightBatteryLevel,
                        label = stringResource(R.string.right_label),
                        modifier = Modifier
                            .weight(1f, false),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                is Battery.HeadPhoneBattery -> {
                    SingleBatteryLevel(
                        level = battery.batteryLevel,
                        label = stringResource(R.string.battery_label),
                        modifier = Modifier
                            .weight(1f, false),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}