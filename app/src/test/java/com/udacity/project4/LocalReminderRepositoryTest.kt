package com.udacity.project4

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDao
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test


class LocalReminderRepositoryTest {

    private lateinit var repository: ReminderDataSource

    @MockK(relaxed = true)
    private lateinit var dao: RemindersDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RemindersLocalRepository(dao)
    }

    @Test
    fun testGetRemindersWithSuccess() = runBlocking {
        val data = listOf(mockk<ReminderDTO>())
        coEvery { dao.getReminders() } returns data
        val result = repository.getReminders() as Result.Success<List<ReminderDTO>>
        assertEquals(data, result.data)
    }

    @Test
    fun testGetRemindersWithFailure() = runBlocking {
        val throwable = RuntimeException("Can't ger reminders")
        coEvery { dao.getReminders() } throws throwable
        val result = repository.getReminders() as Result.Error
        assertEquals(throwable.message, result.message)
    }

    @Test
    fun testGetReminderByIdWithSuccess() = runBlocking {
        val data = mockk<ReminderDTO>()
        coEvery { dao.getReminderById(any()) } returns data
        val result = repository.getReminder("123") as Result.Success<ReminderDTO>
        assertEquals(data, result.data)
    }


    @Test
    fun testGetReminderByIdWithNotFoundResult() = runBlocking {
        val expectedResult = Result.Error("Reminder not found!")
        coEvery { dao.getReminderById(any()) } returns null
        val result = repository.getReminder("123") as Result.Error
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetReminderByIdWithFailure() = runBlocking {
        val throwable = RuntimeException("Can't ger reminders")
        coEvery { dao.getReminderById(any()) } throws throwable
        val result = repository.getReminder("123") as Result.Error
        assertEquals(throwable.message, result.message)
    }

    @Test
    fun testSaveReminder() = runBlocking {
        val input = mockk<ReminderDTO>()
        coEvery { dao.saveReminder(any()) } returns Unit
        repository.saveReminder(input)
        coVerify { dao.saveReminder(input) }
    }


    @Test
    fun testDeleteAllReminders() = runBlocking {
        coEvery { dao.deleteAllReminders() } returns Unit
        repository.deleteAllReminders()
        coVerify { dao.deleteAllReminders() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}