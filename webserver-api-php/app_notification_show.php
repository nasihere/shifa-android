   <?php
     $p = $_REQUEST;
    

    include_once('config.php');
        if (!$link) { 
            die('Could not connect: ' . mysql_error()); 
        } 
             $p = $_REQUEST;
       $q =  "SELECT chat,_id,chatter,datetime,_frm,_to FROM tbl_app_chat order by _id desc limit 1";
     
    $res = mysql_query($q);
       $num_rows = mysql_num_rows($res);
        
     if ($num_rows)
     {
         $row = mysql_fetch_array($res);
         //if ($p['session_id'] == $row['_frm']) return "";
         if ( $row['_to'] == '-999' || $row['_to'] == '')
             $chattitle = "Chat";
         
         else
             $chattitle = "Discussion";
         
         if ($row['_to'] == "0")
              $row['_to'] = $row['_id'];
         
         if($p["session_id"] == $row['_frm'])
            { 
               return ""; 
             }
         print $row['_id'] . ","; //0
         print $row['chat'] . ","; //1
         print $row['chatter'] . ","; //2
         print $chattitle  . ",";//3
         print  $row['_to'].","; //4
         print $row['datetime'] . ":";//5
                
         }
   
?>