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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
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


class MainActivity : ComponentActivity() {

    var triggerNextState: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            RandomApp(this)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomApp(activity: MainActivity) {
    var state by remember { mutableStateOf(State(activity)) }
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
            topBar = { RandomTopAppBar(state) },
            floatingActionButton = { RandomFloatingActionButton(state) },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { RandomBottomBar(state) },
            content = {
                Column(
                    modifier = Modifier.padding(10.dp, 64.dp, 10.dp, 10.dp)
                ) {
                    if (debug) {
                        Button(
                            onClick = {
                                activity.recreate()
                            },
                            content = {
                                Text(text = "Recreate Activity.", color = Color.White)
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
                                Log.d(
                                    "screenshot",
                                    screenshot(activity.window.decorView.rootView).toString()
                                )
                            },
                            content = {
                                Text(text = "Screenshot", color = Color.White)
                            },
                            colors = ButtonDefaults.buttonColors(Color.Black)
                        )
                    }

                    for (index in 0..10) {
                        RandomIE(state = state)
                    }
                }
            }
        )
    }

    if (connection == null || connection!!.isClosed()) {
        connection = DatasetConnection(state, activity)
    } else {
        connection!!.setState(state)
    }
}