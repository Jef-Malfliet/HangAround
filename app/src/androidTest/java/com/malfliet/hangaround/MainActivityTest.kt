package com.malfliet.hangaround

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun onCreateTest() {
        Espresso.onView(ViewMatchers.withId(R.id.activityList))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun onNavigateToActionList() {
        Espresso.onView(ViewMatchers.withId(R.id.activityList))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.activityList))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun onNavigateToFriendsList() {
        Espresso.onView(ViewMatchers.withId(R.id.friends))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.friends))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun onNavigateToProfile() {
        Espresso.onView(ViewMatchers.withId(R.id.profile))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.profile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}