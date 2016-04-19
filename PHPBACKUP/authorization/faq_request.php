<?php

/**
 * @author Vladislavs Ignatjevs
 * 
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();   
$eCounter = 1; 
   
    $faq_raw = $db->getFaq();
    if ($faq_raw != false) {
		
		//output error message false and number of faq
        
	    $response["error"] = FALSE;
		
        $faq= $faq_raw;
		$eCount  = $faq_raw["qCount"];
		//output events data
		$response["data"]=$faq_raw;
	    //encode 
        echo json_encode($response);
    } else {
        //Table missing?
        $response["error"] = TRUE;
        $response["error_msg"] = "There are no frequently asked questions available at the moment :( ";
        echo json_encode($response);
    }

?>

