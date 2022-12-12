// Copyright 2022 Benjamin Meyjohann

package com.example.myapplication

import android.app.Activity
import androidx.annotation.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

var connection: Connection? = null

class Connection: AutoCloseable {

    private var port = 1280

    private var server: ServerSocket? = null
    private var client: Socket? = null
    private var input: BufferedReader? = null
    private var output: PrintWriter? = null

    private var state: State? = null
    private var activity: Activity? = null

    constructor(state: State, activity: Activity) {
        this.state = state
        this.activity = activity

        this.server = ServerSocket(port)

        MainScope().launch {
            withContext(Dispatchers.IO) {
                this@Connection.listen()
            }
        }
    }

    private fun listen() {
        while (true) {
            this.establishConnection()

            val request = JSONObject(this.input!!.readLine())

            if(request.get("header").equals("ready?")) {
                this.sendResponse("ready!")
            } else if(request.get("header").equals("classes?")) {
                this.sendResponse("classes!", this.state!!.registry.data)
            } else if(request.get("header").equals("next state?")) {
                this.state!!.nextState()
                this.sendResponse("next state!")
            } else if(request.get("header").equals("randomize?")) {
                this.sendResponse("randomize!")
            }

            this.closeClientConnection()

            if (request.get("header").equals("randomize")) {
                this.close()
                MainScope().launch {
                    withContext(Dispatchers.Main) {
                        activity!!.recreate()
                    }
                }
                break
            }
        }
    }

    private fun sendResponse(header: Any, body: Any? = null) {
        val response = JSONObject()
        response.put("header", header)
        if (body != null) {
            response.put("body", body)
        }
        this.output!!.println(response.toString())
    }
    private fun establishConnection() {
        this.client = this.server!!.accept()

        this.input = BufferedReader(InputStreamReader(this.client!!.getInputStream()))
        this.output = PrintWriter(this.client!!.getOutputStream(), true)
    }

    private fun closeClientConnection() {
        if (this.client != null) {
            this.client!!.close()
        }
        if (this.input != null) {
            this.input!!.close()
        }
        if (this.output != null) {
            this.output!!.close()
        }
    }

    override fun close() {
        this.closeClientConnection()
        if (this.server != null) {
            this.server!!.close()
        }
    }

    fun isClosed(): Boolean {
        return this.server!!.isClosed
    }

    fun setState(state: State) {
        this.state = state
    }
}