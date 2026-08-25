package com.cedacri.arete.di


import com.cedacri.arete.data.local.datasource.SecureTokenStorage
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.data.local.datastore.TokenStorageImpl
import com.cedacri.arete.data.mapper.LockerMapper
import com.cedacri.arete.data.mapper.OfficeGroupMapper
import com.cedacri.arete.data.mapper.OfficeLayoutMapper
import com.cedacri.arete.data.repository.AuthRepositoryImpl
import com.cedacri.arete.data.repository.ReservationsRepositoryImpl
import com.cedacri.arete.data.repository.SeatReservationRepositoryImpl
import com.cedacri.arete.domain.repository.AuthRepository
import com.cedacri.arete.domain.repository.ReservationsRepository
import com.cedacri.arete.domain.repository.SeatReservationRepository
import com.cedacri.arete.presentation.navigation.NavigationManager
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<NavigationManager> { NavigationManager() }
    single { SecureTokenStorage() }
    single<TokenStorage> { TokenStorageImpl(androidContext(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single {
        Gson()
    }
    single { OfficeLayoutMapper(gson = get()) }

    single { OfficeGroupMapper() }

    single { LockerMapper() }

    single<SeatReservationRepository> {
        SeatReservationRepositoryImpl(
            api = get(),
            mapper = get(),
            groupMapper = get(),
            lockerMapper = get()
        )
    }

    single<ReservationsRepository> {
        ReservationsRepositoryImpl(get())
    }
}