package com.example.sportapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.sportapp.Screen.Screen
import com.example.sportapp.ui.theme.SportAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = Screen.fromRoute(currentDestination?.route ?: "")

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentScreen) {
                            Screen.Home -> "Спортивное приложение"
                            Screen.BMICalculator -> "Калькулятор ИМТ"
                            Screen.Nutrition -> "Питание"
                            Screen.Workouts -> "Тренировки"
                            Screen.Notes -> "Заметки"
                            else -> "Спортивное приложение"
                        }
                    )
                },
                navigationIcon = {
                    if (currentDestination?.route != Screen.Home.route) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Меню")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                BottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentScreen == screen.screen,
                        onClick = {
                            navController.navigate(screen.screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(lastBmi = null)
            }

            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.BMICalculator.route) {
                BMICalculatorScreen { bmi ->
                    navController.navigate("food_management/${bmi ?: "null"}")
                }
            }

            composable(Screen.Nutrition.route) {
                NutritionScreen {
                    navController.navigate("food_management/null")
                }
            }
            composable(Screen.Workouts.route) { WorkoutsScreen() }
            composable(Screen.Notes.route) { NotesScreen() }
            composable(
                "food_management/{bmi}",
                arguments = listOf(navArgument("bmi") { type = NavType.StringType; nullable = true })
            ) { backStackEntry ->
                FoodManagementScreen(backStackEntry.arguments?.getString("bmi")?.toFloatOrNull())
            }
            composable(
                Screen.WorkoutDetails.route,
                arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
            ) { backStackEntry ->
                WorkoutDetailsScreen(backStackEntry.arguments?.getString("workoutId"))
            }
            composable(
                Screen.MealPlan.route,
                arguments = listOf(navArgument("dayId") { type = NavType.StringType })
            ) { backStackEntry ->
                MealPlanScreen(backStackEntry.arguments?.getString("dayId"))
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Настройки") },
                onClick = { showMenu = false },
                leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("Профиль") },
                onClick = { showMenu = false },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("О приложении") },
                onClick = { showMenu = false },
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) }
            )
        }
    }
}

private data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

private val BottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Filled.Home, "Главная"),
    BottomNavItem(Screen.BMICalculator, Icons.Filled.Calculate, "ИМТ"),
    BottomNavItem(Screen.Nutrition, Icons.Filled.Restaurant, "Питание"),
    BottomNavItem(Screen.Workouts, Icons.Filled.FitnessCenter, "Тренировки")
)