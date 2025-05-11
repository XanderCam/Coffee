package com.example.thecoffeeapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.Date

@Dao
interface CoffeeLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(coffeeLog: CoffeeLog)

    @Query("SELECT * FROM coffee_logs ORDER BY timestamp DESC")
    fun getAllLogs(): LiveData<List<CoffeeLog>>

    @Query("SELECT COUNT(*) FROM coffee_logs WHERE date(timestamp/1000, 'unixepoch') = date('now')")
    fun getTodaysCupCount(): LiveData<Int>

    @Query("""
        SELECT COUNT(*) FROM coffee_logs 
        WHERE date(timestamp/1000, 'unixepoch') = date('now', '-1 day')
    """)
    fun getYesterdaysCupCount(): LiveData<Int>

    @Query("SELECT * FROM coffee_logs WHERE date(timestamp/1000, 'unixepoch') = date('now')")
    fun getTodaysLogs(): LiveData<List<CoffeeLog>>

    @Query("""
        SELECT * FROM coffee_logs 
        WHERE date(timestamp/1000, 'unixepoch') = date('now', '-1 day')
    """)
    fun getYesterdaysLogs(): LiveData<List<CoffeeLog>>

    @Delete
    suspend fun deleteLog(coffeeLog: CoffeeLog)

    @Query("DELETE FROM coffee_logs")
    suspend fun deleteAllLogs(): Int
}
