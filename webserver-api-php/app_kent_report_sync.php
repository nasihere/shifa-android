 <?php
	/////////////////////////////// SQL INJECTION ///////////////////////////////////////////
	
	     
   include_once('config.php'); 
        if (!$link) { 
            die('Could not connect: ' . mysql_error()); 
        } 
     
   $ins = "INSERT INTO  `tbl_app_pms_report` (  `_id` ,  `session_id` ,  `datetime` ,  `general` ,  `apphtml` ,  `filename` ,  `contact` ,  `comment`,  `symptoms`  ) 
VALUES (
NULL ,  '".$_REQUEST['session_id']."',  NOW(), '".$_REQUEST['general']."', '".$_REQUEST['apphtml']."',  '".$_REQUEST['filename']."',  '".$_REQUEST['contact']."',  '".$_REQUEST['comment']."',  '".$_REQUEST['symptoms']."'
);";
        
     $res =    mysql_query($ins);
     if ($res)
     {
         echo "Successfully Saved!";
     }
     else
     {
         echo "Server Error, Please try again.";
     }
    mysql_close($link); 
     
     
     ?>