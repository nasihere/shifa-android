  <?php
     
     
     
    include_once('config.php');
     if (!$link) { 
        die('-999'); 
     } 
     $p = $_REQUEST;
    

   
     
    $q =  "SELECT * FROM `tbl_app_pms_report` where contact = '".$_REQUEST['contact']."' order by datetime desc limit 10";
     
     $res = mysql_query($q);
     
     
     
     
     
      mysql_close($link);   
     
          
     
 ?>


 <head >
    <link href="css/main.css" rel="stylesheet">

    </head>
    <body style="background:#FFFFFF">
 <div class="dashboard-wrapper">
        

        <div class="main-container">

       
            
            
            
            
            
            
            <div class="row-fluid">
            <div class="span12">
              <div class="widget no-margin">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="Ã®ÂÂ"></span> Overview
                  </div>
                </div>
                <div class="widget-body">
                  <ul class="imp-messages">
                      
                      
                      
                      
                       <?php while ($rowReport = mysql_fetch_array($res))   { ?>
                      <?php   $table =  str_replace("<table", "<table class='table table-condensed table-bordered no-margin' ", $rowReport['apphtml']);  ?>
                      
                      
                      
                      
                    <li>
                      <img src="shifa-fresh-homepage/ic_chat_doctor.png" class="avatar" alt="Avatar">
                      <div class="message-date">
                        <h7 class="date text-info"><?php echo time_ago1($rowReport['datetime']); ?></h7>
                        <p class="month"><?php echo $rowReport['datetime']; ?></p>
                      </div>
                      <div class="message-wrapper">

                          <h4 class="message-heading"> <b>Contact: </b><?php echo $rowReport['contact']; ?></h4>
                          <h6 class="message-heading"> <b>Age/Sex/Name: </b><?php echo $rowReport['filename']; ?></h6>
                        <blockquote class="message"><?php echo $rowReport['comment']; ?></blockquote>
                        <br>
                        <p class="url">
                          <span class="fs1 text-info" aria-hidden="true" data-icon="Ã®ÂÂ"></span>
                          <a href="#" data-original-title=""></a>
                        </p>
                      </div>
                           <?php echo $table; ?><br>
                    </li>
                    
                        <?php } ?>
                      
                      
                      
                      
                      
                      
                      
                  </ul>
                </div>
              </div>
            </div>
          </div>
            
            
            
            
            
            
            
            
            
            
            
            
           

                          <!------------------------------ End  !-->
        </div>
      </div>
        
        
        
        
        
        
        
        
        
        
        
        
        <?php
        
        
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