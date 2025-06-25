package com.example.todoster.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

inline fun <reified T> Fragment.argument(key: String? = null): ReadOnlyProperty<Fragment, T> {
    return ArgumentProperty(T::class, key)
}

inline fun <reified T> Fragment.nullableArgument(key: String? = null): ReadOnlyProperty<Fragment, T?> {
    return NullableArgumentProperty(T::class, key)
}

inline fun <reified T> Fragment.argumentWithDefault(
    defaultValue: T,
    key: String? = null
): ReadOnlyProperty<Fragment, T> {
    return ArgumentWithDefaultProperty(T::class, defaultValue, key)
}

// Property delegate implementations
class ArgumentProperty<T>(
    private val clazz: KClass<*>,
    private val key: String?
) : ReadOnlyProperty<Fragment, T> {
    private var value: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) {
            val argKey = key ?: property.name
            val arguments = thisRef.arguments
                ?: throw IllegalStateException("Fragment ${thisRef::class.simpleName} arguments are null. Did you forget to call newInstance()?")

            value = getValueFromBundle(arguments, argKey, clazz) as T?
                ?: throw IllegalArgumentException("Required argument '$argKey' not found in Fragment ${thisRef::class.simpleName}")
        }
        return value!!
    }
}

class NullableArgumentProperty<T>(
    private val clazz: KClass<*>,
    private val key: String?
) : ReadOnlyProperty<Fragment, T?> {
    private var value: T? = null
    private var isInitialized = false

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        if (!isInitialized) {
            val argKey = key ?: property.name
            val arguments = thisRef.arguments

            value = if (arguments != null) {
                getValueFromBundle(arguments, argKey, clazz) as T?
            } else null

            isInitialized = true
        }
        return value
    }
}

class ArgumentWithDefaultProperty<T>(
    private val clazz: KClass<*>,
    private val defaultValue: T,
    private val key: String?
) : ReadOnlyProperty<Fragment, T> {
    private var value: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) {
            val argKey = key ?: property.name
            val arguments = thisRef.arguments

            value = if (arguments != null) {
                try {
                    getValueFromBundle(arguments, argKey, clazz) as T? ?: defaultValue
                } catch (e: Exception) {
                    defaultValue
                }
            } else defaultValue
        }
        return value!!
    }
}

@Suppress("UNCHECKED_CAST", "DEPRECATION")
private fun getValueFromBundle(bundle: Bundle, key: String, clazz: KClass<*>): Any? {
    return when (clazz) {
        String::class -> bundle.getString(key)
        Int::class -> if (bundle.containsKey(key)) bundle.getInt(key) else null
        Boolean::class -> if (bundle.containsKey(key)) bundle.getBoolean(key) else null
        Long::class -> if (bundle.containsKey(key)) bundle.getLong(key) else null
        Float::class -> if (bundle.containsKey(key)) bundle.getFloat(key) else null
        Double::class -> if (bundle.containsKey(key)) bundle.getDouble(key) else null
        IntArray::class -> bundle.getIntArray(key)
        Array<String>::class -> bundle.getStringArray(key)
        BooleanArray::class -> bundle.getBooleanArray(key)
        LongArray::class -> bundle.getLongArray(key)
        FloatArray::class -> bundle.getFloatArray(key)
        DoubleArray::class -> bundle.getDoubleArray(key)
        else -> {
            when {
                Parcelable::class.java.isAssignableFrom(clazz.java) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(key, clazz.java as Class<out Parcelable>)
                    } else {
                        bundle.getParcelable(key)
                    }
                }

                Serializable::class.java.isAssignableFrom(clazz.java) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getSerializable(key, clazz.java as Class<out Serializable>)
                    } else {
                        bundle.getSerializable(key)
                    }
                }

                else -> throw IllegalArgumentException("Unsupported argument type: $clazz")
            }
        }
    }
}