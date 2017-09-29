package com.imminentmeals.imminenttime.testing

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.content.res.Resources.NotFoundException
import android.support.test.espresso.ViewAssertion
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import android.support.test.espresso.NoMatchingViewException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`


fun withRecyclerView(recyclerViewId: Int) = RecyclerViewMatcher(recyclerViewId)

fun hasSize(expectedCount: Int) = RecyclerViewSizeAssertion(expectedCount)

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            internal var resources: Resources? = null
            internal var childView: View? = null

            override fun describeTo(description: Description) {
                val idDescription = try {
                    resources?.getResourceName(recyclerViewId) ?: recyclerViewId
                } catch (var4: Resources.NotFoundException) {
                    "$recyclerViewId (resource name not found)"
                }

                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {
                resources = view.resources

                if (childView == null) {
                    val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position).itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    view === childView
                } else {
                    view === childView!!.findViewById<View>(targetViewId)
                }

            }
        }
    }
}

class RecyclerViewSizeAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter.itemCount, `is`(expectedCount))
    }
}