package com.cedacri.arete.data.remote.response

import com.cedacri.arete.domain.model.office.Locker

data class LockersData(
    val offices: List<OfficeResponse>,
    val lockers: List<Locker>
)
