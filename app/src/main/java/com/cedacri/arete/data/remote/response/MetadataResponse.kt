package com.cedacri.arete.data.remote.response

data class MetadataResponse(
    val obstacles: List<ObstacleResponse> = emptyList()
)