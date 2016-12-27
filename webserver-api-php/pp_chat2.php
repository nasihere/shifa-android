 <?php
	
	/////////////////////////////// SQL INJECTION ///////////////////////////////////////////
	
	if(preg_match('/(delete[\s]+TABLE|TRUNCATE[\s]+TABLE|DROP[\s]+DATABASE|DROP[\s]+TABLE|\'[\s]+or[\s]+\'|\"[\s]+or[\s]+\")/i', $_REQUEST['chat'])) 
	{
		$emailcontent = "IP: " .  $_SERVER["REMOTE_ADDR"] . " trying to do SQL Injection. Suspicious values recevied in pp_chat2.php: " . $_REQUEST['chat'] . " sessionid: " . $_REQUEST['_frm'];	
		$success = mail("nasihere@gmail.com", "SQL Injection Attempt",$emailcontent, "From: <noreply@shifa.in>");
		print "-1";
		die;
	}
	////////////////////////////// SQL INJECTION ////////////////////////////////////////////////
	
     

    include_once('config.php');
    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
     $p = $_REQUEST;
 	$p['ip'] = $_SERVER["REMOTE_ADDR"];	 
	
	/////////////////////////////Restrict Words if any user those words ///////////////
	//$bads = array("fuk"=>1,"shit"=>1,"dumb"=>1,"boobs"=>1,"fucking"=>1,"asshole"=>1,"bitch"=>1,"stupid"=>1,"fucker"=>1,"fuck"=>1,"penis"=>1,"choot"=>0,"chodo"=>0,"kutta"=>1,"kamine"=>1,"moo"=>0,"dirty"=>0,"Bhadwa"=>1,"Bhonsda"=>1,"Boobs"=>1,"fuck off"=>1,"Bun marra"=>1,"Chod Dunga"=>1,"Chodna"=>1,"Chordo"=>0,"Kutiya"=>1,"Lannd"=>1,"Loda"=>1,"Maa Chuda"=>1,"Madarchod"=>1,"Mumme"=>1,"Muttha Marna"=>1,"Rand"=>1,"Randi"=>1,"behnchod"=>1,"chutiye"=>1,"ghanta"=>1,"kameenay"=>1,"kameenay"=>1,"teri behen ki choot"=>1,"teri bund ch mera kela"=>1,"ullu de pathe"=>1);	
	include_once("badwords/badwords.php");
	$userchat = trim($p['chat']);
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
					$blockLink = "http://kent.nasz.us/app_php/crack.php?session_id=".$p['_frm']."&force=remove";
					$emailcontent = "Name: " . $p['chatter'] . " Session_id: " . $p['_frm'] . " Chat: " . $p['chat'] . " To: " . $p['_to'] . " Continue Action 1 = Not insert; 0 = Inserted: Current Value: " . $continueaction . " \r\n \r\n Blocked Link: " . $blockLink;
					$success = mail("nasihere@gmail.com", "Insulting message in Shifa Community",$emailcontent, "From: <noreply@shifa.in>");
					$success = mail("n3here@gmail.com", "Insulting message in Shifa Community",$emailcontent, "From: <noreply@shifa.in>");
					if ($continueaction == 1){
						$warningMsg = "We recevied message from your account <b>" . $p['chat'] . "</b>. <br /> Due to some unsolicited unacceptable activity from your account. Shifa team will blocked your account. Shifa team will report your IP " . $p['ip'] . " to concern Cyber Crime Departments for further action. <br /> <i>Under Section:</i> Cyber Security Protection <font color=\'red\'><b>Cyber crimes can involve criminal activities that are traditional in nature, such as theft personal information, spreading wrong messages, insulting others by message, fraud, forgery, defamation and mischief, all of which are subject to the Indian Penal Code. The abuse of computers has also given birth to a gamut of new age crimes that are addressed by the Information Technology Act, 2000.</b></font>";
						
						
						$ins =  "INSERT INTO  `tbl_app_msg` (  `_id` ,  `msg` ,  `session_id` ,  `session_id_to` ,  `chatter`,`chatter_to` ,  `datetime` ) 
					VALUES (
					NULL ,  '".$warningMsg."',  '10205304767877899',  '".$p['_frm']."',  'Shifa Support','".$p['chatter']."',  NOW()
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
	 
	 
	 
     $ua = $_SERVER["HTTP_USER_AGENT"];
     $p['chat']  = str_replace(","," ",$p['chat']);
     $p['chat']  = str_replace(":","-",$p['chat']);
     $p['_frm']  = str_replace(":","-",$p['_frm']);
     $p['to']  = str_replace(":","-",$p['to']);
     
     $p['chatter']  = str_replace(":","-",$p['chatter']);
     if ($p['chatter'] == "") $p['chatter'] = "Unknown";
     if ($p['_frm'] == "")  
     {
         print "-1";
         die;
     }
     if (trim( $p['chat']) == "") 
     {
         print "-1";
         die;
     }
      if ($p['to'] == "")  
     {
          $p['to']  = "-999";
     
     }
    $q = "INSERT INTO  `tbl_app_chat` (  `_id` ,  `chat` ,  `_frm` ,  `_to` ,  `chatter` ,  `datetime`  ,  `device` ) 
VALUES (
NULL ,  '".ucfirst(strtolower($p['chat']))."',  '".$p['_frm']."',  '".$p['to']."',  '".ucfirst(strtolower($p['chatter']))."',  NOW() " . ",'" . $ua . "'
);";

    $result = mysql_query($q);
	
	
   if (!$result) {
     print "-1"; 
    
}
     else
     {
          print "1";
     }
    SearchPaidNowChatToEmailNasirForNotification($p);// if anyone msg on global chat for paid account keyword then nasir will get email
 
   
    
    $ins = "INSERT INTO  `tbl_notification_log` (  `_id` , `_to`, `session_id` , `chatter` ,`menu` ,  `header` ,  `body` ,  `datetime` )
                            VALUES (NULL , '".$p['to']."', '".$p['_frm']."', '".ucfirst(strtolower($p['chatter']))."',  'chat', 'Chat',  '".ucfirst(strtolower($p['chat']))."',  NOW()    );";
        
        mysql_query($ins);

// GCM notification
        $querycount =  "SELECT COUNT( _id ) AS n FROM  `tbl_app_chat` where _frm =  '".$p['_frm']."'";
        $res = mysql_query($querycount);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $countofChat = $row['n'];
             if ($countofChat > 15){
                 include_once("gcm_notification.php");
             }
         }


     mysql_close($link);
    
  
  /****************************************************************************************************************************************************/
	function SearchPaidNowChatToEmailNasirForNotification($p)
	{
		$findme   = 'paid';
		$pos = strpos($p['chat'], $findme);
		if ($pos === false) $pos = strpos($p['chat'], "pay");
		if ($pos === false) $pos = strpos($p['chat'], "shifa");
		if ($pos === false) $pos = strpos($p['chat'], "account");
		if ($p['to'] == "000000000000007" || $p['to'] == "1111111100000" || $p['to'] == "818904868132997"   || $p['to'] == "123456789" || $p['to'] == "10205304767877899" || $pos === true ){
			$paidLink = "Click here To Mark as Paid Account: <a href='http://kent.nasz.us/app_php/crack.php?name=".$p['_frm']."'>Paid Now</a>" ;
			$emailcontent = "Message: " . $p['chatter']." \r\nSessionid: ".$p['_frm'] . " \r\nMessage: " . $p['chat'];
			$emailcontent = $emailcontent . "\r\n" . $paidLink;
			$subject = "Shifa User Sent Personal Message";
			$success = mail("nasihere@gmail.com", $subject,$emailcontent, "From: <noreply@shifa.in>");
			
		}
		
	}
	/****************************************************************************************************************************************************/
	function CheckIsShifaMemberIsAccountBlockedByNasirOrNotReturnTrueIfyes($p)
	{
		$q =  "SELECT session_id,blocked FROM `tbl_app_registration` where session_id = '".$p['_frm']."' or ip = '".$p['ip']."'";
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
				$emailcontent = "Name: " . $p['chatter'] . " Session_id: " . $p['_frm'] . " Chat: " . $p['chat'] . " To: " . $p['_to'];
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