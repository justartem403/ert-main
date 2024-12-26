package com.example.sportapp.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BMICalculatorScreen(onBmiCalculated: (Float?) -> Unit) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf<Float?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Калькулятор ИМТ",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Рост (см)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Вес (кг)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val heightVal = height.toFloatOrNull()
                    val weightVal = weight.toFloatOrNull()

                    if (heightVal != null && weightVal != null && heightVal > 0 && weightVal > 0) {
                        val heightInMeters = heightVal / 100
                        bmi = weightVal / (heightInMeters * heightInMeters)
                        errorMessage = null
                        onBmiCalculated(bmi)
                    } else {
                        errorMessage = "Введите корректные значения"
                        bmi = null
                    }
                } catch (e: Exception) {
                    errorMessage = "Ошибка при расчете. Проверьте введенные данные"
                    bmi = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать ИМТ")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        bmi?.let {
            Text(
                text = "Ваш ИМТ: %.1f".format(it),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = when {
                    it < 18.5 -> "Недостаточный вес"
                    it in 18.5..24.9 -> "Нормальный вес"
                    it in 25.0..29.9 -> "Избыточный вес"
                    else -> "Ожирение"
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}