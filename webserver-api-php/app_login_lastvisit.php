 <?php
   include_once('config.php');
    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
     $p = $_REQUEST;
   
         $q =  "UPDATE  `tbl_app_registration` SET lastvisitdatetime = NOW(), visit = visit + 1 where session_id = '" . $p['session_id'] . "'";
         mysql_query($q);
         
         
         mysql_close($link);              
      
    
   
?>