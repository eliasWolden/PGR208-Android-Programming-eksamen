package com.example.eksamen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson


@Composable
fun Nav() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = "A",
                modifier = Modifier.weight(1f)
            ) {
                composable(route = "A") {
                    ScreenA(navController)
                }
                composable(route = "screenB/{selectedProduct}") { backstackEntry ->
                    val selectedProductJson =
                        backstackEntry.arguments?.getString("selectedProduct")
                    if (selectedProductJson != null) {
                        val selectedProduct =
                            Gson().fromJson(selectedProductJson, Product::class.java)
                        ScreenB(product = selectedProduct, navController)
                    } else {
                        Log.e("ScreenB", "Selected product data is null.")
                    }
                }
                composable(route = "screenC") {
                    ScreenC(navController)
                }
                composable(route = "screenD") {
                    ScreenD()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NavigationBar(
                modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null) },
                    label = { Text(text = "Back") },
                    selected = false,
                    onClick = {
                        if (!navController.navigateUp()) {
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                    label = { Text(text = "Home") },
                    selected = navController.currentBackStackEntry?.destination?.route == "A",
                    onClick = {
                        navController.navigate("A") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null) },
                    label = { Text(text = "Cart") },
                    selected = navController.currentBackStackEntry?.destination?.route == "screenC",
                    onClick = {
                        navController.navigate("screenC") {
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = null) },
                    label = { Text(text = "Order history") },
                    selected = navController.currentBackStackEntry?.destination?.route == "screenD",
                    onClick = {
                        navController.navigate("screenD") {
                        }
                    }
                )
            }
        }
    }
}
