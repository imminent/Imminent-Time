package com.imminentmeals.imminenttime

import android.app.Application
import android.app.Instrumentation.newApplication
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnitRunner
import com.imminentmeals.imminenttime.TimeBudgetApplication.Companion.database
import com.imminentmeals.imminenttime.repository.database.ImminentDatabase

fun newDatabase(): ImminentDatabase = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getContext(),
        ImminentDatabase::class.java
)
        .allowMainThreadQueries()
        .build()

@Suppress("unused")
class ImminentTestRunner : AndroidJUnitRunner() {
    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application =
            super.newApplication(
                    cl,
                    TimeBudgetApplication::class.java.name,
                    context
            ).apply {
                if (this is TimeBudgetApplication) {
                    factory = object : TimeBudgetApplication.Factory {
                        override fun database(context: Context) = newDatabase()
                    }
                }
            }
}