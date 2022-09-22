package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainActivity : ComponentActivity() {

    var triggerNextState: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connection = DatasetConnection()

        setContent {
            RandomApp(this)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            MainScope().launch {
                val message = JSONObject().put("answer", "ready")
                val answer = connection!!.sendAndReceive(message)
                if (answer.get("answer").equals("masks")) {
                    Log.d("answer: masks", "triggerNextState")
                    triggerNextState()
                } else {
                    connection!!.close()
                    this@MainActivity.recreate()
                }
            }
        },250)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomApp(activity: MainActivity) {
    // TODO collect data of all elements
    var state by remember { mutableStateOf(State(activity)) }
    state.onStateChanged = {
        state = state.copy()
        if (!debug) {
            state.nextState()
        }
    }

    // TODO different color schemes
    val scheme = if (state.mask) {
        WhiteColorScheme
    } else {
        val (LightColorScheme, DarkColorScheme) = randomColorSchemes()
        if (probToBool(probDarkScheme)) {
            DarkColorScheme
        } else {
            LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = scheme
    ) {
        Scaffold(
            topBar = { RandomTopAppBar(state) },
            floatingActionButton = { RandomFloatingActionButton(state) },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { RandomBottomBar(state) },
            content = {
                Column(
                    modifier = Modifier.padding(10.dp, 40.dp, 10.dp, 10.dp)
                ) {
                    if (debug) {
                        Button(
                            onClick = {
                                MainScope().launch {
                                    connection!!.close()
                                }
                                activity.recreate()
                            },
                            content = {
                                Text(text = "Recreate Activity.")
                            },
                            colors = ButtonDefaults.buttonColors(Color.Black)
                        )
                        Button(
                            onClick = {
                                state.nextState()
                            },
                            content = {
                                Text(text = "Next State")
                            },
                            colors = ButtonDefaults.buttonColors(Color.Black)
                        )
                        Button(
                            onClick = {
                                Log.d(
                                    "screenshot",
                                    screenshot(activity.window.decorView.rootView).toString()
                                )
                            },
                            content = {
                                Text(text = "Screenshot")
                            },
                            colors = ButtonDefaults.buttonColors(Color.Black)
                        )
                        RandomButton(state = state)
                        RandomCheckbox(state = state)
                        RandomRadioButton(state = state)
                    }

                    for (index in 0..10) {
                        RandomIE(state = state)
                    }
                }
            }
        )
    }

    // TODO send data of all elements, since ui should now be rendered
    // TODO randomize rotation, theme...
}