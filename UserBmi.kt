package com.example.foodvisior.database
// UserBmi.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_bmi")
data class UserBmi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,  // This is the reference to the user
    val weight: Float,
    val height: Float,
    val bmi: Float,
    val bmiCategory: String,
    val date: String,
    val gender: String,
    val diseases: String // Diseases stored as a comma-separated string
)