package com.cedacri.arete.domain.model.office

data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email : String,
    val group : String,
) {

    val fullName: String
        get() = "$firstName $lastName"

    val initials: String
        get() {
            val first = firstName.firstOrNull()?.uppercase() ?: ""
            val last = lastName.firstOrNull()?.uppercase() ?: ""
            return first + last
        }

}