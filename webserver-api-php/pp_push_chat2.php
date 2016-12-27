   <?php

    include_once('config.php');
     if (!$link) { 
            die('Could not connect: ' . mysql_error()); 
        } 
             $p = $_REQUEST;
       $chatindex = $p['_id'] + 200;
       if ($p["discussion"])
       {
        //$q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _to > 1 and _id >= ".$p['_id']." and _id <= ".$chatindex."  and chat != '' and _id != '' and _frm != '' order by _id desc limit 100";
			//$q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _to = '-999' and _to != '' and chat != '' and _id != '' and _frm != '' order by datetime,_id,_to desc limit 100";
			include("pp_push_chat3.php");
			return;
       }
	   else if ($p["range"])
       {
          $q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _id >= ".$p['_id']." and _id <= ".$chatindex."  and chat != '' and _id != '' and _frm != '' order by _id desc limit 100";
       }
       else if ($p['chat'])
       {
           
                $q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _id >= ".$p['_id']."  and chat != '' and _id != '' and _frm != '' order by _id desc limit 100";
       }
       else
       {
                   $q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM  tbl_app_chat where _id >= ".$p['_id']." and chat != '' and _id != '' and _frm != '' order by _id desc limit 100";    
       }
      
     
    $res = mysql_query($q);
       $num_rows = mysql_num_rows($res);

     if ($num_rows)
     {
         while($row = mysql_fetch_array($res))
         {
 if ($p['chat'])
 {
             print $row['_id'] . "-,-";
             print $row['_frm'] . "-,-";
             print $row['chat'] . "-,-";
             print $row['chatter'] . "-,-";
             if ($row['_to'] == "") $row['_to'] = "-999";                 
            
              print $row['_to'] . "-,-";
     print date("M d, H:m", strtotime($row['datetime'])) .  "-:-";
 }
             else
             {
                 print $row['_id'] . "-,-";
                 print $row['_frm'] . "-,-";
                 print $row['chat'] . "-,-";
                 print $row['chatter'] . "-,-";
                   if ($row['_to'] == "") $row['_to'] = "-999";
                 print $row['_to'] .  "-,-";
               print date("M d, H:m", strtotime($row['datetime'])) .  "-:-";
                 
             }
            
         }
           mysql_close($link);             
     }
     else
     {
             print "-1";

     }
                 
   
?>