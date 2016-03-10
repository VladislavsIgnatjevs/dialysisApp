-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 10, 2016 at 01:57 AM
-- Server version: 5.7.9
-- PHP Version: 7.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `android_api`
--

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
CREATE TABLE IF NOT EXISTS `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `doctor_name` varchar(250) NOT NULL,
  `doctor_location` varchar(250) NOT NULL,
  `doctor_number` varchar(11) NOT NULL,
  `doctor_photo` blob,
  `consultant_name` varchar(250) NOT NULL,
  `consultant_location` varchar(250) NOT NULL,
  `consultant_number` varchar(11) NOT NULL,
  `consultant_photo` blob,
  `dietitian_name` varchar(250) NOT NULL,
  `dietitian_location` varchar(250) NOT NULL,
  `dietitian_number` varchar(11) NOT NULL,
  `dietitian_photo` blob,
  `ward_name` varchar(250) NOT NULL,
  `ward_location` varchar(250) NOT NULL,
  `ward_number` varchar(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contacts`
--

INSERT INTO `contacts` (`id`, `user_id`, `doctor_name`, `doctor_location`, `doctor_number`, `doctor_photo`, `consultant_name`, `consultant_location`, `consultant_number`, `consultant_photo`, `dietitian_name`, `dietitian_location`, `dietitian_number`, `dietitian_photo`, `ward_name`, `ward_location`, `ward_number`) VALUES
(1, 1, 'Dr John Scott', 'Room 1.04, Ward 22, Ninewells Hospital', '1382505050', 0x89504e470d0a1a0a0000000d49484452000000600000006008040000004891bfb30000046e494441547801eddaed6b56651cc0f1ef7d2fe6ee872d13371f1167266a2f4a4c14e98d2fd6b421820ccd9982af4d119935d1a2575a626448666e2a853614df66e2c38c284bc91716832563bbe75492a9a9733acaf16bafc573cef53be7bace6ef07cbeffc0cecd75fd769d072c4a24128944623c75347198f374d0c72043c30dd247076d1ca2893ac65194f2d4d34217625027cd2c2747914853c7091e21ca0638ce12d28ca80a1ae94122546033e58c881cdbf907b1d01db69225666bb88158ac970662338d7388834e339518aca31f71d47dd6e054866f11c7b5508623e3f91d89a1dfa8c28119f42031d5c5cb58f62a7f2331769359583443fde777b3831a26523adc446ad849417d09d3b1648272f11458499aa7a559c535e542aac2828c72eb1e2587973cad88a28b9411d93788a2dd04f91c51749088d6297ffd60298e218ad612c134fa556b3f8789727a11e31e504d58ca33cf4a4cad569e91425aa31c9c694c9528e75a037ae49407e61d68ec521eb6b3a86d4354d5a0b11851f53e4a15dc45544d446332a2ea3679541a1165a5688c42946d029c6d32f717a01b12d42188eb25a4ae16632710c4f52656d78aa13c8f10753bb56354dd43b218a947d05750ff23d3b70c232d48a8dec1d4bb48a8f663a40b09550f794c54701d09d55f18188f84ee3b82a5388e84ae523742f57d86bf147b90082d26501312a956f27829e71812a92d043a8c44ec1aab48f3b41256d38b44ac9940e7110b15f8945a26336ab8c9d4b28b1ec4426709d4811471ed04ea438ab85b041a448ab801020d2145dc93e7e0028a7c093d079bd8ce18ede70aa73842f3704738c51f3c8c6b8cb621a1bbc7f76c670993789649bccd87fcc07d247467087408d1c75576b090124cbcc09b7c4227a28f03f60f73fd7cc53cc298cf01f5c26ab47b9cbe4d13a389620cdbb86bf7c9c438e389bc9b17b1e125f63064ef8606a3d5d9c91bd8b4806e5bb794d08c04f43363b0ad925f9180f6616439e2db65ca7161345710df966224c700e2d9235ec195d90cfacebb0c86fc9e1b7c814bfb14af107d2c413c9b8f4b0b11cfdec2589a02e251052e8df6796f9f426133e2d17a5cda8078b4119572ee786ee2d770650e8f9167d6470ea5ad3eef4a2a71a18a1eff33904ed6e731d425f2d856ce659fc7c619f468403cfb893c3655f00be2d90a423a8d787689aa98bec63b4968537def9eba791d1be6d2e37b973705c0cdf7128f594f8a28526c6450ff9d84c641c4b773cc24acd9fc88f8f63591957111f1ed5ff63201ad497cc97f886f1728c5822aba90801ed3c21cc5aa3fcca0c14dd3582c99ce4dc4a03ff988b9a4f152c23c3ea61d31e806d558348b9b8a8fc4dad8cb7bd4b38805c32da29e0deca54df101db756662d974ba9198eaa41a07c6711189a10b8cc591320e218edb4f294eade501e2a87b3410836a4e230e3ac91462d3402f62b1022b8859960fb88d58a88f46328c883c9b220ed72e36926344a5a9a5958788b27e8e52438a22916519fbb98a18d4c13e9692a12855b1982d347396766e31c093e106b8453b67384023b55492482412898435ff03b7e45ec480ddbed40000000049454e44ae426082, 'Dr Lora McKeen', 'Room 1.03, Ward 22, Ninewells Hospital', '1382506820', 0x89504e470d0a1a0a0000000d49484452000000600000006008040000004891bfb30000046e494441547801eddaed6b56651cc0f1ef7d2fe6ee872d13371f1167266a2f4a4c14e98d2fd6b421820ccd9982af4d119935d1a2575a626448666e2a853614df66e2c38c284bc91716832563bbe75492a9a9733acaf16bafc573cef53be7bace6ef07cbeffc0cecd75fd769d072c4a24128944623c75347198f374d0c72043c30dd247076d1ca2893ac65194f2d4d34217625027cd2c2747914853c7091e21ca0638ce12d28ca80a1ae94122546033e58c881cdbf907b1d01db69225666bb88158ac970662338d7388834e339518aca31f71d47dd6e054866f11c7b5508623e3f91d89a1dfa8c28119f42031d5c5cb58f62a7f2331769359583443fde777b3831a26523adc446ad849417d09d3b1648272f11458499aa7a559c535e542aac2828c72eb1e2587973cad88a28b9411d93788a2dd04f91c51749088d6297ffd60298e218ad612c134fa556b3f8789727a11e31e504d58ca33cf4a4cad569e91425aa31c9c694c9528e75a037ae49407e61d68ec521eb6b3a86d4354d5a0b11851f53e4a15dc45544d446332a2ea3679541a1165a5688c42946d029c6d32f717a01b12d42188eb25a4ae16632710c4f52656d78aa13c8f10753bb56354dd43b218a947d05750ff23d3b70c232d48a8dec1d4bb48a8f663a40b09550f794c54701d09d55f18188f84ee3b82a5388e84ae523742f57d86bf147b90082d26501312a956f27829e71812a92d043a8c44ec1aab48f3b41256d38b44ac9940e7110b15f8945a26336ab8c9d4b28b1ec4426709d4811471ed04ea438ab85b041a448ab801020d2145dc93e7e0028a7c093d079bd8ce18ede70aa73842f3704738c51f3c8c6b8cb621a1bbc7f76c670993789649bccd87fcc07d247467087408d1c75576b090124cbcc09b7c4227a28f03f60f73fd7cc53cc298cf01f5c26ab47b9cbe4d13a389620cdbb86bf7c9c438e389bc9b17b1e125f63064ef8606a3d5d9c91bd8b4806e5bb794d08c04f43363b0ad925f9180f6616439e2db65ca7161345710df966224c700e2d9235ec195d90cfacebb0c86fc9e1b7c814bfb14af107d2c413c9b8f4b0b11cfdec2589a02e251052e8df6796f9f426133e2d17a5cda8078b4119572ee786ee2d770650e8f9167d6470ea5ad3eef4a2a71a18a1eff33904ed6e731d425f2d856ce659fc7c619f468403cfb893c3655f00be2d90a423a8d787689aa98bec63b4968537def9eba791d1be6d2e37b973705c0cdf7128f594f8a28526c6450ff9d84c641c4b773cc24acd9fc88f8f63591957111f1ed5ff63201ad497cc97f886f1728c5822aba90801ed3c21cc5aa3fcca0c14dd3582c99ce4dc4a03ff988b9a4f152c23c3ea61d31e806d558348b9b8a8fc4dad8cb7bd4b38805c32da29e0deca54df101db756662d974ba9198eaa41a07c6711189a10b8cc591320e218edb4f294eade501e2a87b3410836a4e230e3ac91462d3402f62b1022b8859960fb88d58a88f46328c883c9b220ed72e36926344a5a9a5958788b27e8e52438a22916519fbb98a18d4c13e9692a12855b1982d347396766e31c093e106b8453b67384023b55492482412898435ff03b7e45ec480ddbed40000000049454e44ae426082, 'Kevin May', 'Room 1.12, Ward 22, Ninewells Hospital', '1382608420', 0x30, 'Renal Dialysis Unit', 'West Block, Ninewells Hospital', '1382465742');

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
CREATE TABLE IF NOT EXISTS `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `event_name` char(50) NOT NULL,
  `event_date` date NOT NULL,
  `event_description` text NOT NULL,
  `event_time` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `medical_history`
--

DROP TABLE IF EXISTS `medical_history`;
CREATE TABLE IF NOT EXISTS `medical_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `date_added` date NOT NULL,
  `serum_creatine` int(11) NOT NULL,
  `gfr` int(11) NOT NULL,
  `bun` int(11) NOT NULL,
  `body_temp` int(11) NOT NULL,
  `urine_protein` int(11) NOT NULL,
  `microalbuminuria` int(11) NOT NULL,
  `urine_creatine` int(11) NOT NULL,
  `serum_albumin` int(11) NOT NULL,
  `npna` int(11) NOT NULL,
  `sga` int(11) NOT NULL,
  `hemoglobin` int(11) NOT NULL,
  `hematocrit` int(11) NOT NULL,
  `tsat` int(11) NOT NULL,
  `pth` int(11) NOT NULL,
  `calcium` int(11) NOT NULL,
  `phosphorus` int(11) NOT NULL,
  `potassium` int(11) NOT NULL,
  `body_weight` int(11) NOT NULL,
  `blood_pressure` int(11) NOT NULL,
  `total_cholesterol` int(11) NOT NULL,
  `hdl_cholesterol` int(11) NOT NULL,
  `triglyceride` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
CREATE TABLE IF NOT EXISTS `profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `dob` date NOT NULL,
  `allergies` text NOT NULL,
  `access_type` varchar(150) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(23) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_id` (`unique_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`) VALUES
(1, '56d78c2c671b49.44164833', 'vladvlad', 'vladvlad@vlad.com', 'BSJAJddghNQdnmxpkrSBqPm8Ze41YTZjNmIyYWRm', '5a6c6b2adf', '2016-03-03 00:58:20', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
