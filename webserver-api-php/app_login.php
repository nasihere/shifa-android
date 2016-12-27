 <?php
	
	/////////////////////////////// SQL INJECTION ///////////////////////////////////////////
	
	
	if (preg_match("/[\s\'\"]/",  $_REQUEST['email'])){
		$emailcontent = "IP: " .  $_SERVER["REMOTE_ADDR"] . " trying to do SQL Injection. Suspicious values recevied in app_login.php: " . $_REQUEST['email'];	
		$success = mail("nasihere@gmail.com", "SQL Injection Attempt",$emailcontent, "From: <noreply@shifa.in>");
		print "-1";
		die;
	}
	if (preg_match("/[\s\'\"]/",  $_REQUEST['password'])){
		$emailcontent = "IP: " .  $_SERVER["REMOTE_ADDR"] . " trying to do SQL Injection. Suspicious values recevied in app_login.php: " . $_REQUEST['password'];	
		$success = mail("nasihere@gmail.com", "SQL Injection Attempt",$emailcontent, "From: <noreply@shifa.in>");
		print "-1";
		die;
	}
	////////////////////////////// SQL INJECTION ////////////////////////////////////////////////
	
	
     $p = $_REQUEST;
	 
     if ($p['session_id'] == "") {
              if ($p['email'] == "" || $p['password'] == "") {
			     
                  print "-1";
                  die;
              }
          }
     
   
    include_once('config.php');
    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
     

   
      if ($p["session_id"])
   {
		$q =  "SELECT session_id,fname,occupation,lname,sex FROM `tbl_app_registration` where session_id = '".$p['session_id']."'";
   } 
     else
     {
     $q =  "SELECT session_id,fname,occupation,lname,sex FROM `tbl_app_registration` where email = '".$p['email']."' and password = '".$p['password']."'";
     }
     
     
	  $res = mysql_query($q);
      $num_rows = mysql_num_rows($res);
     if ($num_rows)
     {
         $profilename = "";
         $row = mysql_fetch_array($res);
         if ($row['occupation'] == "Student")
         {
             $profilename = "";
             /* if ($row['sex'] == "0")
               $profilename = "Ms. " ;
             else
             $profilename = "Mr. " ;*/
         }
         else
         {
                      $profilename = "Dr. " ;
         }
         $profilename .=  $row['fname']; 
         /* 
      */
         
         if ($row['session_id'] == "123456789") $row['session_id'] = "nasir";
         $googlepaid = "0";     
         $googleadsShow = "1";   
          $q =  "   SELECT data FROM `tbl_shifa_log` WHERE Name = 'Counter~Home~Remove~ads~Paid' and data >= 1 and session_id = '".$row['session_id']."'";
       $res = mysql_query($q);
          $num_rows = mysql_num_rows($res);
         if ($num_rows)
     {
          if ($row['data'] != "0")
          {
              $googlepaid = "1";
              $googleadsShow = "1";
                  
          }
     }
          
         /*
         // 01/29/2014 : Cancel Shifa paid auto app service
         // 11/19/2013 : Big changed shifa made free to everyone now - 01/29/2014 Reverted
       
         */
		 //nasir - 5/1/2014 Enabled paid user 
         //$googlepaid = "1"; // Nasir - Jun-20-2014 Disabled Paid user setting if in future if team want to free acount nasir just need to $googlepaid = "1"
         
         //$googleadsShow = 1 for no ads $googleadsShow = 0 for ads
         

            /*
			http://kent.nasz.us/app_php/app_login.php?email=a&password=a&deviceid=353091050457508&serialno=8901260543542941237&androidid=fb7bac4882157cd9
*/
         print $row['session_id'] . ":" . $profilename . ":" . $googlepaid. ":" . $googleadsShow . ":" . "Success";
         
         
         
         mysql_close($link); 

      
     }
     else
     {
             print "-1";

     }
   
?>