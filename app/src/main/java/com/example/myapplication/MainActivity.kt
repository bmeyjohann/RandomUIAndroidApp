package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test", "test2")
        setContent {
            MyApp(this)
        }

        MainScope().launch {
            val con = DatasetConnection()
            val randomize = con.sendAndReceive("test")
            con.close()
            this@MainActivity.recreate()
        }
    }
}

class DatasetConnection: AutoCloseable {

    private var port = 1285;
    private var hostAddress = "192.168.1.106"

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

    suspend fun sendAndReceive(data: String): Boolean {
        withContext(Dispatchers.IO) {
            val jsontext = JSONObject()
            jsontext.put("button1", 111)
            delay(500)
            while(output == null) {

            }
            output!!.println(jsontext.toString())
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


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(activity: ComponentActivity) {
    // TODO collect data of all elements
    var dummy by remember { mutableStateOf(true) }
    var dark by remember { mutableStateOf(false) }
    dummy
    val scheme = if(dark) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = scheme,
        content = {
            Scaffold(
                bottomBar = {
                    dummy
                    if (true) {
                        val numOfEntries = Random.nextInt(3, 6)
                        val selectedEntry = Random.nextInt(1, numOfEntries + 1)
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
                                        dummy= !dummy
                                    }
                                )
                            }
                        }
                    }
                },
                content = {
                    dummy
                    val deviceConfiguration = LocalConfiguration.current
                    val screenWidth = deviceConfiguration.screenWidthDp
                    val screenHeight = deviceConfiguration.screenHeightDp

                    val buttonWidth = screenWidth.times(Random.nextFloat())
                    val buttonHeight = screenHeight.times(Random.nextFloat())

                    val buttonX = screenWidth.minus(buttonWidth).times(Random.nextFloat())
                    val buttonY = screenHeight.minus(buttonHeight).times(Random.nextFloat())

                    Button(

                        enabled = Random.nextBoolean(),
                        onClick = {
                            dummy = !dummy
                            dark = !dark
                        },
                        content = {
                            Text(Random.nextBoolean().toString())
                        },
                        modifier = Modifier
                            .size(buttonWidth.dp, buttonHeight.dp)
                            .absoluteOffset(buttonX.dp, buttonY.dp)
                    )
                }
            )
        }
    )

        // TODO send data of all elements, since ui should now be rendered
        // TODO randomize rotation, theme...
}