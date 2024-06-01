-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 01 Jun 2024 pada 19.33
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_absensi`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `absen`
--

CREATE TABLE `absen` (
  `id_absen` int(11) NOT NULL,
  `nis` varchar(10) DEFAULT NULL,
  `id_pertemuan` int(11) DEFAULT NULL,
  `ket` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `absen`
--

INSERT INTO `absen` (`id_absen`, `nis`, `id_pertemuan`, `ket`) VALUES
(25, '180001', 44, 'Hadir'),
(26, '180003', 44, 'Hadir'),
(27, '180004', 44, 'Hadir'),
(28, '180002', 44, 'hadir'),
(29, '180005', 44, 'Hadir'),
(30, '180006', 44, 'Hadir'),
(31, '180003', 1, 'Hadir'),
(32, '180004', 1, 'Hadir'),
(33, '180001', 1, 'Hadir'),
(34, '180001', 2, 'Hadir'),
(35, '180003', 2, 'Hadir'),
(36, '180004', 2, 'Hadir'),
(37, '180002', 2, 'Hadir'),
(38, '180005', 2, 'Hadir'),
(39, '180006', 2, 'Izin'),
(40, '180001', 3, 'Hadir'),
(41, '180003', 3, 'Hadir'),
(42, '180004', 3, 'Hadir'),
(43, '180007', 3, 'Hadir'),
(44, '180009', 3, 'Hadir'),
(45, '180010', 3, 'Hadir'),
(46, '180013', 3, 'Hadir'),
(47, '180015', 3, 'Hadir'),
(48, '180016', 3, 'Hadir'),
(49, '180019', 3, 'Hadir'),
(50, '180021', 3, 'Hadir'),
(51, '180022', 3, 'Hadir'),
(52, '180025', 3, 'Hadir'),
(53, '180026', 3, 'Hadir'),
(54, '180029', 3, 'Hadir'),
(55, '180031', 3, 'Hadir'),
(56, '180032', 3, 'Hadir'),
(57, '180035', 3, 'Hadir'),
(58, '180036', 3, 'Hadir'),
(59, '180039', 3, 'Hadir'),
(60, '180041', 3, 'Hadir'),
(61, '180042', 3, 'Hadir'),
(62, '180045', 3, 'Hadir'),
(63, '180046', 3, 'Hadir'),
(64, '180049', 3, 'Hadir'),
(65, '180051', 3, 'Hadir'),
(66, '180052', 3, 'Hadir'),
(67, '180055', 3, 'Hadir'),
(68, '180056', 3, 'Hadir'),
(69, '180059', 3, 'Hadir'),
(70, '180061', 3, 'Hadir'),
(71, '180062', 3, 'Hadir'),
(72, '180065', 3, 'Hadir'),
(73, '180066', 3, 'Hadir'),
(74, '180069', 3, 'Hadir'),
(75, '180071', 3, 'Hadir'),
(76, '180072', 3, 'Hadir'),
(77, '180075', 3, 'Hadir'),
(78, '180076', 3, 'Hadir'),
(79, '180079', 3, 'Hadir'),
(80, '180002', 3, 'Hadir'),
(81, '180005', 3, 'Hadir'),
(82, '180006', 3, 'Hadir'),
(83, '180008', 3, 'Hadir'),
(84, '180011', 3, 'Hadir'),
(85, '180012', 3, 'Hadir'),
(86, '180014', 3, 'Hadir'),
(87, '180017', 3, 'Hadir'),
(88, '180018', 3, 'Hadir'),
(89, '180020', 3, 'Hadir'),
(90, '180023', 3, 'Hadir'),
(91, '180024', 3, 'Hadir'),
(92, '180027', 3, 'Hadir'),
(93, '180028', 3, 'Hadir'),
(94, '180030', 3, 'Hadir'),
(95, '180033', 3, 'Hadir'),
(96, '180034', 3, 'Hadir'),
(97, '180037', 3, 'Hadir'),
(98, '180038', 3, 'Hadir'),
(99, '180040', 3, 'Hadir'),
(100, '180043', 3, 'Hadir'),
(101, '180044', 3, 'Hadir'),
(102, '180047', 3, 'Hadir'),
(103, '180048', 3, 'Hadir'),
(104, '180050', 3, 'Hadir'),
(105, '180053', 3, 'Hadir'),
(106, '180054', 3, 'Hadir'),
(107, '180057', 3, 'Hadir'),
(108, '180058', 3, 'Hadir'),
(109, '180060', 3, 'Hadir'),
(110, '180063', 3, 'Hadir'),
(111, '180064', 3, 'Hadir'),
(112, '180067', 3, 'Hadir'),
(113, '180068', 3, 'Hadir'),
(114, '180070', 3, 'Hadir'),
(115, '180073', 3, 'Hadir'),
(116, '180074', 3, 'Hadir'),
(117, '180077', 3, 'Hadir'),
(118, '180078', 3, 'Hadir');

-- --------------------------------------------------------

--
-- Struktur dari tabel `persentase`
--

CREATE TABLE `persentase` (
  `nis` int(11) NOT NULL,
  `kelas` char(1) NOT NULL,
  `persentase` float NOT NULL,
  `nama` char(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `persentase`
--

INSERT INTO `persentase` (`nis`, `kelas`, `persentase`, `nama`) VALUES
(180001, 'A', 100, 'John Doe'),
(180002, 'B', 100, 'Jane Smith'),
(180003, 'A', 100, 'Michael Johnson'),
(180004, 'A', 100, 'Emma Davis'),
(180005, 'B', 100, 'Daniel Wilson'),
(180006, 'B', 66.6667, 'Jane Smith'),
(180007, 'A', 100, 'William Martinez'),
(180008, 'B', 100, 'Sophia Anderson'),
(180009, 'A', 100, 'James Garcia'),
(180010, 'A', 100, 'Olivia Thomas'),
(180011, 'B', 100, 'Benjamin Lopez'),
(180012, 'B', 100, 'Emily Moore'),
(180013, 'A', 100, 'Alexander Walker'),
(180014, 'B', 100, 'Mia Perez'),
(180015, 'A', 100, 'Daniel Taylor'),
(180016, 'A', 100, 'Isabella Hernandez'),
(180017, 'B', 100, 'Jacob Martinez'),
(180018, 'B', 100, 'Ava Gonzalez'),
(180019, 'A', 100, 'Michael Young'),
(180020, 'B', 100, 'Sophia Robinson'),
(180021, 'A', 100, 'Matthew Lee'),
(180022, 'A', 100, 'Amelia Harris'),
(180023, 'B', 100, 'Ethan Clark'),
(180024, 'B', 100, 'Victoria Lewis'),
(180025, 'A', 100, 'David King'),
(180026, 'A', 100, 'Ella Wright'),
(180027, 'B', 100, 'Christopher Hill'),
(180028, 'B', 100, 'Madison Scott'),
(180029, 'A', 100, 'Andrew Green'),
(180030, 'B', 100, 'Sofia Adams'),
(180031, 'A', 100, 'Joshua Baker'),
(180032, 'A', 100, 'Scarlett Hall'),
(180033, 'B', 100, 'Ryan Allen'),
(180034, 'B', 100, 'Grace Nelson'),
(180035, 'A', 100, 'Noah Carter'),
(180036, 'A', 100, 'Chloe Mitchell'),
(180037, 'B', 100, 'Samuel Roberts'),
(180038, 'B', 100, 'Avery Perez'),
(180039, 'A', 100, 'Joseph Turner'),
(180040, 'B', 100, 'Lily Phillips'),
(180041, 'A', 100, 'Logan Campbell'),
(180042, 'A', 100, 'Evelyn Flores'),
(180043, 'B', 100, 'Lucas Morris'),
(180044, 'B', 100, 'Aria Hall'),
(180045, 'A', 100, 'Jackson Thompson'),
(180046, 'A', 100, 'Grace Ward'),
(180047, 'B', 100, 'Carter Rivera'),
(180048, 'B', 100, 'Hannah Gray'),
(180049, 'A', 100, 'Dylan Diaz'),
(180050, 'B', 100, 'Penelope James'),
(180051, 'A', 100, 'Nathan Evans'),
(180052, 'A', 100, 'Layla Stewart'),
(180053, 'B', 100, 'Henry Sanchez'),
(180054, 'B', 100, 'Zoe Morris'),
(180055, 'A', 100, 'Jack Collins'),
(180056, 'A', 100, 'Madeline Price'),
(180057, 'B', 100, 'Wyatt Mitchell'),
(180058, 'B', 100, 'Brooklyn Turner'),
(180059, 'A', 100, 'Luke White'),
(180060, 'B', 100, 'Addison Cook'),
(180061, 'A', 100, 'Gabriel Rivera'),
(180062, 'A', 100, 'Alyssa Kelly'),
(180063, 'B', 100, 'Julian Hughes'),
(180064, 'B', 100, 'Stella Ward'),
(180065, 'A', 100, 'Leo Butler'),
(180066, 'A', 100, 'Willow Bailey'),
(180067, 'B', 100, 'Lucas Foster'),
(180068, 'B', 100, 'Eleanor Perry'),
(180069, 'A', 100, 'Mason Long'),
(180070, 'B', 100, 'Claire Simmons'),
(180071, 'A', 100, 'Gavin Howard'),
(180072, 'A', 100, 'Maya Bryant'),
(180073, 'B', 100, 'Owen Russell'),
(180074, 'B', 100, 'Hazel Cox'),
(180075, 'A', 100, 'Hunter Reed'),
(180076, 'A', 100, 'Savannah Ward'),
(180077, 'B', 100, 'Isaac Griffin'),
(180078, 'B', 100, 'Natalie Fisher'),
(180079, 'A', 100, 'Connor Watson');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pertemuan`
--

CREATE TABLE `pertemuan` (
  `id_pertemuan` int(11) NOT NULL,
  `tgl` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pertemuan`
--

INSERT INTO `pertemuan` (`id_pertemuan`, `tgl`) VALUES
(44, '2024-06-01'),
(1, '2024-06-03'),
(2, '2024-06-04'),
(3, '2024-06-05'),
(4, '2024-06-06'),
(5, '2024-06-07'),
(6, '2024-06-10'),
(7, '2024-06-11'),
(8, '2024-06-12'),
(9, '2024-06-13'),
(10, '2024-06-14'),
(11, '2024-06-17'),
(12, '2024-06-18'),
(13, '2024-06-19'),
(14, '2024-06-20'),
(15, '2024-06-21'),
(16, '2024-06-24'),
(17, '2024-06-25'),
(18, '2024-06-26'),
(19, '2024-06-27'),
(20, '2024-06-28'),
(21, '2024-07-01'),
(22, '2024-07-02'),
(23, '2024-07-03'),
(24, '2024-07-04'),
(25, '2024-07-05'),
(26, '2024-07-08'),
(27, '2024-07-09'),
(28, '2024-07-10'),
(29, '2024-07-11'),
(30, '2024-07-12'),
(31, '2024-07-15'),
(32, '2024-07-16'),
(33, '2024-07-17'),
(34, '2024-07-18'),
(35, '2024-07-19'),
(36, '2024-07-22'),
(37, '2024-07-23'),
(38, '2024-07-24'),
(39, '2024-07-25'),
(40, '2024-07-26'),
(41, '2024-07-29'),
(42, '2024-07-30'),
(43, '2024-07-31');

-- --------------------------------------------------------

--
-- Struktur dari tabel `siswa`
--

CREATE TABLE `siswa` (
  `nis` varchar(10) NOT NULL,
  `no_absen` int(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `kelas` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `siswa`
--

INSERT INTO `siswa` (`nis`, `no_absen`, `nama`, `kelas`) VALUES
('180001', 2, 'John Doe', 'A'),
('180002', 3, 'Jane Smith', 'B'),
('180003', 4, 'Michael Johnson', 'A'),
('180004', 5, 'Emma Davis', 'A'),
('180005', 6, 'Daniel Wilson', 'B'),
('180006', 7, 'Jane Smith', 'B'),
('180007', 8, 'William Martinez', 'A'),
('180008', 9, 'Sophia Anderson', 'B'),
('180009', 10, 'James Garcia', 'A'),
('180010', 11, 'Olivia Thomas', 'A'),
('180011', 12, 'Benjamin Lopez', 'B'),
('180012', 13, 'Emily Moore', 'B'),
('180013', 14, 'Alexander Walker', 'A'),
('180014', 15, 'Mia Perez', 'B'),
('180015', 16, 'Daniel Taylor', 'A'),
('180016', 17, 'Isabella Hernandez', 'A'),
('180017', 18, 'Jacob Martinez', 'B'),
('180018', 19, 'Ava Gonzalez', 'B'),
('180019', 20, 'Michael Young', 'A'),
('180020', 21, 'Sophia Robinson', 'B'),
('180021', 22, 'Matthew Lee', 'A'),
('180022', 23, 'Amelia Harris', 'A'),
('180023', 24, 'Ethan Clark', 'B'),
('180024', 25, 'Victoria Lewis', 'B'),
('180025', 26, 'David King', 'A'),
('180026', 27, 'Ella Wright', 'A'),
('180027', 28, 'Christopher Hill', 'B'),
('180028', 29, 'Madison Scott', 'B'),
('180029', 30, 'Andrew Green', 'A'),
('180030', 31, 'Sofia Adams', 'B'),
('180031', 32, 'Joshua Baker', 'A'),
('180032', 33, 'Scarlett Hall', 'A'),
('180033', 34, 'Ryan Allen', 'B'),
('180034', 35, 'Grace Nelson', 'B'),
('180035', 36, 'Noah Carter', 'A'),
('180036', 37, 'Chloe Mitchell', 'A'),
('180037', 38, 'Samuel Roberts', 'B'),
('180038', 39, 'Avery Perez', 'B'),
('180039', 40, 'Joseph Turner', 'A'),
('180040', 41, 'Lily Phillips', 'B'),
('180041', 42, 'Logan Campbell', 'A'),
('180042', 43, 'Evelyn Flores', 'A'),
('180043', 44, 'Lucas Morris', 'B'),
('180044', 45, 'Aria Hall', 'B'),
('180045', 46, 'Jackson Thompson', 'A'),
('180046', 47, 'Grace Ward', 'A'),
('180047', 48, 'Carter Rivera', 'B'),
('180048', 49, 'Hannah Gray', 'B'),
('180049', 50, 'Dylan Diaz', 'A'),
('180050', 51, 'Penelope James', 'B'),
('180051', 52, 'Nathan Evans', 'A'),
('180052', 53, 'Layla Stewart', 'A'),
('180053', 54, 'Henry Sanchez', 'B'),
('180054', 55, 'Zoe Morris', 'B'),
('180055', 56, 'Jack Collins', 'A'),
('180056', 57, 'Madeline Price', 'A'),
('180057', 58, 'Wyatt Mitchell', 'B'),
('180058', 59, 'Brooklyn Turner', 'B'),
('180059', 60, 'Luke White', 'A'),
('180060', 61, 'Addison Cook', 'B'),
('180061', 62, 'Gabriel Rivera', 'A'),
('180062', 63, 'Alyssa Kelly', 'A'),
('180063', 64, 'Julian Hughes', 'B'),
('180064', 65, 'Stella Ward', 'B'),
('180065', 66, 'Leo Butler', 'A'),
('180066', 67, 'Willow Bailey', 'A'),
('180067', 68, 'Lucas Foster', 'B'),
('180068', 69, 'Eleanor Perry', 'B'),
('180069', 70, 'Mason Long', 'A'),
('180070', 71, 'Claire Simmons', 'B'),
('180071', 72, 'Gavin Howard', 'A'),
('180072', 73, 'Maya Bryant', 'A'),
('180073', 74, 'Owen Russell', 'B'),
('180074', 75, 'Hazel Cox', 'B'),
('180075', 76, 'Hunter Reed', 'A'),
('180076', 77, 'Savannah Ward', 'A'),
('180077', 78, 'Isaac Griffin', 'B'),
('180078', 79, 'Natalie Fisher', 'B'),
('180079', 80, 'Connor Watson', 'A');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id_user`, `username`, `password`) VALUES
(1, '1', '1');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `absen`
--
ALTER TABLE `absen`
  ADD PRIMARY KEY (`id_absen`),
  ADD KEY `nim` (`nis`),
  ADD KEY `id_pertemuan` (`id_pertemuan`);

--
-- Indeks untuk tabel `persentase`
--
ALTER TABLE `persentase`
  ADD PRIMARY KEY (`nis`);

--
-- Indeks untuk tabel `pertemuan`
--
ALTER TABLE `pertemuan`
  ADD PRIMARY KEY (`id_pertemuan`),
  ADD UNIQUE KEY `tgl` (`tgl`);

--
-- Indeks untuk tabel `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`nis`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `absen`
--
ALTER TABLE `absen`
  MODIFY `id_absen` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT untuk tabel `pertemuan`
--
ALTER TABLE `pertemuan`
  MODIFY `id_pertemuan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `absen`
--
ALTER TABLE `absen`
  ADD CONSTRAINT `absen_ibfk_1` FOREIGN KEY (`nis`) REFERENCES `siswa` (`nis`),
  ADD CONSTRAINT `absen_ibfk_2` FOREIGN KEY (`id_pertemuan`) REFERENCES `pertemuan` (`id_pertemuan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
