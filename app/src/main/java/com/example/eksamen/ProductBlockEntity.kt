// ProductBlockEntity.kt
package com.example.eksamen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_blocks")
data class ProductBlockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val totalBlockPrice: Double
)