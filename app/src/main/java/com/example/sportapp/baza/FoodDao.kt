package com.example.sportapp.baza

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sportapp.food.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods")
    fun getAllFoods(): Flow<List<Food>>

    @Insert
    suspend fun insertFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM foods WHERE recommendedForBmiUnder = 1")
    fun getFoodsForLowBmi(): Flow<List<Food>>

    @Query("SELECT * FROM foods WHERE recommendedForBmiOver = 1")
    fun getFoodsForHighBmi(): Flow<List<Food>>
}