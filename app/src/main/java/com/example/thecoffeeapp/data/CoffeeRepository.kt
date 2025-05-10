package com.example.thecoffeeapp.data

import androidx.lifecycle.LiveData
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoffeeRepository(private val coffeeLogDao: CoffeeLogDao) {
    
    val allLogs: LiveData<List<CoffeeLog>> = coffeeLogDao.getAllLogs()
    val todaysCupCount: LiveData<Int> = coffeeLogDao.getTodaysCupCount()
    val yesterdaysCupCount: LiveData<Int> = coffeeLogDao.getYesterdaysCupCount()
    val todaysLogs: LiveData<List<CoffeeLog>> = coffeeLogDao.getTodaysLogs()
    val yesterdaysLogs: LiveData<List<CoffeeLog>> = coffeeLogDao.getYesterdaysLogs()

    suspend fun addCupOfCoffee(note: String? = null) {
        withContext(Dispatchers.IO) {
            val coffeeLog = CoffeeLog(
                timestamp = Date(),
                note = note
            )
            coffeeLogDao.insertLog(coffeeLog)
        }
    }

    suspend fun deleteLog(coffeeLog: CoffeeLog) {
        withContext(Dispatchers.IO) {
            coffeeLogDao.deleteLog(coffeeLog)
        }
    }

    suspend fun clearAllLogs(): Int = withContext(Dispatchers.IO) {
        coffeeLogDao.deleteAllLogs()
    }

    companion object {
        // Threshold for showing taunts (number of cups)
        const val DAILY_CUP_THRESHOLD = 6
    }
}
