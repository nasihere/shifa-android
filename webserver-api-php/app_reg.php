 <?php
 session_start();
 
$sess = session_id(); 
     $p = $_REQUEST;
     if (trim($p['fname']) == "" || trim($p['email']) == ""  || trim($p['password']) == "" ) 
     {
          echo "Some information is not valid or appropirate.";
         die;
     }
     $ip = $_SERVER["REMOTE_ADDR"];
   
    include_once('config.php');
    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
     
  $q =  "INSERT INTO  `tbl_app_registration` (  `_id`  ,   `fname`, `gcmreg` ,  `lname` ,  `email` ,  `dob` ,  `sex` ,  `country` ,  `city` ,  `occupation` ,  `session_id`,`datetime`,`visit`,`lastvisitdatetime`,`password`,`ip`,`refer_emailid` )
VALUES (
NULL,  '".
        $p['fname']."',  '".$p['gcmreg']."',  '".$p['lname']."',  '".$p['email']
        ."',  '".$p['dob']."',  '".
        $p['sex']."',  '".$p['country']."',  '".$p['city']."',  '".$p['occupation']."',  '".
        $sess."',NOW(),1,NOW(),'".$p['password']."','".$ip."','".$p['referemailid']."');";
    
    $q = str_replace("NASSPACENAS", " ", $q);
   $result =  mysql_query($q);
     
   
     
if (!$result) {
     
    print "-404-:-Some information is not valid or correct."; 
      $regqry = "INSERT INTO  `tbl_app_reg_query` (  `_id` ,  `regqry` ,  `datetime` ) 
VALUES (
NULL ,  '".$q."', NOW()
)";
    mysql_query($regqry);
     
    mysql_close($link); 
   
    die;
}
    if ($_REQUEST['web'])
    {
        echo "Registration completed, Please go back and try to login with your email id ". $_REQUEST['email'];
    }  
    
    
   
?>