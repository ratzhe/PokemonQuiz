package com.example.quiz.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class User (

    @ColumnInfo(name = "user") val user: String,
    @ColumnInfo(name = "points") val points: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}