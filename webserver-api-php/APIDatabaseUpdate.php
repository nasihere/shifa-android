<?php
	if ($_REQUEST['id']) // if id is empty then don't fetched any data from server
	{
	header('Location: http://nasihere-001-site5.smarterasp.net/api/Server/'.$_REQUEST['id']);
	exit;
	}
?>