<?php
     $p = $_REQUEST;
    include_once('config.php');
    
     if (!$link) {         die();     } 
     
    echo $q =  "update  tbl_app_registration 
		set   
		personalinfo1 = 1,
		hospital = '".$p['hospital']."',  
		clinic = '".$p['clinic']."',   
		website = '".$p['website']."',  
		mobileNo = '".$p['mobileNo']."',  
		alternateNo = '".$p['alternateNo']."',  
		
		fname = '".$p['FirstName']."',  
		lname = '".$p['LastName']."',  
		country = '".$p['Country']."',  
		city = '".$p['City']."',  
		ClinicAddress = '".$p['ClinicAddress']."',  
		Profession = '".$p['Profession']."',  
		
		speciality = '".$p['speciality']."' where session_id = '".$p['session_id'] ."'";
  
     $result =  mysql_query($q);
     
     mysql_close($link); 
     

?>