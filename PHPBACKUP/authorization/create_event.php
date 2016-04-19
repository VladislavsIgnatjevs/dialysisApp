<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['uid']) && isset($_POST['name']) && isset($_POST['details']) && isset($_POST['date']) && isset($_POST['start']) && isset($_POST['end']) ) {

    // receiving the post params
    $uid = $_POST['uid'];
    $name = $_POST['name'];
	$details = $_POST['details'];
	$date = $_POST['date'];
	$start = $_POST['start'];
	$end = $_POST['end'];

    // create new event
    $user = $db->createEvent($uid, $name, $details, $date, $start, $end);

    if ($user != false) {
        // created successfully
        $response["error"] = FALSE;
        echo json_encode($response);
    } else {
        // error in creating event
        $response["error"] = TRUE;
        $response["error_msg"] = "Uid wrong? Unknown error. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Requested parameter(s) uid/name/details/date/start/end missing!";
    echo json_encode($response);
}
?>

