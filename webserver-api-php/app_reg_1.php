<?php
	error_reporting(0);
	
	include_once('config.php');
	 if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
	
	
	
	$p = $_REQUEST;
	$p['ip'] = $_SERVER["REMOTE_ADDR"];	  
	
	////////////////////////////Check user is valid or not////////////////
	 $UserIsBlocked = CheckIsShifaMemberIsAccountBlockedByNasirOrNotReturnTrueIfyes($p);
	 if ($UserIsBlocked == true) {
		echo "-1"; //Account blocked
		mysql_close($link); 
		return; // terminate the remaining script...
	 }
	 
	 
	 ////////////////////////////Check user is valid then insert the chat////////////////
	 

	
	if ($p['session_id'] == "") {
		session_start();
		$_REQUEST['session_id'] =  $p['session_id'] = session_id(); 
		session_destroy();
	}
	
	LoginCheckReturnInfo($p);		
     
     $ip = $_SERVER["REMOTE_ADDR"];
     
	if ($p['ShifaSessionID'] == "0") // If shifa registration account session id customer dont want to migrate into fb then
	{
	$q =  "INSERT INTO  `tbl_app_registration` (  `_id` ,  `fname` ,  lname ,  `email` ,  `dob` ,  `sex` ,  `country` ,  `city` ,  `occupation` ,  `session_id`,`datetime`,`visit`,`lastvisitdatetime`,`password`,`ip`,`refer_emailid`,`languages`,`relationship_status`,`sports`,`fbresponse`,`username`,`ProfilePic`,`gcmreg` )
VALUES (
NULL,  '".
        $p['fname']."',  '".$p['lname']."',  '".$p['email']
        ."',  '".$p['dob']."',  '".
        $p['sex']."',  '".$p['country']."',  '".$p['city']."',  '".$p['occupation']."',  '".
        $p['session_id']."',NOW(),1,NOW(),'".$p['password']."','".$ip."','".$p['referemailid']."',  '".
		$p['languages']."',  '".$p['relationship_status']."',  '".$p['sports']."',  '".
		$p['fbresponse']."', '" . $p['username']."',  '".$p['ProfilePic']."',  '".$p['gcmreg']."');";
		$result =  mysql_query($q);
    }
	else // if user want to switch and convert shifa account to fb then query this
	{
		 $q =  "update `tbl_app_registration` set session_id = '" . $p['session_id'] . "',
		fname='".$p['fname']."',
		lname='".$p['lname']."',
		email='".$p['email']."',
		dob='".$p['dob']."',
		sex='".$p['sex']."',
		country='".$p['country']."',
		city='".$p['city']."',
		occupation='".$p['occupation']."',
		ip	='".$ip."',
		languages='".$p['languages']."',
		relationship_status='".$p['relationship_status']."',
		sports='".$p['sports']."', 
		fbresponse = '".$p['fbresponse']."',
		username='".$p['username']."',
		ProfilePic = '".$p['ProfilePic']."',
		gcmreg = '".$p['gcmreg']."',
		shifasessionid = '". $p['ShifaSessionID']."' 
		where session_id = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_shifa_log set session_id = '".$p['session_id']."' where session_id = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_msg set chatter = '".$p['fname']."' where session_id = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_msg set chatter = '".$p['fname']."' where session_id_to = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_msg set chatter_to = '".$p['fname']."' where session_id = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_msg set chatter_to = '".$p['fname']."' where session_id_to = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_msg set session_id = '".$p['session_id']."' where session_id = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_msg set session_id_to = '".$p['session_id']."' where session_id_to = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_chat set chatter = '".$p['fname']."' where _to = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_chat set chatter = '".$p['fname']."' where _frm = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_chat set _frm = '".$p['session_id']."' where _frm = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_chat set _to = '".$p['session_id']."' where _to = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		$q = "update tbl_app_pms_report set session_id = '".$p['session_id']."' where session_id = '".$p['ShifaSessionID']."';"; 
	$result =  mysql_query($q);	
		
	}
//	$q = str_replace("NASSPACENAS", " ", $q);
    //$result =  mysql_query($q);
  //  echo $q;
    mysql_close($link);
   
     
if (!$result) {
     
    print "-404-:-Some information is not valid or correct."; 
    echo "Error";
    die;
}
if ($_REQUEST['web'])
{
	echo "Registration completed, Please go back and try to login with your email id ". $_REQUEST['email'];
}  
LoginCheckReturnInfo($_REQUEST);
    
  

function LoginCheckReturnInfo($p)
{
		if ($p['session_id'] == "") return;
		$q =  "SELECT session_id,fname,occupation,lname,sex FROM `tbl_app_registration` where session_id = '".$p['session_id']."'";
		$res = mysql_query($q);	
		$num_rows = mysql_num_rows($res);
		if ($num_rows != 0)
		{
			include_once("app_login.php");
			die;
		}
}
/****************************************************************************************************************************************************/
	function CheckIsShifaMemberIsAccountBlockedByNasirOrNotReturnTrueIfyes($p)
	{
		$q =  "SELECT session_id,blocked FROM `tbl_app_registration` where ip = '".$p['ip']."'";
		$res = mysql_query($q);
		$num_rows = mysql_num_rows($res);
		if ($num_rows == 0)
		{
			return false;
		}
		else if ($num_rows)
		{
			$row = mysql_fetch_array($res);	
			if ($row['blocked'] == 'Y') 
			{
				// this $row['blocked'] == 'Y' is only force to email nasir to know some blocked memeber again trying to send chat or discussion // 11-08-2014
				$emailcontent = "Name: " . $p['fname'] . " IP: " . $p['ip'];
				$success = mail("nasihere@gmail.com", "Blocked IP creating new account",$emailcontent, "From: <noreply@shifa.in>");
				return true;
			}
			
		}
		else
		{
			return false;
		}
	} 
?>