<?php

    if (strlen($p['chat']) < 25){
        return;
    }

    if ($p['to'] == "-999"){
        $selectQry = "select group_concat(gcmreg SEPARATOR '#_#') as gcmids from tbl_app_registration  where gcmreg != '' and session_id != '".$p['_frm']."'  or session_id in ('10205304767877899','818904868132997')";
    }
    else
    {
        $selectQry = "SELECT _frm FROM `tbl_app_chat`  where _id = ".$p['to'];
        $res_frm = mysql_query($selectQry);
        $num_rows = mysql_num_rows($res_frm);
         if ($num_rows)
         {
             $rowFrom = mysql_fetch_array($res_frm);

             $selectQry = "select group_concat(gcmreg SEPARATOR '#_#') as gcmids from tbl_app_registration  where session_id = '".$rowFrom['_frm']."' or session_id in ('10205304767877899','818904868132997')";


         }
         else
         {
            return;
         }

    }
     $result = mysql_query($selectQry);
     $row = mysql_fetch_assoc($result);

    if ($row['gcmids'] != ""){
        // Message to be sent
        $message = $_POST['message'];

        // Set POST variables
        $url = 'https://android.googleapis.com/gcm/send';

        $arrayExplode = explode("#_#",$row['gcmids']);
        $message = "'" . ucfirst(strtolower($p['chatter'])) . "' " . substr($p['chat'],0,24);
        $fields = array(
                        'registration_ids'  => $arrayExplode,
                        'data'              => array( "message" => $message),
                        );

        $headers = array(
                            'Authorization: key=' . "AIzaSyDvx2Ma_210gBBqACBCP6S9ciDtMaCjZ_s",
                            'Content-Type: application/json'
                        );

        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt( $ch, CURLOPT_URL, $url );

        curl_setopt( $ch, CURLOPT_POST, true );
        curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );

        curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $fields ) );

        // Execute post
        $result = curl_exec($ch);

        // Close connection
        curl_close($ch);

        //echo $result;
    }
?>