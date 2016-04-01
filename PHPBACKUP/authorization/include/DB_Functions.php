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
     * @oaram username
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
     * @oaram username
     * 
     */
	 public function getProfile($id)
	 {
		 //statement (mind '?')
		$stmt = $this->conn->prepare("SELECT name, patient_id, dob, allergies, access_type FROM profile WHERE user_id = ?");
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
     * Sending faq
     * @param id
     * 
     */
	 public function getFaq()
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
		 $pre1 = json_encode($pre);
		for ($a=1; $a<=4; $a++)
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
}

?>
