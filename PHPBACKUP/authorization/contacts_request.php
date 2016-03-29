<?php

/**
 * @author Vladislavs Ignatjevs
 * 
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email']) ) {

    // receiving the post params
    $email= $_POST['email'];
    

    // get the user by uid
    $contacts = $db->getEssentialContacts($email);

    if ($contacts != false) {
        // user is found
		//doctor data
        $response["error"] = FALSE;
		$response["contacts"]["head"] = "head";
        $response["contacts"]["doctor_name"] = $contacts["doctor_name"];
        $response["contacts"]["doctor_location"] = $contacts["doctor_location"];
        $response["contacts"]["doctor_number"] = $contacts["doctor_number"];
		//consultant data
        $response["contacts"]["consultant_name"] = $contacts["consultant_name"];
        $response["contacts"]["consultant_location"] = $contacts["consultant_location"];
        $response["contacts"]["consultant_number"] = $contacts["consultant_number"];
	    //dietitian data
        $response["contacts"]["dietitian_name"] = $contacts["dietitian_name"];
        $response["contacts"]["dietitian_location"] = $contacts["dietitian_location"];
        $response["contacts"]["dietitian_number"] = $contacts["dietitian_number"];
		//ward data
        $response["contacts"]["ward_name"] = $contacts["ward_name"];
        $response["contacts"]["ward_location"] = $contacts["ward_location"];
        $response["contacts"]["ward_number"] = $contacts["ward_number"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "email wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter email is missing!";
    echo json_encode($response);
}
?>

