<?php
	 $p = $_REQUEST;

	 if ($p['force'] == "buynow")
	{
			$BodyContent = "Dear ".$p['name'].", 
					You have other option to pay money.\r\n
					
					\r\nFor Indian resident:\r\nYou can also pay cash/direct deposit to our bank account. \r\nDue to secure banking PIN sometimes your card not get accepted\r\n
					Try Direct Deposit.\r\nAccount Name: Nasir Sayed \r\nAmount: {GooglePrice}Rs.\r\nAccount No:910010020952010\r\nIFSC Code:UTIB0000219\r\nSwift Code: AXISINBB219\r\nBank - Branch:Axis Bank - Goregaon-LinkRoad, Mumbai, India\r\n\r\nWhen you are done with direct deposit to bank account, please reply with email subject 'MONEY TRANSFERED' and account details. We will then update your account as paid customer during US business hour between 9AM To 5PM PST.\r\n
					
					\r\nYou can also follow below URL for payment online NOW!
					<a href = 'http://kent.nasz.us/app_php/buynow/buynowwebpayment.php?session_id=".$p['session_id']."'>Web Payment - Click here/PayPal</a>
					Please click on this click and contribute Shifa App for Life time development. No hidden Cost, No Additional Fee in future. 
					
					
					\r\n\r\nA little background about Shifa team; we don't have a big team or company for financing Shifa Team. Developer(Nasir Sayed) and Doctor(Nazim Dadan) are giving personal time for making Shifa App for all of doctors like you to save money from buying big expensive software. 
					
					Help Number\r\n
					US Contact No: +1 323-300-4756\r\n
					India Contact No: +91 9773622313 \r\n
					
					\r\n\r\nOur vision is to create electronic books (database) with all Homeopathy book one by one. \r\n\We are spending our own money to maintain infrastructure such as Database, Third Party Server, Source Code, Registration, Android bug fixes from user requests. \r\n\Doctor: Nazim Dadan and Developer: Nasir Sayed have paid a lot to maintain Shifa service for the past four years and will continue to do it until we don't everything, WebSite & PC Software. \r\n\r\nWe want you to contribute more money for Shifa App account so that we can provide you with more desired features. \r\n\r\nIf you find any difficulties in payment with Google credit card processing or Google is not accepting your payment due to bad card or debit card issue. You can try to make payment online through WebPayment/Paypal Now!\r\n\r\nShifa account is also associated with PayPal payment processing. PayPal accept debit card, credit card or your existing PayPal Account. Its 100% safe money transfer for payment. If you ever find any problem in PayPal transfer then please do not hesitate to ask question to the developer at nasihere@gmail.com\r\nSincerely, \r\nThe Shifa App Team\r\nNasir Sayed\r\n
					\r\n\r\n
					<Note: Its an autogenerated email, Do not reply to this email. if you have any queries please email to nasihere@gmail.com>
					
					
					"; 
					
					echo "Buynow Email Sent: ";
					echo $success = mail($p['email'], "Shifa Payment Options", $BodyContent, "From: <noreply@shifa.us>");
					echo "<pre>";print_r($p);echo "</pre>";
		die;
	}
	
//////////////////////////////////////////////////////////////Database initializ///////////////////////////////
include_once('config.php');

	
	
     if ($p['force'] == "remove")
	 {
		$q = "update tbl_app_registration set blocked = 'Y' where  session_id = '".$p['session_id']."'";
		$res = mysql_query($q);
		$q = "update tbl_app_chat set originalchat = chat where  _frm = '".$p['session_id']."'";
		$res = mysql_query($q);
		$q = "update tbl_app_chat set chat = '' where  _frm = '".$p['session_id']."'";
		$res = mysql_query($q);
		$q = "update tbl_app_chat set _frm = '".$p['session_id']."__deleted' where  _frm = '".$p['session_id']."'";
		$res = mysql_query($q);
		$q = "delete from tbl_notification_log where  session_id = '".$p['session_id']."'";
		$res = mysql_query($q);
		$q = "update tbl_app_msg set session_id = '".$p['session_id']."__deleted' where  session_id = '".$p['session_id']."'";
		$res = mysql_query($q);
		
		echo "Account Deleted! Session id " . $p['session_id'];
		mysql_close($link);  
		
		echo $emailcontent = $p['session_id'] . " account is removed from shifa because of bad insulting shifa app";
		echo "<br>Status Email: " . $success = mail($p['eemail'], "Shifa Account Removed!", $emailcontent, "From: <noreply@shifa.in>");
		
		die;
	 }
	else if ($p['force'] == "paid")
	{
		$q = "insert into tbl_shifa_log (session_id,Name,data,datetime,lastvisitdatetime) values ('".$p['session_id']."','Counter~Home~Remove~ads~Paid',1,NOW(),NOW())";
		$res = mysql_query($q);
		
		echo "<br> ";
		echo $emailcontent = "Hi ".$p['ename'].", \r\n Here is a confirmation. We are happy to annouce that your shifa registered account is paid now in our system. \r\n Now you can do logout or login again to get new setting. \r\n Your shifa login id: " . $p['eemail'] . "\r\n Password:" . $p['epassword'] . "\r\n If you have any problem please feel free to contact at nasihere@gmail.com for support";
		echo "<br>Status Email: " . $success = mail($p['eemail'], "Congratulation, Your Shifa Account is Paid Now!", $emailcontent, "From: <noreply@shifa.in>");
		echo "<br>";
		if (!$success)
		{
			echo "Email id is not correct or something wrong.. Mail Server not able to send confirmation email! <br>";
		}
		echo "Paid account set completed!";
		mysql_close($link);  
		die;
	}
	else if ($p['name'] != "")
	{
		echo  getEmail($p['name']);
		mysql_close($link);  
		die;
	}
	
	else if ($p['email'] != "")
	{
		echo $session_id = getSession($p['email']);
		echo "<hr>";
		$q =  "SELECT * FROM `tbl_shifa_log` where Name = 'Counter~Home~Remove~ads~Paid' and session_id = '".$session_id."' and data != 0"; 
		$res = mysql_query($q);
		$num_rows = mysql_num_rows($res);
		$paid = false;
		if ($num_rows)
		{
			$paid = true;
		}
		else
		{
			echo "tbl_shifalog mein yeh email ki entry nahi hai<br> isliye insert kar raha hoo new entry phele<br>";
			NewAccount($session_id);
		}
		
		if ($paid == true)
		{
			echo "<h5>Already " . $p['email'] . " account is paid!</h5>";		
		}
		else
		{
			PaidSession($session_id);
			
		}
	}
	else
	{
		
	}
	function getSession($email)
	{
		$q = "select session_id from tbl_app_registration where email = '".$email."'";
		$res = mysql_query($q);
		$num_rows = mysql_num_rows($res);
		if ($num_rows)
		{
			$row = mysql_fetch_array($res);
			return $row['session_id'];
		}
		echo "<h2>sesion id not found</h2>";
		return "";
	}
	
	function PaidSession($session_id)
	{
		$q = "UPDATE tbl_shifa_log set data = 1,lastvisitdatetime = now() where Name = 'Counter~Home~Remove~ads~Paid' and session_id = '".$session_id."'";
		$res = mysql_query($q);
		echo "<h4>Successfully Changed account!</h4>";
		mysql_close($link);  
		die;	
	}
	function NewAccount($session_id)
	{
		echo $q = "insert into tbl_shifa_log (session_id,Name,data,datetime,lastvisitdatetime) values ('".$session_id."','Counter~Home~Remove~ads~Paid',1,NOW(),NOW())";
		$res = mysql_query($q);
		echo "<h4>New  account  created with paid!</h4>";
		die;	
	}
	function  getEmail($name)
	{
		$cols = " session_id,email,fname,lname,password,lastvisitdatetime,country,visit,ip,blocked ";
		
		if ($name == "TodayVisitors")
			$q = "select " . $cols . " from tbl_app_registration where lastvisitdatetime >= CURDATE() order by lastvisitdatetime desc";
		else if ($name == "blocked")
			$q = "select " . $cols . " from tbl_app_registration where blocked = 'Y' order by lastvisitdatetime desc";
		else
			$q = "select " . $cols . " from tbl_app_registration where session_id like '%".$name."%' or fname like '%".$name."%' or lname like '%".$name."%' or email like '%".$name."%' or lastvisitdatetime like '%".$name."%' or ip like '%".$name."%'  order by lastvisitdatetime desc";
		$res = mysql_query($q);
		$num_rows = mysql_num_rows($res);
		if ($num_rows)
		{
			while($row = mysql_fetch_array($res)){
				echo "BuyNow: <a href='crack.php?session_id=".$row['session_id']."&force=buynow&name=".$row['fname']."&email=".$row['email']."'>Send Email</a>" ;
				echo "| Email: ";
				echo $row['email']; 
				echo " | FName: ";
				echo $row['fname'];
				echo " | LName: ";
				echo $row['lname'];
				echo "| Paid: <a href='crack.php?session_id=".$row['session_id']."&eemail=".$row['email']."&epassword=".$row['password']."&force=paid&ename=".$row['fname']."'>Paid Now</a>" ;
				
				echo " | Password: ";
				echo $row['password'];
				echo "| No of Visit : " . $row['visit'];
				echo "| Last Visit: " . $row['lastvisitdatetime'];
				echo "| Time: " . time_ago($row['lastvisitdatetime']);
				echo "| Session_Id: " . $row['session_id'];
				echo "| Country: " . $row['country'];
				echo "| IP: " . $row['ip'];
				echo "| blocked: " . $row['blocked'];
				echo "| <a href='crack.php?session_id=".$row['session_id']."&force=remove'>Delete</a>" ;
				echo "<hr>";
			}
		}
		return "";
	}
	     
function time_ago($date)
{
    if(empty($date)) {
        return "No date provided";
    }
 
$periods = array("sec", "min", "hr", "day", "week", "month", "year", "decade");
$lengths = array("60","60","24","7","4.35","12","10");
$now = time();
$unix_date = strtotime($date);
// check validity of date
 
if(empty($unix_date)) {
 
return "Bad date";
 
}
 
// is it future date or past date
 
if($now > $unix_date) {
 
$difference = $now - $unix_date;
 
$tense = "ago";
 
} else {
 
$difference = $unix_date - $now;
$tense = "from now";}
 
for($j = 0; $difference >= $lengths[$j] && $j < count($lengths)-1; $j++) {
 
$difference /= $lengths[$j];
 
}
 
$difference = round($difference);
 
if($difference != 1) {
 
$periods[$j].= "s";
 
}
 
return "$difference $periods[$j] {$tense}";
 
}
?>

<h1>Search in Shifa</h1>
<form action="crack.php" method="post">
	<input type="text" name="name" />
	<input type="submit" name="submit" value="submit">
</form>
<a href="?name=blocked">Blocked Users</a>
<a href="?name=TodayVisitors">Today Visitors</a>