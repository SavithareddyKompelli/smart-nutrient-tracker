package com.example.foodvisior

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodvisior.databinding.GenderInputBinding

class GenderSelectionActivity : AppCompatActivity() {

    private lateinit var binding: GenderInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GenderInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val age = intent.getStringExtra("AGE")
        val greeting = "Hello ! age what is your gender? "

        binding.tvGreeting.text = greeting

        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_female) {
                binding.llPregnancySection.visibility = View.VISIBLE
            } else {
                binding.llPregnancySection.visibility = View.GONE
            }
        }

        binding.btnSubmitGender.setOnClickListener {
            val selectedGender = when (binding.rgGender.checkedRadioButtonId) {
                R.id.rb_male -> "Male"
                R.id.rb_female -> {
                    if (binding.rbPregnantYes.isChecked) {
                        "Female (Pregnant)"
                    } else {
                        "Female"
                    }
                }
                R.id.rb_other -> "Other"
                else -> null
            }

            // Ensure a gender is selected
            if (selectedGender != null) {
                // Check if the "No Disease" option is selected
                val isNoDiseaseSelected = binding.rbDiseaseNo.isChecked

                val intent = if (isNoDiseaseSelected) {
                    Intent(this, BmiCalculationActivity::class.java).apply {
                        putExtra("SELECTED_GENDER", selectedGender)
                    }
                } else {
                    Intent(this, DiseaseSelectionActivity::class.java).apply {
                        putExtra("SELECTED_GENDER", selectedGender)
                    }
                }
                startActivity(intent)
            }
        }
    }
}
