package com.udacity.project4

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemindersListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RemindersListViewModel

    @MockK(relaxed = true)
    private lateinit var app: Application

    @MockK(relaxed = true)
    private lateinit var dataSource: ReminderDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = RemindersListViewModel(app, dataSource)
    }

    @Test
    fun testLoadRemindersWithSuccess() = runBlocking {
        val entity = ReminderDTO("Title", "Description", "location", 10.0, 20.0)
        val data = listOf(entity)
        coEvery { dataSource.getReminders() } returns Result.Success(data)
        viewModel.loadReminders()
        assertNull(viewModel.showSnackBar.value)
        data.forEachIndexed { index, item ->
            assertEquals(item.id, viewModel.remindersList.value?.get(index)?.id)
            assertEquals(item.title, viewModel.remindersList.value?.get(index)?.title)
            assertEquals(item.description, viewModel.remindersList.value?.get(index)?.description)
            assertEquals(item.location, viewModel.remindersList.value?.get(index)?.location)
            assertEquals(item.latitude, viewModel.remindersList.value?.get(index)?.latitude)
            assertEquals(item.longitude, viewModel.remindersList.value?.get(index)?.longitude)
        }
    }

    @Test
    fun testLoadRemindersWithFailure() = runBlocking {
        val error = "Can't load reminders"
        coEvery { dataSource.getReminders() } returns Result.Error(error)
        viewModel.loadReminders()
        assertNull(viewModel.remindersList.value)
        assertNotNull(viewModel.showSnackBar)
        assertEquals(error, viewModel.showSnackBar.value)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}