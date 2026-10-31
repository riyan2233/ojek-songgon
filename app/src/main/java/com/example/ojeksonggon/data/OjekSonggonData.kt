package com.example.ojeksonggon.data

import com.example.ojeksonggon.model.Driver
import com.example.ojeksonggon.model.LocationPoint
import com.example.ojeksonggon.model.MarketProduct
import com.example.ojeksonggon.model.TourPackage
import com.example.ojeksonggon.model.VehicleOption

object OjekSonggonData {

    const val HOTLINE_WHATSAPP = "6282233445566"
    const val HOTLINE_PHONE = "+62 822-3344-5566"
    const val POSKO_ADDRESS = "Jl. Raya Songgon No. 12 (Sebelah Timur Pasar Tradisional Songgon), Banyuwangi"

    val popularLocations = listOf(
        LocationPoint("loc_pasar", "Pasar Tradisional Songgon", "Pasar & Sentra", "Jl. Raya Songgon", 0.0),
        LocationPoint("loc_pinus", "Wisata Hutan Pinus Songgon", "Wisata", "Dusun Sumberagung, Sumberbulu", 3.8),
        LocationPoint("loc_rafting", "Songgon Rafting & Tubing Badeng", "Wisata", "Sungai Badeng, Sumberbulu", 4.2),
        LocationPoint("loc_lider", "Air Terjun Lider (Kaki G. Raung)", "Wisata", "Hutan Lindung Kaki Gunung Raung", 11.5),
        LocationPoint("loc_bayu", "Sentra Durian Songgon (Dusun Bayu)", "Pasar & Sentra", "Dusun Krajan, Desa Bayu", 5.1),
        LocationPoint("loc_villa", "Villa & Camp Kaki Raung", "Wisata", "Lereng Perkebunan Sragi", 7.0),
        LocationPoint("loc_camat", "Kantor Kecamatan Songgon", "Pusat Pelayanan", "Jl. A. Yani No. 1 Songgon", 0.8),
        LocationPoint("loc_puskesmas", "Puskesmas Songgon", "Pusat Pelayanan", "Jl. Raya Bedewang", 1.2),
        LocationPoint("loc_balak", "Balai Desa Balak", "Desa / Pemukiman", "Desa Balak, Songgon", 3.2),
        LocationPoint("loc_rogojampi", "Stasiun KA Rogojampi", "Transportasi", "Rogojampi, Banyuwangi", 12.0),
        LocationPoint("loc_temuguruh", "Stasiun KA Temuguruh", "Transportasi", "Sempu, Banyuwangi", 9.5)
    )

    val vehicleOptions = listOf(
        VehicleOption(
            id = "v_std",
            name = "Motor Standar (Vario/Beat/Nmax)",
            description = "Nyaman untuk rute jalan aspal, antar desa, pasar & stasiun",
            baseFare = 8000,
            perKmRate = 2500,
            iconType = "standard"
        ),
        VehicleOption(
            id = "v_trail",
            name = "Motor Trail Kaki Raung (KLX/CRF)",
            description = "Khusus jalan menanjak, bebatuan, kebun durian & jalur Air Terjun Lider",
            baseFare = 14000,
            perKmRate = 3500,
            iconType = "trail"
        )
    )

    val verifiedDrivers = listOf(
        Driver(
            id = "drv_1",
            name = "Pak Supriadi",
            phone = "081234567801",
            plateNumber = "P 4821 XG",
            motorcycleModel = "Honda Vario 160 Hitam",
            rating = 4.9,
            completedTrips = 1420
        ),
        Driver(
            id = "drv_2",
            name = "Mas Bayu Wicaksono",
            phone = "081234567802",
            plateNumber = "P 6290 ZB",
            motorcycleModel = "Kawasaki KLX 150 Hijau",
            rating = 5.0,
            completedTrips = 890,
            badge = "Spesialis Medan Pegunungan & Wisata"
        ),
        Driver(
            id = "drv_3",
            name = "Cak Roni Durian",
            phone = "081234567803",
            plateNumber = "P 3125 WA",
            motorcycleModel = "Yamaha NMAX Biru",
            rating = 4.95,
            completedTrips = 1130
        ),
        Driver(
            id = "drv_4",
            name = "Pak Slamet Riyadi",
            phone = "081234567804",
            plateNumber = "P 5018 UK",
            motorcycleModel = "Honda Beat ESP Putih",
            rating = 4.88,
            completedTrips = 1750
        )
    )

    val tourPackages = listOf(
        TourPackage(
            id = "tour_pinus_rafting",
            title = "Jelajah Pinus & Tubing Kali Badeng",
            duration = "3 - 4 Jam",
            price = 125000,
            rating = 4.9,
            highlights = listOf("Hutan Pinus Asri", "River Tubing Arus Jernih", "Spot Foto Keren"),
            description = "Nikmati sejuknya deretan pohon pinus Songgon yang rindang dilanjutkan sensasi river tubing menyusuri aliran air jernih pegunungan Raung.",
            includes = listOf("Antar jemput motor ojek PP", "Tiket masuk lokasi wisata", "Perlengkapan tubing & pelampung", "Driver merangkap pemandu lokal")
        ),
        TourPackage(
            id = "tour_lider",
            title = "Ekspedisi Curug Air Terjun Lider",
            duration = "4 - 5 Jam",
            price = 150000,
            rating = 5.0,
            highlights = listOf("Air Terjun Tertinggi 60m", "Motor Trail Offroad", "Hutan Lindung Asli"),
            description = "Petualangan seru menuju air terjun tertinggi di Banyuwangi di lereng Gunung Raung. Naik motor trail menembus perkebunan dan tracking ditemani driver berpengalaman.",
            includes = listOf("Antar jemput motor trail khusus", "Helm & jas hujan darurat", "Tiket masuk kawasan konservasi", "Air mineral & camilan lokal")
        ),
        TourPackage(
            id = "tour_durian_kopi",
            title = "Cicip Durian Merah & Kebun Kopi Raung",
            duration = "2 - 3 Jam",
            price = 95000,
            rating = 4.85,
            highlights = listOf("Sentra Durian Bayu", "Cicip Buah Segar Pohon", "Seduh Kopi Robusta"),
            description = "Wisata rasa keliling kebun durian legendaris Songgon. Belajar membedakan durian merah Banyuwangi dan durian kasur, plus mencicipi kopi khas lereng Raung.",
            includes = listOf("Antar keliling sentra kebun durian", "Sesi cicip 1 porsi durian segar", "Secangkir kopi robusta Songgon", "Rekomendasi durian terbaik untuk oleh-oleh")
        )
    )

    val marketProducts = listOf(
        MarketProduct(
            id = "prod_durian_merah",
            name = "Durian Merah Asli Songgon",
            category = "Durian",
            price = 75000,
            unit = "1 Buah (Sedang)",
            seller = "Sentra Durian Dusun Bayu",
            description = "Durian khas Banyuwangi dengan daging tebal kemerahan, aroma lembut legit, dan rasa manis gurih khas tanah vulkanik Raung.",
            isBestSeller = true
        ),
        MarketProduct(
            id = "prod_durian_kasur",
            name = "Durian Si Kasur Songgon",
            category = "Durian",
            price = 45000,
            unit = "1 Buah (Besar)",
            seller = "Kebun Pak Haji Songgon",
            description = "Varietas lokal Songgon daging kuning montok lembut bagai kasur, manis pulen beralkohol tipis.",
            isBestSeller = true
        ),
        MarketProduct(
            id = "prod_rujak_soto",
            name = "Rujak Soto Khas Banyuwangi",
            category = "Kuliner",
            price = 18000,
            unit = "1 Porsi Lengkap",
            seller = "Warung Mbok Yah Pasar Songgon",
            description = "Kuliner legendaris perpaduan rujak petis uleg disiram kuah soto babat hangat gurih dengan kerupuk emping.",
            isBestSeller = true
        ),
        MarketProduct(
            id = "prod_kopi_raung",
            name = "Kopi Robusta Lereng Raung 250g",
            category = "Hasil Bumi",
            price = 35000,
            unit = "Kemasan 250g Bubuk",
            seller = "Koperasi Petani Songgon",
            description = "Biji kopi petik merah dari lereng Gunung Raung dengan notes cokelat pekat dan aroma rempah alami."
        ),
        MarketProduct(
            id = "prod_bagiak",
            name = "Bagiak Renyah Banyuwangi",
            category = "Kuliner",
            price = 22000,
            unit = "1 Bungkus 250g",
            seller = "Oleh-oleh Songgon Asri",
            description = "Kue kering tradisional khas Banyuwangi dari tepung sagu dan susu jahe, renyah dan lumer di mulut."
        ),
        MarketProduct(
            id = "prod_sayur_pasar",
            name = "Paket Sayuran Segar Petani Lokal",
            category = "Hasil Bumi",
            price = 25000,
            unit = "1 Keranjang Aneka Sayur",
            seller = "Pasar Tradisional Songgon",
            description = "Paket belanja sayur sawi, kubis, tomat, cabai rawit dan tempe segar langsung dari pedagang pasar."
        )
    )

    fun calculateDistanceKm(pickup: LocationPoint, dropoff: LocationPoint): Double {
        val diff = Math.abs(pickup.kmFromBase - dropoff.kmFromBase)
        return if (diff < 1.0) 1.5 else Math.round(diff * 1.2 * 10.0) / 10.0
    }

    fun calculateFare(distanceKm: Double, vehicle: VehicleOption): Int {
        val calculated = vehicle.baseFare + (distanceKm * vehicle.perKmRate).toInt()
        // Round to nearest 1,000 IDR
        return ((calculated + 999) / 1000) * 1000
    }
}
