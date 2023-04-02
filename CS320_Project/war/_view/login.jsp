<!DOCTYPE html>

<html>
  <head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="./_view/css/style.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="./css/all.min.css">
    <link rel="stylesheet" href="https://kit.fontawesome.com/a839866b20.css" crossorigin="anonymous">

    <link href='https://fonts.googleapis.com/css?family=Playfair Display' rel='stylesheet'>
    <script type="text/javascript">
    	function restrict(input){
    	var regex = /[|^;]/g;
		input.value = input.value.replace (regex,"");
    	}
    </script>
</head>

  <body>
    <div class="background-image"></div>
    <div class="login-box">
      <h1>Login </h1>
      <form action="${pageContext.servletContext.contextPath}/login" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" onkeyup="restrict(this)" required>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" onkeyup="restrict(this)" required>
        <h3><a href="forgot_password">Forgot Password?</a></h3>
        <input type="submit" name="login" value="Login">
        <br>
        <div class="social-media">
            <a href="https://www.facebook.com" target=""><i class="fab fa-facebook fa-2x"></i></a>
            <a href="https://www.twitter.com" target=""><i class="fab fa-twitter fa-2x"></i></a>
            <a href="https://www.youtube.com" target="_blank"><i class="fab fa-youtube fa-2x"></i></a>
            <a href="https://www.instagram.com" target="_blank"><i class="fab fa-instagram fa-2x"></i></a>
          </div>          
 	  <h3>${errorMessage}</h3> 
      <h3>Don't have an account? <a href="signup">Sign up</a></h3>
      </form>
    </div>
    <script src="https://kit.fontawesome.com/a839866b20.js" crossorigin="anonymous"></script>
  </body>
</html>
