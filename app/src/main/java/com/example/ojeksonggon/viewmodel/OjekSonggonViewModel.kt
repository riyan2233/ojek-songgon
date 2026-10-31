package com.example.ojeksonggon.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ojeksonggon.data.OjekSonggonData
import com.example.ojeksonggon.model.BookingOrder
import com.example.ojeksonggon.model.LocationPoint
import com.example.ojeksonggon.model.MarketProduct
import com.example.ojeksonggon.model.OrderStatus
import com.example.ojeksonggon.model.ServiceCategory
import com.example.ojeksonggon.model.TourPackage
import com.example.ojeksonggon.model.VehicleOption
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

enum class AppScreen {
    HOME,
    RIDE_BOOKING,
    TOUR_PACKAGES,
    SHOPPING,
    ACTIVE_TRACKING,
    ORDER_HISTORY,
    COMMUNITY_INFO
}

data class ShoppingCartItem(
    val product: MarketProduct,
    val quantity: Int
)

data class OjekUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val activeOrder: BookingOrder? = null,
    val orderHistory: List<BookingOrder> = emptyList(),
    val selectedPickup: LocationPoint = OjekSonggonData.popularLocations[0],
    val selectedDropoff: LocationPoint = OjekSonggonData.popularLocations[1],
    val selectedVehicle: VehicleOption = OjekSonggonData.vehicleOptions[0],
    val rideNotes: String = "",
    val paymentMethod: String = "Tunai (Cash)",
    val shoppingCart: Map<String, Int> = emptyMap(),
    val shoppingDeliveryAddress: String = "Pasar Songgon, Jl. Raya Songgon",
    val shoppingNotes: String = "",
    val courierItemDesc: String = "",
    val courierRecipient: String = "",
    val snackbarMessage: String? = null
)

class OjekSonggonViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OjekUiState())
    val uiState: StateFlow<OjekUiState> = _uiState.asStateFlow()

    private var simulationJob: Job? = null

    init {
        // Seed order history with a realistic completed trip and tour
        val sampleCompletedTrip = BookingOrder(
            id = "ORD-7192",
            serviceCategory = ServiceCategory.OJEK_RIDE,
            pickupName = "Stasiun KA Rogojampi",
            dropoffName = "Wisata Hutan Pinus Songgon",
            vehicleName = "Motor Standar (Vario)",
            distanceKm = 14.2,
            totalFare = 43000,
            status = OrderStatus.COMPLETED,
            driver = OjekSonggonData.verifiedDrivers[0],
            notes = "Bawa tas ransel 1",
            paymentMethod = "QRIS",
            timestamp = System.currentTimeMillis() - 86400000L
        )

        val sampleCompletedTour = BookingOrder(
            id = "ORD-6840",
            serviceCategory = ServiceCategory.WISATA,
            pickupName = "Kantor Kecamatan Songgon",
            dropoffName = "Air Terjun Lider (Kaki G. Raung)",
            vehicleName = "Motor Trail Kaki Raung (KLX)",
            distanceKm = 11.5,
            totalFare = 150000,
            status = OrderStatus.COMPLETED,
            driver = OjekSonggonData.verifiedDrivers[1],
            tourPackageTitle = "Ekspedisi Curug Air Terjun Lider",
            notes = "Foto-foto pemandangan lereng Raung",
            paymentMethod = "Tunai (Cash)",
            timestamp = System.currentTimeMillis() - 172800000L
        )

        _uiState.update {
            it.copy(orderHistory = listOf(sampleCompletedTrip, sampleCompletedTour))
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setPickup(location: LocationPoint) {
        _uiState.update { it.copy(selectedPickup = location) }
    }

    fun setDropoff(location: LocationPoint) {
        _uiState.update { it.copy(selectedDropoff = location) }
    }

    fun setVehicle(vehicle: VehicleOption) {
        _uiState.update { it.copy(selectedVehicle = vehicle) }
    }

    fun setRideNotes(notes: String) {
        _uiState.update { it.copy(rideNotes = notes) }
    }

    fun setPaymentMethod(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun updateCart(productId: String, delta: Int) {
        _uiState.update { state ->
            val newCart = state.shoppingCart.toMutableMap()
            val current = newCart[productId] ?: 0
            val updated = current + delta
            if (updated <= 0) {
                newCart.remove(productId)
            } else {
                newCart[productId] = updated
            }
            state.copy(shoppingCart = newCart)
        }
    }

    fun setShoppingAddress(addr: String) {
        _uiState.update { it.copy(shoppingDeliveryAddress = addr) }
    }

    fun setShoppingNotes(notes: String) {
        _uiState.update { it.copy(shoppingNotes = notes) }
    }

    fun createRideOrder() {
        val pickup = _uiState.value.selectedPickup
        val dropoff = _uiState.value.selectedDropoff
        val vehicle = _uiState.value.selectedVehicle
        val distance = OjekSonggonData.calculateDistanceKm(pickup, dropoff)
        val fare = OjekSonggonData.calculateFare(distance, vehicle)

        val newOrder = BookingOrder(
            id = "ORD-${(1000..9999).random()}",
            serviceCategory = ServiceCategory.OJEK_RIDE,
            pickupName = pickup.name,
            dropoffName = dropoff.name,
            vehicleName = vehicle.name,
            distanceKm = distance,
            totalFare = fare,
            status = OrderStatus.SEARCHING,
            notes = _uiState.value.rideNotes,
            paymentMethod = _uiState.value.paymentMethod
        )

        _uiState.update {
            it.copy(
                activeOrder = newOrder,
                currentScreen = AppScreen.ACTIVE_TRACKING,
                snackbarMessage = "Pesanan berhasil dibuat! Sedang mencari driver..."
            )
        }

        startOrderTrackingSimulation()
    }

    fun createTourOrder(pkg: TourPackage, customNotes: String) {
        val driver = if (pkg.id == "tour_lider") {
            OjekSonggonData.verifiedDrivers[1] // KLX trail driver
        } else {
            OjekSonggonData.verifiedDrivers.random()
        }

        val newOrder = BookingOrder(
            id = "TOUR-${(1000..9999).random()}",
            serviceCategory = ServiceCategory.WISATA,
            pickupName = "Posko Ojek Songgon / Titik Temu",
            dropoffName = pkg.title,
            vehicleName = if (pkg.id == "tour_lider") "Motor Trail Kaki Raung" else "Motor Standar & Helm Wisata",
            distanceKm = 8.5,
            totalFare = pkg.price,
            status = OrderStatus.DRIVER_ASSIGNED,
            driver = driver,
            tourPackageTitle = pkg.title,
            notes = customNotes,
            paymentMethod = "Tunai ke Driver"
        )

        _uiState.update {
            it.copy(
                activeOrder = newOrder,
                currentScreen = AppScreen.ACTIVE_TRACKING,
                snackbarMessage = "Paket wisata dikonfirmasi dengan ${driver.name}!"
            )
        }
    }

    fun createShoppingOrder() {
        val cart = _uiState.value.shoppingCart
        if (cart.isEmpty()) return

        val itemsSummary = cart.mapNotNull { (prodId, qty) ->
            val p = OjekSonggonData.marketProducts.find { it.id == prodId }
            if (p != null) "${p.name} ($qty ${p.unit})" else null
        }.joinToString(", ")

        val totalItemsPrice = cart.entries.sumOf { (prodId, qty) ->
            val p = OjekSonggonData.marketProducts.find { it.id == prodId }
            (p?.price ?: 0) * qty
        }
        val deliveryFee = 10000
        val total = totalItemsPrice + deliveryFee

        val newOrder = BookingOrder(
            id = "BELANJA-${(1000..9999).random()}",
            serviceCategory = ServiceCategory.BELANJA,
            pickupName = "Pasar Tradisional Songgon",
            dropoffName = _uiState.value.shoppingDeliveryAddress,
            vehicleName = "Motor Titip Belanja",
            distanceKm = 2.5,
            totalFare = total,
            status = OrderStatus.SEARCHING,
            shoppingItems = itemsSummary,
            notes = _uiState.value.shoppingNotes,
            paymentMethod = _uiState.value.paymentMethod
        )

        _uiState.update {
            it.copy(
                activeOrder = newOrder,
                shoppingCart = emptyMap(),
                currentScreen = AppScreen.ACTIVE_TRACKING,
                snackbarMessage = "Pesanan belanja diteruskan ke driver!"
            )
        }

        startOrderTrackingSimulation()
    }

    fun createCourierOrder(itemDesc: String, recipient: String, dropoff: LocationPoint) {
        val pickup = _uiState.value.selectedPickup
        val distance = OjekSonggonData.calculateDistanceKm(pickup, dropoff)
        val vehicle = OjekSonggonData.vehicleOptions[0]
        val fare = OjekSonggonData.calculateFare(distance, vehicle) + 3000 // courier handling fee

        val newOrder = BookingOrder(
            id = "KURIR-${(1000..9999).random()}",
            serviceCategory = ServiceCategory.KURIR,
            pickupName = pickup.name,
            dropoffName = dropoff.name,
            vehicleName = "Kurir Ojek Kilat",
            distanceKm = distance,
            totalFare = fare,
            status = OrderStatus.SEARCHING,
            notes = "Barang: $itemDesc | Penerima: $recipient",
            paymentMethod = _uiState.value.paymentMethod
        )

        _uiState.update {
            it.copy(
                activeOrder = newOrder,
                currentScreen = AppScreen.ACTIVE_TRACKING,
                snackbarMessage = "Kurir kilat siap menjemput paket!"
            )
        }

        startOrderTrackingSimulation()
    }

    private fun startOrderTrackingSimulation() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            // Step 1: Finding driver -> 2 seconds
            delay(2500)
            val assignedDriver = OjekSonggonData.verifiedDrivers.random()
            _uiState.update { state ->
                val current = state.activeOrder ?: return@update state
                state.copy(
                    activeOrder = current.copy(
                        status = OrderStatus.DRIVER_ASSIGNED,
                        driver = assignedDriver
                    ),
                    snackbarMessage = "Driver ${assignedDriver.name} (${assignedDriver.plateNumber}) ditemukan!"
                )
            }

            // Step 2: Driver on way -> 6 seconds
            delay(6000)
            _uiState.update { state ->
                val current = state.activeOrder ?: return@update state
                if (current.status == OrderStatus.DRIVER_ASSIGNED) {
                    state.copy(
                        activeOrder = current.copy(status = OrderStatus.IN_TRANSIT),
                        snackbarMessage = "Perjalanan dimulai menuju ${current.dropoffName}"
                    )
                } else state
            }
        }
    }

    fun advanceOrderSimulation() {
        val current = _uiState.value.activeOrder ?: return
        when (current.status) {
            OrderStatus.SEARCHING -> {
                val driver = OjekSonggonData.verifiedDrivers.random()
                _uiState.update {
                    it.copy(
                        activeOrder = current.copy(
                            status = OrderStatus.DRIVER_ASSIGNED,
                            driver = driver
                        ),
                        snackbarMessage = "Driver ${driver.name} telah merespons!"
                    )
                }
            }
            OrderStatus.DRIVER_ASSIGNED -> {
                _uiState.update {
                    it.copy(
                        activeOrder = current.copy(status = OrderStatus.IN_TRANSIT),
                        snackbarMessage = "Penjemputan selesai, sekarang dalam perjalanan!"
                    )
                }
            }
            OrderStatus.IN_TRANSIT -> {
                completeActiveOrder()
            }
            else -> {}
        }
    }

    fun completeActiveOrder() {
        simulationJob?.cancel()
        val current = _uiState.value.activeOrder ?: return
        val completed = current.copy(status = OrderStatus.COMPLETED)
        _uiState.update { state ->
            state.copy(
                activeOrder = null,
                orderHistory = listOf(completed) + state.orderHistory,
                currentScreen = AppScreen.ORDER_HISTORY,
                snackbarMessage = "Perjalanan selesai! Terima kasih telah menggunakan Ojek Songgon."
            )
        }
    }

    fun cancelActiveOrder() {
        simulationJob?.cancel()
        val current = _uiState.value.activeOrder ?: return
        val cancelled = current.copy(status = OrderStatus.CANCELLED)
        _uiState.update { state ->
            state.copy(
                activeOrder = null,
                orderHistory = listOf(cancelled) + state.orderHistory,
                currentScreen = AppScreen.HOME,
                snackbarMessage = "Pesanan telah dibatalkan."
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun getWhatsAppDispatchUrl(order: BookingOrder?): String {
        val o = order ?: _uiState.value.activeOrder
        val text = if (o != null) {
            "Halo Posko Ojek Songgon, saya mau pesan jasa ojek:\n" +
                    "- Layanan: ${o.serviceCategory.title}\n" +
                    "- Jemput: ${o.pickupName}\n" +
                    "- Tujuan: ${o.dropoffName}\n" +
                    "- Kendaraan: ${o.vehicleName}\n" +
                    "- Estimasi Tarif: ${formatRupiah(o.totalFare)}\n" +
                    if (o.notes.isNotBlank()) "- Catatan: ${o.notes}\n" else "" +
                    "Mohon bantuan armada terdekat. Terima kasih!"
        } else {
            "Halo Admin Posko Ojek Songgon, saya ingin memesan ojek / tur wisata Songgon Banyuwangi."
        }
        return "https://wa.me/${OjekSonggonData.HOTLINE_WHATSAPP}?text=${Uri.encode(text)}"
    }

    companion object {
        fun formatRupiah(amount: Int): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            format.maximumFractionDigits = 0
            return format.format(amount).replace("Rp", "Rp ")
        }
    }
}
