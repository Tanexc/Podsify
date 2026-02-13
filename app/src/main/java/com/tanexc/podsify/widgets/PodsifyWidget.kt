package com.tanexc.podsify.widgets

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.appwidget.SizeMode
import com.tanexc.bluetoothtool.domain.usecase.GetConnectionStateUseCase
import com.tanexc.podsify.widgets.core.WidgetSizes
import com.tanexc.podsify.widgets.presentation.ConnectionWidgetContent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PodsifyWidget: GlanceAppWidget(), KoinComponent {
    private val getConnectionStateUseCase: GetConnectionStateUseCase by inject()

    override val sizeMode = SizeMode.Responsive(
        WidgetSizes.asSetOfDpSize()
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            val state by getConnectionStateUseCase().collectAsState()

            GlanceTheme {
                ConnectionWidgetContent(
                    state = state
                )
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
    }
}