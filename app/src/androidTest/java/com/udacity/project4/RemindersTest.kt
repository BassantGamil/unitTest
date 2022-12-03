package com.udacity.project4

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import java.lang.Math.random

class RemindersTest : BaseTest() {

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    override fun startTest() {
        onView(withId(R.id.reminderssRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.addReminderFAB)).perform(click())
        val title = "My work location ${random()}"
        val description = "This is my work location ${random()}"
        onView(withId(R.id.reminderTitle)).perform(typeText(title))
        onView(withId(R.id.reminderDescription)).perform(typeText(description))
        onView(withId(R.id.selectLocation)).perform(click())
        onView(withId(R.id.save_button)).perform(click())
        onView(withId(R.id.saveReminder)).perform(click())

        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(description)).check(matches(isDisplayed()))
    }

    @Test
    fun testSaveReminderWithEmptyTitle() {
        onView(withId(R.id.reminderssRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.addReminderFAB)).perform(click())
        val description = "This is my work location ${random()}"
        onView(withId(R.id.reminderDescription)).perform(typeText(description))
        Espresso.pressBack()
        onView(withId(R.id.saveReminder)).perform(click())
        checkSnackBarTextMatches(R.string.err_enter_title)
    }

    @Test
    fun testSaveReminderWithNotSelectedLocation() {
        onView(withId(R.id.reminderssRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.addReminderFAB)).perform(click())
        val title = "My work location ${random()}"
        val description = "This is my work location ${random()}"
        onView(withId(R.id.reminderTitle)).perform(typeText(title))
        onView(withId(R.id.reminderDescription)).perform(typeText(description))
        Espresso.pressBack()
        onView(withId(R.id.saveReminder)).perform(click())
        checkSnackBarTextMatches(R.string.err_select_location)
    }
}