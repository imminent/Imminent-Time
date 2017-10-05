package com.imminentmeals.imminenttime.repository

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class TimeBudget(val label: String, @PrimaryKey(autoGenerate = true) val id: Long = 0)