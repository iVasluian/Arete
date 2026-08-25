package com.cedacri.arete.domain.model.reservation

enum class ReservationStatus(
    val value: String
) {
    ALL("all"),
    ACTIVE("active"),
    DELETED("deleted")
}