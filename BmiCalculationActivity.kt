package com.example.foodvisior

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.foodvisior.database.UserBmi
import com.example.foodvisior.databinding.ActivityBmiCalculationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BmiCalculationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiCalculationBinding
    private val database by lazy { AppDatabase.getDatabase(this).userBmiDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the diseases passed from the DiseaseSelectionActivity
        val selectedDiseases = intent.getStringExtra("SELECTED_DISEASES")?.split(", ") ?: emptyList()

        binding.btnSubmitBmi.setOnClickListener {
            val weight = binding.etWeight.text.toString().toFloatOrNull()
            val height = binding.etHeight.text.toString().toFloatOrNull()

            if (weight == null || height == null) {
                binding.tvBmiResult.text = "Please enter valid weight and height."
            } else {
                val bmi = calculateBmi(weight, height)
                val bmiCategory = getBmiCategory(bmi)

                val userId = 1 // Example user ID for now

                // Get current date
                val date = getCurrentDate()

                val userBmi = UserBmi(
                    userId = userId,
                    weight = weight,
                    height = height,
                    bmi = bmi,
                    bmiCategory = bmiCategory,
                    date = date,
                    gender = "Male", // This would be dynamic based on user input
                    diseases = selectedDiseases.joinToString(", ")
                )

                // Insert BMI data into the database
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        // Insert the data into the database if it's not null
                        database.insertUserBmi(userBmi)
                    } catch (e: Exception) {
                        // Handle any exception while inserting into the database
                        e.printStackTrace()
                    }
                }

                // Display the result to the user
                displayBmiResult(bmi, bmiCategory)
                displayNutrientRecommendations(selectedDiseases)
            }
        }
        binding.calbtn.setOnClickListener{
            val weight = binding.etWeight.text.toString().toFloatOrNull()
            val height = binding.etHeight.text.toString().toFloatOrNull()
            val age=binding.etAge.text.toString().toIntOrNull()
            if (weight != null && height != null) {
                // Calculate BMI for calorie calculation
                val bmi = calculateBmi(weight, height)
                val intent = Intent(this, CalorieCalculationActivity::class.java).apply {
                    putExtra("BMI", bmi)
                    putExtra("AGE", age) // Pass the age to the calorie activity
                }
                startActivity(intent)
            } else {
                // Show a toast if weight or height is invalid
                Toast.makeText(this, "Please enter valid weight and height for calorie calculation.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateBmi(weight: Float, height: Float): Float {
        return weight / (height * height)
    }

    private fun getBmiCategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }
    }

    private fun getCurrentDate(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(Date())
    }

    private fun displayBmiResult(bmi: Float, bmiCategory: String) {
        binding.tvBmiResult.text = "Your BMI is $bmi, which is considered $bmiCategory."
        val foodRecommendations = getFoodRecommendations(bmiCategory)
        binding.tvFoodRecommendations.text = foodRecommendations
    }
    private fun getFoodRecommendations(bmiCategory: String): String {
        return when (bmiCategory) {
            "Underweight" -> {
                "To gain weight healthily, focus on nutrient-dense foods such as: \n" +
                        "• Whole grains (e.g., oats, brown rice, whole wheat pasta) \n" +
                        "• Healthy fats (e.g., avocados, nuts, seeds, olive oil) \n" +
                        "• Protein-rich foods (e.g., chicken, eggs, beans, lentils) \n" +
                        "• Dairy products (e.g., cheese, milk, yogurt)"
            }
            "Normal weight" -> {
                "To maintain a healthy weight, focus on a balanced diet with: \n" +
                        "• Fresh fruits and vegetables \n" +
                        "• Whole grains (e.g., quinoa, barley, whole wheat) \n" +
                        "• Lean proteins (e.g., fish, tofu, chicken) \n" +
                        "• Healthy fats (e.g., olive oil, nuts, seeds)"
            }
            "Overweight" -> {
                "To manage weight, focus on nutrient-dense, low-calorie foods: \n" +
                        "• Leafy greens and non-starchy vegetables \n" +
                        "• Whole grains (e.g., quinoa, brown rice, oats) \n" +
                        "• Lean protein (e.g., fish, skinless poultry, legumes) \n" +
                        "• Healthy fats in moderation (e.g., olive oil, nuts)"
            }
            "Obesity" -> {
                "To reduce weight and improve health, focus on: \n" +
                        "• Vegetables and fruits (high in fiber and low in calories) \n" +
                        "• Lean proteins (e.g., chicken, turkey, fish) \n" +
                        "• Whole grains (e.g., brown rice, quinoa, oats) \n" +
                        "• Hydration (e.g., drink plenty of water and herbal teas)"
            }
            else -> "No specific recommendations available."
        }
    }


    private fun displayNutrientRecommendations(diseases: List<String>) {
        // Map to store nutrient recommendations for each disease
        val diseaseMicronutrientsMap = mapOf(
            "Diabetes" to "For diabetes, focus on magnesium, vitamin D, chromium, fiber, and omega-3 fatty acids.",
            "Hypertension" to "For hypertension, consume potassium, magnesium, calcium, and vitamin D.",
            "Anemia" to "For anemia, include iron, vitamin B12, folate, and vitamin C (for better absorption).",
            "Asthma" to "For asthma, include vitamin C, vitamin D, magnesium, and omega-3 fatty acids.",
            "Arthritis" to "For arthritis, consume vitamin D, vitamin C, calcium, and omega-3 fatty acids.",
            "Cancer" to "For cancer prevention, focus on antioxidants like vitamins A, C, E, and selenium.",
            "High Cholesterol" to "For high cholesterol, consume omega-3 fatty acids, vitamin E, and fiber.",
            "Obesity" to "For obesity, prioritize fiber, omega-3 fatty acids, antioxidants, vitamin D, and calcium.",
            "HIV/AIDS" to "For HIV/AIDS, focus on zinc, vitamin A, vitamin C, selenium, and omega-3 fatty acids.",
            "Alzheimer's Disease" to "For Alzheimer's disease, include vitamin E, vitamin B12, folate, and omega-3 fatty acids.",
            "Parkinson's Disease" to "For Parkinson's disease, include vitamin E, vitamin D, magnesium, and omega-3 fatty acids.",
            "Epilepsy" to "For epilepsy, focus on magnesium, vitamin D, and vitamin B6.",
            "Migraine" to "For migraines, focus on magnesium, vitamin B2, and vitamin D.",
            "Celiac Disease" to "For celiac disease, focus on calcium, iron, vitamin D, and fiber.",
            "Lupus" to "For lupus, focus on omega-3 fatty acids, vitamin D, calcium, and antioxidants.",
            "Osteoporosis" to "For osteoporosis, include calcium, vitamin D, magnesium, and vitamin K.",
            "Gout" to "For gout, focus on vitamin C, magnesium, and omega-3 fatty acids.",
            "Stroke" to "For stroke recovery, consume omega-3 fatty acids, vitamin E, and potassium.",
            "Thyroid Disorders" to "For thyroid disorders, focus on selenium, zinc, iodine, and vitamin D.",
            "Hepatitis" to "For hepatitis, focus on vitamin D, selenium, zinc, and vitamin C."
        )

        val recommendations = StringBuilder()

        diseases.forEach { disease ->
            val trimmedDisease = disease.trim() // Trim to avoid leading/trailing spaces
            diseaseMicronutrientsMap[trimmedDisease]?.let {
                recommendations.append("$trimmedDisease: $it\n")
            } ?: run {
                // Handle case where there are no recommendations for this disease
                recommendations.append("$trimmedDisease: No specific recommendations available.\n")
            }
        }

        // Check if there are any recommendations to display
        if (recommendations.isNotEmpty()) {
            binding.tvNutrientRecommendations.text = recommendations.toString()
        } else {
            binding.tvNutrientRecommendations.text = "No specific recommendations available."
        }
    }
}
