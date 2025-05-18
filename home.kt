package com.example.foodvisior

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodvisior.databinding.ActivityHomeBinding
import com.example.foodvisior.GenderSelectionActivity

class home:AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            val name = binding.etAge.text.toString()
            if (name.isNotEmpty()) {
                val intent = Intent(this, GenderSelectionActivity::class.java).apply {
                    putExtra("AGE", name)
                }
                startActivity(intent)
            } else {
                println("Please enter a valid age.") }
        }
    }
}