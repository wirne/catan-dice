package com.wirne.catandice.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.wirne.catandice.data.model.Timer
import com.wirne.catandice.datastore.PersistedTimer
import com.wirne.catandice.datastore.PersistedTimerSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val DATA_STORE_FILE_NAME = "persisted_timer.proto"

private val Context.timerStore: DataStore<PersistedTimer> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = PersistedTimerSerializer
)

@Singleton
class TimerRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val timer: Flow<Timer> = context.timerStore.data.map {
        Timer(
            time = it.timeInSeconds.seconds,
            timeLeft = it.timeLeftInSeconds.seconds,
            enabled = it.timerEnabled
        )
    }

    suspend fun resetTimer() {
        context.timerStore.updateData {
            it.toBuilder()
                .setTimeLeftInSeconds(it.timeInSeconds)
                .build()
        }
    }

    suspend fun updateTime(time: Duration) {
        context.timerStore.updateData {
            it.toBuilder()
                .setTimeInSeconds(time.inWholeSeconds)
                .setTimeLeftInSeconds(time.inWholeSeconds)
                .build()
        }
    }

    suspend fun updateTimeLeft(timeLeft: Duration) {
        context.timerStore.updateData {
            it.toBuilder()
                .setTimeLeftInSeconds(timeLeft.inWholeSeconds)
                .build()
        }
    }

    suspend fun toggleTimerEnabled() {
        context.timerStore.updateData {
            it.toBuilder()
                .setTimerEnabled(!it.timerEnabled)
                .build()
        }
    }
}
