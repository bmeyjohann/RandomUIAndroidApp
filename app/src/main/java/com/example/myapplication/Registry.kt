// Copyright 2022 Benjamin Meyjohann

package com.example.myapplication

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class Registry {
    var numOfElements = 0
    val data = JSONObject()
    fun registerElement(type: String): Int {
        data.put(numOfElements.toString(), JSONObject().put("class", type))
        return numOfElements++
    }
}