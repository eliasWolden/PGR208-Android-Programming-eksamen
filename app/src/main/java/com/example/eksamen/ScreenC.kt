// ScreenC.kt
package com.example.eksamen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ScreenC(navController: NavController) {
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var totalPrice by remember { mutableDoubleStateOf(0.0) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productList = withContext(Dispatchers.IO) {
            ProductRepository.getAllProducts()
        }
        totalPrice = withContext(Dispatchers.IO) {
            ProductRepository.getSumProductsPrice()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(productList) { product ->
                var quantity by remember { mutableIntStateOf(1) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(product.image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(80.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                            .width(50.dp)
                    ) {
                        Text(text = product.title, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = product.category, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Price: ${product.price}", fontSize = 18.sp)

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        ProductRepository.removeProduct(product)
                                        totalPrice = ProductRepository.getSumProductsPrice()
                                        if (quantity > 1) {
                                            quantity--
                                        } else {
                                            productList = productList - product
                                            ProductRepository.removeProduct(product)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(30.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = "$quantity", fontSize = 18.sp)

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        ProductRepository.addProduct(product)
                                        totalPrice = ProductRepository.getSumProductsPrice()
                                        quantity++
                                    }
                                },
                                modifier = Modifier
                                    .width(30.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    val listedProducts = ProductRepository.getAllProducts()

                    Log.d("YourTag", "Listed Products: $listedProducts")

                    ProductRepository.insertProductListWithBlock(listedProducts)

                    ProductRepository.clearProductsAndSaveToBlock()

                    navController.navigate("screenD")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Buy Total: $${totalPrice.toInt()}",
                fontSize = 18.sp
            )
        }
    }
}
