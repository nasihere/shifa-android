  <?php
     $p = $_REQUEST;
     $ip = $_SERVER["REMOTE_ADDR"];
     include_once('config.php');
     if (!$link) { 
        die(); 
     } 
     if ($p['accept'] == "0")
     {
           $q =  "delete from tbl_app_contacts where  `session_id` = '".$p['friend_session_id']."' and  `friend_session_id`  = '".$p['session_id']."'";
                  mysql_query($q);
                  
                  $q =  "delete from tbl_app_contacts  where  `session_id` = '".$p['session_id']."' and  `friend_session_id`  = '".$p['friend_session_id']."'";
                  mysql_query($q);
     }
      else
      {
      
          $q =  "INSERT INTO  `tbl_app_contacts` (  `_id` ,  `session_id` ,  `friend_session_id` ,  `friend_name`,  `datetime`,  `accept`) 
              VALUES (NULL,  '".$p['session_id']."',  '".$p['friend_session_id']."',  '".$p['friend_name'] ."',  NOW(),2);";
          $result =  mysql_query($q);
          if (!$result) {
              echo "-1";
          }
          else
          {
              
              /*Check Already Request friend */
              
              $q =  "select _id from tbl_app_contacts where  `session_id` = '".$p['friend_session_id']."' and  `friend_session_id`  = '".$p['session_id']."'";
              $result =  mysql_query($q);
              $num_rows = mysql_num_rows($result);
              if ($num_rows != 0) {
                  $q =  "update tbl_app_contacts set accept = 1 where  `session_id` = '".$p['friend_session_id']."' and  `friend_session_id`  = '".$p['session_id']."'";
                  mysql_query($q);
                  
                  $q =  "update tbl_app_contacts set accept = 1 where  `session_id` = '".$p['session_id']."' and  `friend_session_id`  = '".$p['friend_session_id']."'";
                  mysql_query($q);
              }
              
              echo "Request Sent";
          }
      }
      mysql_close($link); 
    
   
?>