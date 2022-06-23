package com.example.myapplication.ui.theme

import kotlin.random.Random

val debug = true

val probTopAppBar = 1f
val probBottomBar = 1f
val probFloatingActionButton = 1f
val probDarkScheme = 0f

fun probToBool(probability: Float): Boolean {
    return probability >= Random.nextFloat()
}