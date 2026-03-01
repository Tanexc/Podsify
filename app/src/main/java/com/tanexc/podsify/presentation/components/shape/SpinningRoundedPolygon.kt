package com.tanexc.podsify.presentation.components.shape

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.pill
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath

@Composable
fun SpinningRoundedPolygon(
    modifier: Modifier = Modifier,
    easing: Easing = LinearEasing,
    color: Color = MaterialTheme.colorScheme.primary,
    phaseDuration: Int = 1200,
    phaseAngle: Int = 360,
    phaseDelay: Float = 1f,
    shapes: List<ShapeType>,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition()
    val totalProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = shapes.size * (1f + phaseDelay),
        animationSpec = infiniteRepeatable(
            tween(phaseDuration * shapes.size, easing = easing),
            RepeatMode.Restart
        )
    )

    val phase = (totalProgress / (1f + phaseDelay)).toInt() % shapes.size
    val segmentProgress = (totalProgress % (1f + phaseDelay)).coerceAtMost(1f)

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .rotate(phaseAngle * segmentProgress)
                .drawWithCache {
                    onDrawBehind {
                        drawMorphSequence(
                            progress = segmentProgress,
                            color = color,
                            currentShape = shapes[phase],
                            nextShape = shapes[(phase + 1) % shapes.size]
                        )
                    }
                }
        )
        content()
    }


}

fun DrawScope.drawMorphSequence(
    progress: Float,
    color: Color,
    currentShape: ShapeType,
    nextShape: ShapeType
) {
    val current = getShape(currentShape)
    val next = getShape(nextShape)

    val morph = Morph(current, next)

    val path = morph.toPath(progress)

    val bounds = path.asComposePath().getBounds()
    val scale = size.maxDimension / bounds.maxDimension

    val matrix = Matrix().apply {
        translate(-size.width * scale / 2, -size.height * scale / 2)
        scale(scale, scale)
        translate(size.width / (2 * scale), size.height / (2 * scale))
    }


    val transformedPath = path.asComposePath().apply { transform(matrix) }
    drawPath(transformedPath, color = color)
}


fun DrawScope.getShape(shape: ShapeType): RoundedPolygon {
    return when (shape) {
        is ShapeType.Polygon -> RoundedPolygon(
            numVertices = shape.numVertices,
            radius = size.width * 0.5f,
            centerX = size.width / 2,
            centerY = size.height / 2,
            rounding = CornerRounding(size.width * 0.2f),
        )

        is ShapeType.Pill -> RoundedPolygon.pill(
            width = size.width * 1f,
            height = size.height * shape.height,
            centerX = size.width / 2,
            centerY = size.height / 2,
        )

        is ShapeType.Star -> RoundedPolygon.star(
            numVerticesPerRadius = shape.numVertices,
            radius = size.width * 0.5f,
            innerRadius = size.width * 0.2f,
            centerX = size.width / 2,
            centerY = size.height / 2,
            rounding = CornerRounding(size.width * 0.2f),
            innerRounding = CornerRounding(size.width * 0.2f)
        )
    }
}