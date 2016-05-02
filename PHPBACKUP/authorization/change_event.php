<?php

/**
 * @author Vladislavs Ignatjevs
 * 
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['uid']) && isset($_POST['name']) && isset($_POST['details']) && isset($_POST['date']) && isset($_POST['start']) && isset($_POST['end']) ) {

    // receiving the post params
	//new and old
    $uid = $_POST['uid'];
	$name = $_POST['name'];
	$nameOld = $_POST['name_old'];
	$details = $_POST['details'];
	$detailsOld = $_POST['details_old'];
	$date = $_POST['date'];
	$dateOld = $_POST['date_old'];
	$start = $_POST['start'];
	$startOld = $_POST['start_old'];
	$end = $_POST['end'];
	$endOld = $_POST['end_old'];
	

    // create new event
    $user = $db->changeEvent($uid, $name,$nameOld, $details, $detailsOld, $date, $dateOld, $start, $startOld, $end, $endOld);

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
    $response["error_msg"] = "Requested parameter(s) uid/name/nameOld/details/detailsOld/date/dateOld/start/startOld/end/endOld missing!";
    echo json_encode($response);
}
?>

