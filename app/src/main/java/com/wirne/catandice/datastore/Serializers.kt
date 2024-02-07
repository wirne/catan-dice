package com.wirne.catandice.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object GameStateSerializer : Serializer<PersistedGameState> {
    override val defaultValue: PersistedGameState = PersistedGameState.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PersistedGameState {
        try {
            return PersistedGameState.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: PersistedGameState,
        output: OutputStream,
    ) = t.writeTo(output)
}

object PersistedSettingsSerializer : Serializer<PersistedSettings> {
    override val defaultValue: PersistedSettings = PersistedSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PersistedSettings {
        try {
            return PersistedSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: PersistedSettings,
        output: OutputStream,
    ) = t.writeTo(output)
}

object PersistedTimerSerializer : Serializer<PersistedTimer> {
    override val defaultValue: PersistedTimer =
        PersistedTimer.getDefaultInstance()
            .toBuilder()
            .setTimeInSeconds(90L)
            .setTimeLeftInSeconds(90L)
            .build()

    override suspend fun readFrom(input: InputStream): PersistedTimer {
        try {
            return PersistedTimer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: PersistedTimer,
        output: OutputStream,
    ) = t.writeTo(output)
}
