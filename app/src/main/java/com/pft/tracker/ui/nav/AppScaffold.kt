package com.pft.tracker.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pft.tracker.ui.theme.BlobColor1
import com.pft.tracker.ui.theme.BlobColor2
import com.pft.tracker.ui.theme.BlueGradientEnd
import com.pft.tracker.ui.theme.BlueGradientStart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.model.TransactionType
import com.pft.tracker.ui.accounts.AccountDetailScreen
import com.pft.tracker.ui.accounts.AccountEditScreen
import com.pft.tracker.ui.accounts.AccountsScreen
import com.pft.tracker.ui.categories.CategoriesScreen
import com.pft.tracker.ui.creditcards.CreditCardDetailScreen
import com.pft.tracker.ui.creditcards.CreditCardEditScreen
import com.pft.tracker.ui.creditcards.CreditCardsScreen
import com.pft.tracker.ui.creditcards.CreditLimitGroupsScreen
import com.pft.tracker.ui.dashboard.DashboardScreen
import com.pft.tracker.ui.recurring.RecurringEditScreen
import com.pft.tracker.ui.recurring.RecurringScreen
import com.pft.tracker.ui.settings.SettingsScreen
import com.pft.tracker.ui.transactions.AddEditTransactionScreen
import com.pft.tracker.ui.transactions.TransactionListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(container: AppContainer) {
    val navController = rememberNavController()
    var showTypePicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.hierarchy?.firstOrNull()?.route

    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val isKeyboardVisible = androidx.compose.foundation.layout.WindowInsets.ime.getBottom(androidx.compose.ui.platform.LocalDensity.current) > 0

    Scaffold(
        bottomBar = {
            if (!isKeyboardVisible) {
                NavigationBar(
                    containerColor = if (isDark) Color.Black.copy(alpha = 0.9f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    tonalElevation = 0.dp
                ) {
                    val tabs = listOf(
                        Triple(Routes.DASHBOARD, Icons.Rounded.Dashboard, "ภาพรวม"),
                        Triple(Routes.TRANSACTIONS, Icons.Rounded.List, "รายการ"),
                        Triple(Routes.ACCOUNTS, Icons.Rounded.AccountBalance, "บัญชี"),
                        Triple(Routes.CREDIT_CARDS, Icons.Rounded.CreditCard, "บัตรเครดิต"),
                        Triple(Routes.SETTINGS, Icons.Rounded.Settings, "ตั้งค่า")
                    )

                    tabs.forEach { (route, icon, label) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = { route?.let { navController.navigateToTab(it) } },
                            icon = { icon?.let { Icon(it, contentDescription = label) } },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                selectedIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                                selectedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTypePicker = true },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "เพิ่มรายการ", modifier = Modifier.size(28.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDark) {
                        Brush.verticalGradient(colors = listOf(Color.Black, Color(0xFF001A33)))
                    } else {
                        Brush.verticalGradient(colors = listOf(BlueGradientStart, BlueGradientEnd))
                    }
                )
        ) {
            // Decorative Blurred Blobs (Dimmed in Dark Mode)
            if (!isDark) {
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .offset(x = (-100).dp, y = (-50).dp)
                        .background(BlobColor1, CircleShape)
                        .blur(100.dp)
                )
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 80.dp, y = 100.dp)
                        .background(BlobColor2, CircleShape)
                        .blur(80.dp)
                )
            }

            NavHost(
                navController = navController,
                startDestination = Routes.DASHBOARD,
                modifier = Modifier.padding(padding)
            ) {
            composable(Routes.DASHBOARD) {
                DashboardScreen(container, navController)
            }
            composable(Routes.TRANSACTIONS) {
                TransactionListScreen(container, navController)
            }
            composable(
                Routes.TRANSACTION_EDIT,
                arguments = listOf(
                    navArgument("transactionId") { type = NavType.LongType },
                    navArgument("type") { type = NavType.StringType }
                )
            ) { entry ->
                val id = entry.arguments?.getLong("transactionId") ?: 0L
                val type = entry.arguments?.getString("type") ?: TransactionType.EXPENSE.name
                AddEditTransactionScreen(container, navController, id, type)
            }
            composable(Routes.ACCOUNTS) {
                AccountsScreen(container, navController)
            }
            composable(
                Routes.ACCOUNT_DETAIL,
                arguments = listOf(navArgument("accountId") { type = NavType.LongType })
            ) { entry ->
                AccountDetailScreen(container, navController, entry.arguments?.getLong("accountId") ?: 0L)
            }
            composable(
                Routes.ACCOUNT_EDIT,
                arguments = listOf(navArgument("accountId") { type = NavType.LongType })
            ) { entry ->
                AccountEditScreen(container, navController, entry.arguments?.getLong("accountId") ?: 0L)
            }
            composable(Routes.CREDIT_CARDS) {
                CreditCardsScreen(container, navController)
            }
            composable(
                Routes.CREDIT_CARD_DETAIL,
                arguments = listOf(navArgument("cardId") { type = NavType.LongType })
            ) { entry ->
                CreditCardDetailScreen(container, navController, entry.arguments?.getLong("cardId") ?: 0L)
            }
            composable(
                Routes.CREDIT_CARD_EDIT,
                arguments = listOf(navArgument("cardId") { type = NavType.LongType })
            ) { entry ->
                CreditCardEditScreen(container, navController, entry.arguments?.getLong("cardId") ?: 0L)
            }
            composable(Routes.CREDIT_LIMIT_GROUPS) {
                CreditLimitGroupsScreen(container, navController)
            }
            composable(Routes.CATEGORIES) {
                CategoriesScreen(container, navController)
            }
            composable(Routes.RECURRING) {
                RecurringScreen(container, navController)
            }
            composable(
                Routes.RECURRING_EDIT,
                arguments = listOf(navArgument("planId") { type = NavType.LongType })
            ) { entry ->
                RecurringEditScreen(container, navController, entry.arguments?.getLong("planId") ?: 0L)
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(container, navController)
            }
        }
    }
}

    if (showTypePicker) {
        ModalBottomSheet(
            onDismissRequest = { showTypePicker = false },
            sheetState = sheetState
        ) {
            TransactionTypePickerContent { type ->
                scope.launch {
                    sheetState.hide()
                    showTypePicker = false
                    navController.navigate(Routes.transactionEdit(0, type.name))
                }
            }
        }
    }
}

private fun androidx.navigation.NavController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = false
    }
}
