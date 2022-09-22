package com.example.myapplication

import android.graphics.Bitmap
import android.view.View
import androidx.compose.ui.Modifier
import androidx.core.graphics.applyCanvas
import kotlin.random.Random

fun probToBool(probability: Float): Boolean {
    return probability >= Random.nextFloat()
}

fun probsToIndex(vararg probabilities: Float): Int {
    var total = 0.0f
    probabilities.forEach { prob -> total += prob }
    var rand = Random.nextFloat()
    var temp = 0.0f
    for((index, prob) in probabilities.withIndex()) {
        temp += prob / total
        if(rand <= temp) {
            return index
        }
    }
    throw Exception("Could not convert probabilities to index.")
}

fun randomText(numMaxWords: Int, numMaxCharsPerWord: Int): String {
    var string = ""
    for(numWords in 1..numMaxWords) {
        if(string.isNotEmpty()) {
            string += " "
        }
        string += Random.nextInt(65, 90).toChar()
        for(numChars in 0..numMaxCharsPerWord) {
            string += Random.nextInt(97, 122).toChar()
            if(probToBool(numChars.toFloat()/numMaxCharsPerWord.toFloat())) {
                break
            }
        }
        if(probToBool(numWords.toFloat()/numMaxWords.toFloat())) {
            break
        }
    }
    return string
}

fun Modifier.onCondition(condition: Boolean, modifier: Modifier): Modifier {
    return if(condition) {
        then(modifier)
    } else {
        this
    }
}

fun screenshot(view: View): Bitmap {
    return Bitmap.createBitmap(view.width, view.height,
        Bitmap.Config.ARGB_8888).applyCanvas {
        view.draw(this)
    }
}