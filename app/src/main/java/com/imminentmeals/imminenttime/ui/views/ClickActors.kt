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
package com.imminentmeals.imminenttime.ui.views

import android.content.DialogInterface
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

// https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#using-actors-within-ui-context

const val CAPACITY_SINGLE = 0
const val CAPACITY_SINGLE_MOST_RECENT = Channel.CONFLATED
const val CAPACITY_UNLIMITED = Channel.UNLIMITED

/**
 * @receiver click events of this [View] will be observed
 * @param capacity optional of values [CAPACITY_SINGLE], [CAPACITY_SINGLE_MOST_RECENT],
 * [CAPACITY_UNLIMITED], or any positive [Int]
 * @param action executed when receiver [View] is clicked
 * @see [Channel]
 */
fun View.onClick(capacity: Int = CAPACITY_SINGLE, action: suspend () -> Unit) {
    // launch one actor
    val eventActor = actor<Unit>(UI, capacity) {
        for (event in channel) action()
    }
    // install a listener to activate this actor
    setOnClickListener {
        eventActor.offer(Unit)
    }
}

/**
 * @receiver long-click events of this [View] will be observed
 * @param capacity optional of values [CAPACITY_SINGLE], [CAPACITY_SINGLE_MOST_RECENT],
 * [CAPACITY_UNLIMITED], or any positive [Int]
 * @param action executed when receiver [View] is long-clicked
 * @see [Channel]
 */
fun View.onLongClick(capacity: Int = CAPACITY_SINGLE, action: suspend () -> Unit) {
    // launch one actor
    val eventActor = actor<Unit>(UI, capacity) {
        for (event in channel) action()
    }
    // install a listener to activate this actor
    setOnLongClickListener {
        eventActor.offer(Unit)
    }
}

private typealias AlertDialogEvent = Pair<DialogInterface, Int>
private val AlertDialogEvent.dialogInterface get() = first
private val AlertDialogEvent.button get() = second

/**
 * @receiver positive button click events of this [AlertDialog] will be observed
 * @param capacity optional of values [CAPACITY_SINGLE], [CAPACITY_SINGLE_MOST_RECENT],
 * [CAPACITY_UNLIMITED], or any positive [Int]
 * @param action executed when receiver [AlertDialog] positive button is clicked
 * @see [Channel]
 */
fun AlertDialog.Builder.onPositiveButtonClick(
        @StringRes textId: Int,
        capacity: Int = CAPACITY_SINGLE,
        action: suspend (DialogInterface, Int) -> Unit
) = apply {
    // launch one actor
    val eventActor = actor<AlertDialogEvent>(UI, capacity) {
        for (event in channel) action(event.dialogInterface, event.button)
    }
    // install a listener to activate this actor
    setPositiveButton(textId) { dialogInterface, button ->
        eventActor.offer(AlertDialogEvent(dialogInterface, button))
    }
}

/**
 * @receiver negative button click events of this [AlertDialog] will be observed
 * @param capacity optional of values [CAPACITY_SINGLE], [CAPACITY_SINGLE_MOST_RECENT],
 * [CAPACITY_UNLIMITED], or any positive [Int]
 * @param action executed when receiver [AlertDialog] negative button is clicked
 * @see [Channel]
 */
fun AlertDialog.Builder.onNegativeButtonClick(
        @StringRes textId: Int,
        capacity: Int = CAPACITY_SINGLE,
        action: suspend (DialogInterface, Int) -> Unit
) = apply {
    // launch one actor
    val eventActor = actor<AlertDialogEvent>(UI, capacity) {
        for (event in channel) action(event.dialogInterface, event.button)
    }
    // install a listener to activate this actor
    setNegativeButton(textId) { dialogInterface, button ->
        eventActor.offer(AlertDialogEvent(dialogInterface, button))
    }
}

/**
 * @receiver neutral button click events of this [AlertDialog] will be observed
 * @param capacity optional of values [CAPACITY_SINGLE], [CAPACITY_SINGLE_MOST_RECENT],
 * [CAPACITY_UNLIMITED], or any positive [Int]
 * @param action executed when receiver [AlertDialog] neutral button is clicked
 * @see [Channel]
 */
fun AlertDialog.Builder.onNeutralButtonClick(
        @StringRes textId: Int,
        capacity: Int = CAPACITY_SINGLE,
        action: suspend (DialogInterface, Int) -> Unit
) = apply {
    // launch one actor
    val eventActor = actor<AlertDialogEvent>(UI, capacity) {
        for (event in channel) action(event.dialogInterface, event.button)
    }
    // install a listener to activate this actor
    setNeutralButton(textId) { dialogInterface, button ->
        eventActor.offer(AlertDialogEvent(dialogInterface, button))
    }
}