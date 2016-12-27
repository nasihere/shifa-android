   <?php
		$p['Page'] = $p['Page'] - 1;
		if ($p['Search'] != "")
		{
			$words = explode(' ', $p['Search']);
			$sql =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where ";// chat like '%".$p['Search']. "%'  order by _id desc limit " . ($p['Page'] * 10) . ", 10";
			$sql_end = '';
			foreach($words as $word) {
				$sql_end .= " AND chat LIKE '%{$word}%'";
			}
			$sql_end = substr($sql_end, 4);
			$sql = $sql.$sql_end;
			$q = $sql."  order by _id desc limit " . ($p['Page'] * 10) . ", 10";
		}
		else if ($p['forumId'] != "" && $p['forumId'] != "0")
		{
			$q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where  _id = ".$p['forumId']."  order by _id ";
		}
       else if ($p['Page'] == "" || $p['Page'] == "0" ) 
	   {
				$q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _to = '0'  and chat != '' and _id != '' and _frm != '' order by _id desc limit 10";
			$p['Page'] = 1;
		}
		else {
        	 $q =  "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _to = '0'  and chat != '' and _id != '' and _frm != '' order by _id desc limit " . ($p['Page'] * 10) . ", 10";
       }
     
	 
		$res = mysql_query($q);
		
		
         while($row = mysql_fetch_array($res))
         {
					print $row['_id'] . "-,-";
					print $row['_frm'] . "-,-";
					print $row['chat'] . "-,-";
					print $row['chatter'] . "-,-";
					if ($row['_to'] == "") $row['_to'] = "-999";
					print $row['_to'] .  "-,-";
					print date("M d, H:m", strtotime($row['datetime'])) .  "-,-";
												
					$innerQ = "SELECT count(*) FROM tbl_app_chat where _to = '".$row['_id']."'";
					$resQ = mysql_query($innerQ);
					while($row1 = mysql_fetch_array($resQ))
					{
						print $row1[0] . "-,-";
						print "Image" . "-:-";
						
					}
					
					if ($p['forumId'] != "" && $p['forumId'] != "0")
					{
					
								$innerQ = "SELECT chat,_id,_frm,chatter,_to,datetime FROM tbl_app_chat where _to = '".$row['_id']."'";
								$resQ = mysql_query($innerQ);
								while($row1 = mysql_fetch_array($resQ))
								{
									print $row1['_id'] . "-,-";
									print $row1['_frm'] . "-,-";
									print $row1['chat'] . "-,-";
									print $row1['chatter'] . "-,-";
									if ($row1['_to'] == "") $row1['_to'] = "-999";
									print $row1['_to'] .  "-,-";
									print date("M d, H:m", strtotime($row1['datetime'])) .  "-,-";
									print $row[0] . "-,-";
									print "Image" . "-:-";
								}
					}
         }
           mysql_close($link);             
                 
   
?>