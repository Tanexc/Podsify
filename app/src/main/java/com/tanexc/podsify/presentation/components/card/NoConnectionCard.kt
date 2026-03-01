package com.tanexc.podsify.presentation.components.card

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tanexc.podsify.R
import com.tanexc.podsify.presentation.components.shape.ShapeType
import com.tanexc.podsify.presentation.components.shape.SpinningRoundedPolygon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NoConnectionCard() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpinningRoundedPolygon(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.primaryContainer,
            easing = LinearEasing,
            shapes = listOf(ShapeType.Star(4), ShapeType.Polygon(4)),
            phaseDelay = 0f,
            phaseDuration = 3000,
            phaseAngle = -360
        ) {
            LoadingIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = stringResource(R.string.main_screen_waiting_device_text),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
