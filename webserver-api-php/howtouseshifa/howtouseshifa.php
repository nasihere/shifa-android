<?php 
    $fileno = $_REQUEST['id'];
    if ($fileno == "") $fileno = "1";
?>
<html>
    <head>

  
    <style>
        .image {
  font-size: 0;
  text-align: center;
  width: 100%;  /* Container's dimensions */
  height: 100%;
}
img {
  display: inline-block;
  vertical-align: middle;
  max-height: 100%;
  max-width: 100%;
}
.trick {
  display: inline-block;
  vertical-align: middle;
  height: 150px;
}
        
        
    </style>
    </head>
    <div>
    <a style="float:left" href="javascript:window.history.back()">Back</a>
    <a style="float:right" href="howtouseshifa.php?id=<?php echo $fileno + 1; ?>">Next</a></div>
    <div class="image" style="display:block">
        <div class="trick"></div>  
        <img src="imgs/<?php echo $fileno; ?>.png" />
    </div>
</html>
