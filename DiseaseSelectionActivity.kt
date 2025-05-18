package com.example.foodvisior

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodvisior.databinding.ActivityDiseaseSelectionBinding

class DiseaseSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedGender = intent.getStringExtra("SELECTED_GENDER")

        binding.btnSubmitDisease.setOnClickListener {
            val selectedDiseases = mutableListOf<String>()

            // Add diseases based on checkboxes
            if (binding.cbDiabetes.isChecked) selectedDiseases.add("Diabetes")
            if (binding.cbHypertension.isChecked) selectedDiseases.add("Hypertension")
            if (binding.cbAnemia.isChecked) selectedDiseases.add("Anemia")
            if (binding.cbAsthma.isChecked) selectedDiseases.add("Asthma")
            if (binding.cbArthritis.isChecked) selectedDiseases.add("Arthritis")
            if (binding.cbCancer.isChecked) selectedDiseases.add("Cancer")
            if (binding.cbCholesterol.isChecked) selectedDiseases.add("High Cholesterol")
            if (binding.cbObesity.isChecked) selectedDiseases.add("Obesity")
            if (binding.cbHiv.isChecked) selectedDiseases.add("HIV/AIDS")
            if (binding.cbAlzheimer.isChecked) selectedDiseases.add("Alzheimer's Disease")
            if (binding.cbParkinson.isChecked) selectedDiseases.add("Parkinson's Disease")
            if (binding.cbEpilepsy.isChecked) selectedDiseases.add("Epilepsy")
            if (binding.cbMigraine.isChecked) selectedDiseases.add("Migraine")
            if (binding.cbCeliac.isChecked) selectedDiseases.add("Celiac Disease")
            if (binding.cbLupus.isChecked) selectedDiseases.add("Lupus")
            if (binding.cbOsteoporosis.isChecked) selectedDiseases.add("Osteoporosis")
            if (binding.cbGout.isChecked) selectedDiseases.add("Gout")
            if (binding.cbStroke.isChecked) selectedDiseases.add("Stroke")
            if (binding.cbThyroid.isChecked) selectedDiseases.add("Thyroid Disorders")
            if (binding.cbHepatitis.isChecked) selectedDiseases.add("Hepatitis")

            val intent = Intent(this, BmiCalculationActivity::class.java).apply {
                putExtra("SELECTED_GENDER", selectedGender)
                putExtra("SELECTED_DISEASES", selectedDiseases.joinToString(", "))
            }

            startActivity(intent)
            finish()
        }
    }
}
