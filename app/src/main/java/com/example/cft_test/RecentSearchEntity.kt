package com.example.cft_test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent")
data class RecentSearchEntity(
    @PrimaryKey
    @ColumnInfo(name = "bin_number")
    val bin: String,
    @ColumnInfo(name = "last_visit")
    val lastRequestTime: String
)
