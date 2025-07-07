package com.example.todoster.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : Any> Fragment.argument(): ReadOnlyProperty<Fragment, T> = FragmentArgumentDelegate()

private class FragmentArgumentDelegate<T : Any> : ReadOnlyProperty<Fragment, T> {
    
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val key = property.name
        return thisRef.arguments?.get(key) as? T
            ?: throw IllegalArgumentException("Property ${property.name} could not be read")
    }
}

fun <T : Any> Fragment.argumentNullable(): ReadOnlyProperty<Fragment, T?> = 
    NullableFragmentArgumentDelegate()

private class NullableFragmentArgumentDelegate<T : Any> : ReadOnlyProperty<Fragment, T?> {
    
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        val key = property.name
        return thisRef.arguments?.get(key) as? T
    }
}

// String extensions for easier Bundle handling
fun Bundle.putArgs(vararg pairs: Pair<String, Any?>): Bundle {
    pairs.forEach { (key, value) ->
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
            is Bundle -> putBundle(key, value)
            null -> {} // Do nothing for null values
            else -> throw IllegalArgumentException("Unsupported argument type: ${value::class}")
        }
    }
    return this
}

// Extension for creating bundles more easily
fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle {
    return Bundle().putArgs(*pairs)
} 