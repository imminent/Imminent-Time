package com.imminentmeals.imminenttime.repository.database

import android.arch.persistence.room.*
import android.content.Context
import com.imminentmeals.imminenttime.repository.TimeBudget
import java.util.concurrent.TimeUnit

fun createImminentDatabase(context: Context) =
        checkNotNull(Room.databaseBuilder(
                context,
                ImminentDatabase::class.java,
                "imminent_db"
        ).build())

@Database(entities = arrayOf(TimeBudget::class), version = 1)
@TypeConverters(Converters::class)
abstract class ImminentDatabase : RoomDatabase() {
    abstract fun timeBudgets(): TimeBudgets
}

class Converters {
    @TypeConverter
    fun fromValue(value: String?) = value?.let { TimeUnit.valueOf(value) }

    @TypeConverter
    fun timeUnitToValue(timeUnit: TimeUnit?) = timeUnit?.let { timeUnit.name }
}