package com.udacity.project4

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveReminderViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SaveReminderViewModel

    @MockK(relaxed = true)
    private lateinit var app: Application

    @MockK(relaxed = true)
    private lateinit var dataSource: ReminderDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = SaveReminderViewModel(app, dataSource)
    }

    @Test
    fun testSaveReminderWithEmptyTitleThenReturnTitleValidationError() {
        val reminderItem = ReminderDataItem(
            title = "",
            description = "Description",
            location = "Location",
            longitude = 12.0,
            latitude = 12.0
        )
        viewModel.validateAndSaveReminder(reminderItem)
        assertEquals(R.string.err_enter_title, viewModel.showSnackBarInt.value)
    }

    @Test
    fun testSaveReminderWithEmptyDescriptionThenReturnTitleValidationError() {
        val reminderItem = ReminderDataItem(
            title = "Title",
            description = "Description",
            location = "",
            longitude = 12.0,
            latitude = 12.0
        )
        viewModel.validateAndSaveReminder(reminderItem)
        assertEquals(R.string.err_select_location, viewModel.showSnackBarInt.value)
    }

    @Test
    fun testSaveReminderWithValidDataThenSaveReminder() {
        val saveMessage = "Reminder Saved !"
        every { app.getString(R.string.reminder_saved) } returns saveMessage
        val reminderItem = ReminderDataItem(
            title = "Title",
            description = "Description",
            location = "Location",
            longitude = 12.0,
            latitude = 12.0
        )
        viewModel.validateAndSaveReminder(reminderItem)
        assertEquals(saveMessage, viewModel.showToast.value)
        assertEquals(NavigationCommand.Back, viewModel.navigationCommand.value)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}