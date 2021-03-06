/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    private val _state: MutableStateFlow<State> =
        MutableStateFlow(State(10_000, 10_000, TimerState.ready))

    val state: StateFlow<State>
        get() = _state

    var job: Job? = null

    fun action(event: Event) {
        when (event) {
            Event.PlayClicked -> startTimer()
            Event.PauseClicked -> pauseTimer()
            Event.ResetClicked -> reset()
        }
    }

    private fun startTimer() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _state.emit(_state.value.copy(timerState = TimerState.running))
            while (_state.value.timeLeft > 0 && (_state.value.timerState == TimerState.running)) {
                delay(100)
                _state.emit(_state.value.copy(timeLeft = _state.value.timeLeft - 100))
            }
            if (_state.value.timeLeft == 0) {
                _state.emit(_state.value.copy(timerState = TimerState.expired))
            }
        }
    }

    private fun reset() {
        job?.cancel()
        viewModelScope.launch {
            _state.emit(State(10_000, 10_000, TimerState.ready))
        }
    }

    private fun pauseTimer() {
        job?.cancel()
        viewModelScope.launch {
            _state.emit(_state.value.copy(timerState = TimerState.paused))
        }
    }
}

data class State(val totalTime: Int, val timeLeft: Int, val timerState: TimerState) {
    val time: String
        get() = timeLeft.div(1000).toString()
    val percentage: Float
        get() = (totalTime - timeLeft).div(totalTime.toFloat())
}

enum class Event {
    PlayClicked,
    PauseClicked,
    ResetClicked
}

enum class TimerState {
    ready, paused, running, expired,
}
