<?php
    //ini_set('display_startup_errors',1);
    // ini_set('display_errors',1);
      error_reporting(0);
    $p = $_REQUEST;
    
    
    include_once('config.php');
     if (!$link) {         die();     } 
     
   
    function RecentPrivateMsgHolder($q)
    {
        //echo "<hr>" . $q . "<hr>";
        
        $i = 1;
         //$q =  "";
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
            
             $heading = false;
             while($row = mysql_fetch_array($res))
             {
                 //echo "<hr>";
                    $data[$i] = $row["_frm"];
                    $data[$i] = str_replace("__deleted","",$data[$i]);
                 //    print_r($row);

                        /*$pos = strrpos($row['chatter'], "Dr.");
                    if ($_REQUEST['session_id'] == $data[$i]['session_id'])
                            $data[$i]['icon'] = "";
                    else if ($pos === false) { // note: three equal signs
                            $data[$i]['icon'] = "";
                            $data[$i]['status'] = "Student";
                    }
                    else
                    {
                        $data[$i]['icon'] = "";
                        $data[$i]['status'] = "Doctor";
                    }
                    $data[$i]['icon'] = "http://kent.nasz.us/app_php/privatemsg/nasir.PNG";
                    $data[$i]['chatter'] = $row['chatter'];
                        */
                 $i++;
                 // }
             }   
         }
        // print_r($data);
        return $data;
        
     }
     function RecentPrivateMsgChat($datasession)
     {
        
        $q =  "SELECT max(_id)  FROM `tbl_app_msg` where session_id_to = '".$_REQUEST['session_id']."' or  session_id = '".$_REQUEST['session_id']."' order by _id desc limit 1";
          $res = mysql_query($q);
            $num_rows = mysql_num_rows($res);
         while($row = mysql_fetch_array($res))
                 {
                     $maxid = $row[0];
                 }
         //print "-,-" . $maxid . "-,,-";
         
        foreach($datasession as $value)
        {
            //   echo "<hr>";
            if ($dataSessionSkip[$value] != "true")
            {
                
                $dataSessionSkip[$value] = "true";
                
                if ($_REQUEST['session_id'] == $value)
                    $q =  "";
                else
                   $q =  "SELECT _id, msg,iRead,datetime,chatter,chatter_to,session_id_to _to,session_id _frm    FROM `tbl_app_msg` where (session_id_to = '".$_REQUEST['session_id']."' and session_id = '".$value."') or (session_id = '".$_REQUEST['session_id']."' and session_id_to = '".$value."')  order by _id desc limit 1";   
            }
            else
            {
                $q = "";
            }
            //   echo "<hr>" .  $q . "<hr>";
            
            $res = mysql_query($q);
            $num_rows = mysql_num_rows($res);
             if ($num_rows)
             {
                 $heading = false;
                 while($row = mysql_fetch_array($res))
                 {
                      if ($_REQUEST['session_id'] == $row['_frm'])
                      {
                       $data1['chatter'] = $row['chatter_to'];
                      }
                     else
                     {
                     $data1['chatter'] = $row['chatter'];
                     }
                         if ($_REQUEST['session_id'] == $row['_to'])
                         {
                          
                             $data1['sessionid'] = $row['_frm'];  
                             
                         }
                     else
                     {
                          
                          $row['msg'] = "You: " . $row['msg'];
                         $data1['sessionid'] = $row['_to'];  
                     }
                        $dataSessionSkip[$data1['sessionid']] = "true";
                        if ($row['iRead'] == "0" && $_REQUEST['session_id'] ==  $row['_to']) 
                            $row['msg'] = "<b>" . $row['msg'] . "</b>";
                        if ($row['iRead'] == "0")
                            $data1['msg'] = "<b>&#9658; </b>" .$row['msg'];
                        else
                            $data1['msg'] = "<b>&#x2713; </b>" . $row['msg'];
                        $data1['iRead'] = $row['iRead'];
                        
                        
                     
                        
                     
                        
                        $data1['datetime'] = time_ago2($row['datetime']);
                        if ($data1['datetime'] == "Online" && $_REQUEST['session_id'] != $row['_frm'] )
                        {
                            
                            $data1['chatter'] = "<font color='#66CD00'><b>&#9830;</b></font> ". $data1['chatter'];
                        }
                    else
                    {
                        $data1['chatter'] = "<font color='grey'><b>&#9830;</b></font> " . $data1['chatter'];
                    } 
                     
                     
                     
                       $pos = strrpos($data1['chatter'], "Dr.");
                       if ($pos === false) { // note: three equal signs
                            $data1['icon'] = "";
                            $data1['status'] = "Student";
                       }
                       else
                       {
                           $data1['icon'] = "";
                           $data1['status'] = "Doctor";
                       }
                     
                     if (file_exists('../shifaappsettings/'.$data1['sessionid'] . ".jpg") && $_REQUEST['session_id'] == "000000000000007-") 
                     {
                         $data1['icon'] = 'http://kent.nasz.us/app_php/shifaappsettings/'.$data1['sessionid'] . ".jpg";
                     }
                     else
                     {
                         $data1['icon'] = "";
                     }
                         $data1['icon'] = "";
                        echo $data1['sessionid'] . "-,-";
                        echo $data1['icon'] . "-,-";
                        echo $data1['status'] . "-,-";
                        echo ucwords(strtolower($data1['chatter'])) . "-,-";
                        
                        echo $data1['msg'] . "-,-";
                        echo $data1['iRead'] . "-,-";
                        echo   " "  . "-,-";
                        echo $value. "-,,-";
                     
                 }
            }
            else
            {
                
                  $q =  "SELECT fname, lname,occupation,datetime,lastvisitdatetime  FROM `tbl_app_registration` where session_id = '".$value ."'  limit 1";   
           
            
            $res = mysql_query($q);
            $row = mysql_fetch_array($res);
               
                 if (file_exists('../shifaappsettings/'.$value . ".jpg") && $_REQUEST['session_id'] == "000000000000007-") 
                     {
                         $data1['icon'] = 'http://kent.nasz.us/app_php/shifaappsettings/'.$value . ".jpg";
                     }
                     else
                     {
                         $data1['icon'] = "";
                     }
                
                
                echo $value . "-,-";
                echo  $data1['icon'] . "-,-"; //icon
                echo $row['occupation'] . "-,-";
                if (time_ago2($row['lastvisitdatetime']) == "Online")
                    echo "<font color='#66CD00'><b>&#9830;</b></font> ". $row['fname'] . " " . $row['lname']  . "-,-";
                else
                    echo "<font color='grey'>&#9830;</font> ". $row['fname'] . " " . $row['lname']  . "-,-";
                
                echo "<font color='grey'>Send Private Message</font>" . "-,-";
                echo "0" . "-,-";
                echo time_ago2($row['lastvisitdatetime'])  . "-,-";
                echo $value  . "-,,-";
            }
        }
        return $data;
     }
     function ShowAllMsgs()
     {
         //  echo "<hr>";
      $q =  "SELECT  _id, msg,iRead,datetime,chatter,chatter_to,session_id_to,session_id FROM `tbl_app_msg` where (session_id_to = '".$_REQUEST['session_id_to']."' and session_id = '".$_REQUEST['session_id']."') or (session_id = '".$_REQUEST['session_id_to']."' and session_id_to = '".$_REQUEST['session_id']."') order by _id ";
            $res = mysql_query($q);
            $num_rows = mysql_num_rows($res);
            if ($num_rows == 0)
            {
               $q =  "SELECT  _id, msg,iRead,datetime,chatter,chatter_to,session_id_to,session_id FROM `tbl_app_msg` where (session_id_to like '".$_REQUEST['session_id_to']."%' and session_id like  '".$_REQUEST['session_id']."%') or (session_id like '".$_REQUEST['session_id_to']."%' and session_id_to like '".$_REQUEST['session_id']."%') order by _id ";
                $res = mysql_query($q);
            $num_rows = mysql_num_rows($res);
            }
            $_id = "0";
             if ($num_rows)
             {
                 $heading = false;
                 $i=0;
                
                 while($row = mysql_fetch_array($res))
                 {
                     //      echo "<hr>";
                     //     print_r($row);
                     //     echo "<hr>";
                        $_id = $_id . "," . $row['_id'];
                       $data1[$i]['msg'] = $row['msg'];
                           
                        $data1[$i]['datetime'] = time_ago2($row['datetime']);
                       
                        $data1[$i]['icon'] = "";
                        $data1[$i]['status'] = "";
                        $data1[$i]['iRead'] = $row['iRead'];
                        
                     
                     
                     
                       if ($_REQUEST['session_id'] == $row['session_id'])
                      {
                            $data1[$i]['session_id'] = $row['session_id_to'];
                            $sessionIdIcon = $row['session_id'];
                      }
                     else 
                     {
                         $data1[$i]['session_id'] = $row['session_id_to'];
                         $sessionIdIcon = $row['session_id_to'];
                         
                     }
                      $data1[$i]['chatter'] = $row['chatter'];
                      $data1[$i]['msg'] = $data1[$i]['session_id_to'] .   $data1[$i]['msg'];
                     
                       $pos = strrpos($data1[$i]['chatter'], "Dr.");
                       if ($pos === false) { // note: three equal signs
                            $data1[$i]['icon'] = "";
                           $data1[$i]['status'] = "Student";
                       }
                       else
                       {
                           $data1[$i]['icon'] = "";
                          $data1[$i]['status'] = "Doctor";
                       }
                     
                       if (file_exists('../shifaappsettings/'.$row['session_id'] . ".jpg") && $_REQUEST['session_id'] == "000000000000007-" ) 
                     {
                         $data1[$i]['icon'] = 'http://kent.nasz.us/app_php/shifaappsettings/'.$row['session_id']  . ".jpg";
                     }
                     else
                     {
                         $data1[$i]['icon'] = "";
                     }
                     
                        
                        echo $data1[$i]['session_id'] . "-,-";
                        echo $data1[$i]['icon'] . "-,-";
                        echo $data1[$i]['status'] . "-,-";
                        echo ucwords(strtolower($data1[$i]['chatter'])) . "-,-";
                        
                        echo $data1[$i]['msg'] . "-,-";
                        echo $data1[$i]['iRead'] . "-,-";
                        echo $data1[$i]['datetime'] . "-,-";
                        echo $row['_id'] . "-,,-";
                        
                                             $i++;
                }
                
            }
          else
                 {
                        echo $_REQUEST['session_id_to'] . "-,-";
                        echo "" . "-,-";
                        echo  "Student" . "-,-";
                        echo "Shifa" . "-,-";
                        
                     echo "<font color='grey'>Send New Private Message</font>" . "-,-";
                        echo "0" . "-,-";
                        echo "Now" . "-,-";
                        echo "0" . "-,,-";
                 }
        $q = "UPDATE tbl_app_msg SET iRead = 1 where _id in (".$_id.") and session_id != '".$_REQUEST["session_id"]."'";
          mysql_query($q);
            return $data;
     }
      if ($_REQUEST['session_id_to'])
      {
         echo "Private"; //Very important line to distinguish list vs detail chat. Android change function after search private
                $data =  ShowAllMsgs() ;
      }
      else
      {
        echo "Public"; //Very important line to distinguish list vs detail chat. Android change function after search public. Android function name :ShowPvtMsg()
       
           $dataPrivateMsg[0]  = $_REQUEST["session_id"];
          
        $dataPrivateMsg =  RecentPrivateMsgHolder("SELECT  distinct chatter,session_id  _frm FROM `tbl_app_msg`  where session_id_to = '".$_REQUEST['session_id']."'   order by _id desc limit 150") ;
          $dataPrivateMsg1[0]  = $_REQUEST["session_id"];
          
           $dataPrivateMsg1 = RecentPrivateMsgHolder("SELECT  distinct chatter,session_id_to _frm FROM `tbl_app_msg`  where session_id = '".$_REQUEST['session_id']."' order by _id desc limit 150") ;
          $data = array_merge($dataPrivateMsg , $dataPrivateMsg1 );
          
           $dataPrivateMsg2 = RecentPrivateMsgHolder("SELECT  distinct fname chatter,session_id _frm FROM `tbl_app_registration` WHERE session_id != '".$_REQUEST['session_id']."' and session_id != '123456789' and lastvisitdatetime BETWEEN (DATE_SUB(NOW(),INTERVAL 100 MINUTE)) AND NOW() limit 20") ;
          
          //     print_r($dataPrivateMsg2 );
          if ($data == "")
          {
              $data = $dataPrivateMsg2;
          }
          else
          {
           $data = array_merge($data , $dataPrivateMsg2 );
          }
          // $data = array_merge($dataPrivateMsg , $dataPrivateMsg1 );
          
          //  print_r($data);
          
          $dataPrivateMsg= array_unique($data );
          //  echo "<hr>";
          //     print_r($dataPrivateMsg);
         RecentPrivateMsgChat($dataPrivateMsg) ;
      }

    
     mysql_close($link); 
     

?>












<?php
  /********************************** Time to go Conversion **********************/ 
     function time_ago1($date)
    {
        if(empty($date)) {
            return "No date provided";
        }
        
            $periods = array("sec", "min", "hr", "day", "week", "month", "year", "decade");
            $lengths = array("60","60","24","7","4.35","12","10");
        
        $now = time();
        $unix_date = strtotime($date);
        // check validity of date
        if(empty($unix_date)) {
         return "Bad date";
         }
         // is it future date or past date
         if($now > $unix_date) {
         $difference = $now - $unix_date;
         $tense = "ago";
         } else {
         $difference = $unix_date - $now;
        $tense = "from now";}
         for($j = 0; $difference >= $lengths[$j] && $j < count($lengths)-1; $j++) {
        $difference /= $lengths[$j];
        }
        $difference = round($difference);
        if($difference != 1) {
        $periods[$j].= "s";
        }
        return "$difference $periods[$j] {$tense}";
    }
     function time_ago2($date)
    {
        if(empty($date)) {
            return "No date provided";
        }
        
            $periods = array("online", "min", "hr", "day", "week", "month", "year", "decade");
            $lengths = array("900","60","24","7","4.35","12","10");
        
        $now = time();
        $unix_date = strtotime($date);
        // check validity of date
        if(empty($unix_date)) {
         return "Bad date";
         }
         // is it future date or past date
         if($now > $unix_date) {
         $difference = $now - $unix_date;
         $tense = "ago";
         } else {
         $difference = $unix_date - $now;
        $tense = "from now";}
         for($j = 0; $difference >= $lengths[$j] && $j < count($lengths)-1; $j++) {
        $difference /= $lengths[$j];
        }
        $difference = round($difference);
        if($difference != 1) {
        $periods[$j].= "s";
        }
        if ($periods[$j] == "online" || $periods[$j] == "onlines") return "Online";
        if (($periods[$j] == "min" || $periods[$j] == "mins") && $difference <= 5) return "Online";

        return "$difference $periods[$j] {$tense}";
    }

?>