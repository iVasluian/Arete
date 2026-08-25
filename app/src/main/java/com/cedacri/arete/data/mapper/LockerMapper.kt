package com.cedacri.arete.data.mapper

import com.cedacri.arete.data.remote.response.EmployeeResponse
import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.domain.model.office.Locker

class LockerMapper {

    fun map(
        offices: List<OfficeResponse>,
        employees: List<EmployeeResponse>
    ): List<Locker> {

        val employeesById =
            employees.associateBy {
                it.id.lowercase()
            }

        return offices.flatMap { office ->

            office.lockers.map { locker ->

                val employee =
                    locker.employeeId
                        ?.lowercase()
                        ?.let(employeesById::get)

                Locker(
                    id = locker.id,
                    name = locker.name,
                    officeId = office.id,
                    officeName = office.name,
                    lockersCapacity =
                        office.lockersCapacity,
                    employeeId = locker.employeeId,
                    employeeName = employee?.let {
                        "${it.firstName} ${it.lastName}"
                    },
                    updatedAt = locker.updatedAt,
                    updatedBy = locker.updatedBy
                )
            }
        }
    }
}