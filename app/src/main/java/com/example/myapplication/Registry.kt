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
        Log.d("Registry", data.toString(2))
        return numOfElements++
    }
    fun addMask(bitmap: Bitmap, idOfElement: Int) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        data.getJSONObject(idOfElement.toString()).put("mask", Base64.encodeToString(b, Base64.DEFAULT))
    }
}