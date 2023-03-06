<!DOCTYPE html>

<html>
  <head>
    <title>Forgot Password</title>
    <link rel="stylesheet" type="text/css" href="./_view/css/style.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="./css/all.min.css">
    <link rel="stylesheet" href="https://kit.fontawesome.com/a839866b20.css" crossorigin="anonymous">
    <link href='https://fonts.googleapis.com/css?family=Playfair Display' rel='stylesheet'>
    </head>
  <body>
    <div class="background-image"></div>
    <div class="login-box">
      <h1>Forgot Password</h1>
      <form action="" method="post">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
        <button type="submit" name="submit">Submit</button>
      </form>
      <h3>Remembered your password? <a href="login">Login</a></h3>
      <?php
        if(isset($_POST['submit'])){
          // Code to send password reset email to user
          $email = $_POST['email'];
          // You can implement your own logic here to send the password reset email to the user
          echo "An email has been sent to $email with instructions to reset your password.";
        }
      ?>
    </div>
    <script src="https://kit.fontawesome.com/a839866b20.js" crossorigin="anonymous"></script>
  </body>
</html>