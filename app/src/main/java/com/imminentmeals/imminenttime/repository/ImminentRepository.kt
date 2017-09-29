package com.imminentmeals.imminenttime.repository

import com.imminentmeals.imminenttime.TimeBudgetApplication
import com.imminentmeals.imminenttime.repository.database.TimeBudgets

class ImminentRepository internal constructor(factory: Factory) {
    private val timeBudgets = factory.getTimeBudgets()

    constructor() : this(DefaultFactory)

    fun listTimeBudgets() = timeBudgets.list()

    suspend fun addTimeBudget(timeBudget: TimeBudget) {
        timeBudgets.addTimeBudget(timeBudget)
    }

    interface Factory {
        fun getTimeBudgets(): TimeBudgets
    }

    private object DefaultFactory : Factory {
        override fun getTimeBudgets() = TimeBudgetApplication.database.timeBudgets()
    }
}