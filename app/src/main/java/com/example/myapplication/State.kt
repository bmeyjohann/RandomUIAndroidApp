package com.example.myapplication


class State {
    var mask = false
    var idOfMaskElement = -1

    private var activity: MainActivity?
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
    constructor(activity: MainActivity, mask: Boolean, idOfMaskElement: Int, registry: Registry, onStateChanged: () -> Unit) {
        this.activity = activity
        this.mask = mask
        this.idOfMaskElement = idOfMaskElement
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
        return State(activity!!, mask, idOfMaskElement,  registry, onStateChanged)
    }
}