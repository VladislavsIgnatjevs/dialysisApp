<?php

/**
 * @author Vladislavs Ignatjevs
 * 
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();   
 
if (isset($_POST['email']) ) {

    // receiving the post params
    $user_id= $_POST['email'];
    


    $events_raw = $db->getEvents($user_id);
    if ($events_raw != false) {
		//count events rows
	
		$response["error"] = FALSE;
		
        $events= $events_raw;
        $response["sep"] = "sep";
		$response["sep"]=$events;

        echo json_encode($response);
		
	
    } else {
        //Table missing?
		
        $response["error"] = TRUE;
        $response["error_msg"] = "There are no events at the moment. Click 'add event'";
        echo json_encode($response);
    }
}


?>

