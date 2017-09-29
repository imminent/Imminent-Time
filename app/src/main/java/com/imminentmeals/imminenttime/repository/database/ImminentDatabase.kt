package com.imminentmeals.imminenttime.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.imminentmeals.imminenttime.repository.TimeBudget
import java.util.concurrent.TimeUnit

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