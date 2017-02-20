<?php

header("Content-type: bitmap; charset: utf-8");

if(isset($_POST['encoded_string'])){
	
	$encoded_string = $_POST['encoded_string'];
	
	$image_name = $_POST['image_name'];
	
	$decoded_string = base64_decode($encoded_string);
	
	$path = 'images/'.$image_name;
	
	$file = fopen($path, 'wb');
	
	$is_written = fwrite($file,$decoded_string);
	
	fclose($file);	
	
}

else {

echo "Error creating file";

}



?>