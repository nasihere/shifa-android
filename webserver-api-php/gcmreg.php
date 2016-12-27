<?php

	error_reporting(0);

	
    include_once('config.php');
    if (!$link) {
        die('Could not connect: ' . mysql_error());
    }

        $p = $_REQUEST["param"];


    if ($_REQUEST['session_id'] == "0" || $_REQUEST['session_id'] == "") return;


    $query = "update tbl_app_registration " .
                " set gcmreg = '" . $_REQUEST["gcmreg"]."' " .
             " where session_id  = '".$_REQUEST['session_id']."'";


    $result = mysql_query($query);




?>