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
    $contacts_raw = $db->getEssentialContacts($id);
    if ($contacts_raw != false) {
		//output error message false  
	    $response["error"] = FALSE;
		//output profile data
		$response["data"]=$contacts_raw;
	    //encode 
        echo json_encode($response);
	}
	
    } else {
        //Table missing?
        $response["error"] = TRUE;
        $response["error_msg"] = "Contacts table missing in DB? :( ";
        echo json_encode($response);
    }

?>

