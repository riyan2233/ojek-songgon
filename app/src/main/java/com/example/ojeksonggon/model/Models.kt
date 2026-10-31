package com.example.ojeksonggon.model

enum class ServiceCategory(
    val title: String,
    val subtitle: String,
    val iconName: String
) {
    OJEK_RIDE("Ojek Motor", "Antar jemput harian & stasiun", "two_wheeler"),
    WISATA("Tur Wisata", "Jelajah pinus, curug & kebun durian", "forest"),
    BELANJA("Titip Belanja", "Beli oleh-oleh, durian & sayur pasar", "shopping_bag"),
    KURIR("Kurir Kilat", "Antar paket & hasil kebun lokal", "local_shipping")
}

data class LocationPoint(
    val id: String,
    val name: String,
    val category: String, // "Wisata", "Pusat Transportasi", "Pasar & Sentra", "Desa / Pemukiman"
    val address: String,
    val kmFromBase: Double
)

data class VehicleOption(
    val id: String,
    val name: String,
    val description: String,
    val baseFare: Int,
    val perKmRate: Int,
    val iconType: String
)

data class Driver(
    val id: String,
    val name: String,
    val phone: String,
    val plateNumber: String,
    val motorcycleModel: String,
    val rating: Double,
    val completedTrips: Int,
    val badge: String = "Driver Resmi Paguyuban Songgon"
)

enum class OrderStatus(val label: String, val stepIndex: Int) {
    SEARCHING("Mencari Driver Terdekat...", 0),
    DRIVER_ASSIGNED("Driver Menuju Titik Jemput", 1),
    IN_TRANSIT("Dalam Perjalanan ke Tujuan", 2),
    COMPLETED("Perjalanan Selesai", 3),
    CANCELLED("Dibatalkan", -1)
}

data class BookingOrder(
    val id: String,
    val serviceCategory: ServiceCategory,
    val pickupName: String,
    val dropoffName: String,
    val vehicleName: String,
    val distanceKm: Double,
    val totalFare: Int,
    val status: OrderStatus,
    val driver: Driver? = null,
    val notes: String = "",
    val paymentMethod: String = "Tunai (Cash)",
    val tourPackageTitle: String? = null,
    val shoppingItems: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class TourPackage(
    val id: String,
    val title: String,
    val duration: String,
    val price: Int,
    val rating: Double,
    val highlights: List<String>,
    val description: String,
    val includes: List<String>
)

data class MarketProduct(
    val id: String,
    val name: String,
    val category: String, // "Durian", "Kuliner", "Hasil Bumi"
    val price: Int,
    val unit: String,
    val seller: String,
    val description: String,
    val isBestSeller: Boolean = false
)
