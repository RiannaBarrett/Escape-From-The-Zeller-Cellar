<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en-us">
<head>
    <title>Escape from the Zeller Cellar</title>
    <link rel="stylesheet" href="./_view/css/game.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <meta charset="UTF-8">
</head>
<body>
<form action="${pageContext.servletContext.contextPath}/main_page" method="post">
    <div class="header">
         <div id="countdown"></div>
        <div class="title">
        <h3>
            Escape from the Zeller Cellar
            <input type="hidden" name="duration" value="" id="duration">
        </h3>
         </div>
         
    <div class = "loginBtn">
     <input type="submit" name="logout" value="logout">
        </div>
    </div>
    <div class="mainContent">
        <table>
            <td class="gameGraphics">
            	<div ="imageContainer">
                <img class = "roomImg" src ="./Images/RoomPlaceholder${ViewNumber}.png" alt = "Placeholder Room Image">
                <button class="button left" name = "left"><</button>
                <button class="button right" name = "right"><</button>
                <button class="button up" name = "up"><</button>
              	<button class="button down" name = "down"><</button>
                <input class="pickUp" type="submit" name="pickUp" value="Pick Up">
    	

              	<p name = "textOutput">${textOutput}</p>
              	</div>
              	<c:if test="${selected == 'Locked Comic Stand'}">
  					<input type="text" name="comicBookCode" placeholder="Enter code here" pattern="[0-9]">

				</c:if>
              	
                <c:forEach items="${items}" var="item">
              		<div class = "clickableHover">
              		<button type="submit" name = "${item.getName()}" style = "transform: scale(0.1); top:${item.getXPosition()}px; 
              		left:${item.getYPosition()}px; ">
              		<img "class = "clickable" src ="./Images/${item.getName()}.png" alt = "${item.getName()} style="width:20%;">
              		</button>
                	</div>
                </c:forEach>
               <input type="text" name ="selected" style="visibility:hidden;" value="${selected}">
            </td>
        </table>
    </div>

    <div class="inventory">
        <h2 class="inventory-header">Inventory</h2>
  <table class="inventory-table">
    <tr>
      <c:forEach items="${inventory}" var="inv">
        <td class="inventory-slot">
          <button class="inv-button" type="submit" name="${inv.getName()}">
            <img class="inv-img" src="./Images/${inv.getName()}.png" alt="${inv.getName()}" style="width: 100%;">
          </button>
          <div class="inv-name">${inv.getName()}</div>
        </td>
      </c:forEach>
    </tr>
  		</table>
			</div>
			 <script>
				  var timeLeft = 300; // 300 seconds
				  var countdownElement = document.getElementById("countdown");
				  var countdownInterval;
				
				  function startCountdown() {
				    countdownInterval = setInterval(updateCountdown, 1000);
				  }
			
			  function updateCountdown() {
			    var minutes = Math.floor(timeLeft / 60);
			    var seconds = timeLeft % 60;
			    var secondsDisplay = seconds < 10 ? "0" + seconds : seconds;
			    countdownElement.innerHTML = "Time left: " + minutes + ":" + secondsDisplay;
			    timeLeft--;
			    if (timeLeft < 0) {
			      clearInterval(countdownInterval);
			      alert("Game over!"); // show game over message
			      console.log("Redirecting to game over page...");
			      document.forms[0].action = "${pageContext.servletContext.contextPath}/game_over";
			      document.forms[0].submit();
			    } else if (timeLeft == 60) {
			      alert("Hurry up! You only have 60 seconds left!");
			    }
			  }

  				startCountdown();
		</script>

    </form>
</body>
</html>