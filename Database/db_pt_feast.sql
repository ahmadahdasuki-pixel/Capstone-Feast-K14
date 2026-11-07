-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 07, 2026 at 10:17 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_pt_feast`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_pengguna` int NOT NULL,
  `tanggal_diangkat` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_pengguna`, `tanggal_diangkat`) VALUES
(1, '2023-03-01'),
(2, '2024-01-15'),
(3, '2025-06-10');

-- --------------------------------------------------------

--
-- Table structure for table `bahan_baku`
--

CREATE TABLE `bahan_baku` (
  `id_bahan` int NOT NULL,
  `id_admin` int NOT NULL,
  `nama_bahan` varchar(100) NOT NULL,
  `satuan` varchar(20) NOT NULL,
  `stok_saat_ini` decimal(10,2) NOT NULL DEFAULT '0.00',
  `stok_minimum` decimal(10,2) NOT NULL DEFAULT '0.00',
  `status_bahan_baku` varchar(20) NOT NULL DEFAULT 'tersedia'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bahan_baku`
--

INSERT INTO `bahan_baku` (`id_bahan`, `id_admin`, `nama_bahan`, `satuan`, `stok_saat_ini`, `stok_minimum`, `status_bahan_baku`) VALUES
(1, 1, 'Tepung', 'kg', '4000.00', '50.00', 'Nonaktif'),
(2, 1, 'Gula Pasir', 'kg', '89.80', '25.00', 'Aktif'),
(3, 1, 'Mentega', 'kg', '56.30', '15.00', 'Nonaktif'),
(4, 1, 'Telur Ayam', 'kg', '2.40', '20.00', 'Aktif'),
(5, 2, 'Ragi Instan', 'kg', '6.00', '3.00', 'Aktif'),
(6, 1, 'Garam', 'kg', '15.00', '5.00', 'Aktif'),
(7, 2, 'Susu Cair', 'liter', '51.50', '20.00', 'Nonaktif'),
(8, 2, 'Cokelat Batang', 'kg', '28.00', '10.00', 'Aktif'),
(9, 3, 'Keju Cheddar', 'kg', '22.00', '10.00', 'Aktif'),
(10, 3, 'Selai Stroberi', 'kg', '14.00', '5.00', 'Aktif'),
(11, 3, 'Selai Nanas', 'kg', '13.50', '5.00', 'Aktif'),
(12, 3, 'Minyak Goreng', 'liter', '35.00', '15.00', 'Aktif'),
(13, 1, 'Pisang', 'kg', '20.00', '10.00', 'Aktif'),
(14, 1, 'Kacang Tanah', 'kg', '5.00', '8.00', 'Aktif'),
(15, 3, 'Kemasan Plastik', 'pcs', '2000.00', '500.00', 'Nonaktif'),
(19, 1, 'Kopi', 'Liter', '70.00', '50.00', 'Aktif'),
(20, 1, 'leci', 'kg', '80.00', '30.00', 'Aktif'),
(21, 1, 'Pute', 'liter', '90.00', '50.00', 'Aktif'),
(22, 1, 'Kacang', 'pcs', '90.00', '15.00', 'Aktif'),
(23, 1, 'gandum', 'kg', '97.00', '60.00', 'Nonaktif'),
(24, 1, 'Gula', 'kg', '6000.00', '50.00', 'Aktif'),
(25, 1, 'Tepung Terigu', 'kg', '10.00', '5.00', 'Aktif'),
(26, 1, 'Keju', 'pcs', '175.00', '10.00', 'Aktif'),
(27, 1, 'l', 'k', '90.00', '9.00', 'Aktif');

-- --------------------------------------------------------

--
-- Table structure for table `detail_distribusi_batch`
--

CREATE TABLE `detail_distribusi_batch` (
  `id_detail_distribusi` int NOT NULL,
  `id_distribusi` int NOT NULL,
  `id_batch` int NOT NULL,
  `jumlah_diambil` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_distribusi_batch`
--

INSERT INTO `detail_distribusi_batch` (`id_detail_distribusi`, `id_distribusi`, `id_batch`, `jumlah_diambil`) VALUES
(1, 1, 1, 100),
(2, 2, 1, 80),
(3, 2, 2, 70),
(4, 3, 2, 60),
(5, 3, 3, 50),
(6, 4, 4, 150),
(7, 4, 3, 150),
(8, 5, 4, 120),
(9, 5, 1, 120),
(10, 6, 5, 60),
(11, 6, 2, 40),
(12, 7, 5, 90),
(13, 7, 6, 70),
(14, 8, 6, 60),
(15, 8, 7, 80),
(16, 10, 7, 100),
(17, 10, 8, 100),
(18, 11, 8, 120),
(19, 11, 9, 130),
(20, 12, 9, 220),
(21, 12, 8, 40),
(22, 13, 10, 90),
(23, 13, 11, 60),
(24, 14, 11, 70),
(25, 14, 12, 100),
(26, 15, 12, 160),
(27, 15, 13, 90),
(28, 16, 13, 60),
(29, 16, 14, 120),
(30, 17, 14, 100),
(31, 17, 15, 60),
(32, 18, 16, 70),
(33, 18, 15, 40),
(34, 19, 17, 80),
(35, 19, 18, 50),
(36, 20, 19, 60),
(37, 20, 20, 70),
(38, 21, 12, 50),
(39, 22, 5, 20),
(41, 23, 5, 20),
(44, 24, 10, 30),
(47, 25, 16, 20),
(48, 25, 17, 10),
(49, 26, 16, 20),
(50, 26, 17, 10),
(51, 28, 16, 20),
(52, 28, 17, 10),
(53, 29, 18, 20);

-- --------------------------------------------------------

--
-- Table structure for table `detail_resep`
--

CREATE TABLE `detail_resep` (
  `id_detail_resep` int NOT NULL,
  `id_produk` int NOT NULL,
  `id_bahan` int NOT NULL,
  `jumlah_bahan` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_resep`
--

INSERT INTO `detail_resep` (`id_detail_resep`, `id_produk`, `id_bahan`, `jumlah_bahan`) VALUES
(1, 1, 1, '0.40'),
(2, 1, 2, '0.05'),
(3, 1, 3, '0.05'),
(4, 1, 5, '20.00'),
(5, 2, 1, '0.08'),
(6, 2, 8, '0.03'),
(7, 2, 2, '0.01'),
(8, 3, 1, '0.08'),
(9, 3, 9, '0.03'),
(10, 3, 3, '0.01'),
(11, 4, 1, '0.19'),
(12, 4, 10, '0.03'),
(13, 5, 1, '0.08'),
(14, 5, 11, '0.03'),
(15, 6, 1, '0.07'),
(16, 6, 13, '0.05'),
(17, 6, 2, '0.01'),
(18, 7, 1, '0.07'),
(19, 7, 14, '0.03'),
(20, 8, 1, '0.06'),
(21, 8, 2, '0.02'),
(22, 8, 12, '0.02'),
(23, 9, 1, '0.06'),
(24, 9, 8, '0.03'),
(25, 9, 12, '0.02'),
(26, 10, 1, '0.05'),
(27, 10, 4, '0.03'),
(28, 10, 2, '0.03'),
(29, 11, 1, '0.25'),
(30, 11, 7, '0.05'),
(31, 11, 3, '0.03'),
(32, 12, 1, '0.08'),
(33, 12, 7, '0.04'),
(34, 13, 1, '0.07'),
(35, 13, 12, '0.03'),
(36, 13, 9, '0.02'),
(37, 14, 1, '0.12'),
(38, 14, 4, '0.01'),
(39, 14, 3, '0.01'),
(40, 15, 1, '0.09'),
(41, 15, 8, '0.02'),
(42, 15, 9, '0.02'),
(44, 17, 1, '50.00'),
(45, 17, 24, '60.00'),
(46, 16, 22, '10.00'),
(47, 19, 5, '50.00');

-- --------------------------------------------------------

--
-- Table structure for table `detail_transaksi`
--

CREATE TABLE `detail_transaksi` (
  `id_detail` int NOT NULL,
  `id_transaksi` varchar(10) DEFAULT NULL,
  `nama_produk` varchar(100) DEFAULT NULL,
  `jumlah` int DEFAULT NULL,
  `subtotal` decimal(12,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_transaksi`
--

INSERT INTO `detail_transaksi` (`id_detail`, `id_transaksi`, `nama_produk`, `jumlah`, `subtotal`) VALUES
(1, 'TR001', 'Roti Cokelat', 2, '20000.00'),
(2, 'TR001', 'Roti Keju', 1, '12000.00'),
(3, 'TR002', 'Roti Tawar', 3, '30000.00'),
(4, 'TR003', 'Roti Cokelat', 2, '20000.00'),
(5, 'TR004', 'Roti Keju', 2, '24000.00');

-- --------------------------------------------------------

--
-- Table structure for table `distribusi`
--

CREATE TABLE `distribusi` (
  `id_distribusi` int NOT NULL,
  `id_distributor` int NOT NULL,
  `id_staff` int NOT NULL,
  `tanggal_distribusi` date NOT NULL,
  `status_distribusi` varchar(20) NOT NULL,
  `keterangan` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `distribusi`
--

INSERT INTO `distribusi` (`id_distribusi`, `id_distributor`, `id_staff`, `tanggal_distribusi`, `status_distribusi`, `keterangan`) VALUES
(1, 1, 4, '2026-10-02', 'selesai', NULL),
(2, 2, 5, '2026-10-03', 'selesai', 'Pengiriman rutin'),
(3, 3, 6, '2026-10-04', 'selesai', NULL),
(4, 4, 7, '2026-10-05', 'Selesai', 'Pengiriman telah diterima distributor'),
(5, 5, 8, '2026-10-06', 'selesai', NULL),
(6, 6, 9, '2026-10-07', 'selesai', NULL),
(7, 7, 10, '2026-10-09', 'selesai', NULL),
(8, 8, 11, '2026-10-10', 'selesai', 'Kantin kampus'),
(10, 9, 13, '2026-10-12', 'selesai', NULL),
(11, 10, 14, '2026-10-14', 'selesai', NULL),
(12, 11, 15, '2026-10-15', 'selesai', 'Pesanan hotel'),
(13, 12, 4, '2026-10-17', 'selesai', NULL),
(14, 13, 5, '2026-10-19', 'selesai', NULL),
(15, 14, 6, '2026-10-21', 'selesai', 'Pengiriman antar kota'),
(16, 15, 7, '2026-10-23', 'selesai', 'Pengiriman antar kota'),
(17, 2, 8, '2026-10-24', 'Selesai', NULL),
(18, 3, 9, '2026-10-25', 'dikirim', NULL),
(19, 5, 10, '2026-10-26', 'diproses', NULL),
(20, 7, 11, '2026-10-27', 'diproses', 'Pesanan hari ini'),
(21, 3, 5, '2026-10-28', 'Selesai', 'Pengiriman reguler'),
(22, 1, 4, '2026-10-28', 'diproses', NULL),
(23, 1, 4, '2026-10-28', 'batal', NULL),
(24, 2, 4, '2026-10-28', 'Batal', 'Distribusi multi-batch'),
(25, 2, 8, '2026-10-27', 'diproses', 'Distribusi roti ke Minimarket Berkah Jaya'),
(26, 2, 8, '2026-10-27', 'diproses', 'Distribusi roti ke Minimarket Berkah Jaya'),
(28, 2, 8, '2026-10-27', 'diproses', 'Distribusi dengan koreksi jumlah batch'),
(29, 2, 8, '2026-10-27', 'batal', 'Distribusi dengan koreksi jumlah batch'),
(30, 2, 8, '2026-10-27', 'diproses', 'Pengiriman 40 roti cokelat ke Minimarket Berkah Jaya'),
(31, 2, 4, '2026-10-20', 'Selesai', 'Jumlah: 10 (Batch ID: 3)'),
(32, 4, 4, '2026-12-12', 'Selesai', 'Jumlah: 15 (Batch ID: 3)'),
(33, 11, 4, '2026-12-13', 'Batal', '[PRODUK:Donat Gula] [JML:15] (Batch ID: 4)'),
(34, 2, 4, '2026-12-20', 'Selesai', '[PRODUK:Roti Tawar] [JML:20] (Batch ID: 8)'),
(35, 9, 4, '2026-11-10', 'Selesai', '[PRODUK:Roti Susu] [JML:20] (Batch ID: 13)'),
(36, 15, 4, '2026-10-12', 'Selesai', '[PRODUK:Bolu Kukus] [JML:10] (Batch ID: 6)'),
(37, 8, 4, '2026-12-20', 'Selesai', '[PRODUK:Donat Gula] [JML:50] (Batch ID: 4)'),
(38, 8, 4, '2026-11-04', 'diproses', '[PRODUK:Donat Gula] [JML:50] (Batch ID: 4)'),
(39, 11, 4, '2026-10-20', 'diproses', '[PRODUK:Roti Keju] [JML:15] (Batch ID: 3)'),
(40, 11, 4, '2026-09-09', 'diproses', '[PRODUK:Donat Gula] [JML:9] (Batch ID: 4)');

-- --------------------------------------------------------

--
-- Table structure for table `distributor`
--

CREATE TABLE `distributor` (
  `id_distributor` int NOT NULL,
  `id_admin` int NOT NULL,
  `nama_distributor` varchar(100) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `nomor_telepon_distributor` varchar(20) NOT NULL,
  `status_distributor` varchar(20) NOT NULL DEFAULT 'aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `distributor`
--

INSERT INTO `distributor` (`id_distributor`, `id_admin`, `nama_distributor`, `alamat`, `nomor_telepon_distributor`, `status_distributor`) VALUES
(1, 1, 'Toko Roti Sumber Rejeki', 'Jl. Mulawarman No. 10, Samarinda', '0541-741001', 'aktif'),
(2, 1, 'Minimarket Berkah Jaya', 'Jl. Pahlawan No. 25, Samarinda', '0541-741002', 'aktif'),
(3, 1, 'Warung Bu Sari', 'Jl. Juanda No. 8, Samarinda', '0541-741003', 'aktif'),
(4, 2, 'Kafe Kopi Senja', 'Jl. Gatot Subroto No. 14, Samarinda', '0541-741004', 'aktif'),
(5, 2, 'Supermarket Hadi', 'Jl. P. Antasari No. 3, Samarinda', '0541-741005', 'aktif'),
(6, 2, 'Toko Kelontong Maju Terus', 'Jl. Dr. Soetomo No. 19, Samarinda', '0541-741006', 'aktif'),
(7, 3, 'Sejahtera Mart', 'Jl. Cendana No. 7, Samarinda', '0541-741007', 'aktif'),
(8, 3, 'Koperasi Kampus Unmul', 'Jl. Kuaro, Gn. Kelua, Samarinda', '0541-741008', 'aktif'),
(9, 1, 'RM Padang Salero', 'Jl. Lambung Mangkurat No. 21, Samarinda', '0541-741009', 'aktif'),
(10, 1, 'Toko Kue Mawar', 'Jl. Slamet Riyadi No. 5, Samarinda', '0541-741010', 'aktif'),
(11, 2, 'Hotel Mutiara Cafe', 'Jl. Sudirman No. 30, Samarinda', '082136984447', 'Aktif'),
(12, 2, 'Kantin SMA Negeri 1', 'Jl. Dahlia No. 2, Samarinda', '0541-741012', 'aktif'),
(13, 3, 'Warung Pak Haji', 'Jl. Ahmad Yani No. 44, Samarinda', '0541-741013', 'nonaktif'),
(14, 3, 'Bakery Corner Balikpapan', 'Jl. MT Haryono No. 88, Balikpapan', '0542-741014', 'aktif'),
(15, 1, 'Oleh-Oleh Bontang', 'Jl. Awang Long No. 12, Bontang', '0548-741015', 'aktif'),
(16, 2, 'Toko Baru Tenggarong', 'Jl. Diponegoro No. 9, Tenggarong', '0541-741016', 'Nonaktif'),
(17, 1, 'WH FT', 'Gedung Baru', '08535552272728', 'Aktif'),
(18, 1, 'tomoro', 'merk', '08564635434234', 'Aktif');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` int NOT NULL,
  `nama_pengguna` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status_akun` varchar(20) NOT NULL DEFAULT 'aktif',
  `nomor_telepon` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `nama_pengguna`, `username`, `password`, `status_akun`, `nomor_telepon`) VALUES
(1, 'Budi Santoso', 'budi_admin', 'adminBudi2026!', 'aktif', '081234567801'),
(2, 'Rina Kartika', 'rina_admin', 'RinaAdmin#2026', 'aktif', '081234567802'),
(3, 'Agus Prasetyo', 'agus_admin', 'AgusAdmin@2026', 'aktif', '081234567803'),
(4, 'Siti Aminah', 'siti_staff', 'sitiStaff$123', 'aktif', '081234567804'),
(5, 'Dedi Kurniawan', 'dedi_staff', 'SitiStaff#2026', 'aktif', '081234567805'),
(17, 'Staff Baru', 'SukiStaff', 'Suki12345', 'Aktif', '087662627735'),
(18, 'Ahmad Ahdasuki', 'Sukayyy', 'SukayStaff123', 'aktif', '082136984447'),
(19, 'Yuda', 'aqia', '1234567', 'Aktif', '0865256522'),
(20, 'Ananta', 'Atha', 'atha123', 'Aktif', '08535627272'),
(22, 'Sumarna', 'sumar', '123456', 'Nonaktif', '0897524256171'),
(24, 'Yahya', 'yaya', '098765', 'Nonaktif', '0865474637'),
(25, 'L', 'o', '123456', 'Aktif', '08897876756'),
(26, 'Yuna', 'una', '171717', 'Aktif', '086335522627'),
(27, 'Ahmad', 'amad', '000000', 'Aktif', '087654242516'),
(28, 'Dafa', 'apa', '123456', 'Aktif', '08632626262');

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id_produk` int NOT NULL,
  `id_admin` int NOT NULL,
  `nama_produk` varchar(100) NOT NULL,
  `satuan_produk` varchar(20) NOT NULL,
  `harga_jual` decimal(12,2) NOT NULL,
  `status_produk` varchar(20) NOT NULL DEFAULT 'aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`id_produk`, `id_admin`, `nama_produk`, `satuan_produk`, `harga_jual`, `status_produk`) VALUES
(1, 1, 'Roti Tawar', 'pcs', '12500.00', 'Aktif'),
(2, 1, 'Roti Manis Cokelat', 'pcs', '8000.00', 'Aktif'),
(3, 1, 'Roti Keju', 'pcs', '9000.00', 'aktif'),
(4, 2, 'Roti Selai Stroberi', 'pcs', '7000.00', 'Aktif'),
(5, 2, 'Roti Selai Nanas', 'pcs', '7000.00', 'aktif'),
(6, 2, 'Roti Pisang', 'pcs', '8500.00', 'aktif'),
(7, 2, 'Roti Kacang', 'pcs', '8000.00', 'aktif'),
(8, 3, 'Donat Gula', 'pcs', '5000.00', 'Nonaktif'),
(9, 3, 'Donat Cokelat', 'pcs', '6000.00', 'aktif'),
(10, 3, 'Bolu Kukus', 'pcs', '6500.00', 'Aktif'),
(11, 1, 'Roti Sobek', 'pcs', '18000.00', 'aktif'),
(12, 1, 'Roti Susu', 'pcs', '9500.00', 'aktif'),
(13, 3, 'Roti Goreng Isi', 'pcs', '6000.00', 'Nonaktif'),
(14, 2, 'Roti Bun Burger', 'pcs', '12000.00', 'aktif'),
(15, 1, 'Roti Cokelat Keju', 'pcs', '10000.00', 'aktif'),
(16, 1, 'Roti Canai', 'pcs', '9000.00', 'aktif'),
(17, 1, 'Roti Buaya', 'pcs', '8000.00', 'Aktif'),
(19, 1, 'l', 'pcs', '5000.00', 'Aktif'),
(20, 1, 'Roti Gembong', 'pcs', '10000.00', 'Aktif'),
(21, 1, 'Roti Doraemon', 'pcs', '15000.00', 'Aktif');

-- --------------------------------------------------------

--
-- Table structure for table `produksi_batch`
--

CREATE TABLE `produksi_batch` (
  `id_batch` int NOT NULL,
  `id_produk` int NOT NULL,
  `id_staff` int NOT NULL,
  `tanggal_produksi` date NOT NULL,
  `tanggal_kadaluarsa` date NOT NULL,
  `jumlah_produksi` int NOT NULL,
  `sisa_stok_batch` int NOT NULL,
  `status_batch` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produksi_batch`
--

INSERT INTO `produksi_batch` (`id_batch`, `id_produk`, `id_staff`, `tanggal_produksi`, `tanggal_kadaluarsa`, `jumlah_produksi`, `sisa_stok_batch`, `status_batch`) VALUES
(1, 1, 4, '2026-10-01', '2026-10-25', 300, 0, 'Dibuang'),
(2, 2, 5, '2026-10-02', '2026-11-05', 250, 75, 'Selesai'),
(3, 3, 6, '2026-10-03', '2026-11-25', 200, 60, 'Gagal'),
(4, 8, 7, '2026-10-04', '2026-10-07', 400, 6, 'Selesai'),
(5, 11, 4, '2026-10-06', '2026-10-11', 150, 0, 'Gagal'),
(6, 10, 8, '2026-10-08', '2026-10-12', 200, 60, 'Selesai'),
(7, 14, 9, '2026-10-09', '2026-10-14', 180, 0, 'Gagal'),
(8, 1, 5, '2026-10-12', '2026-10-17', 300, 20, 'Selesai'),
(9, 9, 10, '2026-10-13', '2026-10-16', 350, 0, 'Gagal'),
(10, 4, 6, '2026-10-15', '2026-10-20', 220, 100, 'Selesai'),
(11, 6, 11, '2026-10-16', '2026-10-21', 180, 50, 'Selesai'),
(12, 2, 7, '2026-10-18', '2026-10-23', 260, 0, 'Dibuang'),
(13, 12, 12, '2026-10-19', '2026-10-24', 200, 30, 'Selesai'),
(14, 1, 13, '2026-10-21', '2026-10-26', 320, 0, 'Gagal'),
(15, 7, 8, '2026-10-22', '2026-10-27', 150, 50, 'Selesai'),
(16, 11, 14, '2026-10-23', '2026-10-28', 160, 10, 'Selesai'),
(17, 3, 15, '2026-10-24', '2026-10-29', 210, 80, 'Selesai'),
(18, 15, 9, '2026-10-25', '2026-10-30', 190, 120, 'Selesai'),
(19, 5, 10, '2026-10-26', '2026-10-31', 200, 140, 'Selesai'),
(20, 13, 4, '2026-10-27', '2026-10-30', 240, 170, 'Selesai'),
(23, 1, 4, '2026-10-20', '2026-10-30', 100, 0, 'Gagal'),
(24, 5, 4, '2026-11-05', '2026-11-15', 10, 10, 'Selesai'),
(25, 4, 4, '2026-12-20', '2026-12-30', 50, 50, 'Selesai'),
(26, 6, 4, '2026-10-12', '2026-10-15', 90, 0, 'Di Proses'),
(27, 4, 4, '2026-05-20', '2026-12-30', 100, 100, 'Selesai'),
(28, 7, 4, '2026-09-20', '2026-09-30', 75, 75, 'Selesai'),
(30, 1, 4, '2026-10-01', '2026-10-02', 100, 100, 'Selesai'),
(31, 15, 4, '2026-01-20', '2026-01-30', 100, 100, 'Selesai'),
(32, 6, 4, '2026-08-17', '2026-08-25', 100, 100, 'Selesai'),
(33, 12, 4, '2026-02-18', '2026-02-28', 100, 100, 'Selesai'),
(34, 14, 4, '2026-03-03', '2026-03-30', 100, 100, 'Selesai'),
(35, 11, 4, '2026-09-09', '2026-09-10', 90, 90, 'Dibuang'),
(36, 17, 4, '2026-09-05', '2026-09-07', 100, 100, 'Selesai'),
(37, 10, 4, '2026-01-10', '2026-01-15', 20, 20, 'Di Proses'),
(38, 10, 4, '2026-10-10', '2026-10-20', 100, 100, 'Di Proses'),
(39, 10, 4, '2026-07-10', '2026-07-20', 100, 0, 'Dibuang');

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `id_pengguna` int NOT NULL,
  `shift` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`id_pengguna`, `shift`) VALUES
(4, 'Pagi'),
(5, 'Siang'),
(6, 'Malam'),
(7, 'Pagi'),
(8, 'Siang'),
(9, 'Malam'),
(10, 'Pagi'),
(11, 'Siang'),
(12, 'Malam'),
(13, 'Pagi'),
(14, 'Siang'),
(15, 'Malam'),
(16, 'Siang'),
(17, 'Pagi'),
(18, 'Pagi'),
(19, 'Pagi'),
(20, 'Siang'),
(22, 'Pagi'),
(24, 'Pagi'),
(25, 'Pagi'),
(26, 'Pagi'),
(27, 'Pagi'),
(28, 'Pagi');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` varchar(10) NOT NULL,
  `tanggal_transaksi` date DEFAULT NULL,
  `nama_pelanggan` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `tanggal_transaksi`, `nama_pelanggan`) VALUES
('TR001', '2026-10-01', 'Ahmad'),
('TR002', '2026-10-02', 'Budi'),
('TR003', '2026-10-03', 'Citra'),
('TR004', '2026-10-04', 'Dina');

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_transaksi_toko`
-- (See below for the actual view)
--
CREATE TABLE `view_transaksi_toko` (
`id_transaksi` varchar(10)
,`jumlah` int
,`nama_pelanggan` varchar(100)
,`nama_produk` varchar(100)
,`subtotal` decimal(12,2)
,`tanggal_transaksi` date
);

-- --------------------------------------------------------

--
-- Structure for view `view_transaksi_toko`
--
DROP TABLE IF EXISTS `view_transaksi_toko`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_transaksi_toko`  AS SELECT `transaksi`.`id_transaksi` AS `id_transaksi`, `transaksi`.`tanggal_transaksi` AS `tanggal_transaksi`, `transaksi`.`nama_pelanggan` AS `nama_pelanggan`, `detail_transaksi`.`nama_produk` AS `nama_produk`, `detail_transaksi`.`jumlah` AS `jumlah`, `detail_transaksi`.`subtotal` AS `subtotal` FROM (`transaksi` join `detail_transaksi` on((`transaksi`.`id_transaksi` = `detail_transaksi`.`id_transaksi`)))  ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_pengguna`);

--
-- Indexes for table `bahan_baku`
--
ALTER TABLE `bahan_baku`
  ADD PRIMARY KEY (`id_bahan`),
  ADD KEY `fk_bahan_admin` (`id_admin`);

--
-- Indexes for table `detail_distribusi_batch`
--
ALTER TABLE `detail_distribusi_batch`
  ADD PRIMARY KEY (`id_detail_distribusi`),
  ADD KEY `fk_ddb_distribusi` (`id_distribusi`),
  ADD KEY `fk_ddb_batch` (`id_batch`);

--
-- Indexes for table `detail_resep`
--
ALTER TABLE `detail_resep`
  ADD PRIMARY KEY (`id_detail_resep`),
  ADD KEY `fk_resep_produk` (`id_produk`),
  ADD KEY `fk_resep_bahan` (`id_bahan`);

--
-- Indexes for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_transaksi` (`id_transaksi`);

--
-- Indexes for table `distribusi`
--
ALTER TABLE `distribusi`
  ADD PRIMARY KEY (`id_distribusi`),
  ADD KEY `fk_distribusi_distributor` (`id_distributor`),
  ADD KEY `fk_distribusi_staff` (`id_staff`);

--
-- Indexes for table `distributor`
--
ALTER TABLE `distributor`
  ADD PRIMARY KEY (`id_distributor`),
  ADD KEY `fk_distributor_admin` (`id_admin`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`),
  ADD KEY `fk_produk_admin` (`id_admin`);

--
-- Indexes for table `produksi_batch`
--
ALTER TABLE `produksi_batch`
  ADD PRIMARY KEY (`id_batch`),
  ADD KEY `fk_batch_produk` (`id_produk`),
  ADD KEY `fk_batch_staff` (`id_staff`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`id_pengguna`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bahan_baku`
--
ALTER TABLE `bahan_baku`
  MODIFY `id_bahan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `detail_distribusi_batch`
--
ALTER TABLE `detail_distribusi_batch`
  MODIFY `id_detail_distribusi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `detail_resep`
--
ALTER TABLE `detail_resep`
  MODIFY `id_detail_resep` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  MODIFY `id_detail` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `distribusi`
--
ALTER TABLE `distribusi`
  MODIFY `id_distribusi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `distributor`
--
ALTER TABLE `distributor`
  MODIFY `id_distributor` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id_pengguna` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `produk`
--
ALTER TABLE `produk`
  MODIFY `id_produk` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `produksi_batch`
--
ALTER TABLE `produksi_batch`
  MODIFY `id_batch` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `fk_admin_pengguna` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`);

--
-- Constraints for table `bahan_baku`
--
ALTER TABLE `bahan_baku`
  ADD CONSTRAINT `fk_bahan_admin` FOREIGN KEY (`id_admin`) REFERENCES `admin` (`id_pengguna`);

--
-- Constraints for table `detail_distribusi_batch`
--
ALTER TABLE `detail_distribusi_batch`
  ADD CONSTRAINT `fk_ddb_batch` FOREIGN KEY (`id_batch`) REFERENCES `produksi_batch` (`id_batch`),
  ADD CONSTRAINT `fk_ddb_distribusi` FOREIGN KEY (`id_distribusi`) REFERENCES `distribusi` (`id_distribusi`);

--
-- Constraints for table `detail_resep`
--
ALTER TABLE `detail_resep`
  ADD CONSTRAINT `fk_resep_bahan` FOREIGN KEY (`id_bahan`) REFERENCES `bahan_baku` (`id_bahan`),
  ADD CONSTRAINT `fk_resep_produk` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`);

--
-- Constraints for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD CONSTRAINT `detail_transaksi_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id_transaksi`);

--
-- Constraints for table `distribusi`
--
ALTER TABLE `distribusi`
  ADD CONSTRAINT `fk_distribusi_distributor` FOREIGN KEY (`id_distributor`) REFERENCES `distributor` (`id_distributor`),
  ADD CONSTRAINT `fk_distribusi_staff` FOREIGN KEY (`id_staff`) REFERENCES `staff` (`id_pengguna`);

--
-- Constraints for table `distributor`
--
ALTER TABLE `distributor`
  ADD CONSTRAINT `fk_distributor_admin` FOREIGN KEY (`id_admin`) REFERENCES `admin` (`id_pengguna`);

--
-- Constraints for table `produk`
--
ALTER TABLE `produk`
  ADD CONSTRAINT `fk_produk_admin` FOREIGN KEY (`id_admin`) REFERENCES `admin` (`id_pengguna`);

--
-- Constraints for table `produksi_batch`
--
ALTER TABLE `produksi_batch`
  ADD CONSTRAINT `fk_batch_produk` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`),
  ADD CONSTRAINT `fk_batch_staff` FOREIGN KEY (`id_staff`) REFERENCES `staff` (`id_pengguna`);

--
-- Constraints for table `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `fk_staff_pengguna` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
