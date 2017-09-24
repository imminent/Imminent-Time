package com.imminentmeals.imminenttime

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TimeBudgetActivityTest {
    @Rule @JvmField var activityRule = ActivityTestRule(TimeBudgetActivity::class.java)

    @Test fun hasTimeBudgeTitle() {
        timeBudget {
            hasTitle("Imminent Time")
        }
    }

    @Test fun addBudgetOpensWhenFloatingActionButtonIsTapped() {
        timeBudget {

        }.addBudget {
            isDisplayingDialog()
        }
    }

    @Test fun addBudgetClosesWhenAddIsTapped() {
        timeBudget {

        }.addBudget {
            label("Imminent")
        }.add {
            didCloseDialog()
        }
    }

    @Test fun addBudgetHasHint() {
        timeBudget {

        }.addBudget {
            hasHint("Budget label")
        }
    }

    @Test fun addBudgetAddIsDisabledWhenLabelIsEmpty() {
        timeBudget {

        }.addBudget {

        }.add {
            isDisplayingDialog()
        }
    }
}
