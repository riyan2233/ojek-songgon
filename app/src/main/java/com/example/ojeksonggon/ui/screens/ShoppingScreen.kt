package com.example.ojeksonggon.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ojeksonggon.data.OjekSonggonData
import com.example.ojeksonggon.model.MarketProduct
import com.example.ojeksonggon.ui.theme.SonggonAmber
import com.example.ojeksonggon.ui.theme.SonggonGreenContainer
import com.example.ojeksonggon.ui.theme.SonggonGreenDark
import com.example.ojeksonggon.ui.theme.SonggonGreenPrimary
import com.example.ojeksonggon.viewmodel.OjekSonggonViewModel
import com.example.ojeksonggon.viewmodel.OjekUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    uiState: OjekUiState,
    viewModel: OjekSonggonViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Durian", "Kuliner", "Hasil Bumi")

    val filteredProducts = remember(selectedCategory) {
        if (selectedCategory == "Semua") {
            OjekSonggonData.marketProducts
        } else {
            OjekSonggonData.marketProducts.filter { it.category == selectedCategory }
        }
    }

    val totalItemsCount = uiState.shoppingCart.values.sum()
    val totalItemsPrice = uiState.shoppingCart.entries.sumOf { (id, qty) ->
        val p = OjekSonggonData.marketProducts.find { it.id == id }
        (p?.price ?: 0) * qty
    }
    val deliveryFee = if (totalItemsCount > 0) 10000 else 0
    val grandTotal = totalItemsPrice + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Titip Belanja & Durian", fontWeight = FontWeight.Bold) },
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
            if (totalItemsCount > 0) {
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
                                    text = "$totalItemsCount Item + Ongkir Ojek (${OjekSonggonViewModel.formatRupiah(deliveryFee)})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = OjekSonggonViewModel.formatRupiah(grandTotal),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SonggonGreenDark
                                )
                            }

                            Button(
                                onClick = { viewModel.createShoppingOrder() },
                                colors = ButtonDefaults.buttonColors(containerColor = SonggonGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("btn_confirm_shopping_order")
                            ) {
                                Text("Pesan Titip Belanja", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
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
                .testTag("shopping_screen_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Category Filter
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }

            // Products list
            items(filteredProducts) { product ->
                val quantity = uiState.shoppingCart[product.id] ?: 0
                ProductItemCard(
                    product = product,
                    quantity = quantity,
                    onIncrease = { viewModel.updateCart(product.id, 1) },
                    onDecrease = { viewModel.updateCart(product.id, -1) }
                )
            }

            // Address and instructions (when cart has items)
            if (totalItemsCount > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Alamat Pengantaran & Catatan",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = uiState.shoppingDeliveryAddress,
                                onValueChange = { viewModel.setShoppingAddress(it) },
                                label = { Text("Alamat Antar di Songgon") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.shoppingNotes,
                                onValueChange = { viewModel.setShoppingNotes(it) },
                                label = { Text("Catatan Belanja Khusus (opsional)") },
                                placeholder = { Text("Contoh: Pilih durian yang sudah matang di pohon") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemCard(
    product: MarketProduct,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SonggonAmber.copy(alpha = 0.15f),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = SonggonAmber,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                if (product.isBestSeller) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = SonggonAmber.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Khas Songgon",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${OjekSonggonViewModel.formatRupiah(product.price)} / ${product.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SonggonGreenPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = product.seller,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Stepper
            if (quantity == 0) {
                Button(
                    onClick = onIncrease,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SonggonGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("+ Tambah", fontSize = 12.sp)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(onClick = onDecrease)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Kurang",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    Surface(
                        shape = CircleShape,
                        color = SonggonGreenPrimary,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(onClick = onIncrease)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
