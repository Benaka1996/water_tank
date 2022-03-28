<?php

include 'notify.php';

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$response = array();
 
if (isset($_GET['water_level'])) {
 
    $water_level = $_GET['water_level'];
    $time_stamp = round(microtime(true) * 1000);
 
    $filepath = realpath (dirname(__FILE__));
	require_once($filepath."/db_connect.php");
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("INSERT INTO water_tank(water_level, time_stamp) VALUES('$water_level','$time_stamp')");
 
    if ($result) {
		
        $response["success"] = 1;
        $response["message"] = "Water level data uploaded.";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Something went wrong!";
 
        echo json_encode($response);
    }
    
    if($water_level > 300){
        $to="/topics/water_tank";
        $data=array(
            'title'=>'Water Tank',
            'body'=>'Water tank is full, turn off the motor!'
        );
        notify($to,$data);
        echo "Notification Sent";
    }
    
} else {

    $response["success"] = 0;
    $response["message"] = "Parameter(s) are missing. Please check the request";
 
    echo json_encode($response);
}
?>