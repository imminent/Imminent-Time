package com.imminentmeals.imminenttime.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.imminentmeals.imminenttime.assertThat
import com.imminentmeals.imminenttime.repository.ImminentRepository
import com.imminentmeals.imminenttime.repository.TimeBudget
import com.imminentmeals.imminenttime.repository.database.TimeBudgets
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TimeBudgetViewModelTest {
    private lateinit var viewModel: TimeBudgetViewModel

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = TimeBudgetViewModel(TestFactory)
    }

    @Test
    fun getBudgetsWithEmptyList() {
        assertThat { viewModel.budgets.value.isEmpty() }
    }

    @Test
    fun getBudgetsWithBudgets() {
        runBlocking {
            viewModel.addTimeBudget(TimeBudget("first")).await()
            viewModel.addTimeBudget(TimeBudget("second")).await()
            assertThat {
                viewModel.budgets.value contains TimeBudget("first")
                viewModel.budgets.value contains TimeBudget("second")
            }
        }
    }

    @Test
    fun observingBudgets() {
        val observer = object : Observer<List<TimeBudget>> {
            var list: List<TimeBudget>? = null

            override fun onChanged(t: List<TimeBudget>?) {
                list = t
            }
        }
        viewModel.budgets.observeForever(observer)
        assertThat { viewModel.budgets.hasActiveObservers().isTrue() }

        runBlocking {
            viewModel.addTimeBudget(TimeBudget("first")).await()
            assertThat { observer.list contains TimeBudget("first") }

            viewModel.addTimeBudget(TimeBudget("second")).await()
            assertThat { observer.list contains TimeBudget("second") }

            viewModel.budgets.removeObserver(observer)
        }
    }

    object TestFactory : TimeBudgetViewModel.Factory {
        override fun repository() = ImminentRepository(object : ImminentRepository.Factory {
            override fun getTimeBudgets() = object : TimeBudgets {
                val list: MutableList<TimeBudget> = mutableListOf()

                override fun list(): LiveData<List<TimeBudget>> = MutableLiveData<List<TimeBudget>>().apply {
                    value = list
                }

                override fun addTimeBudget(timeBudget: TimeBudget) {
                    list.add(timeBudget)
                }
            }
        })
    }
}