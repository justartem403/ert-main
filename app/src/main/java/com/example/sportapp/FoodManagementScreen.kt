package com.example.sportapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodManagementScreen(
    currentBmi: Float? = null,
    viewModel: FoodViewModel = viewModel()
) {
    data class FoodFormState(
        val name: String = "",
        val calories: String = "",
        val proteins: String = "",
        val fats: String = "",
        val carbs: String = ""
    )

    var formState by remember { mutableStateOf(FoodFormState()) }
    var isEditMode by remember { mutableStateOf(false) }
    var currentEditFood by remember { mutableStateOf<Food?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val foods by viewModel.allFoods.collectAsState(initial = emptyList())
    val lowBmiFoods by viewModel.lowBmiFoods.collectAsState(initial = emptyList())
    val highBmiFoods by viewModel.highBmiFoods.collectAsState(initial = emptyList())

    fun validateForm(): Boolean {
        return when {
            formState.name.isBlank() -> {
                errorMessage = "Введите название продукта"
                false
            }
            formState.calories.toIntOrNull() == null -> {
                errorMessage = "Введите корректное значение калорий"
                false
            }
            formState.proteins.toFloatOrNull() == null -> {
                errorMessage = "Введите корректное значение белков"
                false
            }
            formState.fats.toFloatOrNull() == null -> {
                errorMessage = "Введите корректное значение жиров"
                false
            }
            formState.carbs.toFloatOrNull() == null -> {
                errorMessage = "Введите корректное значение углеводов"
                false
            }
            else -> true
        }
    }

    fun clearForm() {
        formState = FoodFormState()
        isEditMode = false
        currentEditFood = null
        showError = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Рекомендованные продукты по ИМТ
        if (currentBmi != null) {
            if (currentBmi < 18.5 && lowBmiFoods.isNotEmpty()) {
                Text(
                    text = "Рекомендованные продукты для набора веса:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lowBmiFoods) { food ->
                        FoodItem(
                            food = food,
                            onEdit = {},
                            onDelete = {},
                            currentBmi = currentBmi,
                            isRecommendationMode = true
                        )
                    }
                }
            }

            if (currentBmi > 25 && highBmiFoods.isNotEmpty()) {
                Text(
                    text = "Рекомендованные продукты для снижения веса:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(highBmiFoods) { food ->
                        FoodItem(
                            food = food,
                            onEdit = {},
                            onDelete = {},
                            currentBmi = currentBmi,
                            isRecommendationMode = true
                        )
                    }
                }
            }
        }

        // Форма добавления/редактирования продукта
        Text(
            text = if (isEditMode) "Редактировать продукт" else "Добавить продукт",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Поля ввода
        OutlinedTextField(
            value = formState.name,
            onValueChange = { formState = formState.copy(name = it) },
            label = { Text("Название продукта") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.calories,
            onValueChange = { formState = formState.copy(calories = it) },
            label = { Text("Калории") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.proteins,
            onValueChange = { formState = formState.copy(proteins = it) },
            label = { Text("Белки (г)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.fats,
            onValueChange = { formState = formState.copy(fats = it) },
            label = { Text("Жиры (г)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.carbs,
            onValueChange = { formState = formState.copy(carbs = it) },
            label = { Text("Углеводы (г)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                if (validateForm()) {
                    val foodItem = Food(
                        name = formState.name,
                        calories = formState.calories.toInt(),
                        proteins = formState.proteins.toFloat(),
                        fats = formState.fats.toFloat(),
                        carbs = formState.carbs.toFloat(),
                        recommendedForBmiUnder = currentBmi != null && currentBmi < 18.5f,
                        recommendedForBmiOver = currentBmi != null && currentBmi > 25f
                    )

                    if (isEditMode && currentEditFood != null) {
                        viewModel.updateFood(foodItem.copy(id = currentEditFood!!.id))
                    } else {
                        viewModel.addFood(foodItem)
                    }

                    clearForm()
                } else {
                    showError = true
                }
            }
        ) {
            Text(if (isEditMode) "Обновить продукт" else "Добавить продукт")
        }

        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Список продуктов
        AnimatedVisibility(
            visible = foods.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = foods,
                    key = { it.id }
                ) { food ->
                    FoodItem(
                        food = food,
                        onEdit = {
                            formState = FoodFormState(
                                name = food.name,
                                calories = food.calories.toString(),
                                proteins = food.proteins.toString(),
                                fats = food.fats.toString(),
                                carbs = food.carbs.toString()
                            )
                            isEditMode = true
                            currentEditFood = food
                            showError = false
                        },
                        onDelete = {
                            viewModel.deleteFood(food)
                        },
                        currentBmi = currentBmi
                    )
                }
            }
        }
    }
}

@Composable
fun NutrientProgress(
    label: String,
    value: Int,
    maxValue: Int,
    unit: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$value $unit",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        LinearProgressIndicator(
            progress = { (value.toFloat() / maxValue).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun FoodItem(
    food: Food,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    currentBmi: Float? = null,
    isRecommendationMode: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${food.calories} ккал",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!isRecommendationMode) {
                    Row {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, "Редактировать")
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, "Удалить")
                        }
                    }
                }
            }


            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                NutrientProgress(
                    label = "Белки",
                    value = food.proteins.toInt(),
                    maxValue = 100,
                    unit = "г"
                )
                NutrientProgress(
                    label = "Жиры",
                    value = food.fats.toInt(),
                    maxValue = 100,
                    unit = "г"
                )
                NutrientProgress(
                    label = "Углеводы",
                    value = food.carbs.toInt(),
                    maxValue = 100,
                    unit = "г"
                )
            }


            if (currentBmi != null) {
                if (currentBmi < 18.5 && food.recommendedForBmiUnder) {
                    Text(
                        text = "Рекомендовано при низком ИМТ",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (currentBmi > 25 && food.recommendedForBmiOver) {
                    Text(
                        text = "Рекомендовано при высоком ИМТ",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}