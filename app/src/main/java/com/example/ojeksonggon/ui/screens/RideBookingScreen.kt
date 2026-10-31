package com.example.ojeksonggon.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ojeksonggon.data.OjekSonggonData
import com.example.ojeksonggon.model.LocationPoint
import com.example.ojeksonggon.model.VehicleOption
import com.example.ojeksonggon.ui.theme.SonggonAmber
import com.example.ojeksonggon.ui.theme.SonggonGreenContainer
import com.example.ojeksonggon.ui.theme.SonggonGreenDark
import com.example.ojeksonggon.ui.theme.SonggonGreenPrimary
import com.example.ojeksonggon.viewmodel.AppScreen
import com.example.ojeksonggon.viewmodel.OjekSonggonViewModel
import com.example.ojeksonggon.viewmodel.OjekUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideBookingScreen(
    uiState: OjekUiState,
    viewModel: OjekSonggonViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    val distanceKm = remember(uiState.selectedPickup, uiState.selectedDropoff) {
        OjekSonggonData.calculateDistanceKm(uiState.selectedPickup, uiState.selectedDropoff)
    }

    val totalFare = remember(distanceKm, uiState.selectedVehicle) {
        OjekSonggonData.calculateFare(distanceKm, uiState.selectedVehicle)
    }

    var showPickupPicker by remember { mutableStateOf(false) }
    var showDropoffPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesan Ojek Songgon", fontWeight = FontWeight.Bold) },
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
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Estimasi Tarif",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = OjekSonggonViewModel.formatRupiah(totalFare),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = SonggonGreenDark
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SonggonGreenContainer
                        ) {
                            Text(
                                text = "${distanceKm} km • ~${(distanceKm * 3.5).toInt() + 5} mnt",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SonggonGreenDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val url = viewModel.getWhatsAppDispatchUrl(null)
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("btn_wa_dispatch"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Pesan via WA", fontSize = 13.sp)
                        }

                        Button(
                            onClick = { viewModel.createRideOrder() },
                            modifier = Modifier
                                .weight(1.3f)
                                .height(48.dp)
                                .testTag("btn_confirm_ride_order"),
                            colors = ButtonDefaults.buttonColors(containerColor = SonggonGreenPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Pesan Sekarang", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("ride_booking_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Rute Penjemputan & Tujuan",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Pickup
                        LocationSelectionRow(
                            label = "TITIK JEMPUT",
                            location = uiState.selectedPickup,
                            isPickup = true,
                            onClick = { showPickupPicker = !showPickupPicker }
                        )

                        if (showPickupPicker) {
                            LocationPickerList(
                                currentSelected = uiState.selectedPickup,
                                onSelect = {
                                    viewModel.setPickup(it)
                                    showPickupPicker = false
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Swap Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    val currentPickup = uiState.selectedPickup
                                    viewModel.setPickup(uiState.selectedDropoff)
                                    viewModel.setDropoff(currentPickup)
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Tukar Lokasi",
                                    tint = SonggonGreenPrimary
                                )
                            }
                        }

                        // Dropoff
                        LocationSelectionRow(
                            label = "TUJUAN",
                            location = uiState.selectedDropoff,
                            isPickup = false,
                            onClick = { showDropoffPicker = !showDropoffPicker }
                        )

                        if (showDropoffPicker) {
                            LocationPickerList(
                                currentSelected = uiState.selectedDropoff,
                                onSelect = {
                                    viewModel.setDropoff(it)
                                    showDropoffPicker = false
                                }
                            )
                        }
                    }
                }
            }

            // Vehicle Option Selection
            item {
                Text(
                    text = "Pilih Jenis Motor Ojek",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OjekSonggonData.vehicleOptions.forEach { vehicle ->
                    val isSelected = vehicle.id == uiState.selectedVehicle.id
                    VehicleCard(
                        vehicle = vehicle,
                        isSelected = isSelected,
                        onSelect = { viewModel.setVehicle(vehicle) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Catatan & Metode Pembayaran
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Catatan Perjalanan & Pembayaran",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = uiState.rideNotes,
                            onValueChange = { viewModel.setRideNotes(it) },
                            label = { Text("Catatan untuk driver (opsional)") },
                            placeholder = { Text("Contoh: Jemput depan warung kopi / bawa 1 koper") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_ride_notes"),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Metode Pembayaran",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("Tunai (Cash)", "QRIS Paguyuban").forEach { method ->
                                val isSelected = uiState.paymentMethod == method
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setPaymentMethod(method) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) SonggonGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, SonggonGreenPrimary) else null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Payment,
                                            contentDescription = null,
                                            tint = if (isSelected) SonggonGreenPrimary else Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = method,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) SonggonGreenDark else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationSelectionRow(
    label: String,
    location: LocationPoint,
    isPickup: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPickup) Icons.Default.NearMe else Icons.Default.LocationOn,
                contentDescription = null,
                tint = if (isPickup) SonggonGreenPrimary else SonggonAmber,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = location.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "Ganti",
                style = MaterialTheme.typography.labelSmall,
                color = SonggonGreenPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LocationPickerList(
    currentSelected: LocationPoint,
    onSelect: (LocationPoint) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = "Pilih Lokasi di Songgon & Sekitarnya:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OjekSonggonData.popularLocations.forEach { loc ->
            val isSelected = loc.id == currentSelected.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(loc) }
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = loc.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) SonggonGreenPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${loc.category} • ${loc.address}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Terpilih",
                        tint = SonggonGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleCard(
    vehicle: VehicleOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SonggonGreenContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, SonggonGreenPrimary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) SonggonGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBike,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vehicle.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) SonggonGreenDark else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = vehicle.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tarif Awal: ${OjekSonggonViewModel.formatRupiah(vehicle.baseFare)} (+${OjekSonggonViewModel.formatRupiah(vehicle.perKmRate)}/km)",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) SonggonGreenDark else SonggonGreenPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
