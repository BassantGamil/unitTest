package com.udacity.project4

import androidx.test.core.app.ActivityScenario
import com.udacity.project4.locationreminders.RemindersActivity
import org.junit.Before
import org.junit.Test

abstract class BaseTest {
    lateinit var scenario: ActivityScenario<RemindersActivity>
    lateinit var activity: RemindersActivity

    @Before
    fun setUp() {
        createActivity()
    }

    private fun createActivity() {
        scenario = ActivityScenario.launch(RemindersActivity::class.java)
        scenario.onActivity {
            activity = it
        }
    }

    @Test
    abstract fun startTest()
}