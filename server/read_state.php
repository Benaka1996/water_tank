<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
//Creating Array for JSON response
$response = array();
 
$filepath = realpath (dirname(__FILE__));
require_once($filepath."/db_connect.php");

$db = new DB_CONNECT();	
 
$result = mysql_query("SELECT * FROM water_tank_status WHERE type = 'water_level'") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    
    $response["water_tank_status"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $water_tank_status = array();
        $water_tank_status["id"] = $row["id"];
        $water_tank_status["type"] = $row["type"];
        $water_tank_status["value"] = $row["value"];
        $water_tank_status["time_stamp"] = $row["time_stamp"];
        array_push($response["water_tank_status"], $water_tank_status);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
    
}	
else 
{
	$response["success"] = 0;
    $response["message"] = "No water level state";
 
    echo json_encode($response);
}
?>