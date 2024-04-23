// ProductEntity.kt
package com.example.eksamen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val image: String,
    var blockId: Long = 0
)