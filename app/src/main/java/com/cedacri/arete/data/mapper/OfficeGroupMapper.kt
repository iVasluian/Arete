package com.cedacri.arete.data.mapper

import com.cedacri.arete.data.remote.response.GroupRequestResponse
import com.cedacri.arete.domain.model.office.OfficeGroup

class OfficeGroupMapper {

    fun map(
        groups: List<GroupRequestResponse>
    ): List<OfficeGroup> {
        return groups.map { group ->

            OfficeGroup(
                id = group.id,
                name = group.name,
                color = group.color,
                seatIds = group.seats
                    .map { it.id }
                    .toSet(),
                employeeIds = group.employees
                    .map { it.lowercase() }
                    .toSet()
            )
        }
    }
}