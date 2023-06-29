package com.example.quiz.room

import androidx.room.*


@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(users: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user")
    fun nukeTable()
}