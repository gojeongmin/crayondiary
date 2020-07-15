package com.landvibe.photodiary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary")
    fun getAll(): List<Diary>

    @Query("SELECT * FROM diary WHERE id = :id")
    fun get(id: Int): Diary

    @Insert(onConflict = REPLACE)
    fun insert(diary: Diary): Long

    @Query("DELETE from diary WHERE id = :id")
    fun delete(id: Int)
}