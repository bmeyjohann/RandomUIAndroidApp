package com.example.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.random.Random


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
    var state by remember { mutableStateOf(State(activity))}
    state.onStateChanged = {
        state = state.copy()
        state.nextState()
    }

    // TODO different color schemes
    val scheme = if(state.mask) WhiteColorScheme else (if(probToBool(probDarkScheme)) DarkColorScheme else LightColorScheme)

    MaterialTheme(
        colorScheme = scheme,

    ) {
        Scaffold(
            topBar = { RandomTopAppBar(state) },
            floatingActionButton = { RandomFloatingActionButton(state) },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { RandomBottomBar(state) },
            content = {
                val deviceConfiguration = LocalConfiguration.current
                val appWidth = deviceConfiguration.screenWidthDp
                val appHeight = deviceConfiguration.screenHeightDp

                val id = "button_test"

                val buttonWidth = appWidth.times(Random.nextFloat())
                val buttonHeight = appHeight.times(Random.nextFloat())

                //val buttonX = appWidth.minus(buttonWidth).times(Random.nextFloat())
                //val buttonY = 100 + appHeight.minus(buttonHeight).times(Random.nextFloat())
                val buttonX = 300
                val buttonY = 600

                val buttonData = JSONObject()

                val pos = JSONObject()
                pos.put("x", buttonX / appWidth)
                pos.put("y", buttonY / appHeight)
                buttonData.put("pos", pos)

                val size = JSONObject()
                size.put("x", buttonWidth / appWidth)
                size.put("y", buttonHeight / appHeight)
                buttonData.put("size", size)

                //data.put(id, buttonData)

                if(debug) {
                    Column {
                        Button(
                            onClick = {
                                MainScope().launch {
                                    connection!!.close()
                                }
                                activity.recreate()
                            },
                            content = {
                                Text(text = "Recreate Activity.")
                            }
                        )
                        Button(
                            onClick = {
                                state.nextState()
                            },
                            content = {
                                Text(text = "Next State")
                            }
                        )
                        Button(
                            onClick = {
                                Log.d("screenshot", screenshot(activity.window.decorView.rootView).toString())
                            },
                            content = {
                                Text(text = "Screenshot")
                            }
                        )
                        RandomButton(state = state)
                    }
                }

                Box(
                    content = {
                        RandomButton(state = state)
                    },
                    modifier = Modifier
                        .size(buttonWidth.dp, buttonHeight.dp)
                        .absoluteOffset(buttonX.dp, buttonY.dp)
//                            .onGloballyPositioned { button ->
//                                val buttonData = JSONObject()
//
//                                val pos12 = JSONObject()
//                                pos12.put("x",appWidth)
//                                pos12.put("y", appHeight)
//                                buttonData.put("app", pos12)
//
//                                val pos1 = JSONObject()
//                                pos1.put("x",button.positionInWindow().x)
//                                pos1.put("y", button.positionInWindow().y)
//                                buttonData.put("pos_abs", pos1)
//
//                                val size1 = JSONObject()
//                                size1.put("x", button.size.width.toFloat())
//                                size1.put("y", button.size.height.toFloat())
//                                buttonData.put("size_abs", size1)
//
//                                val pos = JSONObject()
//                                pos.put("x",button.positionInWindow().x.dp / appWidth)
//                                pos.put("y", button.positionInWindow().y.dp / appHeight)
//                                buttonData.put("pos", pos)
//
//                                val size = JSONObject()
//                                size.put("x", button.size.width.toFloat().dp / appWidth)
//                                size.put("y", button.size.height.toFloat().dp / appHeight)
//                                buttonData.put("size", size)
//
//                                Log.e("test", buttonData.toString(4))
//
//                                data.put(data.get("numOfElements").toString(), buttonData)
//                                data.put("numOfElements", data.get("numOfElements").toString().toInt() + 1)
//                            }
                )
            }
        )
    }

    // TODO send data of all elements, since ui should now be rendered
    // TODO randomize rotation, theme...
}

fun screenshot(view: View): Bitmap {
    return Bitmap.createBitmap(view.width, view.height,
        Bitmap.Config.ARGB_8888).applyCanvas {
        view.draw(this)
    }
}