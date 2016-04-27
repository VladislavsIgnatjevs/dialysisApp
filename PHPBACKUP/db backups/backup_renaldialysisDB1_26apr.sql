-- --------------------------------------------------------
-- Host:                         silva.computing.dundee.ac.uk
-- Server version:               5.6.20-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.5059
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for renaldialysisdb
CREATE DATABASE IF NOT EXISTS `renaldialysisdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `renaldialysisdb`;

-- Dumping structure for table renaldialysisdb.contacts
CREATE TABLE IF NOT EXISTS `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(23) NOT NULL,
  `doctor_name` varchar(250) NOT NULL,
  `doctor_location` varchar(250) NOT NULL,
  `doctor_number` varchar(16) NOT NULL,
  `doctor_photo` blob,
  `consultant_name` varchar(250) NOT NULL,
  `consultant_location` varchar(250) NOT NULL,
  `consultant_number` varchar(16) NOT NULL,
  `consultant_photo` blob,
  `dietitian_name` varchar(250) NOT NULL,
  `dietitian_location` varchar(250) NOT NULL,
  `dietitian_number` varchar(16) NOT NULL,
  `dietitian_photo` blob,
  `ward_name` varchar(250) NOT NULL,
  `ward_location` varchar(250) NOT NULL,
  `ward_number` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FOREIGN` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table renaldialysisdb.contacts: ~0 rows (approximately)
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` (`id`, `user_id`, `doctor_name`, `doctor_location`, `doctor_number`, `doctor_photo`, `consultant_name`, `consultant_location`, `consultant_number`, `consultant_photo`, `dietitian_name`, `dietitian_location`, `dietitian_number`, `dietitian_photo`, `ward_name`, `ward_location`, `ward_number`) VALUES
	(3, '56f14527e91210.87913842', 'Dr John Scott', 'Room 1.04, Ward 22,  Ninewells Hospital', '(+44)01382505050', NULL, 'Dr Lora McKeen', 'Room 1.03, Ward 22, Ninewells Hospital', '(+44)01382676869', NULL, 'Kevin May', 'Room 1.12, Ward 22, Ninewells Hospital', '(+44)01382151613', NULL, 'Renal Dialysis Unit', 'West Block, Ninewells Hospital', '(+44)01382465742'),
	(4, '56f913b39e74d0.76329288', 'Dr John Scott', 'Room 1.04,  Ward 22,  Ninewells Hospital', '(+44)01382505050', NULL, 'Dr Lora McKeen', 'Room 1.03, Ward 22, Ninewells Hospital', '(+44)01382676869', NULL, 'Kevin May', 'Room 1.12, Ward 22, Ninewells Hospital', '(+44)01382151613', NULL, 'Renal Dialysis Unit', 'West Block, Ninewells Hospital', '(+44)01382465742');
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;

-- Dumping structure for table renaldialysisdb.events
CREATE TABLE IF NOT EXISTS `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(23) NOT NULL,
  `event_name` char(50) NOT NULL,
  `event_date` date NOT NULL,
  `event_description` text NOT NULL,
  `event_startTime` time NOT NULL,
  `event_endTime` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FOREIGN` (`user_id`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`unique_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- Dumping data for table renaldialysisdb.events: ~11 rows (approximately)
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` (`id`, `user_id`, `event_name`, `event_date`, `event_description`, `event_startTime`, `event_endTime`) VALUES
	(1, '56f14527e91210.87913842', 'Treatment123', '2016-05-28', 'Treatment at RDU', '09:15:00', '11:00:00'),
	(2, '56f913b39e74d0.76329288', 'Treatment', '2016-05-29', 'Treatment at RDU', '09:15:00', '10:00:00'),
	(3, '56f14527e91210.87913842', 'Blood check', '2016-05-29', 'Assesment in room 2.03 with Dr. Keen', '13:00:00', '13:15:00'),
	(4, '56f913b39e74d0.76329288', 'Blood check', '2016-05-29', 'Assesment in room 2.03 with Dr. Keen', '13:00:00', '14:00:00'),
	(5, '56f14527e91210.87913842', 'Assesment', '2016-05-30', 'Assesment in room 2.05 with Dr.Keen', '14:00:00', '14:30:00'),
	(6, '56f913b39e74d0.76329288', 'Assesment', '2016-05-30', 'Assesment in room 2.05 with Dr.Keen', '14:00:00', '15:00:00'),
	(7, '56f14527e91210.87913842', 'Check-up', '2016-05-07', 'Room 2.06', '13:30:00', '13:45:00'),
	(8, '56f14527e91210.87913842', 'Meeting', '2016-05-28', 'See charge nurse Nichola at 2.05', '15:00:30', '15:30:00'),
	(9, '56f14527e91210.87913842', 'Surgery', '2016-06-02', 'See email', '11:00:00', '11:30:00'),
	(10, '56f14527e91210.87913842', 'blsw', '2016-04-12', 'by I v', '06:35:00', '17:35:00'),
	(11, '56f14527e91210.87913842', 'Assesment', '2016-04-27', 'Assesment in room 2.05 with Dr.Keen', '14:00:00', '14:30:00'),
	(12, '56f14527e91210.87913842', 'val', '2016-04-13', 'val2', '06:55:00', '17:55:00'),
	(13, '56f14527e91210.87913842', 'g', '2016-04-15', 'g', '19:11:00', '23:11:00');
/*!40000 ALTER TABLE `events` ENABLE KEYS */;

-- Dumping structure for table renaldialysisdb.faq
CREATE TABLE IF NOT EXISTS `faq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` text NOT NULL,
  `answer` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Dumping data for table renaldialysisdb.faq: ~4 rows (approximately)
/*!40000 ALTER TABLE `faq` DISABLE KEYS */;
INSERT INTO `faq` (`id`, `question`, `answer`) VALUES
	(1, 'Will I see a dietitian?', 'Dietitians visit all the units regularly and see patients, according to clinical need. They also phone patients'),
	(2, 'Will I be able to transfer to a dialysis unit nearer home?', 'If you are not already dialysing in the unit nearest to your home, you may wish to ask about transferring. This will depend on whether you are well enough to dialyse away from the hospital. '),
	(3, 'Can I learn to do my own dialysis?', 'Self-care dialysis is when patients learn how to do some or all of their own dialysis treatment. It can also involve learning more about living with kidney disease and staying healthy. Self-care dialysis can offer you'),
	(4, 'Will I be able to go on holiday?', 'Yes, but before making any plans please talk to a doctor or nurse at least two months before the holiday,  to ensure you are fit enough to travel.'),
	(5, 'What are the most commong cause of death between hemodialysis patients?', 'Fistula infection');
/*!40000 ALTER TABLE `faq` ENABLE KEYS */;

-- Dumping structure for table renaldialysisdb.medical_history
CREATE TABLE IF NOT EXISTS `medical_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(23) NOT NULL,
  `date_added` date NOT NULL,
  `creative_protein` varchar(50) NOT NULL,
  `iron` varchar(50) NOT NULL,
  `transferrin` varchar(50) NOT NULL,
  `satn_transferrin` varchar(50) NOT NULL,
  `phosphate` varchar(50) NOT NULL,
  `bicarbonate` varchar(50) NOT NULL,
  `ferritin` varchar(50) NOT NULL,
  `glucose` varchar(50) NOT NULL,
  `magnesium` varchar(50) NOT NULL,
  `sodium` varchar(50) NOT NULL,
  `potassium` varchar(50) NOT NULL,
  `urea` varchar(50) NOT NULL,
  `creatinine` varchar(50) NOT NULL,
  `alt` varchar(50) NOT NULL,
  `bilirubins` varchar(50) NOT NULL,
  `alkaline_phosphatase` varchar(50) NOT NULL,
  `albumin` varchar(50) NOT NULL,
  `calcium` varchar(50) NOT NULL,
  `calcium_corrected` varchar(50) NOT NULL,
  `est_gfr` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FOREIGN` (`user_id`),
  CONSTRAINT `medical_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`unique_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dumping data for table renaldialysisdb.medical_history: ~1 rows (approximately)
/*!40000 ALTER TABLE `medical_history` DISABLE KEYS */;
INSERT INTO `medical_history` (`id`, `user_id`, `date_added`, `creative_protein`, `iron`, `transferrin`, `satn_transferrin`, `phosphate`, `bicarbonate`, `ferritin`, `glucose`, `magnesium`, `sodium`, `potassium`, `urea`, `creatinine`, `alt`, `bilirubins`, `alkaline_phosphatase`, `albumin`, `calcium`, `calcium_corrected`, `est_gfr`) VALUES
	(1, '56f14527e91210.87913842', '2015-04-11', '14', '113', '2.53', '21', '1.12', '26', '327', '4.9', '1.16', '137', '5.3', '27.8', '1009', '37', '8', '88', '43', '2.45', '2.41', '5');
/*!40000 ALTER TABLE `medical_history` ENABLE KEYS */;

-- Dumping structure for table renaldialysisdb.profile
CREATE TABLE IF NOT EXISTS `profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(23) NOT NULL,
  `name` varchar(250) NOT NULL,
  `patient_id` varchar(50) NOT NULL,
  `dob` date NOT NULL,
  `allergies` text NOT NULL,
  `access_type` varchar(150) NOT NULL,
  `sex` char(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FOREIGN` (`user_id`),
  CONSTRAINT `profile_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`unique_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table renaldialysisdb.profile: ~2 rows (approximately)
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` (`id`, `user_id`, `name`, `patient_id`, `dob`, `allergies`, `access_type`, `sex`) VALUES
	(1, '56f14527e91210.87913842', 'Bob Dickens', 'NW456585', '1986-02-28', 'pinicelin', 'fistula', 'm'),
	(2, '56f913b39e74d0.76329288', 'Paul McKeen', 'NW456568', '1975-08-14', 'pinicelin', 'fistula', 'm');
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;

-- Dumping structure for table renaldialysisdb.users
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table renaldialysisdb.users: ~2 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`) VALUES
	(1, '56f14527e91210.87913842', 'Vlad Ignatjev', 'vlad@vlad.com', '0c4XrRo2hZKoh1Qo4jeLluygdj4wNzJjNjU1Y2Ez', '072c655ca3', '2016-03-22 13:14:15', NULL),
	(2, '56f913b39e74d0.76329288', 'Vlad2', 'vlad2@vlad.com', 'Svjo4tG3H8S92bAynVf2mc3vHZkyOWY5MDQyNmU1', '29f90426e5', '2016-03-28 12:21:23', NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
