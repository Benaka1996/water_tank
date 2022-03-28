<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
//Creating Array for JSON response
$response = array();
 
$filepath = realpath (dirname(__FILE__));
require_once($filepath."/db_connect.php");

$db = new DB_CONNECT();	
 
$result = mysql_query("SELECT *FROM water_tank WHERE id=(SELECT max(id) from water_tank)") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    
    $response["water_tank"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $water_tank = array();
        $water_tank["id"] = $row["id"];
        $water_tank["water_level"] = $row["water_level"];
        $water_tank["time_stamp"] = $row["time_stamp"];
        array_push($response["water_tank"], $water_tank);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
    
}	
else 
{
	$response["success"] = 0;
    $response["message"] = "No water level data";
 
    echo json_encode($response);
}
?>