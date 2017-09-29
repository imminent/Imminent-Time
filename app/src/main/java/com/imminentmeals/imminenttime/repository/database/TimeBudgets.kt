package com.imminentmeals.imminenttime.repository.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.imminentmeals.imminenttime.repository.TimeBudget

@Dao
interface TimeBudgets {

    @Query("SELECT * FROM timeBudget")
    fun list(): LiveData<List<TimeBudget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTimeBudget(timeBudget: TimeBudget)
}