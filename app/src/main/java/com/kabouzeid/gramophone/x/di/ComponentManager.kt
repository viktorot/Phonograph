package com.kabouzeid.gramophone.x.di

object ComponentManager {

    private val map = mutableMapOf<String, Any>()

    fun init(component: AppComponent) {
        map[AppComponent::class.java.name] = component
    }

    val appComponent: AppComponent
        get() =
            map[AppComponent::class.java.name] as? AppComponent? ?: throw IllegalStateException()

    fun add(component: Any) {
        map[component::class.java.name] = component
    }

    fun remove(component: Any) {
        map.remove(component::class.java.name)
    }
}