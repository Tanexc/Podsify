package com.tanexc.podsify.presentation.components.shape

sealed interface ShapeType {
    data class Polygon(val numVertices: Int) : ShapeType

    data class Star(val numVertices: Int) : ShapeType

    data class Pill(val height: Float) : ShapeType
}