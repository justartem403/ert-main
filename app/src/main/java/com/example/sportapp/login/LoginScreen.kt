package com.example.sportapp.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportapp.baza.FirebaseHelper
import com.example.sportapp.food.User


object UserRepository {
    private val registeredUsers = mutableMapOf<String, User>()

    fun registerUser(username: String, password: String, email: String): Boolean {
        if (registeredUsers.containsKey(username)) {
            return false
        }
        registeredUsers[username] = User(username, password, email)
        FirebaseHelper.registerUser(email, username, password)
        return true
    }

    fun loginUser(username: String, password: String): Boolean {
        FirebaseHelper.signInUser(username, password)
        return FirebaseHelper.getCurrentUser() != null;
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoginMode by remember { mutableStateOf(true) }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isLoginMode) "Вход в приложение" else "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (!isLoginMode) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisibility) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                if (isLoginMode) {
                    if (UserRepository.loginUser(username, password)) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMessage = "Неверный логин или пароль"
                    }
                } else {
                    if (validateRegistration(username, password, email)) {
                        if (UserRepository.registerUser(username, password, email)) {
                            errorMessage = "Регистрация прошла успешно. Ваш email: $email"
                            isLoginMode = true
                        } else {
                            errorMessage = "Пользователь с таким логином уже существует"
                        }
                    } else {
                        errorMessage = "Некорректные данные для регистрации"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(if (isLoginMode) "Войти" else "Зарегистрироваться")
        }

        TextButton(
            onClick = {
                isLoginMode = !isLoginMode
                errorMessage = null
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (isLoginMode) "Нет аккаунта? Зарегистрируйтесь" else "Уже есть аккаунт? Войти")
        }
    }
}

fun validateRegistration(username: String, password: String, email: String): Boolean {
    return username.length >= 4 &&
            password.length >= 6 &&
            email.contains("@") &&
            email.contains(".")
}