package com.imminentmeals.imminenttime

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.imminentmeals.imminenttime.testing.hasSize
import com.imminentmeals.imminenttime.testing.withRecyclerView

fun timeBudget(block: TimeBudgetRobot.() -> Unit) = TimeBudgetRobot().apply(block)

class TimeBudgetRobot {
    private val title by lazy(LazyThreadSafetyMode.NONE) { withText(R.string.title_time_budget) }
    private val list by lazy(LazyThreadSafetyMode.NONE) { withRecyclerView(android.R.id.list) }
    private val action by lazy(LazyThreadSafetyMode.NONE) { withId(R.id.fab) }

    // BEGIN: title
    fun hasTitle(title: String) {
        onView(this.title).check(matches(withText(title)))
    }
    // END: title

    // BEGIN: action
    fun addBudget(block: AddBudgetRobot.() -> Unit): AddBudgetRobot {
        onView(action).perform(click())
        return AddBudgetRobot().apply(block)
    }
    // END: action

    // BEGIN: list
    fun hasLabelInRow(label: String, row: Int) {
        onView(list.atPosition(row)).check(matches(withText(label)))
    }

    fun hasNoBudgets() {
        onView(withId(android.R.id.list)).check(hasSize(0))
    }
    // END: list
}

class AddBudgetRobot {
    private val budgetLabel by lazy(LazyThreadSafetyMode.NONE) { withId(android.R.id.input) }
    private val add by lazy(LazyThreadSafetyMode.NONE) { withText(R.string.button_add) }

    fun isDisplayingDialog() {
        onView(budgetLabel).check(matches(isDisplayed()))
    }

    fun didCloseDialog() {
        onView(budgetLabel).check(doesNotExist())
    }

    // BEGIN: budget label
    fun hasHint(hint: String) {
        onView(budgetLabel).check(matches(withHint(hint)))
    }

    fun label(label: String) {
        onView(budgetLabel).perform(clearText(), typeText(label))
    }
    // END: budget label

    // BEGIN: add
    fun add(block: AddBudgetRobot.() -> Unit): AddBudgetRobot {
        onView(add).perform(click())
        return AddBudgetRobot().apply(block)
    }
    // END: add
}