package com.imminentmeals.imminenttime

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.imminentmeals.imminenttime.repository.database.ImminentDatabase

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
        override fun database(context: Context) = Room.databaseBuilder(
                context,
                ImminentDatabase::class.java,
                "imminent_db"
        ).build()!!
    }
}