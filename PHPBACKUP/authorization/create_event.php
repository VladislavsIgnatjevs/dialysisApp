<?php

/**Script to create event
 * @author Vladislavs Ignatjevs
 * 
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
    $event = $db->createEvent($uid, $name, $details, $date, $start, $end);

    if ($event != false) {
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

