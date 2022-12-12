// Copyright 2022 Benjamin Meyjohann

package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.random.Random

var debug = false

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure behavior of hidden system bars
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide status bar and navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // Dataset collection is hard with screen turned off
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            RandomApp(this)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomApp(activity: MainActivity) {
    var state by remember { mutableStateOf(State()) }
    val outerAlignment by remember { mutableStateOf(randomHorizontalAlignment()) }
    val innerAlignment by remember { mutableStateOf(randomHorizontalAlignment()) }
    val showTopAppBar by remember { mutableStateOf(probToBool(probTopAppBar)) }

    state.onStateChanged = {
        state = state.copy()
    }

    val scheme = if (state.mask) {
        BlackColorScheme
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
            topBar = { if(showTopAppBar) RandomTopAppBar(state) },
            floatingActionButton = { RandomFloatingActionButton(state) },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { RandomBottomBar(state) },
            content = {
                Column(
                    modifier = Modifier
                        .padding(10.dp, if(showTopAppBar) 64.dp else 10.dp, 10.dp, 0.dp)
                        .fillMaxSize(1f),
                    horizontalAlignment = outerAlignment
                ) {
                    Column(
                        horizontalAlignment = innerAlignment
                    ) {
                        if (debug) {
                            Button(
                                onClick = {
                                    activity.recreate()
                                },
                                content = {
                                    Text(text = "Recreate Activity", color = Color.White)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Black)
                            )
                            Button(
                                onClick = {
                                    state.nextState()
                                },
                                content = {
                                    Text(text = "Next State", color = Color.White)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Black)
                            )
                            Button(
                                onClick = {
                                    debug = false
                                    activity.recreate()
                                },
                                content = {
                                    Text(text = "Turn Off Debug", color = Color.White)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Black)
                            )
                        }

                        for (index in 0..13) {
                            RandomIE(state = state)
                        }
                    }
                }
            }
        )
    }

    if (connection == null || connection!!.isClosed()) {
        connection = Connection(state, activity)
    } else {
        connection!!.setState(state)
    }
}