/**
 * MIT License
 *
 * Copyright (c) 2017 Dandré Allison
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import android.app.Activity
import android.app.Fragment
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.util.TypedValue
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private typealias Initializer<T> = () -> T

/**
 * Delegate function exposed to [LifecycleOwner]s that will initialize a property immediately after
 * the onCreate lifecycle of the owner. For [Activity]s this means the property will be initialized by
 * [Activity.onPostCreate]
 */
fun <T> LifecycleOwner.onCreate(initializer: Initializer<T>): ReadOnlyProperty<LifecycleOwner, T> =
        OnCreateProperty<LifecycleOwner, T>(initializer).also {
            lifecycle.addObserver(it)
        }

/**
 * Initializes a property with the DP value
 * @receiver An [Activity] that is a [LifecycleOwner]
 * @return [Int] representation of the DP value
 * @see [onCreate]
 */
fun <T> T.dp(dp: Int) where T : Activity, T : LifecycleOwner = onCreate {
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}

/**
 * Initializes a property with the DP value
 * @receiver An [Activity] that is a [LifecycleOwner]
 * @return [Float] representation of the DP value
 * @see [onCreate]
 */
fun <T> T.dp(dp: Float) where T : Activity, T : LifecycleOwner = onCreate {
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

/**
 * Initializes a property with the DP value
 * @receiver A [Fragment] that is a [LifecycleOwner]
 * @return [Int] representation of the DP value
 * @see [onCreate]
 */
fun <T> T.dp(dp: Int) where T : Fragment, T : LifecycleOwner = onCreate {
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}

/**
 * Initializes a property with the DP value
 * @receiver A [Fragment] that is a [LifecycleOwner]
 * @return [Float] representation of the DP value
 * @see [onCreate]
 */
fun <T> T.dp(dp: Float) where T : Fragment, T : LifecycleOwner = onCreate {
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

private val uninitializedValue = Any()

/**
 * A property that isn't initialized until the [LifecycleOwner] is created
 */
private class OnCreateProperty<in R, out T>(initializer: Initializer<T>) : ReadOnlyProperty<R, T>, LifecycleObserver {
    private var initializer: Initializer<T>? = initializer
    private var _value: Any? = uninitializedValue
    // final field is required to enable safe publication of constructed instance
    private val isInitialized get() = _value !== uninitializedValue

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        val typedValue = initializer!!()
        _value = typedValue
        initializer = null
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: R, property: KProperty<*>): T =
            if (isInitialized) _value as T else error("Can't access ${property.name} before {$thisRef} onCreate")

    override fun toString(): String = if (isInitialized) _value.toString() else "Value not initialized yet."
}