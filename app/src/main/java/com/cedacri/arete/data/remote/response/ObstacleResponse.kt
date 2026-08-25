package com.cedacri.arete.data.remote.response

data class ObstacleResponse(
    val id: String,
    val type: String,
    val row: Int,
    val col: Int,
    val rowSpan: Int,
    val colSpan: Int,
    val width: Int,
    val height: Int,
    val label: String,
    val tooltip: String,
    val position: String
)