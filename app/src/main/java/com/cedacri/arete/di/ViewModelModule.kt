package com.cedacri.arete.di

import com.cedacri.arete.presentation.screens.lockers.LockersViewModel
import com.cedacri.arete.presentation.screens.login.LoginViewModel
import com.cedacri.arete.presentation.screens.preferences.PreferencesViewModel
import com.cedacri.arete.presentation.screens.reservations.ReservationsViewModel
import com.cedacri.arete.presentation.screens.seatReservation.SeatReservationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SeatReservationViewModel(get(), get(), get(), get()) }
    viewModel { PreferencesViewModel(get()) }
    viewModel { LockersViewModel(get()) }
    viewModel { ReservationsViewModel(get()) }
}