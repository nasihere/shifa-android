<?php
     $p = $_REQUEST;
   
    include_once('config.php');
     if (!$link) {         die();     } 
     
     
    // user id is session_id 
     if ($p['type'] == 2)
     {
         $q =  "update  `tbl_app_msg`  set session_id  = CONCAT(session_id,'__deleted')  where session_id = '" . $_REQUEST['session_id'] . "' and  session_id_to = '" . $_REQUEST['session_id_to'] . "'";
         $result =  mysql_query($q);
         $q =  "update  `tbl_app_msg`  set session_id_to = CONCAT(session_id_to ,'__deleted')  where session_id_to = '" . $_REQUEST['session_id'] . "' and  session_id = '" . $_REQUEST['session_id_to'] . "'";
        
         
     }
     else if ($p['type'] == 1)
        $q =  "update  `tbl_app_msg` set iRead = 1 where `_id` = " . $_REQUEST['_id'];
     else 
        $q =  "delete from `tbl_app_msg`  where `_id` = " . $_REQUEST['_id'];
        
     $result =  mysql_query($q);
     
     mysql_close($link); 
     

?>