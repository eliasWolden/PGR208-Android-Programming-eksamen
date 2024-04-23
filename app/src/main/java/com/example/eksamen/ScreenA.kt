package com.example.eksamen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun ScreenA(navController: NavController) {
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("All") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = selectedCategory) {
        val apiEndpoint = "https://fakestoreapi.com/products"
        val result = withContext(Dispatchers.IO) {
            try {
                val response = URL(apiEndpoint).readText()
                Gson().fromJson(response, Array<Product>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        }
        productList = result
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DropdownMenuItem(
                { Text(text = "All Categories") },
                onClick = {
                    selectedCategory = "All"
                    isDropdownExpanded = false
                }
            )
            DropdownMenuItem(
                { Text(text = "Men's clothing") },
                onClick = {
                    selectedCategory = "men's clothing"
                    isDropdownExpanded = false
                }
            )
            DropdownMenuItem(
                { Text(text = "Women's clothing") },
                onClick = {
                    selectedCategory = "women's clothing"
                    isDropdownExpanded = false
                }
            )
            DropdownMenuItem(
                { Text(text = "Jewelery") },
                onClick = {
                    selectedCategory = "jewelery"
                    isDropdownExpanded = false
                }
            )
            DropdownMenuItem(
                { Text(text = "Electronics") },
                onClick = {
                    selectedCategory = "electronics"
                    isDropdownExpanded = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
            Text(text = "Filter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(productList.filter {
                selectedCategory.equals("All", ignoreCase = true) ||
                        it.category.equals(selectedCategory, ignoreCase = true)
            }) { product ->
                ProductItem(product = product, onProductClick = {
                    navController.navigate("screenB/${Uri.encode(Gson().toJson(product))}")
                })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
