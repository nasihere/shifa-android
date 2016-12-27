<?php
     $p = $_REQUEST;

    include_once('config.php');
     mysql_select_db(shifakent); 
     if (!$link) {         die();     } 
     
     $q =  "INSERT INTO  `tbl_app_contacts` (  `_id` ,  `session_id` ,  `friend_session_id` ,  `friend_name` ) 
        VALUES (NULL,  '".$p['session_id']."',  '".$p['friend_session_id']."',  '".$p['friend_name'] ."');";
  
     $result =  mysql_query($q);
     
     mysql_close($link); 
     

?>