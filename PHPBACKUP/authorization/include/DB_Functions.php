<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

	
	
	 /**
     * Sending essential contacts
     * @param user id
	 * author Vladislavs Ignatjevs
     * 
     */
	 public function getEssentialContacts($id)
	 {
		 //statement
		$stmt = $this->conn->prepare("SELECT consultant_name,consultant_number,consultant_location,dietitian_name,dietitian_number,dietitian_location,doctor_name,doctor_number,doctor_location,ward_name,ward_number,ward_location FROM contacts WHERE user_id = ?");
        $stmt->bind_param("s", $id);


        if ($stmt->execute()) {
            $userContacts = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $userContacts;
        } else {
            return NULL;
        }
	 }

	 /**
     * Sending profile data
     * @param user id
	 * author Vladislavs Ignatjevs
     * 
     */
	 public function getProfile($id)
	 {
		 //statement (mind '?')
		$stmt = $this->conn->prepare("SELECT name, patient_id, dob, allergies, access_type, sex FROM profile WHERE user_id = ?");
        $stmt->bind_param("s", $id);


        if ($stmt->execute()) {
            $userProfile = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $userProfile;
        } else {
            return NULL;
        }
	 }
	 
	 /**
     * Sending faq
     * @param none
	 * author Vladislavs Ignatjevs
     * 
     */
	 public function getFaqOld()
	 {
		 $qCount=1;
		 $faq_full;
		 //counting rows
	    $preConn = $this->conn->prepare("SELECT COUNT(id) FROM faq");
        if ($preConn->execute()) {
			
            $pre = $preConn->get_result()->fetch_assoc();
            $preConn->close();
            
        } else {
            $pre=null;
        }
		 //main statement
		// $pre1 = json_encode($pre);
		// echo ("pre variable = {$pre['COUNT(id)']} !!!!!!!!!");
		for ($a=1; $a<=$pre['COUNT(id)']; $a++)
		{
		$stmt = $this->conn->prepare("SELECT * FROM faq where id = ?");
		$stmt->bind_param("s", $qCount);
        if ($stmt->execute()) {
			
            $faq = $stmt->get_result()->fetch_assoc();
            $stmt->close();
			$response["question$qCount"] = $faq["question"];	
			$response["answer$qCount"] = $faq["answer"];	
            $faq_full[$qCount]=$response;	
            		
        } else {
            $faq_full[$qCount]= NULL;
        }
		$qCount++;
		}
		return $faq_full;
	 }
	 /**
     * Sending faq
     * @param none
	 * author Vladislavs Ignatjevs
     * 
     */
	 public function getFaq()
	 {
		

		$stmt = $this->conn->prepare("SELECT * FROM faq");
		$stmt->execute();
		$c = 1;
		$result = $stmt->get_result();
	
			while($response = $result->fetch_assoc())
			{
			$resp["question$c"] = $response["question"];
			$resp["answer$c"]= $response["answer"];
			$c++;
			}
		$c--;
		$resp["qCount"] = "\"$c";
		return $resp;
		
	 }
	  
	 
		/**
		* Sending events 
		* @param uid
		* author Vladislavs Ignatjevs
		* 
		*/

		public function getEvents($editID)
		{

		$stmt = $this->conn->prepare("SELECT * FROM events WHERE user_id = ? ORDER BY event_startTime ASC");
		$stmt->bind_param("s", $editID);
		$stmt->execute();
		$c = 1;
		$result = $stmt->get_result();
	
			while($response = $result->fetch_assoc())
			{
			$resp["name$c"] = $response["event_name"];
			$resp["date$c"]= $response["event_date"];
			$resp["description$c"]= $response["event_description"];
			$resp["start_time$c"]= $response["event_startTime"];
			$resp["end_time$c"] = $response["event_endTime"];
			$c++;
			}
		$c--;
		$resp["eCount"] = "\"$c";
		return $resp;
		}
		
		/**
		* Creating new event
		* @param uid, name, details, date, start, end 
		* author Vladislavs Ignatjevs
		* 
		*/

		public function createEvent($uid, $name, $details, $date, $start, $end)
		{

		$stmt = $this->conn->prepare("insert into events (user_id,event_name,event_date,event_description,event_startTime, event_endTime) values (?,?,?,?,?,?)");
		$stmt->bind_param("ssssss", $uid, $name, $date,$details, $start, $end);
		$result = $stmt->execute();
		$stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM events WHERE user_id = ?");
            $stmt->bind_param("s", $uid);
            $stmt->execute();
            $event = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $event;
        } else {
            return false;
        }
		}
		
		
		/**
		* Changing existing event
		* @param uid, name, name_old, details,details_old, date,date_old, start,start_old, end, end_old
		* author Vladislavs Ignatjevs
		* 
		*/

		public function changeEvent($uid, $name,$nameOld, $details, $detailsOld, $date, $dateOld, $start, $startOld, $end, $endOld)
		{

		$stmt = $this->conn->prepare("update events SET event_name = ?, event_date= ?, event_description = ?, event_startTime=?, event_endTime=? where user_id=? AND event_name=? AND event_date=? AND event_description=? AND event_startTime =? AND event_endTime=?");
		$stmt->bind_param("sssssssssss", $name, $date, $details, $start, $end, $uid, $nameOld, $dateOld, $detailsOld, $startOld, $endOld);
		$result = $stmt->execute();
		$stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM events WHERE user_id = ? AND event_name=? AND event_date=? AND event_description=? AND event_startTime =? AND event_endTime=?");
            $stmt->bind_param("ssssss", $uid, $name, $date, $details, $start, $end);
            $stmt->execute();
            $event = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $event;
        } else {
            return false;
        }
		}		
	 
	 
	    /**
		* Sending medical history
		* @param uid
		* author Vladislavs Ignatjevs
		* 
		*/

		public function getMedHistory($editID)
		{

		$stmt = $this->conn->prepare("SELECT * FROM medical_history WHERE user_id = ? ORDER BY date_added ASC");
		$stmt->bind_param("s", $editID);
		$stmt->execute();
		$c = 1;
		$result = $stmt->get_result();
	
			while($response = $result->fetch_assoc())
			{
			$resp["date_added$c"] = $response["date_added"];
			$resp["creative_protein$c"]= $response["creative_protein"];
			$resp["iron$c"]= $response["iron"];
			$resp["transferrin$c"]= $response["transferrin"];
			$resp["satn_transferrin$c"] = $response["satn_transferrin"];		
			$resp["phosphate$c"]= $response["phosphate"];
			$resp["bicarbonate$c"]= $response["bicarbonate"];
			$resp["ferritin$c"] = $response["ferritin"];
			$resp["glucose$c"]= $response["glucose"];
			$resp["magnesium$c"]= $response["magnesium"];
			$resp["sodium$c"]= $response["sodium"];
			$resp["potassium$c"]= $response["potassium"];
			$resp["urea$c"] = $response["urea"];	
			$resp["creatinine$c"]= $response["creatinine"];
			$resp["alt$c"]= $response["alt"];
			$resp["bilirubins$c"] = $response["bilirubins"];
			$resp["alkaline_phosphatase$c"]= $response["alkaline_phosphatase"];
			$resp["albumin$c"]= $response["albumin"];
			$resp["calcium$c"] = $response["calcium"];		
			$resp["calcium_corrected$c"]= $response["calcium_corrected"];
			$resp["est_gfr$c"]= $response["est_gfr"];
			$c++;
			}
		$c--;
		$resp["eCount"] = "\"$c";
		return $resp;
		}
	 
	 
	 
	 
	 
	 }
	 
	 

?>
