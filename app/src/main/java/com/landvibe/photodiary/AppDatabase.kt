package com.landvibe.photodiary

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Diary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        val instance = Room.databaseBuilder(
            MainApplication.context,
            AppDatabase::class.java, "photodiary.db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}