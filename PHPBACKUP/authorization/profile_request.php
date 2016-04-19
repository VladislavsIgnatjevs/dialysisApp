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
    $faq_raw = $db->getProfile($id);
    if ($faq_raw != false) {
		//output error message false 
        
	    $response["error"] = FALSE;
		
        $faq= $faq_raw;
		
		//output profile data
		$response["data"]=$faq_raw;
	    //encode 
        echo json_encode($response);
		
	}


    } else {
        //Table missing?
        $response["error"] = TRUE;
        $response["error_msg"] = "Profile table missing in DB? :( ";
        echo json_encode($response);
    }

?>

