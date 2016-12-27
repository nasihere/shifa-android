<?php
     include("app_login_lastvisit.php");
    include("app_functions2.php");
    include("app_tbl_shifa_log.php");
    $dataChat = RecentChatDiscussion(" where _to = '-999' or _to = ''", "20") ;
    $dataPrivateMsg = RecentPrivateMsg(" where session_id_to  = '".$_REQUEST['session_id']."'", "13") ;
        $dataDiscussion   =  RecentUserDiscussionThread();
        $dataDiscussionOther = DiscussionThreadForOthers();
    $dataChatOnline = Online_User_List();
    $buynow = BuyNow();
   
    include ("shifa-homepage.html");
 
    
?>
