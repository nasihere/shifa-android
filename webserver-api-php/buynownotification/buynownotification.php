<?php
     $p = $_REQUEST;
	if($p['phone'] || $p['email'])
	{
		$text = "Name :" . $p['name']. '\r\n';
		$text = $text . "Sessionid :" . $p['session']. '\r\n';
		$text = $text . "Phone :" . $p['phone']. '\r\n';
		$text = $text . "Email :" . $p['email']. '\r\n';
		$text = $text . "Note :" . $p['note']. '\r\n';
		$text = $text . "Send Email BuyNow : http://kent.nasz.us/app_php/crack.php?session_id=".$p['session']."&force=buynow&name=".$p['name']."&email=".$p['email']."\r\n";
		$success = mail("nasihere@gmail.com", "ShifiContacts", $text, "From: <shifacontact@shifa.us>");
		$success = mail("shaikhsaba00@gmail.com", "ShifiContacts", $text, "From: <shifacontact@shifa.us>");
		
		
		$BodyContent = "Dear ".$p['name'].", 
					<b>You have other option to pay money.</b>\r\n
					
					\r\nFor Indian resident:\r\nYou can also pay cash/direct deposit to our bank account. \r\nDue to secure banking PIN sometimes your card not get accepted\r\n
					Try Direct Deposit.\r\nAccount Name: Nasir Sayed \r\nAmount: {GooglePrice}Rs.\r\nAccount No:910010020952010\r\nIFSC Code:UTIB0000219\r\nSwift Code: AXISINBB219\r\nBank - Branch:Axis Bank - Goregaon-LinkRoad, Mumbai, India\r\n\r\nWhen you are done with direct deposit to bank account, please reply with email subject 'MONEY TRANSFERED' and account details. We will then update your account as paid customer during US business hour between 9AM To 5PM PST.\r\n<hr>
					
					\r\nYou can also follow below URL for payment online NOW!
					<a href = 'http://kent.nasz.us/app_php/buynow/buynowwebpayment.php?session_id=".$_REQUEST['session_id']."'>Web Payment - Click here/PayPal</a>
					Please click on this click and contribute Shifa App for Life time development. No hidden Cost, No Additional Fee in future. 
					
					
					\r\n\r\nA little background about Shifa team; we don't have a big team or company for financing Shifa Team. Developer(Nasir Sayed) and Doctor(Nazim Dadan) are giving personal time for making Shifa App for all of doctors like you to save money from buying big expensive software. 
					
					<b>Help Number</b>\r\n
					<b>US Contact No:</b> +1 323-300-4756\r\n
					<b>India Contact No:</b> +91 9773622313 \r\n
					
					\r\n\r\nOur vision is to create electronic books (database) with all Homeopathy book one by one. We are spending our own money to maintain infrastructure such as Database, Third Party Server, Source Code, Registration, Android bug fixes from user requests. Doctor: Nazim Dadan and Developer: Nasir Sayed have paid a lot to maintain Shifa service for the past three years and will continue to do it until we don't achieve it for iOS, WebSite & PC Software. \r\n\r\nWe want you to contribute more money for Shifa App account so that we can provide you with more desired features. \r\n\r\nIf you find any difficulties in payment with Google credit card processing or Google is not accepting your payment due to bad card or debit card issue. You can try to make payment online through WebPayment/Paypal Now!\r\n\r\nShifa account is also associated with PayPal payment processing. PayPal accept debit card, credit card or your existing PayPal Account. Its 100% safe money transfer for payment. If you ever find any problem in PayPal transfer then please do not hesitate to ask question to the developer at nasihere@gmail.com\r\nSincerely, \r\nThe Shifa App Team\r\nNasir Sayed\r\n<Note: Its an autogenerated email, Do not reply to this email. if you have any queries please email to nasihere@gmail.com>
					
					
					"; 
					// send email 
					//$success = mail($EmailTo, $Subject, $Body, "From: <$EmailFrom>");
					
					
		
		$success = mail($p['email'], "Shifa APP Support Team", $BodyContent, "From: <noreply@shifa.us>");
		echo "<b>Thankyou so much! Shifa team will contact you very soon.";
		exit;
	}
		  
include_once('config.php');
    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
     

	 
   $q =  "SELECT * FROM `tbl_shifa_log` where Name = 'Counter~Home~Remove~ads~Paid' and session_id = '".$p['session_id']."' and data != 0"; 
    $res = mysql_query($q);
    $num_rows = mysql_num_rows($res);
    $paid = false;
    if ($num_rows)
    {
        $paid = true;
    }
	
    if ($paid == false) // so paid is false
    {
        //Send Email
			$q =  "SELECT fname, password,email FROM `tbl_app_registration` WHERE session_id = '".$_REQUEST['session_id']."'"; 
		 
			$res = mysql_query($q);
			$num_rows = mysql_num_rows($res);
			if ($num_rows)
			{
					$row = mysql_fetch_array($res);
					$password = $row['password'];
					$fname = $row['fname'];
					$EmailFrom = "shifa-buynow@shifa.us";
					$EmailTo = $row['email'];
					$Subject = "WebPayment Call From Shifa, Are you unable to make Payment?";

					// prepare email body text
		   echo   $Body = "Dear ".$fname.", 
	
	<br><br>Thank you for using Shifa App and giving such good response. 
	<br><br>
	<form action='buynownotification.php' method=post>
		<b>Do you want shifa team to contact you for more option in payment details? Please give some details.</b><br>
		<b>Note:</b> You have other options to pay by debit card or bank direct deposit. <a href='#clickhere'>Click here</a><br>
		
		<input name='name' type='hidden' value='".$fname."' />
		<input name='session' type='hidden' value='".$_REQUEST['session_id']."' />
		<table>
			<tr>
				<td>
					Phone(Country Code):
				</td>
				<td>
					<input type='text' name = 'phone' value='+91' >
				</td>
			</tr>
			<tr>
				<td>
					Email:
				</td>
				<td>
					<input type='text' name = 'email' value='".$EmailTo."' >
				</td>
			</tr>
			<tr>
				<td>
					What problem you are facing in payment process?
				</td>
				<td>
					<input type='text' name = 'note' ><br>
					<b>if you are facing problem with your debit card please try for paypal payment process.</b>
				</td>
			</tr>
			<tr>
				<td>
					
				</td>
				<td>
					<input type='submit' value ='Submit' />
				</td>
			</tr>
		</table>
		
	</form>
	<br>
	<div id = 'clickhere'></div>
	<br>For Indian resident:<br>You can also pay cash/direct deposit to our bank account. <br>Due to secure banking PIN sometimes your card not get accepted<br>
	Try Direct Deposit.<br>Account Name: Nasir Sayed <br>Amount: {GooglePrice}Rs.<br>Account No:910010020952010<br>IFSC Code:UTIB0000219<br>Swift Code: AXISINBB219<br>Bank - Branch:Axis Bank - Goregaon-LinkRoad, Mumbai, India<br><br>When you are done with direct deposit to bank account, please reply with email subject 'MONEY TRANSFERED' and account details. We will then update your account as paid customer during US business hour between 9AM To 5PM PST.<br><hr>
	
	<br>You can also follow below URL for payment online NOW!
	<a href = 'http://kent.nasz.us/app_php/buynow/buynowwebpayment.php?session_id=".$_REQUEST['session_id']."'>Web Payment - Click here/PayPal</a>
	Please click on this click and contribute Shifa App for Life time development. No hidden Cost, No Additional Fee in future. 
	
	
	
	<br><br>A little background about Shifa team; we don't have a big team or company for financing Shifa Team. Developer(Nasir Sayed) and Doctor(Nazim Dadan) are giving personal time for making Shifa App for all of doctors like you to save money from buying big expensive software. 
	
	<br><br>Our vision is to create electronic books (database) with all Homeopathy book one by one. We are spending our own money to maintain infrastructure such as Database, Third Party Server, Source Code, Registration, Android bug fixes from user requests. Doctor: Nazim Dadan and Developer: Nasir Sayed have paid a lot to maintain Shifa service for the past three years and will continue to do it until we don't achieve it for iOS, WebSite & PC Software. <br><br>We want you to contribute more money for Shifa App account so that we can provide you with more desired features. <br><br>If you find any difficulties in payment with Google credit card processing or Google is not accepting your payment due to bad card or debit card issue. You can try to make payment online through WebPayment/Paypal Now!<br><br>Shifa account is also associated with PayPal payment processing. PayPal accept debit card, credit card or your existing PayPal Account. Its 100% safe money transfer for payment. If you ever find any problem in PayPal transfer then please do not hesitate to ask question to the developer at nasihere@gmail.com<br><br>Just incase if you forgot your username or password after logout. <br>Your Username: ".$EmailTo."<br>Your Password: ".$password."<br><br>Sincerely, <br>The Shifa App Team<br>Nasir Sayed<br><Note: Its an autogenerated email, Do not reply to this email. if you have any queries please email to nasihere@gmail.com>"; 
					

					// send email 
					//$success = mail($EmailTo, $Subject, $Body, "From: <$EmailFrom>");
					$success = mail("nasihere@gmail.com", "WebRequest", $_REQUEST['session_id'] . " - " . $fname, "From: <shifarequest@shifa.us>");
					die;
				
				
			}
    
    }
    
	echo  "Thankyou for using shifa app!!!"
	
    
    
    
    
    
?>