package com.imminentmeals.imminenttime.ui.views

import android.content.DialogInterface
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.actor

fun ViewGroup.inflate(@LayoutRes layoutRes: Int) = LayoutInflater.from(context)
        .inflate(layoutRes, this, false)!!

fun View.onClick(capacity: Int = 0, action: suspend () -> Unit) {
    // launch one actor
    val eventActor = actor<Unit>(UI, capacity) {
        for (event in channel) action()
    }
    // install a listener to activate this actor
    setOnClickListener {
        eventActor.offer(Unit)
    }
}

fun AlertDialog.Builder.onPositiveButtonClick(
        @StringRes textId: Int,
        action: suspend (DialogInterface, Int) -> Unit
) = apply {
    data class Event(val dialogInterface: DialogInterface, val button: Int)

    // launch one actor
    val eventActor = actor<Event>(UI) {
        for (event in channel) action(event.dialogInterface, event.button)
    }
    // install a listener to activate this actor
    setPositiveButton(textId) { dialogInterface, button ->
        eventActor.offer(Event(dialogInterface, button))
    }
}