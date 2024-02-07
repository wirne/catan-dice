package com.wirne.catandice.feature.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirne.catandice.data.repository.GameStateRepository
import com.wirne.catandice.feature.stats.StatsContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StatsViewModel
    @Inject
    constructor(
        gameStateRepository: GameStateRepository,
    ) : ViewModel(), StatsContract {
        override val state: StateFlow<State> =
            gameStateRepository.gameState.map { gameState ->
                State(
                    twoDiceSumCount =
                        gameState.rollHistory
                            .groupBy { it.twoDiceOutcome.sum }
                            .mapValues { (_, history) ->
                                Count(
                                    totalCount = history.count(),
                                    randomCount = history.count { it.random },
                                )
                            },
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = State.Initial,
            )

        override fun event(event: StatsContract.Event) {}

        override val effect: Flow<StatsContract.Effect> = emptyFlow()
    }
