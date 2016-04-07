<?php

/**
 * @author Vladislavs Ignatjevs
 * 
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();   
$questionCounter = 0; 
   
    $faq_raw = $db->getFaq();
    if ($faq_raw != false) {
		//count faq rows
        $questionCount = count($faq_raw);
		$response["error"] = FALSE;
		
        $faq= $faq_raw;
        $response["sep"] = "sep";
		$response["sep"]=$faq[1];

		
		
        echo json_encode($response);
		echo (" faq value");
		echo json_encode($faq);
		echo (",qCount:$questionCount");
    } else {
        //Table missing?
        $response["error"] = TRUE;
        $response["error_msg"] = "There are no frequently asked questions available at the moment :( ";
        echo json_encode($response);
    }

?>

