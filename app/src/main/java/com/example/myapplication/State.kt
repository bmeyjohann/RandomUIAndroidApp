package com.example.myapplication

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject


class State {
    var mask = false
    var idOfMaskElement = -1
    var material3 = true

    var activity: MainActivity?
    var registry = Registry()

    var onStateChanged: () -> Unit = {}

    constructor(activity: MainActivity) {
        this.activity = activity

        this.activity!!.triggerNextState = {
            this.nextState()
        }

        registry.registerElement("screen")
    }

    // copy constructor
    constructor(activity: MainActivity, mask: Boolean, idOfMaskElement: Int, material3: Boolean, registry: Registry, onStateChanged: () -> Unit) {
        this.activity = activity
        this.mask = mask
        this.idOfMaskElement = idOfMaskElement
        this.material3 = material3
        this.registry = registry
        this.onStateChanged = onStateChanged
    }

    fun nextState() {
        if (!mask) {
            mask = true
        }
        this.idOfMaskElement++
        onStateChanged()
    }

    fun copy(): State {
        return State(activity!!, mask, idOfMaskElement, material3, registry, onStateChanged)
    }
}