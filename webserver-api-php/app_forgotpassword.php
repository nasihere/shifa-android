<?php

/*
*** MEssage --=-101-=--:-Password Sent you by email.
*/
   include_once('config.php');
     
 if (!$link) {         die();     } 
     $p = $_REQUEST;
    
     $q =  "SELECT fname, password FROM `tbl_app_registration` WHERE email = '".$_REQUEST['email']."'";
     
     $res = mysql_query($q);
     $num_rows = mysql_num_rows($res);
     if ($num_rows)
     {
         $row = mysql_fetch_array($res);
         $password = $row['password'];
         $fname = $row['fname'];
$EmailFrom = "shifa-supports@shifa.us";
$EmailTo = $_REQUEST['email'];
    $Subject = "Shifa: Forgot Password!!";

// prepare email body text
$Body = "Dear ". $fname .", \n\n We have received a request for your account password. Please see your password is " . $password;
$Body .= "\n \n Thanks \n Nasir Sayed \n Shifa Team";

// send email 
$success = mail($EmailTo, $Subject, $Body, "From: <$EmailFrom>");

    include_once('config.php');
    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
         $result =  mysql_query("INSERT INTO  `contact` (  `id` ,  `name` ,  `email` ,  `message`  ) VALUES (NULL ,  '".$Name."',  '".$EmailTo."',  '".$Body."');");
  
     
     }
if (!$result) {
    echo "-=-101-=--:-Your email id is not found in our database, Please create a new account or try again."; 
}
    else
    {
        echo "-=-101-=--:-Password Sent on your email."; 
    }



?>