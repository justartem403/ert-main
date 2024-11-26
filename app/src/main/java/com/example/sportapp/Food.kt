package com.example.sportapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,           // Название продукта
    val calories: Int,          // Калории
    val proteins: Float,        // Белки
    val fats: Float,           // Жиры
    val carbs: Float,          // Углеводы
    val recommendedForBmiUnder: Boolean = false,  // Рекомендуется при низком ИМТ
    val recommendedForBmiOver: Boolean = false    // Рекомендуется при высоком ИМТ
)