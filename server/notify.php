<?php

function notify($to,$data){

    $api_key="AAAA5GRTHD8:APA91bGUHjjlwGOCw3-9oLvfZ3wHCwPU1nPXQnKpBroIHZV6gmySsX7ZZ-Pejvz7UVolJq4Oiw8sRzza51o251N7ASaBa5R1TZAjAVzZadN0hObmhdBxUFON62MRagCvEG0edWao37Ub";
    $url="https://fcm.googleapis.com/fcm/send";
    $fields=json_encode(array('to'=>$to,'notification'=>$data));

    // Generated by curl-to-PHP: http://incarnate.github.io/curl-to-php/
    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, ($fields));

    $headers = array();
    $headers[] = 'Authorization: key ='.$api_key;
    $headers[] = 'Content-Type: application/json';
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

    $result = curl_exec($ch);
    if (curl_errno($ch)) {
        echo 'Error:' . curl_error($ch);
    }
    curl_close($ch);
}


?>