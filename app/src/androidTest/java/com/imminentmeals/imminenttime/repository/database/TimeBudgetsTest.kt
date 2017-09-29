package com.imminentmeals.imminenttime.repository.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.test.runner.AndroidJUnit4
import com.imminentmeals.imminenttime.repository.TimeBudget
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// TODO: improve with Assertions DSL
@RunWith(AndroidJUnit4::class)
class TimeBudgetsTest : DatabaseTest() {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun duplicateInsertionsReplace() {
        with(db.timeBudgets()) {
            list().liveDataTest {
                addTimeBudget(TimeBudget("replace me", 1))
                addTimeBudget(TimeBudget("replaced", 1))
                assertThat(value, hasSize(1))
                assertThat(value, contains(TimeBudget("replaced", 1)))
            }
        }
    }

    @Test
    fun emptyList() {
        with(db.timeBudgets()) {
            list().liveDataTest {
                assertThat(value, hasSize(0))
            }
        }
    }

    @Test
    fun listsEnteredTimeBudgets() {
        with(db.timeBudgets()) {
            list().liveDataTest {
                addTimeBudget(TimeBudget("first entry"))
                addTimeBudget(TimeBudget("second entry"))
                assertThat(value, contains(
                        TimeBudget("first entry", 1),
                        TimeBudget("second entry", 2))
                )
            }
        }
    }

    private fun <T> LiveData<T>.liveDataTest(block: TestObserver<T>.() -> Unit) {
        val observer = TestObserver<T>()
        observeForever(observer)
        block(observer)
        removeObserver(observer)
    }

    class TestObserver<T> : Observer<T> {
        var value: T? = null

        override fun onChanged(t: T?) {
            value = t
        }
    }
}