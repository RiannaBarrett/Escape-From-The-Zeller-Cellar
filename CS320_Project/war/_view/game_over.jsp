<!DOCTYPE html>

<html>
  <head>
    <title>Escape from the Zeller Cellar</title>
    <link href="./_view/css/game.css" rel="stylesheet" type="text/css" />
    <meta charset="utf-8">
  		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
  		<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  		<link rel="stylesheet" href="./css/all.min.css">
  		<link rel="stylesheet" href="https://kit.fontawesome.com/a839866b20.css" crossorigin="anonymous">
    </head>
<body>
<form action="${pageContext.servletContext.contextPath}/game_over" method="post">
  <section class="hero">
    <div class="main-with">
      <div class="content">
        <div class="game-over">
        <h3>GAME OVER</h3>
          <p>YOU DID NOT ESCAPE ZELLER'S CELLAR</p>
           <button name = "reset" class="button1"><a href="./main_page">START OVER</a></button>
        </div>
      </div>
      
    </div>
  </section>


  <script src="https://kit.fontawesome.com/a839866b20.js" crossorigin="anonymous"></script>
  </form>
</body>
</html>