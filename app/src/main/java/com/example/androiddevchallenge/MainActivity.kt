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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.components.SandTime
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: MyViewModel by viewModels()

        setContent {
            MyTheme {
                MyApp(model)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(model: MyViewModel) {
    Surface(color = MaterialTheme.colors.background) {

        val state by model.state.collectAsState()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            Box(modifier = Modifier) {
                SandTime(
                    state.percentage,
                    state.timerState == TimerState.running,
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.secondary
                )
            }
            Text(
                text = state.time,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(16.dp)
            )
            Buttons(model, state)
        }
    }
}

@Composable
private fun Buttons(
    model: MyViewModel,
    state: State
) {
    Row {
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { model.action(Event.ResetClicked) },
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Reset",
                modifier = Modifier.padding(4.dp),
            )
        }
        PlayPauseButton(state, model)
    }
}

@Composable
private fun PlayPauseButton(
    state: State,
    model: MyViewModel
) {
    if (state.timerState != TimerState.expired) {
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                if (state.timerState == TimerState.running) {
                    model.action(Event.PauseClicked)
                } else {
                    model.action(Event.PlayClicked)
                }
            },
            shape = RoundedCornerShape(50)
        ) {
            when (state.timerState) {
                TimerState.ready -> Text(
                    text = "Play",
                    modifier = Modifier.padding(4.dp),
                )
                TimerState.paused -> Text(
                    text = "Play",
                    modifier = Modifier.padding(4.dp),
                )
                TimerState.running -> Text(
                    text = "Pause",
                    modifier = Modifier.padding(4.dp),
                )
                TimerState.expired -> {
                    /*ignore*/
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(MyViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(MyViewModel())
    }
}
