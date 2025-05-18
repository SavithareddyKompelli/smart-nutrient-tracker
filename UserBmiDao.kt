package com.example.foodvisior.database

import androidx.room.*

@Dao
interface UserBmiDao {
    @Insert
    suspend fun insertUserBmi(userBmi: UserBmi)

    @Query("SELECT * FROM user_bmi WHERE userId = :userId")
    suspend fun getUserBmi(userId: Int): List<UserBmi>

    @Query("SELECT * FROM user_bmi")
    suspend fun getAllUserBmi(): List<UserBmi>
}


