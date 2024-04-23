package com.example.eksamen

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, ProductBlockEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

