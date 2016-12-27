 <?php
     header('Location: http://kent.nasz.us/app_php/shifa-homepage.php?ver=1&session_id='.$_REQUEST['session_id']); 
    include("app_functions2.php");
    $dataChat = RecentChatDiscussion(" where _to = '-999' or _to = ''", "13") ;
    $dataPrivateMsg = RecentPrivateMsg(" where session_id_to  = '".$_REQUEST['session_id']."'", "13") ;

    
        $dataDiscussion   =  RecentUserDiscussionThread();
        $dataDiscussionOther = DiscussionThreadForOthers();
    $dataChatOnline = Online_User_List();
     
     include("../../backup_shifahomepagehtml.html");
 
    
?>
 <?php 
     /*
    if ($_REQUEST['ver'] == "")
        $replystyle = "display:none"; 
    else
        $replystyle = "color:#428bca;float:right"; 
    $session_name = CheckUserName();
     
     function AddFriend($session_id,$position)
     {
    
         $acceptSent = Online_User_History("Select accept from tbl_app_contacts where friend_session_id   = '". $session_id."' and session_id = '".$_REQUEST['session_id']."'","1");
         if ($acceptSent != "2") {  
             $accept = Online_User_History("Select accept from tbl_app_contacts where session_id  = '". $session_id."' and friend_session_id = '".$_REQUEST['session_id']."'","1");
             
             if ($accept == "2") {
                 echo '<a  style="float:'.$position.'" class="ancAddFriend-'.$session_id.'" onclick="javascript:FriendRequest(\''.$session_id.'\',1)"><i class="icon-signal"></i>Confirm Friend</a>';
                 
             }
             else if ($accept == "1") {
               echo '<a  style="float:'.$position.'" class="ancAddFriend-'.$session_id.'" onclick="javascript:FriendRequest(\''.$session_id.'\',0)"><i class="icon-signal"></i>Delete Friend</a>';
                
                 
                 
             }
             else
             {
                 echo '<a  style="float:'.$position.'" class="ancAddFriend-'.$session_id.'" onclick="javascript:FriendRequest(\''.$session_id.'\',2)"><i class="icon-signal"></i>Add Friend</a>';
             }
         }   
         else
         {
               echo '<a  style="float:'.$position.'" class="ancAddFriend-'.$session_id.'" ><i class="icon-signal"></i>Friend Request Already Sent</a>';
         }
         
     }
   ?> 
<!--<head>
    <link href="css/main.css" rel="stylesheet">
        <script>
            function doFirst(){
                articles = document.getElementsByClassName('replydiscussion');
                for (var i = 0; i < articles.length; i++) {
                    articles[i].onclick = function() {
                        
                        var id = "discussionreply-" + this.getAttribute("data-brand");
                        document.getElementById(id).style.display="block";
                        
                                                
                        
                        
                    }
                }
            }
            function SendDiscussion(id)
            {
                var chat =   document.getElementById("txtdiscussionreply-"+id).value;
                if (chat == "") return;
                
                var to =   id;
                var chatter  =   document.getElementById("chatter").value;
                var _frm =   document.getElementById("session_id").value;
                if (_frm == "123456789") 
                {
                    alert("Demo account doesn't have rights to reply. Please register yourself");
                    return;
                }         
                
                
                var xmlhttp;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp=new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange=function()
                    {
                        if (xmlhttp.readyState==4 && xmlhttp.status==200)
                        {
                            if (id == "-999")
                            {
                                location.reload();
                            }
                            else
                            {
                                
                            }
                            //response
                            // document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
                        }
                    }
                xmlhttp.open("POST","pp_chat2.php",true);
                xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                xmlhttp.send("chat="+chat+"&_frm="+_frm + "&to="+to + "&chatter="+chatter);
                document.getElementById("txtdiscussionreply-"+id).disabled=true;
                document.getElementById("btndiscussionreply-"+id).style.display="none";
           
               
            }
            function FriendRequest(fSessionId,Accept)
            {
                //addFriend.php?session_id=&friend_session_id=&friend_name="
                
                var SessionId =   document.getElementById("session_id").value;
                var  articles = document.getElementsByClassName('ancAddFriend-'+ fSessionId);

                var xmlhttp;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp=new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange=function()
                    {
                        if (xmlhttp.readyState==4 && xmlhttp.status==200)
                        {
                            
                            for (var i = 0; i < articles.length; i++) {
                                articles[i].style.display="none";
                        
                               
                                
                            }
                        
                        }
                    }
                xmlhttp.open("POST","addFriend.php",true);
                xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                xmlhttp.send("session_id="+SessionId+"&friend_session_id="+fSessionId+"&accept="+Accept);
               
           

            }
            function AjaxRequestNoResponse(URL,Parameter)
            {
                
                var xmlhttp;
                if (window.XMLHttpRequest)
                {// code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp=new XMLHttpRequest();
                }
                else
                {// code for IE6, IE5
                    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange=function()
                {
                    if (xmlhttp.readyState==4 && xmlhttp.status==200)
                    {
                        
                    }
                }
                xmlhttp.open("POST",URL,true);
                xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                xmlhttp.send(Parameter);
               
                
            }
                
            </script>
    </head>
    <body onload="doFirst()" style="background:#f2f2f2">
        
        <input type="hidden" value="<?php echo $_REQUEST['session_id']; ?>" id="session_id"  >
        <input type="hidden" value="<?php echo $session_name; ?>" id="chatter" />
        
 <div class="dashboard-wrapper">
        <div id="dvtopmenu">
            <div>
                <span style="display: block;width: 30px;text-align: center;background:blue;color: white;"><?php echo   Online_User_History("Select count(*) from tbl_app_msg where iRead = 0 and session_id_to = '". $_REQUEST['session_id']."'","1"); ?></span>
                <img onclick="javascript:showprivatemsg()" src="shifa-fresh-homepage/mail.png" style="height:45px">
                
                <?php  $personalinfo1 =   Online_User_History("Select personalinfo1 from tbl_app_registration where session_id  = '". $_REQUEST["session_id"]."'","1"); ?></span>
                <?php if($personalinfo1 == 0) { ?>
                    <script>
                        var bPInfoWindowFlag = false;
                        function showPersonalInfoWindow()
                        {
                            if (bPInfoWindowFlag == false)
                            {
                                bPInfoWindowFlag = true;
                                document.getElementById("quickupdate_personalinfo1").style.display="";
                                window.scrollTo(0,document.getElementById("quickupdate_personalinfo1").offsetTop);
                            }
                            else
                            {
                                bPInfoWindowFlag = false;
                                document.getElementById("quickupdate_personalinfo1").style.display="none";
                            }
                        }
                    </script>
                    <img onclick="javascript:showPersonalInfoWindow()" src="shifa-fresh-homepage/profile.png" style="height:45px">
                <?php } ?>
            </div>
            
            
          
          
          
          <!-- PRIVATE MSG !--->
          
            
<!--                
                
                <div id="dvPrivateMsg" style="position:fixed;z-index:999;width:50%;background:#efefef;display:none" class="row-fluid">
                <script>
                    var bPMesgWindowFlag = false;
                    function showprivatemsg()
                    {
                        if (bPMesgWindowFlag == false)
                        {
                            bPMesgWindowFlag = true;
                            document.getElementById("dvPrivateMessagesWindow").style.display = "";
                            window.scrollTo(0,document.getElementById("dvPrivateMessagesWindow").offsetTop);
                        }
                        else
                        {
                            bPMesgWindowFlag = false;
                            document.getElementById("dvPrivateMessagesWindow").style.display = "none";
                        }
                    }
                            function sendPrivateMessage()
                            {
                            
                                var msg = document.getElementById("txtPrivateMesg").value;
                                var SessionId =   document.getElementById("session_id").value;
                                
                                var To_Name = document.getElementById("chatter").value;
                                var to_Id = document.getElementById("hidPrivateMsgToId").value;
                                
                                var Parameter = "session_id="+SessionId+"&session_id_to="+to_Id+"&msg="+msg+"&chatter="+To_Name;
                                AjaxRequestNoResponse("privatemsg/InsertPrivateMessage.php",Parameter);
                                document.getElementById("dvPrivateMsg").style.display = "none";
                                
                            }
                            
                            function openPrivateMessage(to_Id, To_Name)
                            {
                                
                                    document.getElementById("spnPrivateMsgTo").innerHTML = To_Name;
                                    document.getElementById("hidPrivateMsgToId").value = to_Id;
                                    document.getElementById("dvPrivateMsg").style.display = "block";
                                
                            }
                            function CancelPrivateMessage()
                            {
                                document.getElementById("dvPrivateMsg").style.display = "none";
                            }
                            function ActionMessage(type,_id)
                            {
                                var Parameter = "type="+type+"&_id="+_id;
                                AjaxRequestNoResponse("privatemsg/ActionPrivateMessage.php",Parameter);
                                
                                
                                
                                if(type == 1)
                                {
                                    document.getElementById("dvShowPrivateMsgConfirm-"+_id).style.display = "none";
                                    document.getElementById("smallShowPrivateMsg-"+_id).style.display = "block";
                                    document.getElementById("imgPrivateMsgSrc-"+_id).style.display = "none";
                                    document.getElementById("imgPrivateMsgSrcOpen-"+_id).style.display = "";
                                    
                                    
                                }
                                else
                                {
                                    document.getElementById("dvShowPrivateMsgGroup-"+_id).style.display = "none";
                                }
                                
                            }
                        </script>
                        
                        <h4>Private Message To <span id="spnPrivateMsgTo"></span></h4>
                        
              <form class="tweet-form no-margin">
                  <input type="hidden" id="hidPrivateMsgToId" />
                  <input type="text" style="float:left;" id="txtPrivateMesg" class="input-block-level" rows="2" placeholder="">
                  <input id="btndiscussionreply-0"type="button" class="btn btn-info pull-right" value="Send" onclick="sendPrivateMessage(0)">&nbsp;
                  <input id="btndiscussionreply-0" type="button" class="btn btn-info pull-right" value="Cancel" onclick="CancelPrivateMessage()">
                        <div class="clearfix"></div>
              </form>
            
          </div>
          
          
          
        </div>
        
        <div class="main-container">
             <?php  include("quickdataentry/quickdataentry_specialist_zipcode_contact.html"); ?>
           
            <?php  if ($_REQUEST['ver'] == "") { ?>
            <B>Note: </b> <i>To do reply of any chat and discussion though here, So please <a href="https://play.google.com/store/apps/details?id=com.shifa.kent&hl=en" style="color:#428bca">update</a> shifa app now from play store   </i>
            <?php } ?>

           
           
           
           
           
           
             <div class="row-fluid" style="display:none"; id="dvPrivateMessagesWindow">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="?"></span> Private Messages
                  </div>
                </div>
                
                <div class="widget-body">
                  <div id="scrollbar-two">
                    
                      <div class="viewport" style="height:477px">
                      <div class="overview">
                        <div id="chats">
                            <div style="height:564px;overflow:auto" class="tab-widget">
                            <ul class="chats">
                              <?php if ($dataPrivateMsg) { ?>  
                              <?php foreach($dataPrivateMsg as $value) { ?>
                              <li class="no-padding">
                              <div id="dvShowPrivateMsgGroup-<?php echo $value['_id'];?>" >
                                <div class="info no-margin">
                                    <img style="height: 25px;width: 25px;" src="shifa-fresh-homepage/<?php echo $value['icon']; ?>" alt="user">
                                    <img style="height: 15px;" src="shifa-fresh-homepage/<?php echo $value['iRead'];?>" id="imgPrivateMsgSrc-<?php echo $value['_id'];?>" alt="user" onclick="javascript:openPrivateMessage('<?php echo $value['session_id']; ?>','<?php echo $value['chatter']; ?>')">
                                    <img  onclick="javascript:openPrivateMessage('<?php echo $value['session_id']; ?>','<?php echo $value['chatter']; ?>')" style="height: 15px;display:none" src="shifa-fresh-homepage/msgprivateopen.png" id="imgPrivateMsgSrcOpen-<?php echo $value['_id'];?>" alt="user" >
                                    <span  class="name"><?php echo $value['chatter']; ?></span>: 
                                        <small id="smallShowPrivateMsg-<?php echo $value['_id'];?>" style="<?php echo $value['readstyle'];?>"><?php echo $value['chat']; ?></small>
                                        <?php if ($value['Read'] == 0) { ?>
                                        <div id="dvShowPrivateMsgConfirm-<?php echo $value['_id'];?>" >
                                            <small> Sent you a new message, Click Yes to show the message or No to delete the message?</small>
                                            <span><a href="javascript:ActionMessage(1,<?php echo $value['_id'];?>)" class="replydiscussion" > Yes </a></span>
                                            <small> or </small>
                                                <span><a  href="javascript:ActionMessage(2,<?php echo $value['_id'];?>)" class="replydiscussion" > No </a></span>
                                            </div>
                                        <?php } ?>
                                            
                                    
                                </div>
                                </div>
                              </li>
                           <? } ?>
                                <? } ?>
                            </ul>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          
          
          
          
          
          
           
           
           
           
           
           
           
           
           
           
           
           
           <div class="row-fluid">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="?"></span> Recent Messages
                  </div>
                </div>
                  <?php if  ($_REQUEST['ver'] != "") { ?>
                   <form class="tweet-form no-margin">
                       <input type="text" id="txtdiscussionreply--999" class="input-block-level" rows="2" placeholder="@<?php echo $session_name; ?>: Reply here"></input>
                        <input id="btndiscussionreply--999" type="button" class="btn btn-info pull-right" value="Send" onclick="SendDiscussion(-999)">
                        <div class="clearfix"></div>
                                </form>
                  <?php } ?>
                <div class="widget-body">
                  <div id="scrollbar-two">
                    
                      <div class="viewport" style="height:477px">
                      <div class="overview">
                        <div id="chats">
                            <div style="height:564px;overflow:auto" class="tab-widget">
                            <ul class="chats">
                              <?php if ($dataChat) { ?>  
                              <?php foreach($dataChat as $value) { ?>
                              <li class="no-padding">
                                <div class="info no-margin">
                                    <img style="height: 25px;width: 25px;" src="shifa-fresh-homepage/<?php echo $value['icon']; ?>" alt="user">
                                    <img style="height: 15px;" src="sendcontactinfo/imgs/msgprivate.png" alt="user" onclick="javascript:openPrivateMessage('<?php echo $value['session_id']; ?>','<?php echo $value['chatter']; ?>')">
                                    <span class="name"><?php echo $value['chatter']; ?></span>: <small><?php echo $value['chat']; ?></small>
                                    <?php // Add Friend Code is ready just need to disable the comment  // echo AddFriend($value['session_id'],"Right"); ?>
                                    <?php // Send Contact Info   ?>
                                    
                                </div>
                              </li>
                           <? } ?>
                                <? } ?>
                            </ul>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

            
            
            
            
              <?php if  ($_REQUEST['ver'] != "") { ?>
          
            <div class="row-fluid">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                      <span class="fs1" aria-hidden="true" data-icon="?"></span> What's on your mind for discussion? <a style="color:red">(New)</a>
                  </div>
                </div>
                <div class="widget-body">
                                
              <form class="tweet-form no-margin">
                  <input type="text" id="txtdiscussionreply-0" class="input-block-level" rows="2" placeholder="Example: Weak memory causing alot of problem..."></input>
                        <input id="btndiscussionreply-0" type="button" class="btn btn-info pull-right" value="Send" onclick="SendDiscussion(0)">
                        <div class="clearfix"></div>
                                </form>
            
                </div>
              </div>
            </div>
          </div>
            <?php } ?>
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            <?php if ($dataDiscussion) { ?>
          <div class="row-fluid">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="?"></span> Your Recent Discussion
                  </div>
                </div>
                <div class="widget-body">
                  <ul class="tweets tweet-group">
                      <?php if ($dataDiscussion) { ?>  
                   <?php foreach($dataDiscussion as $value) { ?>
                    <li class="in">
                        
                      <div class="message no-margin">
                          <img class="avatar" style="height: 25px;width: 25px;" alt="" src="shifa-fresh-homepage/<?php echo $value['icon']; ?>">
                        <div>
                            <p class="no-margin"><span  class="name"><?php echo $value['chatter']; ?></span>: <span class="designation"><?php echo $value['chat']; ?></span></p><small>at <?php echo $value['time']; ?></small>
                        </div>
                        <p class="body no-margin">
                         
                        </p>
                        <div class="clearfix"></div>
                      </div>
                    </li>
                      <?php if ($value['reply']) { ?>
                   <?php foreach($value['reply'] as $valuereply) { ?>
                    <li class="out">
                      <div class="message no-margin">
                         <img class="avatar"  style="height: 25px;width: 25px;" alt="" src="shifa-fresh-homepage/<?php echo $valuereply['icon']; ?>">

                        <span class="up-arrow"></span>
                        <span class="arrow"></span>
                          <p class="no-margin"><span  class="name">@<?php echo $valuereply['chatter']; ?></span>: <span  class="designation"><?php echo $valuereply['chat']; ?></span></p><small class="date-time"> at <?php echo $valuereply['time']; ?></small>
                      </div>
                    </li>
                      <?php } ?>
                      <? } } } ?>
                  </ul>
                </div>
              </div>
            </div>
          </div>
            <? }  ?>
          
          
          

          <div class="row-fluid">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="?"></span> Recent Discussion
                  </div>
                </div>
                <div class="widget-body">
                  <ul class="tweets tweet-group">
                      <?php if ($dataDiscussionOther) { ?>  
                   <?php foreach($dataDiscussionOther as $value) {?>
                    <li class="in">
                        
                      <div class="message no-margin">
                          <img class="avatar" style="height: 25px;width: 25px;" alt="" src="shifa-fresh-homepage/<?php echo $value['icon']; ?>">
                        <div>
                            <p class="no-margin"><span  class="name"><?php echo $value['chatter']; ?></span>: <span class="designation"><?php echo $value['chat']; ?></span> <small>
                                
                                <span style="<?php echo $replystyle; ?>" class="replydiscussion" data-brand="<?php echo $value['_id']; ?>" >Reply</span>
                             
                                </small></p>
                               
                            <?php  // Add Friend Code is ready just need to disable the comment  //echo AddFriend($value['session_id'],"Left"); ?>
                            <div id="discussionreply-<?php echo $value['_id']; ?>" style="display:none"> <form class="tweet-form no-margin">
                        <input type="text" id="txtdiscussionreply-<?php echo $value['_id']; ?>" class="input-block-level" rows="2" placeholder="Reply to @<?php echo $value['chatter']; ?>"></input>
                        <input id="btndiscussionreply-<?php echo $value['_id']; ?>" type="button" class="btn btn-info pull-right" value="Send" onclick="SendDiscussion(<?php echo $value['_id']; ?>)">
                        <div class="clearfix"></div>
                                </form></div>
                        </div>
                        <p class="body no-margin">
                         
                        </p>
                        <div class="clearfix"></div>
                      </div>
                    </li>
                      
                   <?php 
if ($value['reply'] ) {
foreach($value['reply'] as $valuereply) { ?>
                    <li class="out">
                      <div class="message no-margin">
                         <img class="avatar"  style="height: 25px;width: 25px;" alt="" src="shifa-fresh-homepage/<?php echo $valuereply['icon']; ?>">
                          <?php // Add Friend Code is ready just need to disable the comment  // echo AddFriend($valuereply['session_id'],"Right"); ?>
                        <span class="up-arrow"></span>
                        <span class="arrow"></span>
                          <p class="no-margin"><span  class="name">@<?php echo $valuereply['chatter']; ?></span> (<?php echo   Online_User_History("Select count(*) from tbl_app_chat where _frm = '". $valuereply['session_id']."'","1"); ?>): <span  class="designation"><?php echo $valuereply['chat']; ?></span></p><small class="date-time"> at <?php echo $valuereply['time']; ?></small>
                      </div>
                    </li>
                      <? } } } } ?>
                  </ul>
                </div>
              </div>
            </div>
          </div>

     
          
          
          
          <div class="row-fluid">
            

            <div class="span4">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="?"></span> Recent Login
                  </div>
                </div>
                <div class="widget-body">
                  <div class="tab-widget">
                    <ul class="signups">
                    <?php if ($dataChatOnline) { 
                    foreach($dataChatOnline as $value){ ?>
                      <li class="no-padding">
                       
                        <div class="no-margin">
                            <p class="no-margin"><img style="height: 25px;width: 25px;" src="shifa-fresh-homepage/<?php echo $value['icon']; ?>" alt="user"><span style="color:#428bca" class="name"><?php echo $value['chatter']; ?></span> - <span class="designation"><?php echo $value['status']; ?></span></p>
                          <small>Last Logged In: <?php echo $value['time']; ?></small>
                          <br><small>Profile Created : <?php echo $value['regdate']; ?></small>  
                            <br><small>Helped Others : <?php echo   Online_User_History("Select count(*) from tbl_app_chat where _frm = '". $value['session_id']."'"); ?> </small>  
                            <br><small><?php  echo AddFriend($value['session_id'],""); ?></small>  
             
                        </div>
                      </li>
                   <? } } ?>
                    </ul>
                  </div>
                </div>
              </div>
            </div>

            
          </div>
          

          
          
          
          
          
          
          
          
          
          
          
          
                
                
                
                
                

        </div>
      </div>
 
</body>