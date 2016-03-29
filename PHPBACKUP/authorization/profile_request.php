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
    $id= $_POST['email'];
    

    // get the user by uid
    $profile = $db->getProfile($id);

    if ($profile != false) {
       // user is found
		//doctor data
        $response["error"] = FALSE;
		$response["profile"]["head"] = "head";
        $response["profile"]["name"] = $profile["name"];
        $response["profile"]["patient_id"] = $profile["patient_id"];
        $response["profile"]["dob"] = $profile["dob"];
		//consultant data
        $response["profile"]["allergies"] = $profile["allergies"];
        $response["profile"]["access_type"] = $profile["access_type"];
    
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "id wrong. Please try again! id was sent: $id";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter id is missing!";
    echo json_encode($response);
}
?>

