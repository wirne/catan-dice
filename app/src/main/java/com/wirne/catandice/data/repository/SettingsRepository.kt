package com.wirne.catandice.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.wirne.catandice.data.model.Settings
import com.wirne.catandice.datastore.PersistedSettings
import com.wirne.catandice.datastore.PersistedSettingsSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "persisted_settings.proto"

private val Context.settingsStore: DataStore<PersistedSettings> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = PersistedSettingsSerializer,
)

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val settings: Flow<Settings> = context.settingsStore.data.map { it.toSettings() }

    suspend fun updateRandomPercentage(randomPercentage: Int) {
        context.settingsStore.updateData {
            it.toBuilder()
                .setRandomPercentage(randomPercentage)
                .build()
        }
    }

    suspend fun toggleCitiesAndKnightsEnabled() {
        context.settingsStore.updateData {
            it.toBuilder()
                .setCitiesAndKnightsEnabled(!it.citiesAndKnightsEnabled)
                .build()
        }
    }
}

internal fun PersistedSettings.toSettings(): Settings =
    Settings(
        citiesAndKnightsEnabled = citiesAndKnightsEnabled,
        randomPercentage = randomPercentage,
    )
