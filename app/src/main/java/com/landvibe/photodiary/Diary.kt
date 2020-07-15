package com.landvibe.photodiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Diary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var content: String,
    var photoFileId: String,
    var date: String,
    var weather: String
)