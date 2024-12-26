package com.example.sportapp.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(lastBmi: Float?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Профиль пользователя",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileInfoItem("ФИО", "Алиев Артём")
        ProfileInfoItem("Возраст", "19 лет")

        lastBmi?.let { bmi ->
            ProfileInfoItem(
                "Последний ИМТ",
                "%.1f (${getBmiCategory(bmi)})".format(bmi)
            )
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun getBmiCategory(bmi: Float): String = when {
    bmi < 18.5 -> "Недостаточный вес"
    bmi in 18.5..24.9 -> "Нормальный вес"
    bmi in 25.0..29.9 -> "Избыточный вес"
    else -> "Ожирение"
}