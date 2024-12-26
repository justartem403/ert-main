package com.example.sportapp.Screen

sealed class Screen(val route: String) {
    // Основные экраны
    data object Home : Screen("home")
    data object BMICalculator : Screen("bmi_calculator")
    data object Nutrition : Screen("nutrition")
    data object Workouts : Screen("workouts")
    data object Notes : Screen("notes")
    data object Login : Screen("login")
    data object Profile : Screen("profile")
    data object FoodManagement : Screen("food_management")

    data object WorkoutDetails : Screen("workout_details/{workoutId}") {
        fun createRoute(workoutId: String) = "workout_details/$workoutId"
    }

    data object MealPlan : Screen("meal_plan/{dayId}") {
        fun createRoute(dayId: String) = "meal_plan/$dayId"
    }

    companion object {
        fun fromRoute(route: String): Screen {
            return when (route) {
                "home" -> Home
                "bmi_calculator" -> BMICalculator
                "nutrition" -> Nutrition
                "workouts" -> Workouts
                "notes" -> Notes
                "workout_details" -> WorkoutDetails
                "meal_plan" -> MealPlan
                "login" -> Login
                "profile" -> Profile
                "food_management" -> FoodManagement // Добавляем обработку нового маршрута
                else -> Home
            }
        }
    }
}