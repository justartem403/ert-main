package com.example.sportapp.GYM

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String,
    val description: String
)

data class WorkoutPlan(
    val title: String,
    val description: String,
    val exercises: List<Exercise>
)

object WorkoutPlans {
    fun getWorkoutPlanForBMI(bmi: Float?): WorkoutPlan {
        return when {
            bmi == null -> defaultPlan
            bmi < 18.5 -> underweightPlan
            bmi in 18.5..24.9 -> normalWeightPlan
            bmi in 25.0..29.9 -> overweightPlan
            else -> obesityPlan
        }
    }

    private val defaultPlan = WorkoutPlan(
        title = "Общий план тренировок",
        description = "Базовый план для начинающих",
        exercises = listOf(
            Exercise("Приседания", 3, "10-12", "Базовое упражнение для ног"),
            Exercise("Отжимания", 3, "8-10", "Упражнение для верхней части тела"),
            Exercise("Планка", 3, "30 сек", "Упражнение для корпуса")
        )
    )

    private val underweightPlan = WorkoutPlan(
        title = "План для набора массы",
        description = "Силовые упражнения с акцентом на наращивание мышечной массы",
        exercises = listOf(
            Exercise("Приседания со штангой", 4, "6-8", "Тяжелые веса, медленный темп"),
            Exercise("Жим штанги лежа", 4, "6-8", "Базовое упражнение для груди"),
            Exercise("Тяга штанги в наклоне", 4, "6-8", "Упражнение для спины"),
            Exercise("Жим гантелей стоя", 3, "8-10", "Упражнение для плеч")
        )
    )

    private val normalWeightPlan = WorkoutPlan(
        title = "План поддержания формы",
        description = "Сбалансированная программа для поддержания текущей формы",
        exercises = listOf(
            Exercise("Приседания", 3, "12-15", "Классические приседания"),
            Exercise("Отжимания", 3, "10-12", "Классические отжимания"),
            Exercise("Подтягивания", 3, "по возможностям", "Подтягивания на турнике"),
            Exercise("Планка", 3, "45 сек", "Статическое упражнение")
        )
    )

    private val overweightPlan = WorkoutPlan(
        title = "План снижения веса",
        description = "Программа с акцентом на жиросжигание",
        exercises = listOf(
            Exercise("Берпи", 4, "30 сек", "Интенсивное кардио"),
            Exercise("Прыжки на скакалке", 3, "2 мин", "Кардио нагрузка"),
            Exercise("Приседания с выпрыгиванием", 3, "12-15", "Взрывная нагрузка"),
            Exercise("Скручивания", 3, "20", "Для пресса")
        )
    )

    private val obesityPlan = WorkoutPlan(
        title = "План для начального уровня",
        description = "Безопасные упражнения для постепенного снижения веса",
        exercises = listOf(
            Exercise("Ходьба", 1, "30 мин", "Кардио низкой интенсивности"),
            Exercise("Приседания с поддержкой", 3, "10", "Облегченный вариант приседаний"),
            Exercise("Отжимания от стены", 3, "10-12", "Безопасный вариант отжиманий"),
            Exercise("Подъемы рук с гантелями", 3, "12-15", "Легкие веса")
        )
    )
}