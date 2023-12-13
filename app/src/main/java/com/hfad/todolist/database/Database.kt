package com.hfad.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Entity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)

abstract class Database : RoomDatabase() {
    abstract fun Dao(): Dao
    abstract fun taskDao(): Any
}