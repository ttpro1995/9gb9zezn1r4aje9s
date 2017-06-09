-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 15, 2017 at 10:00 PM
-- Server version: 5.7.17-0ubuntu0.16.04.1
-- PHP Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Life360`
--

-- --------------------------------------------------------

--
-- Table structure for table `Circle`
--

CREATE TABLE `Circle` (
  `id` int(11) NOT NULL,
  `name` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `idUser_Owner` int(11) NOT NULL,
  `createdTime` datetime NOT NULL,
  `updatedTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `CircleMember`
--

CREATE TABLE `CircleMember` (
  `id` int(11) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idCircle` int(11) NOT NULL,
  `createdTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `CircleShare`
--

CREATE TABLE `CircleShare` (
  `idCircle` int(11) NOT NULL,
  `code` varchar(6) COLLATE utf8_unicode_ci NOT NULL,
  `expire` datetime NOT NULL,
  `createTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Device`
--

CREATE TABLE `Device` (
  `idUser` int(11) NOT NULL,
  `tokenFirebase` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `batery` tinyint(4) NOT NULL,
  `createdTime` datetime NOT NULL,
  `updatedTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Item`
--

CREATE TABLE `Item` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `complete` tinyint(1) NOT NULL,
  `idUserAdd` int(11) NOT NULL,
  `idUserComplete` int(11) NOT NULL,
  `createdTime` datetime NOT NULL,
  `updatedTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `List`
--

CREATE TABLE `List` (
  `id` int(11) NOT NULL,
  `name` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `share` tinyint(1) NOT NULL,
  `notify` tinyint(1) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idCircle` int(11) NOT NULL,
  `createdTime` datetime NOT NULL,
  `updatedTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Location`
--

CREATE TABLE `Location` (
  `idUser` int(11) NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `share` tinyint(1) NOT NULL,
  `fromTime` datetime NOT NULL,
  `toTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Message`
--

CREATE TABLE `Message` (
  `id` int(11) NOT NULL,
  `typeData` tinyint(4) NOT NULL,
  `data` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `idUserOwner` int(11) NOT NULL,
  `typeChatPrivate` tinyint(1) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idCircle` int(11) NOT NULL,
  `createdTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Place`
--

CREATE TABLE `Place` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `zone` int(11) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `idUserOwner` int(11) NOT NULL,
  `idCircle` int(11) NOT NULL,
  `createTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PlaceLike`
--

CREATE TABLE `PlaceLike` (
  `id` int(11) NOT NULL,
  `idPlace` int(11) NOT NULL,
  `idCircleMember` int(11) NOT NULL,
  `isLike` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PlaceNotify`
--

CREATE TABLE `PlaceNotify` (
  `id` int(11) NOT NULL,
  `idPlace` int(11) NOT NULL,
  `idCircleMemer` int(11) NOT NULL,
  `notifyArrive` tinyint(1) NOT NULL,
  `notifyLeave` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Timeline`
--

CREATE TABLE `Timeline` (
  `id` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `fromTime` datetime NOT NULL,
  `toTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `firstName` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `lastName` varchar(35) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `phoneNumber` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `updatedTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Circle`
--
ALTER TABLE `Circle`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `CircleMember`
--
ALTER TABLE `CircleMember`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `CircleShare`
--
ALTER TABLE `CircleShare`
  ADD PRIMARY KEY (`idCircle`);

--
-- Indexes for table `Device`
--
ALTER TABLE `Device`
  ADD PRIMARY KEY (`idUser`);

--
-- Indexes for table `Item`
--
ALTER TABLE `Item`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `List`
--
ALTER TABLE `List`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Location`
--
ALTER TABLE `Location`
  ADD PRIMARY KEY (`idUser`);

--
-- Indexes for table `Message`
--
ALTER TABLE `Message`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Place`
--
ALTER TABLE `Place`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `PlaceLike`
--
ALTER TABLE `PlaceLike`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `PlaceNotify`
--
ALTER TABLE `PlaceNotify`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Timeline`
--
ALTER TABLE `Timeline`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Circle`
--
ALTER TABLE `Circle`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `CircleMember`
--
ALTER TABLE `CircleMember`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Item`
--
ALTER TABLE `Item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `List`
--
ALTER TABLE `List`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Message`
--
ALTER TABLE `Message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Place`
--
ALTER TABLE `Place`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `PlaceLike`
--
ALTER TABLE `PlaceLike`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `PlaceNotify`
--
ALTER TABLE `PlaceNotify`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Timeline`
--
ALTER TABLE `Timeline`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
