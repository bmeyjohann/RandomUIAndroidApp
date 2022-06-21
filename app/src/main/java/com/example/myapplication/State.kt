package com.example.myapplication

import android.app.Activity
import android.util.Log

class State {
    var mask = false
    var idOfMaskElement = 0
    var material3 = true

    var activity = Activity()
    var registry = Registry()

    constructor(activity: Activity) {
        this.activity = activity
    }

    constructor(activity: Activity, mask: Boolean, idOfMaskElement: Int, material3: Boolean, registry: Registry): this(activity) {
        this.mask = mask
        this.idOfMaskElement = idOfMaskElement
        this.material3 = material3
        this.registry = registry
    }

    fun nextState(): Boolean {
        if (!mask) {
            mask = true
            Log.d("State", "Mask: $mask, idOfMaskElement: $idOfMaskElement, Registry.numOfElements: ${registry.numOfElements}")
            return mask
        } else if(idOfMaskElement < registry.numOfElements - 1) {
            idOfMaskElement++
            Log.d("State", "Mask: $mask, idOfMaskElement: $idOfMaskElement, Registry.numOfElements: ${registry.numOfElements}")
            return mask
        } else {
            Log.d("State", "Recreate")
            activity.recreate()
            return mask
        }
    }

    fun copy(): State {
        return State(activity, mask, idOfMaskElement, material3, registry)
    }

    fun nextStateByCopy(): State {
        this.nextState()
        return this.copy()
    }
}