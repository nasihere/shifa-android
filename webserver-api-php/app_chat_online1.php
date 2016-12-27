<?php
    if ($_REQUEST['version'] == "") return;

    include("app_login_lastvisit.php");
    include("app_functions.php");
    include("app_tbl_shifa_log.php");
    
    print ChatStatusMessage(). "-:-" . "" . "-:-" .FlashMessage() . "-:-" . GoogleAds() . "-:-" . GoogleAdsRemoteURL() . "-:-".FreshStatus()."-:-" . AppUpGradeVersionNumber()  . "-:-".AppUpGradeVersionNumber()."-:-".RemoveAdsStatus() . "-:-" . DiscussionStatus(). "-:-" . PrivateMsgStatus(); 
    die;    

    

    ?>
