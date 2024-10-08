package com.wirne.catandice.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@Stable
data class StateDispatch<STATE, EVENT>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit,
)

@Composable
inline fun <reified STATE, EVENT> use(
    viewModel: ViewModelContract<EVENT, STATE>,
): StateDispatch<STATE, EVENT> {
    val state by viewModel.state.collectAsState()

    val dispatch: (EVENT) -> Unit = { event ->
        viewModel.event(event)
    }
    return StateDispatch(
        state = state,
        dispatch = dispatch,
    )
}

interface ViewModelContract<EVENT, STATE> {
    val state: StateFlow<STATE>

    fun event(event: EVENT)
}

@Suppress("ComposableNaming")
@Composable
fun <T> Flow<T>.collectInLaunchedEffect(collector: FlowCollector<T>) {
    val flow = this
    LaunchedEffect(key1 = flow) {
        flow.collect(collector)
    }
}
