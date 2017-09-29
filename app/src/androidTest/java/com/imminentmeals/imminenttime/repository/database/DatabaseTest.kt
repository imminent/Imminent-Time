package com.imminentmeals.imminenttime.repository.database

import android.support.test.InstrumentationRegistry
import android.arch.persistence.room.Room
import org.junit.After
import org.junit.Before

abstract class DatabaseTest {
    protected lateinit var db: ImminentDatabase

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                ImminentDatabase::class.java
        )
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun closeDb() {
        db.close()
    }
}
