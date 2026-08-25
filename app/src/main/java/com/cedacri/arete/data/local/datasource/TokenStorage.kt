package com.cedacri.arete.data.local.datasource

import com.cedacri.arete.domain.model.login.LoginResponse
import com.cedacri.arete.domain.model.preferences.Appearance
import kotlinx.coroutines.flow.Flow

interface TokenStorage {

    suspend fun saveLoginData(response: LoginResponse)
    suspend fun clear()
    suspend fun getLoginId() : String?
    suspend fun getFirstName() : String?
    suspend fun getLastName() : String?
    suspend fun getEmail() : String?
    suspend fun getRole() : Int?
    suspend fun getAuthToken() : String?
    suspend fun getPreferences() : String?
    suspend fun getTokenDate(): String?
    suspend fun saveAppearance(appearance: Appearance)
    suspend fun getAppearance(): Appearance
    fun getAppearanceFlow(): Flow<Appearance>
    suspend fun saveAvailableSeatColor(color: String)
    suspend fun getAvailableSeatColor(): String
    fun getAvailableSeatColorFlow(): Flow<String>

}