package com.example.ojeksonggon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ojeksonggon.ui.screens.ActiveTrackingScreen
import com.example.ojeksonggon.ui.screens.DriverCommunityScreen
import com.example.ojeksonggon.ui.screens.HomeScreen
import com.example.ojeksonggon.ui.screens.OrderHistoryScreen
import com.example.ojeksonggon.ui.screens.RideBookingScreen
import com.example.ojeksonggon.ui.screens.ShoppingScreen
import com.example.ojeksonggon.ui.screens.TourPackagesScreen
import com.example.ojeksonggon.ui.theme.OjekSonggonTheme
import com.example.ojeksonggon.ui.theme.SonggonAmber
import com.example.ojeksonggon.ui.theme.SonggonGreenDark
import com.example.ojeksonggon.ui.theme.SonggonGreenPrimary
import com.example.ojeksonggon.viewmodel.AppScreen
import com.example.ojeksonggon.viewmodel.OjekSonggonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OjekSonggonTheme {
                OjekSonggonApp()
            }
        }
    }
}

@Composable
fun OjekSonggonApp(viewModel: OjekSonggonViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (uiState.activeOrder != null && uiState.currentScreen != AppScreen.ACTIVE_TRACKING) {
                FloatingActionButton(
                    onClick = { viewModel.navigateTo(AppScreen.ACTIVE_TRACKING) },
                    containerColor = SonggonAmber,
                    contentColor = Color.Black,
                    modifier = Modifier.testTag("fab_tracking")
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Pantau Pesanan Aktif"
                    )
                }
            }
        },
        bottomBar = {
            // Show bottom navigation on primary destinations
            if (uiState.currentScreen != AppScreen.ACTIVE_TRACKING &&
                uiState.currentScreen != AppScreen.RIDE_BOOKING &&
                uiState.currentScreen != AppScreen.SHOPPING
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = uiState.currentScreen == AppScreen.HOME,
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                        label = { Text("Beranda") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SonggonGreenPrimary,
                            selectedTextColor = SonggonGreenDark,
                            indicatorColor = SonggonGreenPrimary.copy(alpha = 0.15f)
                        )
                    )

                    NavigationBarItem(
                        selected = uiState.currentScreen == AppScreen.RIDE_BOOKING,
                        onClick = { viewModel.navigateTo(AppScreen.RIDE_BOOKING) },
                        icon = { Icon(Icons.Default.DirectionsBike, contentDescription = "Pesan Ojek") },
                        label = { Text("Pesan Ojek") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SonggonGreenPrimary,
                            selectedTextColor = SonggonGreenDark,
                            indicatorColor = SonggonGreenPrimary.copy(alpha = 0.15f)
                        )
                    )

                    NavigationBarItem(
                        selected = uiState.currentScreen == AppScreen.TOUR_PACKAGES,
                        onClick = { viewModel.navigateTo(AppScreen.TOUR_PACKAGES) },
                        icon = { Icon(Icons.Default.Landscape, contentDescription = "Wisata") },
                        label = { Text("Wisata") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SonggonGreenPrimary,
                            selectedTextColor = SonggonGreenDark,
                            indicatorColor = SonggonGreenPrimary.copy(alpha = 0.15f)
                        )
                    )

                    NavigationBarItem(
                        selected = uiState.currentScreen == AppScreen.ORDER_HISTORY,
                        onClick = { viewModel.navigateTo(AppScreen.ORDER_HISTORY) },
                        icon = {
                            if (uiState.orderHistory.isNotEmpty()) {
                                BadgedBox(badge = { Badge { Text("${uiState.orderHistory.size}") } }) {
                                    Icon(Icons.Default.History, contentDescription = "Riwayat")
                                }
                            } else {
                                Icon(Icons.Default.History, contentDescription = "Riwayat")
                            }
                        },
                        label = { Text("Riwayat") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SonggonGreenPrimary,
                            selectedTextColor = SonggonGreenDark,
                            indicatorColor = SonggonGreenPrimary.copy(alpha = 0.15f)
                        )
                    )

                    NavigationBarItem(
                        selected = uiState.currentScreen == AppScreen.COMMUNITY_INFO,
                        onClick = { viewModel.navigateTo(AppScreen.COMMUNITY_INFO) },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Posko") },
                        label = { Text("Posko") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SonggonGreenPrimary,
                            selectedTextColor = SonggonGreenDark,
                            indicatorColor = SonggonGreenPrimary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.RIDE_BOOKING -> {
                    RideBookingScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.TOUR_PACKAGES -> {
                    TourPackagesScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.SHOPPING -> {
                    ShoppingScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.ACTIVE_TRACKING -> {
                    ActiveTrackingScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.ORDER_HISTORY -> {
                    OrderHistoryScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.COMMUNITY_INFO -> {
                    DriverCommunityScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
            }
        }
    }
}
