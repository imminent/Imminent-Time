package com.imminentmeals.imminenttime

import android.app.Application
import android.content.Context
import com.imminentmeals.imminenttime.repository.database.ImminentDatabase
import com.imminentmeals.imminenttime.repository.database.createImminentDatabase

class TimeBudgetApplication internal constructor(internal var factory: Factory): Application() {

    companion object {
        lateinit var database: ImminentDatabase
    }

    @Suppress("unused")
    constructor() : this(DefaultFactory)

    override fun onCreate() {
        super.onCreate()
        database = factory.database(this)
    }

    interface Factory {
        fun database(context: Context): ImminentDatabase
    }

    private object DefaultFactory : Factory {
        override fun database(context: Context) = createImminentDatabase(context)
    }
}