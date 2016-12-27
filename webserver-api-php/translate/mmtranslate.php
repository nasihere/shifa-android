<?php
    //ini_set('display_startup_errors',1);
    // ini_set('display_errors',1);
    //  error_reporting(-1);
    $p = $_REQUEST;
    include_once('config.php');
    
     if (!$link) {         die();     } 
     
	$q =  "SELECT  " .$_REQUEST['field'] . " FROM `tbl_rem_info` where rem = '".$_REQUEST['rem']."'";
	$res = mysql_query($q);
    $num_rows = mysql_num_rows($res);
    if ($num_rows)
    {    
		while($row = mysql_fetch_array($res))
        {
			echo $row[0];
		}
    
          
    }

    
     mysql_close($link); 
     

?>










