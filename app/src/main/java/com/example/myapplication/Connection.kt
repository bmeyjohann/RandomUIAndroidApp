package com.example.myapplication

import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

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