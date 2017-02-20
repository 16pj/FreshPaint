<?php

$files = scandir("images/");




foreach ($files as &$i)
{

if (preg_match("/R./", $i, $match)){
	echo $i.";";
}
}
unset($i);

?>