package com.cedacri.arete.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cedacri.arete.common.dataStore
import com.cedacri.arete.data.local.datasource.SecureTokenStorage
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.domain.model.login.LoginResponse
import com.cedacri.arete.domain.model.preferences.Appearance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TokenStorageImpl(
    private val context: Context,
    private val secureTokenStorage: SecureTokenStorage
) : TokenStorage {

    private object Keys {
        val LOGIN_ID = stringPreferencesKey("login_id")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val EMAIL = stringPreferencesKey("email")
        val ROLE = intPreferencesKey("role")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val PREFERENCES = stringPreferencesKey("preferences")
        val TOKEN_DATE = stringPreferencesKey("token_date")
        val APPEARANCE = stringPreferencesKey("appearance")
        val AVAILABLE_SEAT_COLOR = stringPreferencesKey("available_seat_color")
    }

    override suspend fun saveLoginData(
        response: LoginResponse
    ) {

        context.dataStore.edit {

            it[Keys.LOGIN_ID] = response.id
            it[Keys.FIRST_NAME] = response.firstName
            it[Keys.LAST_NAME] = response.lastName
            it[Keys.EMAIL] = response.email
            it[Keys.ROLE] = response.role
            it[Keys.AUTH_TOKEN] = secureTokenStorage.encrypt(response.authToken)
            it[Keys.PREFERENCES] = response.preferences
            it[Keys.TOKEN_DATE] =
                LocalDate.now().toString()
        }
    }

    override suspend fun clear() {

        context.dataStore.edit {

            it.remove(Keys.LOGIN_ID)
            it.remove(Keys.FIRST_NAME)
            it.remove(Keys.LAST_NAME)
            it.remove(Keys.EMAIL)
            it.remove(Keys.ROLE)
            it.remove(Keys.AUTH_TOKEN)
            it.remove(Keys.TOKEN_DATE)
            it.remove(Keys.PREFERENCES)
        }
    }

    override suspend fun getLoginId(): String? {
        return context.dataStore.data.first()[Keys.LOGIN_ID]
    }

    override suspend fun getFirstName(): String? {
        return context.dataStore.data.first()[Keys.FIRST_NAME]
    }

    override suspend fun getLastName(): String? {
        return context.dataStore.data.first()[Keys.LAST_NAME]
    }

    override suspend fun getEmail(): String? {
        return context.dataStore.data.first()[Keys.EMAIL]
    }

    override suspend fun getRole(): Int? {
        return context.dataStore.data.first()[Keys.ROLE]
    }

    override suspend fun getAuthToken(): String? {
        val encryptedToken =
            context.dataStore
                .data
                .first()[Keys.AUTH_TOKEN]
                ?: return null

        return runCatching {
            secureTokenStorage.decrypt(
                encryptedToken
            )
        }.getOrNull()
    }

    override suspend fun getPreferences(): String? {
        return context.dataStore.data.first()[Keys.PREFERENCES]
    }

    override suspend fun getTokenDate(): String? {
        return context.dataStore
            .data
            .first()[Keys.TOKEN_DATE]
    }

    override suspend fun saveAppearance(
        appearance: Appearance
    ) {
        context.dataStore.edit {
            it[Keys.APPEARANCE] =
                appearance.name
        }
    }

    override suspend fun getAppearance(): Appearance {
        val value =
            context.dataStore
                .data
                .first()[Keys.APPEARANCE]

        return runCatching {
            Appearance.valueOf(value ?: Appearance.SYSTEM.name)
        }.getOrDefault(
            Appearance.SYSTEM
        )
    }

    override suspend fun saveAvailableSeatColor(
        color: String
    ) {
        context.dataStore.edit {
            it[Keys.AVAILABLE_SEAT_COLOR] =
                color
        }
    }

    override suspend fun getAvailableSeatColor(): String {
        return context.dataStore
            .data
            .first()[Keys.AVAILABLE_SEAT_COLOR]
            ?: "#1A000000"
    }

    override fun getAvailableSeatColorFlow(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[Keys.AVAILABLE_SEAT_COLOR]
                ?: "#1A000000"
        }
    }

    override fun getAppearanceFlow(): Flow<Appearance> {
        return context.dataStore.data.map { preferences ->

            runCatching {
                Appearance.valueOf(
                    preferences[Keys.APPEARANCE]
                        ?: Appearance.SYSTEM.name
                )
            }.getOrDefault(
                Appearance.SYSTEM
            )
        }
    }
}