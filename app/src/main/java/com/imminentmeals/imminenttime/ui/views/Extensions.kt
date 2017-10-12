package com.imminentmeals.imminenttime.ui.views

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int) = LayoutInflater.from(context)
        .inflate(layoutRes, this, false)!!
