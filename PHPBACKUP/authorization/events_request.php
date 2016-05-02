<?php

/**
 * @author Vladislavs Ignatjevs
 * 
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();   
$eCounter = 1; 
if (isset($_POST['email']) ) {
       $id= $_POST['email'];
    $events_raw = $db->getEvents($id);
    if ($events_raw != false) {
		//output error message false and number of events
        
	    $response["error"] = FALSE;
		
        
		$eCount  = $events_raw["eCount"];
		//output events data
		$response["data"]=$events_raw;
	    //encode 
        echo json_encode($response);
		
	}


    } else {
        //Table missing?
        $response["error"] = TRUE;
        $response["error_msg"] = "Events table missing in DB? :( ";
        echo json_encode($response);
    }

?>

