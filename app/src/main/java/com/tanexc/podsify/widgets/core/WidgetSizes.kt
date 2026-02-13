package com.tanexc.podsify.widgets.core

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

enum class WidgetSizes(val dpSize: DpSize) {
    SingleLine(DpSize(0.dp, 80.dp)),
    TwoLine(DpSize(0.dp, 160.dp)),
    MultiLine(DpSize(0.dp, 320.dp));

    companion object {
        fun asSetOfDpSize(): Set<DpSize> = entries
            .map { it.dpSize }
            .toSet()

        fun DpSize.asWidgetSize(): WidgetSizes = when {
            height >= MultiLine.dpSize.height -> MultiLine
            height >= TwoLine.dpSize.height -> TwoLine
            else -> SingleLine
        }
    }
}