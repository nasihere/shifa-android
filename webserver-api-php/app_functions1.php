 <?php


    include_once('config.php');
    if (!$link) {      die();  } 
     $p = $_REQUEST;
     
     /************** User No Version  **************/
     function CheckUser()
     {
      $q =  "SELECT  _id  as n
             FROM  `tbl_shifa_log` 
             WHERE session_id =  '".$_REQUEST['session_id']."'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
             return $val;
         }
         else
         {
             return "";
         }
     }
     
     
     function Chat_tbl_app_chat_Max_Counter() //Its return the max number of _id
     {
         $q =  "SELECT MAX( _id ) as n
             FROM  `tbl_app_chat` 
             WHERE _to =  '-999'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
             return $val;
         }
         else
         {
             return "-1";
         }
         
     }
     /********** UNREAD CHAT COUNTER ***************/
     
     /*********  UNREAD CHAT USER COUNTER **********/
     function Chat_tbl_shifa_log_Chat_Counter() //Its return the max number of _id
     {
       $q =  "SELECT data as n FROM `tbl_shifa_log`  where session_id = '".$_REQUEST['session_id']."' and Name = 'Counter~Home~ChatIndex'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
             return $val;
         }
         else
         {
             return "0";
         }
         
     }
     
      /*********  No. User Online CHAT  **********/
     function Chat_tbl_app_reg_lastvisit_recent_Counter() //Its return the max number of _id
     {
         $q =  "SELECT distinct count(_id) as n FROM `tbl_app_registration` WHERE lastvisitdatetime BETWEEN (DATE_SUB(NOW(),INTERVAL 30 MINUTE)) AND NOW()";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
             return $val;
         }
         else
         {
             return "1";
         }
         
     }
     /************** Create Chat Status Message **************/
     function ChatStatusMessage()
     {
         if (Chat_tbl_app_chat_Max_Counter() <= Chat_tbl_shifa_log_Chat_Counter())
         {
            $UnReadChatMsg =  "No New";
         }
         
         else
         {
             $UnReadChatMsg =  (Chat_tbl_app_chat_Max_Counter() - Chat_tbl_shifa_log_Chat_Counter() ) . " <font color='green'>New</font>";
         }
         $ChatOnlineStatus = $UnReadChatMsg . " <br> " . Chat_tbl_app_reg_lastvisit_recent_Counter();
         return $ChatOnlineStatus;
     }
     /********** Discussion posted by user COUNTER ***************/
     function Chat_tbl_app_chat_Discussion_Posted_Counter() 
     {
         $q =  "SELECT COUNT( _id ) AS n
        FROM  `tbl_app_chat` 
        WHERE (
        _to !=  '-999'
        AND _to !=  ''
        )
        AND _frm =  '".$_REQUEST['session_id']."'
        AND _to =  '0'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
             return $val;
         }
         else
         {
             return "";
         }
         
     }
     /********** Discussion Total by all user COUNTER ***************/
     function Chat_tbl_app_chat_Discussion_All_User_Posted_Counter() 
     {
         $q =  "SELECT COUNT( _id ) AS n
        FROM  `tbl_app_chat` 
        WHERE (
        _to !=  '-999'
        AND _to !=  ''
        )
        AND _to =  '0'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
             return $val;
         }
         else
         {
             return "";
         }
         
     }

     /********** Online User List ***************/
     function Online_User_List() 
     {
         $q =  "SELECT fname, lastvisitdatetime
                FROM  `tbl_app_registration` 
                WHERE lastvisitdatetime
                BETWEEN (
                DATE_SUB( NOW( ) , INTERVAL 30 
                MINUTE )
                )
                AND NOW( )";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         $val = "<b>Online</b><br>";
         if ($num_rows)
         {
             while ($row = mysql_fetch_array($res))
             {
                $val .= "<i>" . ucfirst($row['fname']) ."</i> - <font color='green'>" . time_ago1($row['lastvisitdatetime']) . "</font><br>";
             }
             return $val;
         }
         else
         {
             return "";
         }
         
     }
     
      /********** Online User History ***************/
     function Online_User_History($q) 
     {
         
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
        
         if ($num_rows)
         {
             while ($row = mysql_fetch_array($res))
             {
                 return $row[0];  
             }
             
         }
         else
         {
             return "";
         }
         
     }


    function RecentChatDiscussion($clause, $limit)
    {
        $q =  "SELECT chat,chatter,datetime,_to FROM `tbl_app_chat` " . $clause . " group by chat order by _id desc limit " . $limit  ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $heading = false;
             while($row = mysql_fetch_array($res))
             {
               
                if ($heading == false)
                {
                    $heading = true;
                      if ($row['_to'] == "" || $row['_to'] == "-999")
                          $RecentChat = "<b><u>Ch@t</u></b><br>";
                    else if ($row['_to'] == "0" )
                        $RecentChat = "<b><u>Discussion</u></b><br>";
                   
                }
                if ($row['_to'] == "" || $row['_to'] == "-999")
                {
                    $RecentChat .=  substr($row['chat'],0,100) . "...<i><font color='#6297BC'> - " . $row['chatter']. "</font></i><i><font color='#C0C0C0'> - " .time_ago1($row['datetime']). "</font></i><br>";
                }
                
                else if ($row['_to'] == "0" )
                {
                    $RecentChat .=   substr($row['chat'],0,100) . "...<i><font color='#6297BC'> - " . $row['chatter']. "</font></i><i><font color='#C0C0C0'> - " .time_ago1($row['datetime']). "</font></i><br>";
                }
               
                
             }   
         }
         return $RecentChat;
     }
     
      function RecentUserDiscussionThread()
    {
        $q =  "SELECT chat,chatter,datetime,_to,_frm,_id FROM `tbl_app_chat` where _frm  = '".$_REQUEST['session_id']."' and _to = '0' group by chat order by _id desc limit 2" ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $heading = false;
             $RecentChat  = "<b><u>Your Posted</u></b><br>";
             while($row = mysql_fetch_array($res))
             {
               
                    $RecentChat .=   substr($row['chat'],0,100) . "...<i><font color='#6297BC'> - " . $row['chatter']. "</font></i><i><font color='#C0C0C0'> - " .time_ago1($row['datetime']). "</font></i><br>";
                
               
                 $q1 =  "SELECT chat,chatter,datetime,_to,_frm FROM `tbl_app_chat` where _to = '".$row['_id']."' order by datetime desc limit 2" ;
                 $res1 = mysql_query($q1);
                  $num_rows = mysql_num_rows($res1); 
                 
                 if ($num_rows)
                 {
                     while($row = mysql_fetch_array($res1))
                     {
                         $RecentChat .=  "<font color='green'> &#8801;&#8801;&#8801; Re: " . $row['chatter']. "</font><i> - " . substr($row['chat'],0,100) . " - " .time_ago1($row['datetime']). "</i><br>";
                     }
                 }
                 else
                 {
                     $RecentChat .=  "<b><font color='#C0C0C0'> &#934; - Be the first to reply...</font></b><br>";
                 }
                
             }   
         }
         return $RecentChat;
     }

     function DiscussionThreadForOthers()
     {
        $q =  "SELECT chat,chatter,datetime,_to,_frm,_id FROM `tbl_app_chat` where _frm  != '".$_REQUEST['session_id']."' and _to = '0' group by chat order by datetime desc limit 10" ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $heading = false;
             $RecentChat  = "<b><u>Recent Discussion</u></b><br>";
             while($row = mysql_fetch_array($res))
             {
               
                 $RecentChat .=  "<br><b>&#8801;</b> ". substr($row['chat'],0,100) . "...<i><font color='#6297BC'> - " . $row['chatter']. "</font></i><i><font color='#C0C0C0'> - " .time_ago1($row['datetime']). "</font></i><br>";
                
               
                 $q1 =  "SELECT chat,chatter,datetime,_to,_frm FROM `tbl_app_chat` where _to = '".$row['_id']."' order by datetime desc limit 4" ;
                 $res1 = mysql_query($q1);
                  $num_rows = mysql_num_rows($res1); 
                 
                 if ($num_rows)
                 {
                     while($row = mysql_fetch_array($res1))
                     {
                         $RecentChat .=  "<font color='green'> &#8801;&#8801;&#8801; Re: " . $row['chatter']. "</font><i> - " . substr($row['chat'],0,100) . " - " .time_ago1($row['datetime']). "</i><br>";
                     }
                 }
                 else
                 {
                     $RecentChat .=  "<b><font color='#C0C0C0'> &#934; - Be the first to reply...</font></b><br>";
                 }
                
             }   
         }
         return $RecentChat;
     }
        /**************************************************************************************************************************
        ***************************************************************************************************************************
        ***************************************************************************************************************************
        ***************************************************************************************************************************
        **************************************************************************************************************************/
    
     /************** Create Flash Status Message **************/
     function FlashMessage()
     {
         $FirstLine = "Discussion and medica two new features added.<br>";
         
         $HowToUseDiscussion = "<b>FYI</b><br>Please make a thread communication.. Guyz!! <br> Let me explain you the concept of discussion. Any user can post his or her query on <b>whats on your mind today for discussion</b> text box. Others user like <b>doctor or student</b> can tap or hit that query from the list to reply of that post.<br><b>we need your cooperation.</b><br>";
         
         $FlashMessage = $FirstLine . "<br>"   . $HowToUseDiscussion . "<br>" .  RecentChatDiscussion(" where _to = '-999' or _to = ''", "6") . "<br>"  . RecentUserDiscussionThread() . "<br>"  . DiscussionThreadForOthers() . "<br>"  . Online_User_List() ;
         return $FlashMessage;
     }
     /************** Ads Show and hide decision **************/
     function GoogleAds()
     {
         return "";
         $q =  "SELECT  data as n
             FROM  `tbl_shifa_log` 
             WHERE session_id =  '".$_REQUEST['session_id']."' and Name = 'Counter~Home~Remove~ads~Paid'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             if ($row['n'] == "0")
                 return "ads";
             else
                 return "";
         }
         else
         {
             return "ads";
         }
         

     }
     
     
     /************** Block User and show web page for notification  **************/
     function GoogleAdsRemote()
     {
         return "adsremote";
     }
     /************** Startup Homepage open web page url  **************/
     function GoogleAdsRemoteURL()
     {
         return "http://shifa.nasz.us";
     }
     /************** Fresh menu status   **************/
     function FreshStatus()
     {
         return "PMS & MAP Soon <br> In This Section";
     }
     /************** Fresh menu status   **************/
     function MyProfile()
     {
         return "My Profile <br> under progress ";
     }
     /************** App upgrade Version Number   **************/
     function AppUpGradeVersionNumber()
     {
         return "29";
     }
     /************** Remove Ads Status   **************/
     function RemoveAdsStatus()
     {
         return "Donate";
     }
     /************** Discussion Status   **************/
     function DiscussionStatus()
     {
         if (Chat_tbl_app_chat_Discussion_Posted_Counter() == "0")
            return "Total: " . Chat_tbl_app_chat_Discussion_All_User_Posted_Counter() ;
         else
            return "You Posted:  " . Chat_tbl_app_chat_Discussion_Posted_Counter() . " <br> " . "Total: " . Chat_tbl_app_chat_Discussion_All_User_Posted_Counter() ;
     }
             
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
     
   ?>