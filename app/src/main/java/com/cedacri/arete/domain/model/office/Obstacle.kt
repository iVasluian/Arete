package com.cedacri.arete.domain.model.office

data class Obstacle(
    val id: String,
    val type: ObstacleType,
    override val row: Int,
    override val column: Int,
    override val rowSpan: Int,
    override val columnSpan: Int,
    val label: String
) : OfficeItem