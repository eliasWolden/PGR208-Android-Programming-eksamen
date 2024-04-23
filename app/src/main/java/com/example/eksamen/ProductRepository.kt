// ProductRepository.kt
package com.example.eksamen

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProductRepository {
    private lateinit var productDao: ProductDao
    private lateinit var database: AppDatabase

    fun initialize(context: Context): AppDatabase {
        val migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
            }
        }

        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "product_database"
        )
            .addMigrations(migration)
            .fallbackToDestructiveMigration()
            .build()

        productDao = database.productDao()
        return database
    }

    suspend fun getAllProducts(blockId: Long = 0): List<Product> = withContext(Dispatchers.IO) {
        return@withContext productDao.getAllProducts(blockId).map {
            Product(
                id = it.id.toInt(),
                title = it.name,
                price = it.price,
                category = it.category,
                description = it.description,
                image = it.image
            )
        }
    }

    suspend fun insertProductListWithBlock(products: List<Product>, blockId: Long = 0) =
        withContext(Dispatchers.IO) {
            val productEntities = products.map { product ->
                ProductEntity(
                    name = product.title,
                    price = product.price,
                    category = product.category,
                    description = product.description,
                    image = product.image,
                    blockId = blockId
                )
            }
            productDao.insertProductList(productEntities)
        }

    suspend fun addProduct(product: Product, blockId: Long = 0) = withContext(Dispatchers.IO) {
        val productEntity = ProductEntity(
            name = product.title,
            price = product.price,
            category = product.category,
            description = product.description,
            image = product.image,
            blockId = blockId
        )
        productDao.insertProduct(productEntity)
    }

    suspend fun removeProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.removeProductById(product.id.toLong())
    }


    suspend fun getSumProductsPrice(blockId: Long = 0): Double = withContext(Dispatchers.IO) {
        return@withContext productDao.getAllProducts(blockId).sumOf { it.price }
    }

    suspend fun clearProductsAndSaveToBlock(blockId: Long = 0) = withContext(Dispatchers.IO) {
        val totalBlockPrice = getSumProductsPrice(blockId)
        val productBlockEntity = ProductBlockEntity(totalBlockPrice = totalBlockPrice)
        productDao.insertProductBlock(productBlockEntity)
        productDao.deleteAllProducts()
    }

    suspend fun getAllProductBlocks(): List<ProductBlock> = withContext(Dispatchers.IO) {
        return@withContext productDao.getAllProductBlocks().map { blockEntity ->
            ProductBlock(
                productList = productDao.getProductsForBlock(blockEntity.id).map { productEntity ->
                    Product(
                        id = productEntity.id.toInt(),
                        title = productEntity.name,
                        price = productEntity.price,
                        category = productEntity.category,
                        description = productEntity.description,
                        image = productEntity.image
                    )
                },
                products = emptyList(),
                totalBlockPrice = blockEntity.totalBlockPrice
            )
        }

    }
    fun getDatabaseInstance(): AppDatabase {
        if (!::database.isInitialized) {
            throw UninitializedPropertyAccessException("Database is not initialized")
        }
        return database
    }
}

