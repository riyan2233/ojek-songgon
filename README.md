# Ojek Songgon - Aplikasi Ojek, Kurir Kilat & Wisata Lokal Banyuwangi

Aplikasi Android modern berbasis Kotlin dan Jetpack Compose untuk layanan transportasi ojek lokal, ekspedisi tur wisata alam lereng Gunung Raung, titip belanja hasil bumi (Durian Songgon & Pasar Tradisional), serta kurir kilat dokumen dan paket bagi masyarakat serta wisatawan di Kecamatan Songgon, Kabupaten Banyuwangi.

## Fitur Utama

1. **Ojek Penumpang (Harian & Stasiun):**
   - Pemilihan titik jemput dan tujuan di seluruh desa, fasilitas publik, dan sentra wisata Songgon.
   - Pilihan armada: **Motor Standar** (aspal/harian) dan **Motor Trail Kaki Raung** (tanjakan & rute alam).
   - Perhitungan estimasi jarak dan tarif transparan sesuai standar paguyuban lokal.
   - Pilihan pembayaran: Tunai (Cash) atau QRIS Paguyuban.

2. **Tur Wisata Songgon (Jelajah Alam & Kebun Durian):**
   - Paket wisata terkurasi dengan pemandu rider lokal:
     - *Jelajah Pinus & Tubing Kali Badeng*
     - *Ekspedisi Curug Air Terjun Lider* (Air Terjun tertinggi Banyuwangi)
     - *Cicip Durian Merah & Kebun Kopi Raung*
   - Pemesanan langsung atau konsultasi via WhatsApp Posko.

3. **Titip Belanja & Hasil Bumi Songgon:**
   - Katalog produk khas: Durian Merah Asli Songgon, Durian Si Kasur, Rujak Soto Banyuwangi, Kopi Robusta Lereng Raung, Bagiak, serta paket sayuran pasar segar.
   - Keranjang belanja interaktif dan catatan pesanan untuk rider.

4. **Live Order Tracking & Komunikasi:**
   - Pelacakan status berjenjang: *Mencari Driver -> Driver Menuju Lokasi -> Dalam Perjalanan -> Selesai*.
   - Informasi lengkap pengemudi: Nama, foto/avatar, jenis motor, nomor plat polisi, dan rating.
   - Integrasi panggilan telepon, chat WhatsApp langsung, dan tombol bagikan status perjalanan.
   - Simulator perkembangan status perjalanan interaktif.

5. **Posko & Informasi Paguyuban Ojek:**
   - Profil resmi posko paguyuban di Jl. Raya Songgon (depan Pasar Tradisional).
   - Hotline darurat 24 jam dan standar keselamatan berkendara (helm SNI & rider terverifikasi).
   - Riwayat pesanan lengkap dengan rincian struk dan fitur pesan ulang (*repeat order*).

## Teknologi & Arsitektur

- **Bahasa:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Arsitektur:** MVVM (Model-View-ViewModel) dengan StateFlow & Coroutines
- **Build System:** Gradle (Kotlin DSL) dengan Version Catalog (`libs.versions.toml`)
- **Desain & Aset:** Adaptive Launcher Icon Material You dan visual hero banner bernuansa alam Songgon Banyuwangi.