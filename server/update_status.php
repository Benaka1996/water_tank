<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$response = array();
 
if (isset($_GET['type']) && isset($_GET['value'])) {
 
    $type = $_GET['type'];
	$value = $_GET['value'];
	$time_stamp = round(microtime(true) * 1000);

 
    $filepath = realpath (dirname(__FILE__));
	require_once($filepath."/db_connect.php");
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("UPDATE water_tank_status  SET value = '$value', time_stamp = '$time_stamp' WHERE type = '$type'");
 
    if ($result) {
		
        $response["success"] = 1;
        $response["message"] = "Water tank read status updated";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Something went wrong!";
 
        echo json_encode($response);
    }
    
} else {

    $response["success"] = 0;
    $response["message"] = "Parameter(s) are missing. Please check the request";
 
    echo json_encode($response);
}
?>