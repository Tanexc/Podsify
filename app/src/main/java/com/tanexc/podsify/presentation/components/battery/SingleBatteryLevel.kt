package com.tanexc.podsify.presentation.components.battery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanexc.podsify.R

@Composable
fun SingleBatteryLevel(
    label: String,
    level: Int,
    modifier: Modifier = Modifier,
    textColor: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Box(
            modifier = modifier
                .heightIn(100.dp, 120.dp)
                .widthIn(60.dp, 80.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            when {
                level > 0 -> {
                    val animatedLevel by animateIntAsState(level)
                    Column {
                        level.takeIf { it < 100 }?.let {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp, 16.dp, 2.dp, 2.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(0.2f))
                                    .fillMaxSize()
                                    .weight(1 - (animatedLevel / 100f)),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                this@Column.AnimatedVisibility(
                                    visible = level < 40,
                                    enter = slideInVertically { it / 2 } + expandVertically(),
                                    exit = slideOutVertically { it / 2 } + shrinkVertically()
                                ) {
                                    Text(
                                        text = "$level %",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Spacer(Modifier.size(2.5.dp))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp, 2.dp, 16.dp, 16.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .fillMaxSize()
                                .weight(animatedLevel / 100f),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            this@Column.AnimatedVisibility(
                                visible = level >= 40,
                                enter = slideInVertically() + expandVertically(),
                                exit = slideOutVertically() + shrinkVertically()
                            ) {
                                Text(
                                    text = "$level %",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(0.2f))
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.battery_unknown),
                            contentDescription = null,
                            tint = textColor
                        )
                    }
                }
            }
        }
    }
}
