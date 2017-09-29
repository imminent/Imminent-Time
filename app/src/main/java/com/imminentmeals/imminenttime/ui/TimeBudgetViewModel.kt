package com.imminentmeals.imminenttime.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.imminentmeals.imminenttime.repository.ImminentRepository
import com.imminentmeals.imminenttime.repository.TimeBudget
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class TimeBudgetViewModel internal constructor(factory: Factory) : ViewModel() {
    private val timeBudgets = factory.repository()
    val budgets: LiveData<List<TimeBudget>> = timeBudgets.listTimeBudgets()

    @Suppress("unused")
    constructor() : this(DefaultFactory)

    suspend fun addTimeBudget(timeBudget: TimeBudget) = async(CommonPool) {
        timeBudgets.addTimeBudget(timeBudget)
    }


    interface Factory {
        fun repository(): ImminentRepository
    }

    private object DefaultFactory : Factory {
        override fun repository() = ImminentRepository()

    }
}