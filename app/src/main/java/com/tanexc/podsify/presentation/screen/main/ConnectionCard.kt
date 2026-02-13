package com.tanexc.podsify.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tanexc.bluetoothtool.domain.Battery
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.domain.Device
import com.tanexc.podsify.R

@Composable
fun ConnectionCard(
    modifier: Modifier = Modifier,
    state: ConnectionState
) {
    Box {
        AnimatedVisibility(
            visible = state is ConnectionState.Connected,
            exit = fadeOut(),
            enter = fadeIn()
        ) {
            val device = (state as? ConnectionState.Connected)?.device ?: Device()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = device.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.size(32.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Battery info", fontWeight = FontWeight.Bold)
                        when (val battery = device.battery) {
                            is Battery.Undefined -> {
                                Text(text = "No battery info yet")
                            }

                            is Battery.EarBudsBattery -> {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(text = "Left", fontWeight = FontWeight.Bold)
                                        Text(text = "Case", fontWeight = FontWeight.Bold)
                                        Text(text = "Right", fontWeight = FontWeight.Bold)
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(text = "${battery.leftBatteryLevel.takeIf { it != -1 } ?: "-"} %")
                                        Text(text = "${battery.caseBatteryLevel.takeIf { it != -1 } ?: "-"} %")
                                        Text(text = "${battery.rightBatteryLevel.takeIf { it != -1 } ?: "-"} %")
                                    }
                                }
                            }

                            is Battery.HeadPhoneBattery -> {
                                Text(text = "${battery.batteryLevel.takeIf { it != -1 } ?: "-"} %")
                            }
                        }
                    }
                    Spacer(Modifier.size(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Manufacturer", fontWeight = FontWeight.Bold)
                        Text(device.manufacturer.name)
                    }
                    Spacer(Modifier.size(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "MAC-address", fontWeight = FontWeight.Bold)
                        Text(device.address)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state is ConnectionState.NoConnection,
            exit = fadeOut(),
            enter = fadeIn()
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(128.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.main_screen_waiting_device_text),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}