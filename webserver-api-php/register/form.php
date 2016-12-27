<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shifa Register Form</title>
<link rel="stylesheet" type="text/css" href="view.css" media="all">
<script type="text/javascript" src="view.js"></script>
<script type="text/javascript" src="calendar.js"></script>
</head>
<body id="main_body" >
    
    <img id="top" src="top.png" alt="">
    <div id="form_container">
    
        <h1><a>Reigster Form</a></h1>
        <form id="form_705522" class="appnitro"  method="post" action="http://kent.shifa.in/app_php/app_reg.php">
                    <div class="form_description">
            <h2>Shifa - Register </h2>
            <p>Register Form</p>
        </div>                        
            <ul >
            
                    <li id="li_1" >
        <label class="description" for="email">Email Id </label>
        <div>
            <input id="element_1" name="email" class="element text medium" type="text" maxlength="255" value=""/> 
        </div><p class="guidelines" id="guide_1"><small>nasiXXX@gmail.com</small></p> 
        </li>        <li id="li_2" >
        <label class="description" for="password">Password </label>
        <div>
            <input id="element_2" name="password" class="element text medium" type="password" maxlength="255" value=""/> 
        </div> 
        </li>        <li id="li_3" >
        <label class="description" for="element_3">Name </label>
        <span>
            <input id="element_3_1" name= "fname" class="element text" maxlength="255" size="8" value=""/>
            <label>First</label>
        </span>
        <span>
            <input id="element_3_2" name= "lname" class="element text" maxlength="255" size="14" value=""/>
            <label>Last</label>
        </span> 
        </li>        <li id="li_4" >
        <label class="description" for="element_4">Date of Birth</label>
        
        <span>
             <input id="element_4_3" name="dob" class="element text" size="4" maxlength="12" value="" type="text">
            <label for="dob">MM/DD/YYYY</label>
        </span>
     
        </li>        <li id="li_7" >
        <label class="description" for="sex">Gender </label>
        <span>
            <input id="element_7_1" name="sex" class="element radio" type="radio" value="0" />
<label class="choice" for="element_7_1">Male</label>
<input id="element_7_2" name="sex" class="element radio" type="radio" value="1" />
<label class="choice" for="element_7_2">Female</label>

        </span> 
        </li>        <li id="li_5" >
        <label class="description" for="city">City </label>
        <div>
            <input id="element_5" name="city" class="element text medium" type="text" maxlength="255" value=""/> 
        </div> 
        </li>        <li id="li_6" >
        <label class="description" for="country">Country </label>
        <div>
            <input id="element_6" name="country" class="element text medium" type="text" maxlength="255" value=""/> 
        </div> 
        </li>        <li id="li_8" >
        <label class="description" for="occupation">Occupation </label>
        <span>
            <input id="element_8_1" name="occupation" class="element radio" type="radio" value="Student" />
<label class="choice" for="occupation">Student</label>
<input id="element_8_2" name="occupation" class="element radio" type="radio" value="Doctor" />
<label class="choice" for="occupation">Doctor</label>

        </span> 
        </li>
            
                    <li class="buttons">
                <input type="hidden" name="form_id" value="705522" />
                                        <input type="hidden" name="web" value="web" />
                
                <input id="saveForm" class="button_text" type="submit" name="submit" value="Submit" />
        </li>
            </ul>
        </form>    
        
    </div>
    <img id="bottom" src="bottom.png" alt="">
    </body>
</html>