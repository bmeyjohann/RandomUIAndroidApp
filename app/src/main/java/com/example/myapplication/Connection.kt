package com.example.myapplication

import android.util.Log
import android.util.LogPrinter
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

var connection: DatasetConnection? = null

class DatasetConnection: AutoCloseable {

    private var port = 1281
    private var hostAddress = "192.168.1.100"

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
                while(client == null) {
                    client = Socket(hostAddress, port)
                }
                output = PrintWriter(client!!.getOutputStream(), true)
                input = BufferedReader(InputStreamReader(client!!.getInputStream()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun sendAndReceive(data: JSONObject): JSONObject {
        var response = JSONObject()

        withContext(Dispatchers.IO) {
            while(output == null) {}
            output!!.println(data.toString())
            // TODO check if while is necessary
            while(response.length() == 0) {
                try {
                    response = JSONObject(input!!.readLine())
                    Log.e("message from server", response.toString(4))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return response
    }

    override fun close() {
        try {
            client!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}