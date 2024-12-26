package com.example.sportapp.baza

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportapp.food.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val foodDao = AppDatabase.getDatabase(application).foodDao()
    val allFoods: Flow<List<Food>> = foodDao.getAllFoods()
    val lowBmiFoods: Flow<List<Food>> = foodDao.getFoodsForLowBmi()
    val highBmiFoods: Flow<List<Food>> = foodDao.getFoodsForHighBmi()

    fun addFood(food: Food) = viewModelScope.launch {
        foodDao.insertFood(food)
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        foodDao.updateFood(food)
    }

    fun deleteFood(food: Food) = viewModelScope.launch {
        foodDao.deleteFood(food)
    }
}