package com.example.thecoffeeapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.data.CoffeeDatabase
import com.example.thecoffeeapp.data.CoffeeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.lang.ref.WeakReference

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val appReference = WeakReference(application)
    private val repository: CoffeeRepository
    val todaysCupCount: LiveData<Int>
    val yesterdaysCupCount: LiveData<Int>

    private val _dailyQuote = MutableStateFlow<String?>(null)
    val dailyQuote: StateFlow<String?> = _dailyQuote

    private val _showTaunt = MutableStateFlow<String?>(null)
    val showTaunt: StateFlow<String?> = _showTaunt

    init {
        val coffeeLogDao = CoffeeDatabase.getDatabase(application).coffeeLogDao()
        repository = CoffeeRepository(coffeeLogDao)
        todaysCupCount = repository.todaysCupCount
        yesterdaysCupCount = repository.yesterdaysCupCount
        loadDailyQuote()
    }

    private fun loadDailyQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                appReference.get()?.let { app ->
                    val quotes = app.resources.getStringArray(R.array.daily_quotes)
                    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                    val quoteIndex = dayOfYear % quotes.size
                    withContext(Dispatchers.Main) {
                        _dailyQuote.value = quotes[quoteIndex]
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _dailyQuote.value = "Error loading daily quote"
                }
            }
        }
    }

    fun addCupOfCoffee() {
        viewModelScope.launch {
            try {
                repository.addCupOfCoffee()
                checkCupThreshold()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun checkCupThreshold() {
        viewModelScope.launch {
            try {
                if ((todaysCupCount.value ?: 0) >= CoffeeRepository.DAILY_CUP_THRESHOLD) {
                    appReference.get()?.let { app ->
                        val taunts = app.resources.getStringArray(R.array.coffee_taunts)
                        _showTaunt.value = taunts.random()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearTaunt() {
        _showTaunt.value = null
    }

    override fun onCleared() {
        super.onCleared()
        appReference.clear()
    }
}
