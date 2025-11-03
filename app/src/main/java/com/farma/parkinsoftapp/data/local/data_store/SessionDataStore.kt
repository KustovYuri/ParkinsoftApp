package com.farma.parkinsoftapp.data.local.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

class SessionDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Constants.PREFERENCES")

    private val USER_ROLE = stringPreferencesKey("userRole")
    private val USER_ID = stringPreferencesKey("userId")
    fun getCurrentUserRole(): Flow<String> = context.dataStore.data.map {
        it[USER_ROLE] ?: UserRoleValues.UNAUTHORIZED .value
    }

    fun getCurrentUserId(): Flow<String> = context.dataStore.data.map {
        it[USER_ID] ?: ""
    }

    suspend fun setCurrentUserRole(userRole: UserRoleValues) {
        context.dataStore.edit {
            it[USER_ROLE] = userRole.value
        }
    }

    suspend fun setCurrentUserId(userId: Long) {
        context.dataStore.edit {
            it[USER_ID] = userId.toString()
        }
    }
}