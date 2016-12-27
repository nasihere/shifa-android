<meta http-equiv="refresh" content="120;URL=rep.php"> 
<?php

    include_once('config.php');
        if (!$link) { 
        die('Could not connect: ' . mysql_error()); 
    } 
   
    //echo $q = 'UPDATE tbl_shifa SET selected = "0" where level = "0" and Name = "nasir"';
    
     function dateret($days)
     {
         $query = "SELECT CURDATE() - " .$days.";";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
   $curdat = $r[0];
     return $r[0] ;
     
         
     }
    $query = "SELECT CURDATE();";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
   $curdat = $r[0];
   
     $query = "SELECT count(*) FROM tbl_app_registration where personalinfo1 = 1  ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $todreg = $r[0];
     echo "Total Personal info added : </b> " . $r[0] ;
    echo "<hr>";
 
     
       $query = "SELECT count(*) FROM tbl_app_msg   ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $todreg = $r[0];
     echo "Total private msg sent :  </b> " . $r[0] ;
    echo "<hr>";
 
      
    
    
        $query = "SELECT count(*) FROM tbl_app_chat where datetime >= CURDATE() ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
    echo "<b  style='color: red'>Total Chat Over Today </b> " . $r[0] ;
    echo "<hr>";
 
         $query = "SELECT count(data) FROM tbl_shifa_log where Name = 'Counter~Home~Remove~ads' and data != 0 ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     echo "<b  style='color: blue'>Buy Now option clicked: </b> " . $r[0];
    echo "<hr>";     

     $query = "SELECT count(data) FROM tbl_shifa_log where Name = 'Counter~Home~Remove~ads' and lastvisitdatetime >=CURDATE()-1 and lastvisitdatetime <=CURDATE()and session_id !='123456789' ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     echo "<b  style='color: blue'>Buy Now option clicked Yesterday: </b> " . $r[0];
    echo "<hr>";   
     
        $query = "SELECT count(data) FROM tbl_shifa_log where Name = 'Counter~Home~Remove~ads' and  lastvisitdatetime >= CURDATE() and session_id != '123456789'";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     echo "<b  style='color: blue'>Buy Now option clicked Today: </b> " . $r[0];
    echo "<hr>";   
     
     $query = "SELECT count(*) FROM tbl_shifa_log where Name = 'Counter~Home~Remove~ads~Paid' and data != 0 ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
    echo "<b  style='color: blue'>Total in app Paid </b> " . ($r[0]-3287);
    echo "<hr>";       
     
     $query = "SELECT count(*) FROM tbl_app_pms_report";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     $totreg = $r[0];
    echo "<b>Total PMS REPORT </b>" . $r[0] ;
    
     
      echo "<hr>";
 
     
     $query = "SELECT count(*) FROM tbl_app_registration ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     $totreg = $r[0];
    echo "<b>Total Registration  </b>" . $r[0] ;
    
     
     echo "<hr>";
 
   $query = "SELECT count(*) FROM tbl_app_registration where ProfilePic != ''    ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $todreg = $r[0];
     echo "<b style='color: blue'>Facebook Registration ".dateret(0)." : </b> " . $r[0] ;
    echo "<hr>";
     
	 $query = "SELECT ProfilePic,fname FROM tbl_app_registration where ProfilePic != ''   order by _id desc ";
    $result = mysql_query($query);
    while($r= mysql_fetch_array($result))
	{
       echo "<img src='".$r[0]."' />".$r[1].", ";
	}
     
    echo "<hr>";
     
     
     
      $query = "SELECT count(*) FROM tbl_app_registration where datetime >= CURDATE()    ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $todreg = $r[0];
     echo "<b style='color: red'>Total Registration ".dateret(0)." : </b> " . $r[0] ;
    echo "<hr>";
     
     
     
     $query = "SELECT count(*) FROM tbl_app_registration where datetime >= CURDATE()  - 1 and datetime <= CURDATE() ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $yesreg = $r[0];
     echo "<b style='color: red'>Total Registration ".dateret(1)." : </b> " . $r[0] ;
    echo "<hr>";
     
      $query = "SELECT count(*) FROM tbl_app_registration where datetime >= CURDATE() - 2 and datetime <= CURDATE() - 1  ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $yesreg1 = $r[0];
     echo "<b  style='color: red'>Total Registration ".dateret(2) .": </b> " . $r[0] ;
    echo "<hr>";
      
      $query = "SELECT count(*) FROM tbl_app_registration where datetime >= CURDATE() - 3 and datetime <= CURDATE() - 2  ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
       $yesreg2 = $r[0];
     echo "<b  style='color: red'>Total Registration ".dateret(3).": </b> " . $r[0] ;
    echo "<hr>";
      
     
     
      $query = "SELECT sum(visit) FROM tbl_app_registration where session_id not in ('000000000000007','1111111100000') ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
      $totvis = $r[0];
     echo "<b>Total Visitors: <b> " . $r[0] ;
    echo "<hr>";
  
     
     
     $query = "SELECT count(*) FROM tbl_app_registration where lastvisitdatetime >= CURDATE()    and  session_id not in ('000000000000007','1111111100000')";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     $todvis = $r[0];
     echo "<b style='color: green'>Total Visitors ".dateret(0) .": : </b> " . $r[0] ;
    echo "<hr>";   

$query = "SELECT count(*) FROM tbl_app_registration where  lastvisitdatetime between  CURDATE()  - 1 and  CURDATE()  and  session_id not in ('000000000000007','1111111100000')";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     $yesvis = $r[0];
     echo "<b style='color: green'>Total Visitors ".dateret(1) .": : </b> " . $r[0] ;
    echo "<hr>";

     
      $query = "SELECT count(*) FROM tbl_app_registration where  lastvisitdatetime between  CURDATE() - 2 and CURDATE() - 1 and  session_id not in ('000000000000007','1111111100000')";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     $yesvis1 = $r[0];
     echo "<b style='color: green'>Total Visitors ".dateret(2) .": : </b> " . $r[0] ;
    echo "<hr>";
     
     $query = "SELECT count(*) FROM tbl_app_registration where lastvisitdatetime between CURDATE() - 3 and   curdate()  - 2 and  session_id not in ('000000000000007','1111111100000')";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     $yesvis2 = $r[0];
     echo "<b style='color: green'>Total Visitors  ".dateret(3) .": : </b> " . $r[0] ;
    echo "<hr>";
     
     
     $query = "SELECT count(*) FROM tbl_app_registration where visit = 1 ";
    $result = mysql_query($query);
    $r= mysql_fetch_array($result);
     
     echo "<b>One time visitor  : </b> " . $r[0] ;
    echo "<hr>";
     
     
     ?>








<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">

      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);
 google.setOnLoadCallback(drawChart1);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        data.addRows([
          ['Today', <?php echo $todreg; ?>],
          ['Yesterday', <?php echo $yesreg; ?>],
          ['Before Yesterday', <?php echo $yesreg1; ?>],
            ['Before Before Yesterday', <?php echo $yesreg2; ?>]
        ]);

        // Set chart options
        var options = {'title':'Registration',
                       'width':400,
                       'height':300};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
        
        
         function drawChart1() {

        // Create the data table.
        var data = new google.visualization.DataTable();
     