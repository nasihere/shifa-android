 <?php
     
     function get_data($url,$filename) {
        
    $ch = curl_init();
    $timeout = 5;
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, $timeout);
    $data = curl_exec($ch);
    curl_close($ch);
        
         $rem = $_REQUEST['rem'];
         $rem = str_replace("-","_", $rem );

          $ind1 = strpos($data,'<a name="'.strtoupper($rem).'"></a>',0);
         $ind2 = strpos($data,'#808080',0);
       $data =    substr($data,$ind1,($ind2-$ind1-1));
      
         
         $ALLOWABLE_TAGS = '<font>';
         //   $data= strip_tags($data,$ALLOWABLE_TAGS);
        $data=    str_replace("ff0000","0968A3", $data);
         
         
           $data= str_replace("-","_", $data);
         $data= str_replace("'","", $data);
         
         echo  $data;
        
         if ($_REQUEST['force'] == 1)
         {
          $u = "insert into tbl_rem_info (rem,data) values ('". strtolower ($filename) ."','".$data."')";
                  mysql_query($u);
             //  echo "<hr>";
           $u = "update tbl_rem_info set  data = '".$data."' where rem =  '". $filename ."'";
         }
         else
         {
         $u = "insert into tbl_rem_info (rem,data) values ('". strtolower ($filename) ."','".$data."')";
         }
     mysql_query($u);
        
   
    return $data;
}


     include_once('config.php');
     
    if (!$link) { 
         print "-999"; 
        die;
    } 
    $p = $_REQUEST;
     
  $q =  "select * from tbl_rem_info where rem = '" . $p['rem'] . "'";
     
   $res =  mysql_query($q);
     if (!$res) {
    mysql_close($link); 
    print "-999"; 
    
}
     if (mysql_num_rows($res) == 0 || $p['force'] == 1)
     {
        
         $file = $p['rem'];
           $alpha = substr($file,0,1);
     $f =  strtolower ("http://www.homeoint.org/books/boericmm/".$alpha ."/".$file . ".htm") ;
       
         $content = get_data($f, $p['rem']);

     }
     else
     {
         $data =  mysql_fetch_array($res);
          echo  $data['data'];
     }
     
      $q =  "UPDATE tbl_rem_info set hits = hits + 1 where rem = '" . $p['rem'] . "'";
     
      $res =  mysql_query($q);
 
    mysql_close($link); 
    
   
?>