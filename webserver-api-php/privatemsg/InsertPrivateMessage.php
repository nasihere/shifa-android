<?php
	/////////////////////////////// SQL INJECTION ///////////////////////////////////////////
	
	if(preg_match('/(delete[\s]+TABLE|TRUNCATE[\s]+TABLE|DROP[\s]+DATABASE|DROP[\s]+TABLE|\'[\s]+or[\s]+\'|\"[\s]+or[\s]+\")/i', $_REQUEST['msg'])) 
	{
		$emailcontent = "IP: " .  $_SERVER["REMOTE_ADDR"] . " trying to do SQL Injection. Suspicious values recevied in InsertPrivateMessage.php: " . $_REQUEST['msg'] . " sessionid: " . $_REQUEST['session_id'] . " sessionid_to: " . $_REQUEST['session_id_to'];	
		$success = mail("nasihere@gmail.com", "SQL Injection Attempt",$emailcontent, "From: <noreply@shifa.in>");
		print "-1";
		die;
	}
	////////////////////////////// SQL INJECTION ////////////////////////////////////////////////
	include_once('config.php');
     if (!$link) {         die();     } 
     
	 $p = $_REQUEST;
	$p['ip'] = $_SERVER["REMOTE_ADDR"];	   
	 /////////////////////////////Restrict Words if any user those words ///////////////
	
	include_once("../badwords/badwords.php");
	$userchat = trim($p['msg']);
	$userchatArray = explode(" ",$userchat);
	foreach ($bads as $bad => $continueaction) {
		 $soundBad = $bad;
		foreach($userchatArray as $userchatword)
		{
			
			$checkChat = preg_replace("/[^a-zA-Z]/", "", $userchatword);
			$soundChat = $checkChat;
			
			
			
				if ($soundBad == $soundChat)
				{
					// YES!            
					$blockLink = "http://kent.nasz.us/app_php/crack.php?session_id=".$p['session_id']."&force=remove";
					$emailcontent = "Name: " . $p['chatter'] . " Session_id: " . $p['session_id'] . " PVT Chat: " . $p['msg'] . " To: " . $p['session_id_to'] . " Continue Action 1 = Not insert; 0 = Inserted: Current Value: " . $continueaction . " \r\n \r\n Blocked Link: " . $blockLink;
					$success = mail("nasihere@gmail.com", "Insulting message in Shifa Community",$emailcontent, "From: <noreply@shifa.in>");
					$success = mail("n3here@gmail.com", "Insulting message in Shifa Community",$emailcontent, "From: <noreply@shifa.in>");
					if ($continueaction == 1){
						
						$warningMsg = "We recevied message from your account <b>" . $p['msg'] . "</b>. <br /> Due to some unsolicited unacceptable activity from your account. Shifa team will blocked your account. Shifa team will report your IP " . $p['ip'] . " to concern Cyber Crime Departments for further action. <br /> <i>Under Section:</i> Cyber Security Protection <font color=\'red\'><b>Cyber crimes can involve criminal activities that are traditional in nature, such as theft personal information, spreading wrong messages, insulting others by message, fraud, forgery, defamation and mischief, all of which are subject to the Indian Penal Code. The abuse of computers has also given birth to a gamut of new age crimes that are addressed by the Information Technology Act, 2000.</b></font>";
						
						
						$ins =  "INSERT INTO  `tbl_app_msg` (  `_id` ,  `msg` ,  `session_id` ,  `session_id_to` ,  `chatter`,`chatter_to` ,  `datetime` ) 
					VALUES (
					NULL ,  '".$warningMsg."',  '10205304767877899',  '".$p['session_id']."',  'Shifa Support','".$p['chatter']."',  NOW()
					);";


						mysql_query($ins);
				
				
						print "-1";
						die;
					}
					
					//return true;			
					
				}
		}
	}
	
	/////////////////////////////Restrict Words if any user those words ///////////////
	
	
	 ////////////////////////////Check user is valid or not////////////////
	 $UserIsBlocked = CheckIsShifaMemberIsAccountBlockedByNasirOrNotReturnTrueIfyes($p);
	 if ($UserIsBlocked == true) {
		echo "-1"; //Account blocked
		mysql_close($link); 
		return; // terminate the remaining script...
	 }
	 
	 
	 ////////////////////////////Check user is valid then insert the chat////////////////
	 
	 
    
     $q =  "SELECT fname,lname,occupation  FROM `tbl_app_registration` where session_id = '".$_REQUEST['session_id_to']."' limit 1";   
           
            $res = mysql_query($q);
            $num_rows = mysql_num_rows($res);
             if ($num_rows)
             {
                 $heading = false;
                 while($row = mysql_fetch_array($res))
                 {
                      $isDrKeyword = substr($row['fname'], 0, 3); //Dr.
                      $isDrKeyword = strtolower($isDrKeyword);
                      $pos = strrpos( $isDrKeyword, "dr.");
                      if ($pos === true) {
                             $row['fname'] = strtolower($isDrKeyword);
                             $row['fname'] = str_replace("dr.", "",$row['fname']);
                      }
                      if ($row['occupation'] == "Student")
                      {
                      
                          $chatter = $row['fname'] . " " . $row['lname'];
                      }
                      else
                      {
                          $chatter = "Dr. " . $row['fname'] . " " . $row['lname'];
                      }
                      $chatter_to = ucwords(strtolower($chatter));
                 }
             }
                     
   
     $q =  "SELECT fname,lname,occupation  FROM `tbl_app_registration` where session_id = '".$_REQUEST['session_id']."' limit 1";   
           
            $res = mysql_query($q);
            $num_rows = mysql_num_rows($res);
             if ($num_rows)
             {
                 $heading = false;
                 while($row = mysql_fetch_array($res))
                 {
                      $isDrKeyword = substr($row['fname'], 0, 3); //Dr.
                      $isDrKeyword = strtolower($isDrKeyword);
                      $pos = strrpos( $isDrKeyword, "dr.");
                      if ($pos === true) {
                             $row['fname'] = strtolower($isDrKeyword);
                             $row['fname'] = str_replace("dr.", "",$row['fname']);
                      }
                      if ($row['occupation'] == "Student")
                      {
                      
                          $chatter = $row['fname'] . " " . $row['lname'];
                      }
                      else
                      {
                          $chatter = "Dr. " . $row['fname'] . " " . $row['lname'];
                      }
                      $chatter_from = ucwords(strtolower($chatter));
                 }
             }


     
    $q =  "INSERT INTO  `tbl_app_msg` (  `_id` ,  `msg` ,  `session_id` ,  `session_id_to` ,  `chatter`,`chatter_to` ,  `datetime` ) 
VALUES (
NULL ,  '".$p['msg']."',  '".$p['session_id']."',  '".$p['session_id_to']."',  '".$chatter_from ."','".$chatter_to."',  NOW()
);";
  
     $result =  mysql_query($q);
		SearchPaidNowChatToEmailNasirForNotification($p);// if anyone msg on global chat for paid account keyword then nasir will get email
	
     mysql_close($link); 
		$text = "http://kent.nasz.us/app_php/shifa-homepage.php?ver=72&session_id=" . $p['session_id_to'] . "\r\n Private Message:" . $p['msg'];
		if ($p['session_id_to'] == "10205304767877899" || $p['session_id_to'] == "000000000000007")
			$success = mail("nasihere@gmail.com", "Shifa Private Message", $text, "From: <noreply@shifa.in>");
		else if ($p['session_id_to'] == "100000404746202" || $p['session_id_to'] == "1111111100000" || $p['session_id_to'] == "818904868132997"){
			$success = mail("n3here@gmail.com", "Shifa Private Message", $text, "From: <noreply@shifa.in>");
			$success = mail("nasihere@gmail.com", "Shifa Private Message", $text, "From: <noreply@shifa.in>");
		}
	
	
	/****************************************************************************************************************************************************/
	function SearchPaidNowChatToEmailNasirForNotification($p)
	{
		$findme   = 'paid';
		$pos = strpos($p['msg'], $findme);
		if ($pos === false) $pos = strpos($p['msg'], "pay");
		if ($pos === false) $pos = strpos($p['msg'], "shifa");
		if ($pos === false) $pos = strpos($p['msg'], "account");
		if ($p['session_id_to'] == "000000000000007" || $p['session_id_to'] == "1111111100000" || $p['session_id_to'] == "818904868132997"  || $p['session_id_to'] == "123456789" || $p['session_id_to'] == "10205304767877899" || $p['session_id_to'] == "818904868132997" || $pos === true ){
			$paidLink = " Crack Link: http://kent.nasz.us/app_php/crack.php?name=".$p['session_id'];
			$emailcontent = "Message: " . $p['chatter']." \r\nSessionid: ".$p['session_id'] . " \r\nMessage: " . $p['msg'];
			$emailcontent = $emailcontent . "\r\n" . $paidLink;
			$subject = "Private Message";
			$success = mail("nasihere@gmail.com", $subject,$emailcontent, "From: <noreply@shifa.in>");
			
			
		}
		
	}
	/****************************************************************************************************************************************************/
	function CheckIsShifaMemberIsAccountBlockedByNasirOrNotReturnTrueIfyes($p)
	{
		$q =  "SELECT session_id,blocked FROM `tbl_app_registration` where session_id = '".$p['session_id']."'";
		$res = mysql_query($q);
		$num_rows = mysql_num_rows($res);
		if ($num_rows == 0)
		{
			return true;
		}
		else if ($num_rows)
		{
			$row = mysql_fetch_array($res);	
			if ($row['blocked'] == 'Y') 
			{
				// this $row['blocked'] == 'Y' is only force to email nasir to know some blocked memeber again trying to send chat or discussion // 11-08-2014
				$emailcontent = "Name: " . $p['chatter'] . " Session_id: " . $p['session_id'] . " Chat: " . $p['msg'] . " To: " . $p['session_id_to'];
				$success = mail("nasihere@gmail.com", "Blocked memeber entering",$emailcontent, "From: <noreply@shifa.in>");
				return true;
			}
			
		}
		else
		{
			return false;
		}
	} 
?>