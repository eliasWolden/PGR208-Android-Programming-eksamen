package com.example.eksamen

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Insert
    suspend fun insertProductList(products: List<ProductEntity>)


    @Delete
    suspend fun removeProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE blockId = :blockId OR :blockId = 0")
    suspend fun getAllProducts(blockId: Long = 0): List<ProductEntity>

    @Insert
    suspend fun insertProductBlock(productBlock: ProductBlockEntity)

    @Query("SELECT * FROM product_blocks")
    suspend fun getAllProductBlocks(): List<ProductBlockEntity>

    @Query("SELECT * FROM products WHERE blockId = :blockId")
    suspend fun getProductsForBlock(blockId: Long): List<ProductEntity>

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun removeProductById(productId: Long)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
