package com.tanexc.podsify.widgets.presentation.componet.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.tanexc.bluetoothtool.domain.Battery
import com.tanexc.bluetoothtool.domain.Device
import com.tanexc.podsify.R
import com.tanexc.podsify.widgets.core.WidgetSizes
import com.tanexc.podsify.widgets.core.WidgetSizes.Companion.asWidgetSize
import com.tanexc.podsify.widgets.presentation.componet.battery.EarButsBattery
import com.tanexc.podsify.widgets.presentation.componet.battery.HeadphonesBattery
import com.tanexc.podsify.widgets.presentation.componet.battery.UndefinedBattery
import com.tanexc.podsify.widgets.presentation.util.stringResource

@Composable
fun BaseDeviceConnectionCard(
    device: Device
) {
    val size = LocalSize
        .current
        .asWidgetSize()

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.widgetBackground)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier
                .background(GlanceTheme.colors.primaryContainer)
                .fillMaxSize()
                .cornerRadius(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (size != WidgetSizes.SingleLine) {
                Text(
                    modifier = GlanceModifier.padding(8.dp),
                    text = device.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onPrimaryContainer
                    )
                )
            }

            Row(
                modifier = GlanceModifier.defaultWeight().fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (val battery = device.battery) {
                    is Battery.EarBudsBattery -> {
                        if (battery.leftBatteryLevel >= 0) {
                            EarButsBattery(
                                modifier = GlanceModifier.defaultWeight(),
                                title = stringResource(R.string.widget_earbuts_left_title),
                                level = battery.leftBatteryLevel,
                                iconResource = R.drawable.left_airpods_pro
                            )
                        }

                        if (battery.caseBatteryLevel >= 0) {
                            EarButsBattery(
                                modifier = GlanceModifier.defaultWeight(),
                                title = stringResource(R.string.widget_earbuts_case_title),
                                level = battery.caseBatteryLevel,
                                iconResource = R.drawable.case_airpods_pro
                            )
                        }

                        if (battery.rightBatteryLevel >= 0) {
                            EarButsBattery(
                                modifier = GlanceModifier.defaultWeight(),
                                title = stringResource(R.string.widget_earbuts_right_title),
                                level = battery.rightBatteryLevel,
                                iconResource = R.drawable.right_airpods_pro
                            )
                        }
                    }

                    is Battery.HeadPhoneBattery -> {
                        if (battery.batteryLevel >= 0) {
                            HeadphonesBattery(
                                modifier = GlanceModifier.defaultWeight(),
                                level = battery.batteryLevel,
                                iconResource = R.drawable.headphones
                            )
                        }
                    }

                    is Battery.Undefined -> UndefinedBattery()
                }
            }
        }
    }
}