package com.example.thecoffeeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "coffee_logs")
data class CoffeeLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Date,
    val cupCount: Int = 1,
    val note: String? = null
)
