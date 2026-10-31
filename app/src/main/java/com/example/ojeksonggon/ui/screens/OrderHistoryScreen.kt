package com.example.ojeksonggon.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ojeksonggon.model.BookingOrder
import com.example.ojeksonggon.model.OrderStatus
import com.example.ojeksonggon.model.ServiceCategory
import com.example.ojeksonggon.ui.theme.SonggonAmber
import com.example.ojeksonggon.ui.theme.SonggonGreenContainer
import com.example.ojeksonggon.ui.theme.SonggonGreenDark
import com.example.ojeksonggon.ui.theme.SonggonGreenPrimary
import com.example.ojeksonggon.viewmodel.AppScreen
import com.example.ojeksonggon.viewmodel.OjekSonggonViewModel
import com.example.ojeksonggon.viewmodel.OjekUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    uiState: OjekUiState,
    viewModel: OjekSonggonViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedCategoryFilter by remember { mutableStateOf("Semua") }
    var selectedOrderForDetail by remember { mutableStateOf<BookingOrder?>(null) }

    val filterOptions = listOf("Semua", "Ojek Motor", "Tur Wisata", "Belanja")

    val filteredList = remember(selectedCategoryFilter, uiState.orderHistory) {
        if (selectedCategoryFilter == "Semua") {
            uiState.orderHistory
        } else {
            uiState.orderHistory.filter { order ->
                when (selectedCategoryFilter) {
                    "Ojek Motor" -> order.serviceCategory == ServiceCategory.OJEK_RIDE
                    "Tur Wisata" -> order.serviceCategory == ServiceCategory.WISATA
                    "Belanja" -> order.serviceCategory == ServiceCategory.BELANJA
                    else -> true
                }
            }
        }
    }

    if (selectedOrderForDetail != null) {
        val o = selectedOrderForDetail!!
        AlertDialog(
            onDismissRequest = { selectedOrderForDetail = null },
            title = {
                Text(
                    text = "Struk Pesanan ${o.id}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Layanan: ${o.serviceCategory.title}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Dari: ${o.pickupName}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Ke: ${o.dropoffName}", style = MaterialTheme.typography.bodySmall)
                    if (o.driver != null) {
                        Text(
                            text = "Driver: ${o.driver.name} (${o.driver.plateNumber})",
                            style = MaterialTheme.typography.bodySmall,
                            color = SonggonGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (!o.notes.isNullOrBlank()) {
                        Text(text = "Catatan: ${o.notes}", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total Tarif: ${OjekSonggonViewModel.formatRupiah(o.totalFare)} (${o.paymentMethod})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SonggonGreenDark
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedOrderForDetail = null
                        viewModel.navigateTo(AppScreen.RIDE_BOOKING)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SonggonGreenPrimary)
                ) {
                    Text("Pesan Lagi")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedOrderForDetail = null }) {
                    Text("Tutup")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pesanan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SonggonGreenPrimary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("history_screen_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filterOptions) { opt ->
                        FilterChip(
                            selected = selectedCategoryFilter == opt,
                            onClick = { selectedCategoryFilter = opt },
                            label = { Text(opt) }
                        )
                    }
                }
            }

            if (filteredList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Belum ada riwayat untuk kategori ini",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredList) { order ->
                    OrderHistoryCard(
                        order = order,
                        onClick = { selectedOrderForDetail = order },
                        onRepeat = {
                            viewModel.navigateTo(AppScreen.RIDE_BOOKING)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: BookingOrder,
    onClick: () -> Unit,
    onRepeat: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("in", "ID")) }
    val dateStr = remember(order.timestamp) { dateFormat.format(Date(order.timestamp)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = when (order.serviceCategory) {
                            ServiceCategory.OJEK_RIDE -> SonggonGreenContainer
                            ServiceCategory.WISATA -> Color(0xFFE0F2FE)
                            ServiceCategory.BELANJA -> Color(0xFFFEF3C7)
                            ServiceCategory.KURIR -> Color(0xFFEDE9FE)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when (order.serviceCategory) {
                                    ServiceCategory.OJEK_RIDE -> Icons.Default.DirectionsBike
                                    ServiceCategory.WISATA -> Icons.Default.Landscape
                                    ServiceCategory.BELANJA -> Icons.Default.ShoppingBag
                                    ServiceCategory.KURIR -> Icons.Default.DirectionsBike
                                },
                                contentDescription = null,
                                tint = when (order.serviceCategory) {
                                    ServiceCategory.OJEK_RIDE -> SonggonGreenDark
                                    ServiceCategory.WISATA -> Color(0xFF0369A1)
                                    ServiceCategory.BELANJA -> Color(0xFFB45309)
                                    ServiceCategory.KURIR -> Color(0xFF6D28D9)
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = order.serviceCategory.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (order.status == OrderStatus.COMPLETED) SonggonGreenContainer else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = if (order.status == OrderStatus.COMPLETED) "Selesai" else "Dibatalkan",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (order.status == OrderStatus.COMPLETED) SonggonGreenDark else Color(0xFFB91C1C),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${order.pickupName}  ➔  ${order.dropoffName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            if (order.driver != null) {
                Text(
                    text = "Driver: ${order.driver.name} • ${order.driver.plateNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = OjekSonggonViewModel.formatRupiah(order.totalFare),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SonggonGreenDark
                )

                OutlinedButton(
                    onClick = onRepeat,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pesan Ulang", fontSize = 11.sp)
                }
            }
        }
    }
}
