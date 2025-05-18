package com.example.foodvisior

import AppDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodvisior.databinding.ActivityCaloriesCalculationBinding

class CalorieCalculationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCaloriesCalculationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaloriesCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get BMI and age from the previous activity
        val bmi = intent.getFloatExtra("BMI", 0f)
        val age = intent.getIntExtra("AGE", 0)

        // Display BMI result
        binding.tvBmiResult.text = "Your BMI is: $bmi"

        // Set up the Spinner for activity levels
        val activityLevels = arrayOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Super Active")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, activityLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerActivityLevel.adapter = adapter

        // Set the spinner item selected listener
        binding.spinnerActivityLevel.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedActivityLevel = parent.getItemAtPosition(position).toString()
                // Calculate and display the daily calorie intake based on the selected activity level
                calculateDailyCalorieIntake(bmi, age, selectedActivityLevel)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle when nothing is selected (optional)
            }
        })
    }

    private fun calculateDailyCalorieIntake(bmi: Float, age: Int, activityLevel: String) {
        // For simplicity, let's assume we use a basic formula for calorie needs calculation
        val bmr = when {
            age < 18 -> 2000f // Example baseline BMR for children
            bmi < 18.5 -> 2500f // Example baseline for underweight
            bmi in 18.5..24.9 -> 2200f // Example for normal weight
            else -> 2700f // Example for overweight/obesity
        }

        val activityFactor = when (activityLevel) {
            "Sedentary" -> 1.2
            "Lightly Active" -> 1.375
            "Moderately Active" -> 1.55
            "Very Active" -> 1.725
            "Super Active" -> 1.9
            else -> 1.2 // Default to Sedentary if no match
        }

        val dailyCalories = bmr * activityFactor
        binding.tvDailyCalorieIntake.text = "Daily Calorie Intake: $dailyCalories kcal"

        // Display food recommendations based on BMI category
        val foodRecommendations = getFoodRecommendations(bmi)
        binding.tvFoodRecommendations.text = foodRecommendations

        // Display water intake recommendation based on BMI
        val waterIntakeRecommendation = getWaterRecommendation(bmi)
        binding.tvWaterIntake.text = "Recommended Water Intake: $waterIntakeRecommendation"

        // Display protein, carbs, and vitamins recommendations
        val (protein, carbs, vitamins) = getNutrientRecommendations(bmi)
        binding.tvProteinIntake.text = "Protein: $protein grams/day"
        binding.tvCarbsIntake.text = "Carbohydrates: $carbs grams/day"
        binding.tvVitaminsIntake.text = "Vitamins: Focus on nutrient-rich foods."
    }

    private fun getFoodRecommendations(bmi: Float): String {
        return when {
            bmi < 18.5 -> {
                "To gain weight healthily, focus on nutrient-dense foods such as: \n" +
                        "• Whole grains (e.g., oats, brown rice, whole wheat pasta) \n" +
                        "• Healthy fats (e.g., avocados, nuts, seeds, olive oil) \n" +
                        "• Protein-rich foods (e.g., chicken, eggs, beans, lentils) \n" +
                        "• Dairy products (e.g., cheese, milk, yogurt)"
            }
            bmi in 18.5..24.9 -> {
                "To maintain a healthy weight, focus on a balanced diet with: \n" +
                        "• Fresh fruits and vegetables \n" +
                        "• Whole grains (e.g., quinoa, barley, whole wheat) \n" +
                        "• Lean proteins (e.g., fish, tofu, chicken) \n" +
                        "• Healthy fats (e.g., olive oil, nuts, seeds)"
            }
            bmi in 25.0..29.9 -> {
                "To manage weight, focus on nutrient-dense, low-calorie foods: \n" +
                        "• Leafy greens and non-starchy vegetables \n" +
                        "• Whole grains (e.g., quinoa, brown rice, oats) \n" +
                        "• Lean protein (e.g., fish, skinless poultry, legumes) \n" +
                        "• Healthy fats in moderation (e.g., olive oil, nuts)"
            }
            else -> {
                "To reduce weight and improve health, focus on: \n" +
                        "• Vegetables and fruits (high in fiber and low in calories) \n" +
                        "• Lean proteins (e.g., chicken, turkey, fish) \n" +
                        "• Whole grains (e.g., brown rice, quinoa, oats) \n" +
                        "• Hydration (e.g., drink plenty of water and herbal teas)"
            }
        }
    }

    private fun getWaterRecommendation(bmi: Float): String {
        return when {
            bmi < 18.5 -> {
                "2.5 liters/day" // Underweight individuals need more water
            }
            bmi in 18.5..24.9 -> {
                "2.0 liters/day" // Normal weight individuals
            }
            bmi in 25.0..29.9 -> {
                "2.5 liters/day" // Overweight individuals need more water
            }
            else -> {
                "3.0 liters/day" // Obese individuals may need even more hydration
            }
        }
    }

    private fun getNutrientRecommendations(bmi: Float): Triple<String, String, String> {
        val protein: String
        val carbs: String
        val vitamins: String

        when {
            bmi < 18.5 -> { // Underweight
                protein = "1.2g per kg body weight"
                carbs = "5-6g per kg body weight"
                vitamins = "Focus on vitamin-rich foods like fruits and vegetables"
            }
            bmi in 18.5..24.9 -> { // Normal weight
                protein = "1.0g per kg body weight"
                carbs = "4-5g per kg body weight"
                vitamins = "A balanced amount of vitamins from fruits and vegetables"
            }
            bmi in 25.0..29.9 -> { // Overweight
                protein = "1.0g per kg body weight"
                carbs = "3-4g per kg body weight"
                vitamins = "High-fiber vegetables and fruits rich in vitamins"
            }
            else -> { // Obese
                protein = "1.2g per kg body weight"
                carbs = "3g per kg body weight"
                vitamins = "Focus on hydration and high-fiber vegetables"
            }
        }

        return Triple(protein, carbs, vitamins)
    }
}
