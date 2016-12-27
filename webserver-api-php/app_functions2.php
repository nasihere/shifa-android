 <?php

    $data = "";
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
	   /********** Online User History ***************/
     function ResultSet($q) 
     {
         
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
        
         if ($num_rows)
         {
			return $res;
             
         }
         else
         {
             return "";
         }
         
     }
	 
       /********** Online User History ***************/
     function Online_User_History($q,$type = "") 
     {
         
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
        
         if ($num_rows)
         {
             while ($row = mysql_fetch_array($res))
             {
                 if ($type == "1")
                 {
                     return  $row[0];
                 }
                 if ($row[0] == "0") 
                     return "Not yet but very soon";
                 else
                     return  $row[0] . " Times";  
             }
             
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
 /************** User No Version  **************/
     function CheckUserName()
     {
      $q =  "SELECT  fname as n,occupation
             FROM  `tbl_app_registration` 
             WHERE session_id =  '".$_REQUEST['session_id']."'";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $row = mysql_fetch_array($res);
             $val = $row['n'];
              $pos = strrpos($row['occupation'], "Doctor");
              if ($pos === false) { // note: three equal signs
                           return $val;
                     }
                    else
                    {
                            return "Dr. " . $val;
                    }
           
         }
         else
         {
             return "";
         }
     }
     /********** Online User List ***************/
     function Online_User_List() 
     {
         $i = 0;
        $q =  "SELECT fname,session_id,occupation,datetime, lastvisitdatetime
                FROM  `tbl_app_registration` 
                WHERE lastvisitdatetime
                BETWEEN (
                DATE_SUB( NOW( ) , INTERVAL 900
                MINUTE )
                )
                AND NOW( ) order by lastvisitdatetime desc limit 10";
         
         $res = mysql_query($q);
         $num_rows = mysql_num_rows($res);
         $val = "<b>Online</b><br>";
         if ($num_rows)
         {
             while ($row = mysql_fetch_array($res))
             {
                 $pos = strrpos($row['occupation'], "Doctor");
                 if ($_REQUEST['session_id'] == $row['session_id']) {
                         $data[$i]['icon'] = "ic_chatonline_from.png";
                         $data[$i]['status'] = "You";
                     }
                    else if ($pos === false) { // note: three equal signs
                         $data[$i]['icon'] = "ic_chat_student.png";
                         $data[$i]['status'] = "Student";
                     }
                    else
                    {
                          $data[$i]['icon'] = "ic_chat_doctor.png";
                          $data[$i]['status'] = "Doctor";
                    }

                    $data[$i]['chatter'] = ucfirst($row['fname']);
                    $data[$i]['session_id'] = $row['session_id'];
                    $data[$i]['time'] =  time_ago1($row['lastvisitdatetime']);
                    $data[$i]['regdate'] =  time_ago1($row['datetime']);
                 
                 $i++;   
             }
             return $data;
         }
         else
         {
             return "";
         }
         
     }
     
    function RecentChatDiscussion($clause, $limit)
    {
       $i = 0;
    $q =  "SELECT chat,chatter,datetime,_to,_frm FROM `tbl_app_chat` " . $clause . " order by _id desc limit " . $limit  ;
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
                      {
                          $section = 'chat';
                         
                          
                     }
                    else if ($row['_to'] == "0" )
                         {
                             $section = 'discussion'; 
                           
                            
                    }
                }

                if ($row['_to'] == "" || $row['_to'] == "-999")
                {
                    $data[$i]['chat'] = $row['chat'];
                    $data[$i]['session_id'] = $row['_frm'];
                    
                    $pos = strrpos($row['chatter'], "Dr.");
                    if ($_REQUEST['session_id'] == $row['_frm'])
                                            $data[$i]['icon'] = "ic_chatonline_from.png";
                                        else if ($pos === false) { // note: three equal signs
                                            $data[$i]['icon'] = "ic_chat_student.png";
                                            $data[$i]['status'] = "Student";
                                  }
                                  else
                                  {
                                            $data[$i]['icon'] = "ic_chat_doctor.png";
                                      $data[$i]['status'] = "Doctor";
                                      
                                        }


                                    $data[$i]['chatter'] = $row['chatter'];
                    $data[$i]['time'] =  time_ago1($row['datetime']). "";
                }
                
                else if ($row['_to'] == "0" )
                {
                   $data[$i]['chat'] =   $row['chat'];
                                                           if ($pos === false) { // note: three equal signs
                                            $data[$i]['icon'] = "ic_chat_student.png";
                                                                $data[$i]['status'] = "Student";
                                  }
                                  else
                                  {
                                            $data[$i]['icon'] = "ic_chat_doctor.png";
                                      $data[$i]['status'] = "Doctor";
                                        }

                    $data[$i]['chatter'] = $row['chatter'];
                    $data[$i]['time'] =  time_ago1($row['datetime']). "";
                }
               $i++;
                
             }   
         }
        return $data;
        
     }
     
	 
	 /*Private Msg - 2 / 13 / 2014 */
	 function RecentPrivateMsg($clause, $limit)
    {
       $i = 0;
    $q =  "SELECT _id,msg chat,chatter,datetime,session_id_to _to,session_id _frm,iRead FROM `tbl_app_msg` " . $clause . " order by _id desc limit " . $limit  ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $heading = false;
             while($row = mysql_fetch_array($res))
             {
                   
					$data[$i]['Read'] = $row['iRead'];
					 if ($row['iRead'] == 0)
					 {
						$data[$i]['iRead']  = "msgprivate.png";
						$data[$i]['readstyle']  = "display:none";
					 }
					 else
					 {
						$data[$i]['iRead']  = "msgprivateopen.png";
						
					 }
					
					$data[$i]['_id'] = $row['_id'];
                    $data[$i]['chat'] = $row['chat'];
                    $data[$i]['session_id'] = $row['_frm'];
                    
                    $pos = strrpos($row['chatter'], "Dr.");
                    if ($_REQUEST['session_id'] == $row['_frm'])
                                            $data[$i]['icon'] = "ic_chatonline_from.png";
                                        else if ($pos === false) { // note: three equal signs
                                            $data[$i]['icon'] = "ic_chat_student.png";
                                            $data[$i]['status'] = "Student";
                                  }
                                  else
                                  {
                                            $data[$i]['icon'] = "ic_chat_doctor.png";
                                      $data[$i]['status'] = "Doctor";
                                      
                                        }


                                    $data[$i]['chatter'] = $row['chatter'];
                    $data[$i]['time'] =  time_ago1($row['datetime']). "";
          
                
				
				
				
               $i++;
                
             }   
         }
        return $data;
        
     }
	 
      function RecentUserDiscussionThread()
    {
                $i = 0;
        $q =  "SELECT chat,chatter,datetime,_to,_frm,_id FROM `tbl_app_chat` where _frm  = '".$_REQUEST['session_id']."' and _to = '0' group by chat order by _id desc limit 10" ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $heading = false;
             $RecentChat  = "<b><u>Your Posted</u></b><br>";
             while($row = mysql_fetch_array($res))
             {
            
                    $data[$i]['chat'] =   $row['chat'];
                    
                    $data[$i]['icon'] = "ic_chatonline_from.png";

                    $data[$i]['chatter'] = $row['chatter'];
                    $data[$i]['time'] =  time_ago1($row['datetime']);
                                            
               
                 $q1 =  "SELECT _id,chat,chatter,datetime,_to,_frm FROM `tbl_app_chat` where _to = '".$row['_id']."' order by datetime desc " ;
                 $res1 = mysql_query($q1);
                  $num_rows = mysql_num_rows($res1); 
                                    $j = 0;
                 if ($num_rows)
                 {
                     
                     while($row = mysql_fetch_array($res1))
                     {
                                $data[$i]['reply'][$j]['chat'] =   $row['chat'];
                                $data[$i]['reply'][$j]['_id'] =   $row['_id'];
                                $pos = strrpos($row['chatter'], "Dr.");
                                if ($_REQUEST['session_id'] == $row['_frm'])
                                                    {
                                                        $data[$i]['reply'][$j]['icon'] = "ic_chatonline_from.png";
                                                    }
                                                    else if ($pos === false) { // note: three equal signs
                                                        $data[$i]['reply'][$j]['icon'] = "ic_chat_student.png";
                                              }
                                              else
                                              {
                                                        $data[$i]['reply'][$j]['icon'] = "ic_chat_doctor.png";
                                                    }
            
                                $data[$i]['reply'][$j]['chatter'] = $row['chatter'];
                                $data[$i]['reply'][$j]['time'] =  time_ago1($row['datetime']);
                                                    $j++;                                                 
                     }
                 }
                
               $i++; 
             }   
         }
         return $data;
     }

     function DiscussionThreadForOthers()
     {
             $i = 0;
        $q =  "SELECT chat,chatter,datetime,_to,_frm,_id FROM `tbl_app_chat` where _frm  != '".$_REQUEST['session_id']."' and _to = '0' group by chat order by datetime desc limit 10" ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
         if ($num_rows)
         {
             $heading = false;
             $RecentChat  = "<b><u>Recent Discussion</u></b><br>";
             while($row = mysql_fetch_array($res))
             {
               
                 $data[$i]['_id'] =   $row['_id'];
                    $data[$i]['chat'] =   $row['chat'];
                                            $data[$i]['icon'] = "ic_chatonline_group.png";

                    $data[$i]['chatter'] = $row['chatter'];
                    $data[$i]['session_id'] = $row['_frm'];
                    $data[$i]['time'] =  time_ago1($row['datetime']);
                
               
                 $q1 =  "SELECT _id, chat,chatter,datetime,_to,_frm FROM `tbl_app_chat` where _to = '".$row['_id']."' order by datetime desc limit 4" ;
                 $res1 = mysql_query($q1);
                  $num_rows = mysql_num_rows($res1); 
                                $j = 0;                 
                 if ($num_rows)
                 {
                     while($row = mysql_fetch_array($res1))
                     {
                                $data[$i]['reply'][$j]['chat'] =   $row['chat'];
                                $data[$i]['reply'][$j]['_id'] =   $row['_id'];
                                $data[$i]['reply'][$j]['session_id'] =   $row['_frm'];
                                $pos = strrpos($row['chatter'], "Dr.");
                                if ($_REQUEST['session_id'] == $row['_frm'])
                                                        $data[$i]['reply'][$j]['icon'] = "ic_chatonline_from.png";
                                                    else if ($pos === false) { // note: three equal signs
                                                        $data[$i]['reply'][$j]['icon'] = "ic_chat_student.png";
                                              }
                                              else
                                              {
                                                        $data[$i]['reply'][$j]['icon'] = "ic_chat_doctor.png";
                                                    }
            
                                $data[$i]['reply'][$j]['chatter'] = $row['chatter'];
                                $data[$i]['reply'][$j]['time'] =  time_ago1($row['datetime']);
                                                    $j++;                                                 
                     }
                 }
                 else
                 {
                     /* $data[$i]['reply'][$j]['chat'] =   "<b><font color='#C0C0C0'> &#934; - Be the first to reply...</font></b><br>";
                    $pos = strrpos($row['chatter'], "Dr.");
                                        if ($_REQUEST['session_id'] == $row['_frm'])
                                            $data[$i]['reply'][$j]['icon'] = "ic_chatonline_from.png";
                                        else if ($pos === false) { // note: three equal signs
                                            $data[$i]['reply'][$j]['icon'] = "ic_chat_student.png";
                                  }
                                  else
                                  {
                                            $data[$i]['reply'][$j]['icon'] = "ic_chat_doctor.png";
                                        }

                    $data[$i]['reply'][$j]['chatter'] = $row['chatter'];
                    $data[$i]['reply'][$j]['time'] =  time_ago1($row['datetime']). "</font></i><br>";
                     */
                 }
               $i++;
                
             }   
         }
                  return $data;
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
         return "99";
     }
     /************** Remove Ads Status   **************/
     function RemoveAdsStatus()
     {
         return "$3.59";
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
     //02-08-2014 - Buynow Condition. To show BuyNow Alert or OpenBuy now on shifa home page load
     function BuyNow()
     {
        $q =  "SELECT data n from tbl_shifa_log where  session_id  = '".$_REQUEST['session_id']."' and  Name = 'Counter~Home~Remove~ads~Paid' " ;
        $res = mysql_query($q);
        $num_rows = mysql_num_rows($res);
        if ($num_rows)
        {
             $row = mysql_fetch_array($res);
             if ($row['n'] == "0")
             {
              $q =  "SELECT visit n from tbl_app_registration where  session_id  = '".$_REQUEST['session_id']."'" ;
                $res = mysql_query($q);
                $row = mysql_fetch_array($res);
                if ($row['n'] >= 25 && $row['n'] <= 40)
                {
                    return 1;
                }
                else if ($row['n'] >= 40)
                {
                    return 2;
                }
                
             }
             
        }
        return 0;
        
     }
   ?>