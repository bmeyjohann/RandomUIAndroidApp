package com.example.myapplication

import android.util.Log
import org.json.JSONObject

class Registry {
    var numOfElements = 0
    val interactiveElements = JSONObject()
    fun registerElement(type: String): Int {
        interactiveElements.put(numOfElements.toString(), type)
        Log.d("Registry", interactiveElements.toString(2))
        return numOfElements++
    }
}