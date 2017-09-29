package com.imminentmeals.imminenttime

import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue

inline fun assertThat(block: Assertions.() -> Unit) = Assertions().run(block)

class Assertions {
    // BEGIN: Collections
    fun <E> Collection<E>?.isEmpty() {
        assertTrue(this?.isNotEmpty()?.not() ?: true)
    }

    infix fun <E> Collection<E>?.contains(element: E) {
        assertTrue(this?.contains(element) ?: false)
    }
    // END: Collections

    // START: Boolean
    fun Boolean?.isTrue() {
        assertTrue(this ?: false)
    }
    // END: Boolean
}