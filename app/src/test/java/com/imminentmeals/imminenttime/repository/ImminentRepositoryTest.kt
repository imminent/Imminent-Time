package com.imminentmeals.imminenttime.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.imminentmeals.imminenttime.assertThat
import com.imminentmeals.imminenttime.repository.database.TimeBudgets
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImminentRepositoryTest {
    private lateinit var repo: ImminentRepository

    @Rule @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repo = ImminentRepository(TestFactory)
        TestTimeBudgets.list = mutableListOf()
    }

    @Test
    fun listTimeBudgetsWithEmptyList() {
        assertThat { repo.listTimeBudgets().value.isEmpty() }
    }

    @Test
    fun listTimeBudgetsWithValues() {
        runBlocking {
            repo.addTimeBudget(TimeBudget("first"))
            repo.addTimeBudget(TimeBudget("second"))
        }
        assertThat {
            with(repo.listTimeBudgets().value) {
                this contains TimeBudget("first")
                this contains TimeBudget("second")
            }
        }
    }

    object TestFactory : ImminentRepository.Factory {
        override fun getTimeBudgets() = TestTimeBudgets
    }

    object TestTimeBudgets : TimeBudgets {
        lateinit var list: MutableList<TimeBudget>

        override fun list(): LiveData<List<TimeBudget>> = MutableLiveData<List<TimeBudget>>().apply {
            value = this@TestTimeBudgets.list
        }

        override fun addTimeBudget(timeBudget: TimeBudget) {
            list.add(timeBudget)
        }
    }
}