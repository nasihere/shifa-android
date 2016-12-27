<?php 
    setcookie("session_id", $_REQUEST['session_id'], time()+3600);  
    include_once('config.php');
    
    if ($_REQUEST['success'] == "1")
    {
         $_COOKIE["session_id"];
         $q =  "SELECT session_id FROM `tbl_shifa_log` where session_id = '".$_COOKIE["session_id"]."'"; 
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
        if ($num_rows)
        {

             $q14 =  "UPDATE `tbl_shifa_log` SET  lastvisitdatetime = NOW(), Data = Data + 4 where Name =  'Counter~Home~Remove~ads~Paid'  and session_id= '" . $_COOKIE['session_id'] . "'";
        }
        else
        {
            $q14 =  "INSERT INTO  `tbl_shifa_log`  (  `_id` ,  `session_id` ,  `Name` ,  `Data`, `datetime`, `lastvisitdatetime`  )
        VALUES ( NULL,  '".$_COOKIE['session_id']."',  'Counter~Home~Remove~ads~Paid',  4,NOW(),NOW());";
        }
        $result =  mysql_query($q14);
        //setcookie("session_id", $_REQUEST['session_id'], time()+3600);  
        echo "<h2>Thanks for the payment, Please do logout from shifa app homescreen & Login again for new settings. <h2>"; die;
    }
    else if ($_REQUEST['session_id'])
    {
    $q14 =  "UPDATE `tbl_shifa_log` SET  lastvisitdatetime = NOW(), Data = Data + 1 where Name =  'Counter~Home~Remove~ads'  and session_id= '" . $_REQUEST['session_id'] . "'";
        mysql_query($q14);
         
        setcookie("session_id", $_REQUEST['session_id'], time()+7200);
    }
    ?>
<html>
    <body bgcolor="#cccccc">
        <div style="width:800px; margin:0 auto;">
        
    <b> Purchase Item</b>: Life time Shifa access<br>
   
        <b> Payment method </b>: 
            <?php 
    if ($_REQUEST['ispaid'] == "") { ?>
            <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="S4K7ZFMPKNR9U">
<input type="image" src="https://www.paypalobjects.com/en_GB/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal â The safer, easier way to pay online.">
<img alt="" border="0" src="https://www.paypalobjects.com/en_GB/i/scr/pixel.gif" width="1" height="1">
</form>
              <?php } ?>
            <?php 
    if ($_REQUEST['ispaid'] == "true") { ?>
<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="XY6X6KZ3UAUX6">
<table>
<tr><td><input type="hidden" name="on0" value="Shifa">Shifa</td></tr><tr><td><select name="os0">
    <option value="App Development">App Development $5.00 USD</option>
    <option value="App Database">App Database $5.00 USD</option>
    <option value="App Maintenance">App Maintenance $5.00 USD</option>
</select> </td></tr>
</table>
<input type="hidden" name="currency_code" value="USD">
<input type="image" src="https://www.paypalobjects.com/en_GB/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal â The safer, easier way to pay online.">
<img alt="" border="0" src="https://www.paypalobjects.com/en_GB/i/scr/pixel.gif" width="1" height="1">
</form>


            <?php } ?>

        <nr>
            Paypal is recommeneded neither PayPal requires you to make an account.
          
    </div>
        </body>
</html>