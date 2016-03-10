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
        $response["contacts"]["doctor_name"] = $user["doctor_name"];
        $response["contacts"]["doctor_location"] = $user["doctor_location"];
        $response["contacts"]["doctor_number"] = $user["doctor_number"];
		//consultant data
        $response["contacts"]["consultant_name"] = $user["consultant_name"];
        $response["contacts"]["consultant_location"] = $user["consultant_location"];
        $response["contacts"]["consultant_number"] = $user["consultant_number"];
	    //dietitian data
        $response["contacts"]["dietitian_name"] = $user["dietitian_name"];
        $response["contacts"]["dietitian_location"] = $user["dietitian_location"];
        $response["contacts"]["dietitian_number"] = $user["dietitian_number"];
		//ward data
        $response["contacts"]["ward_name"] = $user["ward_name"];
        $response["contacts"]["ward_location"] = $user["ward_location"];
        $response["contacts"]["ward_number"] = $user["ward_number"];
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

