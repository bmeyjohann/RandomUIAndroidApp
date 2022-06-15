package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.DarkColorScheme
import com.example.myapplication.ui.theme.LightColorScheme
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket
import kotlin.random.Random

enum class Visibility {
    SHOW, HIDE, WHITE, BLACK
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = JSONObject()

        setContent {
            RandomApp(this, data)
        }

        MainScope().launch {
            val con = DatasetConnection()
            val randomize = con.sendAndReceive(data)
            con.close()
            this@MainActivity.recreate()
        }
    }
}

class DatasetConnection: AutoCloseable {

    private var port = 1285;
    private var hostAddress = "192.168.1.120"

    private var client: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    constructor() {
        this.open()
    }

    constructor(port: Int, hostAddress: String) {
        this.port = port
        this.hostAddress = hostAddress

        this.open()
    }

    private fun open() {
        MainScope().launch { open(port, hostAddress) }
    }

    private suspend fun open(port: Int, hostAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                client = Socket(hostAddress, port)
                output = PrintWriter(client!!.getOutputStream(), true)
                input = BufferedReader(InputStreamReader(client!!.getInputStream()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun sendAndReceive(data: JSONObject): Boolean {
        withContext(Dispatchers.IO) {
            delay(500)
            while(output == null) {

            }
            output!!.println(data.toString())
            while(true) {
                val response = JSONObject(input!!.readLine())
                Log.e("message from server", response.toString(4))
                return@withContext response.get("answer") == "randomize"
            }
        }
        return false
    }

    override fun close() {
        try {
            client!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomApp(activity: ComponentActivity, data: JSONObject) {
    // TODO collect data of all elements
    var dark by remember { mutableStateOf(false) }
    val scheme = if(dark) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = scheme,
        content = {
            Scaffold(
                bottomBar = { RandomBottomBar(Visibility.SHOW) },
                content = {
                    val deviceConfiguration = LocalConfiguration.current
                    val appWidth = deviceConfiguration.screenWidthDp
                    val appHeight = deviceConfiguration.screenHeightDp

                    val id = "button_test"

                    val buttonWidth = appWidth.times(Random.nextFloat())
                    val buttonHeight = appHeight.times(Random.nextFloat())

                    val buttonX = appWidth.minus(buttonWidth).times(Random.nextFloat())
                    val buttonY = appHeight.minus(buttonHeight).times(Random.nextFloat())

                    val buttonData = JSONObject()

                    val pos = JSONObject()
                    pos.put("x",buttonX / appWidth)
                    pos.put("y", buttonY / appHeight)
                    buttonData.put("pos", pos)

                    val size = JSONObject()
                    size.put("x", buttonWidth / appWidth)
                    size.put("y", buttonHeight / appHeight)
                    buttonData.put("size", size)

                    Log.e("test", buttonData.toString(4))

                    data.put(id, buttonData)

                    Button(

                        enabled = Random.nextBoolean(),
                        onClick = {
                            dark = !dark
                        },
                        content = {
                            Text(Random.nextBoolean().toString())
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
    )

        // TODO send data of all elements, since ui should now be rendered
        // TODO randomize rotation, theme...
}

@Composable
fun RandomBottomBar(v: Visibility) {
    val numOfEntries by remember { mutableStateOf(Random.nextInt(3, 6)) }
    val selectedEntry = Random.nextInt(1, numOfEntries + 1)
    if(v != Visibility.HIDE) {
        NavigationBar {
            for (i in 1..numOfEntries) {
                NavigationBarItem(
                    selected = i == selectedEntry,
                    icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    },
                    /* TODO Randomize label and icon. Also no icon or no label.  */
                    label = { Text(text = "test$i" + Random.nextBoolean().toString()) },
                    onClick = {
                    }
                )
            }
        }
    }
}