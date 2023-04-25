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
        
        <div class="title">
        <h1>
            Escape from the Zeller Cellar
        </h1>
         </div>
        <div class = "loginBtn">
        <input type="submit" name="logout" value="logout">
        </div>
    </div>

    <div class="mainContent">
        <table>
            <td class="keyDescriptions">
                <input class="pickUp"type="submit" name="pickUp" value="Pick Up">
            </td>
            <td class="gameGraphics">
            	<div ="imageContainer">
                <img class = "roomImg" src ="./Images/RoomPlaceholder${ViewNumber}.png" alt = "Placeholder Room Image">
                <button class="button left" name = "left"><</button>
                <button class="button right" name = "right"><</button>
                <button class="button up" name = "up"><</button>
              	<button class="button down" name = "down"><</button>
              	</div>
                <c:forEach items="${items}" var="item">
              		<div class = "clickableHover">
              		<button type="submit" name = "${item.getName()}" style = "transform: scale(0.10); top:${item.getXPosition()}px; 
              		left:${item.getYPosition()}px; ">
              		<img "class = "clickable" src ="./Images/${item.getName()}.png" alt = "${item.getName()} style="width:20%;">
              		</button>
                	</div>
                </c:forEach>
               
               <input type="text" name ="selected" style="visibility:hidden;" value="${selected}">
            </td>
            <td class="objectives">
                <div class="obj"> 
                <h1 class="objective-header"> Objectives </h1>
                 </div>
            </td>
        </table>
    </div>
<p name = "textOutput">${textOutput}</p>
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
    </form>
</body>
</html>