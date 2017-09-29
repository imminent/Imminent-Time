package com.imminentmeals.imminenttime.repository

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity
data class TimeBudget(val label: String, @PrimaryKey(autoGenerate = true) val id: Long = 0)