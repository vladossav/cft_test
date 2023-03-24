package com.example.cft_test

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentDao {
    @Query("REPLACE INTO recent VALUES (:binNum,datetime('now','localtime'))" )
    suspend fun insert(binNum: String)

    @Query("DELETE FROM recent")
    suspend fun deleteAll()

    @Query("SELECT bin_number FROM recent ORDER BY last_visit DESC")
    fun getRecentList(): Flow<MutableList<String>>
}