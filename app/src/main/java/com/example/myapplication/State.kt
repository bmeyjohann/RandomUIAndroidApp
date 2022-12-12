// Copyright 2022 Benjamin Meyjohann

package com.example.myapplication

class State {
    var mask = false
    var idOfMaskElement = 0

    var registry = Registry()

    var onStateChanged: () -> Unit = {}

    constructor()

    // copy constructor
    constructor(state: State) {
        this.mask = state.mask
        this.idOfMaskElement = state.idOfMaskElement
        this.registry = state.registry
        this.onStateChanged = state.onStateChanged
    }

    fun nextState() {
        if (!mask) {
            mask = true
        } else {
            this.idOfMaskElement++
        }
        onStateChanged()
    }

    fun copy(): State {
        return State(this)
    }
}