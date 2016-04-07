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
    $faq_raw = $db->getEvents($id);
    if ($faq_raw != false) {
		//output error message false and number of events
        
	    $response["error"] = FALSE;
		
        $faq= $faq_raw;
		$eCount  = $faq_raw["eCount"];
		//output events data
		$response["data"]=$faq_raw;
	    //encode 
        echo json_encode($response);
		
	}


    } else {
        //Table missing?
        $response["error"] = TRUE;
        $response["error_msg"] = "There are no frequently asked questions available at the moment :( ";
        echo json_encode($response);
    }

?>

