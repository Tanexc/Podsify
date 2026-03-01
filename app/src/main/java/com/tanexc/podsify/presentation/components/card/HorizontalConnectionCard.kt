package com.tanexc.podsify.presentation.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tanexc.bluetoothtool.domain.model.Battery
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.podsify.R
import com.tanexc.podsify.presentation.components.battery.BatteryInfo

@Composable
fun HorizontalConnectionCard(
    state: ConnectionState
) {
    Box {
        AnimatedVisibility(
            visible = state is ConnectionState.Connected,
            exit = fadeOut(),
            enter = fadeIn()
        ) {
            when (state) {
                is ConnectionState.Connected -> {
                    val device = state.device

                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                    ) {
                        val endCorners by animateDpAsState(
                            targetValue = if (device.battery is Battery.Undefined) 22.dp else 4.dp
                        )
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(22.dp, endCorners, 4.dp, 4.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(22.dp),
                                    text = device.name,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp, 4.dp, endCorners, 22.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.manufacturer,
                                        device.manufacturer
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = stringResource(R.string.address, device.address),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }


                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp, 22.dp, 22.dp, 4.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AnimatedVisibility(
                                visible = device.battery !is Battery.Undefined,
                                enter = expandHorizontally() + slideInHorizontally(),
                                exit = shrinkHorizontally() + slideOutHorizontally()
                            ) {
                                BatteryInfo(
                                    modifier = Modifier
                                        .padding(8.dp),
                                    battery = device.battery
                                )
                            }
                        }
                    }
                }

                else -> {}
            }
        }

        AnimatedVisibility(
            visible = state is ConnectionState.NoConnection,
            exit = fadeOut(),
            enter = fadeIn()
        ) {
            NoConnectionCard()
        }
    }
}