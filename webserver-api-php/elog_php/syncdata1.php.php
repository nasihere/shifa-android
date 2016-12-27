<?php
   session_start();
   
   $sess = session_id(); 
   $p = $_REQUEST;
   
   $ip = $_SERVER["REMOTE_ADDR"];
  include_once('config.php');
  
   if (!$link) { 
   die('Could not connect: ' . mysql_error()); 
   } 
   
    
	
	$data = str_replace(":",$p['SyncData']);
	foreach($data => $val)
	{
		$InsertLogQuery = "INSERT INTOelog (_id ,
		session_id ,
		employee_id ,
		action ,
		timestamp ,
		dd ,
		mo ,
		yy ,
		hh ,
		mm ,
		ampm ,
		dow ,
		devicetime ,
		ip ) VALUES (
					NULL ,
					$p['session_id'],  
					$val[0], 
					'$val[1]', 
					NOW( ) , 
					'$val[2]', 
					'$val[2]',  
					'$val[3]',  
					'$val[4]', 
					'$val[5]', 
					'$val[6]', 
					'$val[7]', 
					'$val[8]', 
					'$ip'  
			);";
   
	$result =mysql_query($InsertLogQuery);
   }
   
   
   if (!$result) {
   	print "-1";
   }
   else
   {
   	echo "1001";
   }
   mysql_close($link); 
   
   ?>	