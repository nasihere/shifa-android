 <?php

     
      include_once('config.php');
           if (!$link) { 
        die('-999'); 
     } 
     $p = $_REQUEST;
    
	
	////////////////////////// If delete_id only will be available when user click on delete from UI
	if ($_REQUEST['delete_id'] != "")
	{
		if ($_REQUEST['session_id'] == '001') 
		{
			echo "Sorry! You can't remove this record..";
			
		}
		else
		{
			// will load the _id and delete it for th list
			$q =  "delete  FROM `tbl_app_pms_report` where _id = " . $_REQUEST['delete_id'] . " and session_id = '" . $_REQUEST['session_id'] . "'";
			$res = mysql_query($q);
     	}
	}
		
     $q =  "SELECT * FROM `tbl_app_pms_report` where (session_id = '".$_REQUEST['session_id']."' or session_id = '001' )  group by contact  order by _id desc limit 150";
     $res = mysql_query($q);
     
     
     
      mysql_close($link);   
     
     function GetVal($qry)
     {
        
    include_once('config.php');
    
          if (!$link) { 
              die('-999'); 
          } 
          
          
          mysql_select_db(shifakent); 
          $res_1 = mysql_query($qry);
          $value= mysql_fetch_row($res_1);
          mysql_close($link); 
          return $value[0];
      }
     
 ?>


 <head>
    <link href="css/main.css" rel="stylesheet">

    </head>
          

    <body style="background:#f2f2f2">
 <div class="dashboard-wrapper">
     <h5>If you have any suggestion for how to improve Patient Management System please feel free to email me at nasihere@gmail.com</h5>

        <div class="main-container">
        
            
              <div class="row-fluid">
            
            <div class="span4">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="ÃÂÃÂÃÂÃÂ®ÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ£"></span> Recent Patient List
                  </div>
                </div>
                <div class="widget-body">
                  <div id="dt_example" class="example_alt_pagination">
                    <table class="table table-condensed table-striped table-hover table-bordered" id="data-table">
                      <thead>
                        <tr>
                          <th style="width:35%">
                            Name, Contact & Time
                          </th>
                          <th style="width:65%">
                            Contact
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                         <?php while ($rowReport = mysql_fetch_array($res))   { 
     $pms_sym = GetVal('select symptoms from tbl_app_pms_report where contact = "'. $rowReport['contact'] .'" order by _id desc');
    $pms_comment= GetVal('select comment from tbl_app_pms_report where contact = "'. $rowReport['contact'] .'" order by _id desc');
    $pms_filename = GetVal('select filename from tbl_app_pms_report where contact = "'. $rowReport['contact'] .'" order by _id desc');

    
    ?>
                          <tr class="gradeA success">
                              <td style="style='font-size: small;'"><?php echo $rowReport['filename']; ?><br><?php echo $rowReport['contact']; ?><br><?php echo time_ago1($rowReport['datetime']); ?></td>
                              <td>
                                  <span>
								  <a target="_self" href="APP_KENT_REPORT#-#<?php echo $pms_sym; ?>#-#<?php echo $pms_filename; ?>#-#<?php echo $pms_comment; ?>#-#<?php echo $rowReport['contact']; ?>" role="button" class="btn btn-small  btn-primary" data-toggle="modal" data-original-title="">Ammend</a> / 
								  <a href="app_kent_report_view.php?contact=<?php echo $rowReport['contact']; ?>" role="button" class="btn btn-small  btn-primary" data-toggle="modal"data-original-title="">View</a> / 
								  <a href="app_kent_report_index.php?delete_id=<?php echo $rowReport['_id']; ?>&session_id=<?php echo $rowReport['session_id']; ?>" role="button" class="btn btn-small  btn-primary" data-toggle="modal"data-original-title="">Delete</a>
								  </span>
								  
                          </td>
                          </tr>
                          <?php } ?>
                      </tbody>
                    </table>
                    <div class="clearfix"></div>
                  </div>
                </div>
              </div>
            </div>
     </dov>
            
            
            
            
            
            
            
            
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