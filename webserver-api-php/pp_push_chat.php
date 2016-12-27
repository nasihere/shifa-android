  <?php

    
    
    include_once('config.php');    if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
     $p = $_REQUEST;
   $q =  "SELECT chat,_id,_frm,chatter,_to FROM tbl_app_chat where _id >= ".$p['_id']." and chat != '' and _id != '' and _frm != '' and (_to = '' || _to = '-999')   order by _id desc limit 100";
      
      //$q =  "SELECT chat,_id,_frm,chatter FROM tbl_app_chat where _id >= ".$p['_id']." and chat != '' and _id != '' and _frm != ''   order by _id desc limit 100";
     
    $res = mysql_query($q);
       $num_rows = mysql_num_rows($res);

     if ($num_rows)
     {
         while($row = mysql_fetch_array($res))
         {

             print $row['_id'] . ",";
             print $row['_frm'] . ",";
             print $row['chat'] . ",";
             print $row['chatter'] .":";

            
         }
           mysql_close($link);             
     }
     else
     {
             print "-1";

     }
   
?>