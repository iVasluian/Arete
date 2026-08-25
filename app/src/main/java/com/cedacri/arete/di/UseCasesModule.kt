package com.cedacri.arete.di

import com.cedacri.arete.domain.usecase.CreateSeatReservationUseCase
import com.cedacri.arete.domain.usecase.GetOfficeGroupsUseCase
import com.cedacri.arete.domain.usecase.GetReservationsUseCase
import org.koin.dsl.module

val useCasesModule = module {
    factory { CreateSeatReservationUseCase(get()) }
    factory { GetOfficeGroupsUseCase(get()) }
    factory { GetReservationsUseCase(get()) }
}