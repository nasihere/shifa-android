 <?php
     include_once('config.php');
     if (!$link) { 
        die('-999'); 
     } 
     $p = $_REQUEST;
     include("app_chat_online1.php");

     $q =  "SELECT distinct count(_id) as count FROM `tbl_app_registration` WHERE lastvisitdatetime BETWEEN (DATE_SUB(NOW(),INTERVAL 30 MINUTE)) AND NOW()";
     
     $res = mysql_query($q);
     $num_rows = mysql_num_rows($res);
     if ($num_rows)
     {
         $row = mysql_fetch_array($res);
         $numberUser = $row['count'];
     }
     else
     {
         mysql_close($link);              
         print "-:--:--:--:-";
         return;
     }
   
     $q =  "SELECT chat,chatter,datetime FROM `tbl_app_chat` where  _frm != '000000000000007' order by _id desc limit 1";
     $res = mysql_query($q);
     $num_rows = mysql_num_rows($res);
     if ($num_rows)
     {
        $RecentChat .= "<b>Recent Chat:</b><br>";
         while($row = mysql_fetch_array($res))
         {
             $RecentChat .=  $row['chat'] . "<i><font color='#6297BC'> - " . $row['chatter']. "</font></i><i><font color='#ff2500'> - " .time_ago($row['datetime']). "</font></i><br>";
         }   
     }
     $RecentMsg1 = $RecentChat;
     
     $RecentMsg1 = "<font color='red'> Shifa update is up now. </font><b>Recommend: Please relogin for new changes ..</b> ";
     $RecentMsgLine2 = "";
          $onlineFlash =  $RecentMsg1 . $RecentMsgLine2; // Online Flash with Recent Chat and Line two 
    
     //000000000000007 - nasir
     //1111111100000 - nazim 
    
     print $numberUser . "-:-" . "4 New" . "-:-" .$onlineFlash . "-:-" . "ads". "-:-" . "http://shifa.nasz.us" . "-:-5 News <br> 4 Events-:-15 days ago" . "-:-21-:-Donate" . "-:- 5 New Post <br>2 Replied"; 
     
     mysql_close($link);      

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     /*
     ************* This will block user to use our app and show notification and adverstiment very badly *****
     print $numberUser . "-:-" . "2 New" . "-:-" .$onlineFlash . "-:-" . "adsremote". "-:-" . "http://shifa.nasz.us";
     /***
     Parameter: 1 - No. Of User Online
     Parameter: 2 - Caption for events 
     Parameter: 3 - Google Ads Show or hide if text ads
     Parameter: 4 - Redirect to othersite forcefully example block user if text adsremote
     Parameter: 5 - URL to redirect every three second from home page
     Parameter: 6 - Fresh Status
     Parameter: 7 - My Profile Status
     Parameter: 8 - App Updated Version for alert
     Parameter: 9 - Disucssion Status
     */
     
     
     
     
     
     
     
     
function time_ago($date)
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