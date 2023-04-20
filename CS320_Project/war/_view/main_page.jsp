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
            Escape from the Zellar Cellar
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
                <img class = "roomImg" src ="./Images/RoomPlaceholder${ViewNumber}.jpg" alt = "Placeholder Room Image">
                <button class="button left" name = "left"><</button>
                <button class="button right" name = "right"><</button>
                <button class="button up" name = "up"><</button>
              	<button class="button down" name = "down"><</button>
              	</div>
                <c:forEach items="${items}" var="item">
              		<div class = "clickableHover">
              		<button type="submit" name = "${item.getName()}" style="top:${item.getYPosition()}px; 
              		left:${item.getXPosition()}px;">
              		<img "class = "clickable" src ="./Images/${item.getName()}.png" alt = "${item.getName()} style="width:20%;">
              		</button>
                	</div>
                </c:forEach>
               <p name = "textOutput">${textOutput}</p>
               <input type="text" name ="selected" style="visibility:hidden;" value="${selected}">
            </td>
            <td class="objectives">
                Objectives
            </td>
        </table>
    </div>

    <div class="inventory">
        <table class="inventorySlot">
        <tr>
        <c:forEach items= "${inventory}" var="inv">
        <th> 
            <div>
            <button class = "invButton" type = "submit" name="${inv.getName()}">
            <img class = "inv" src ="./Images/${inv.getName()}.png" alt = "${inv.getName()} style="width:10%;">
            </button>
            </div>  
            </th>
        </c:forEach>
            
        </tr>
        <tr>
        <c:forEach items = "${inventory}" var = "inv">
        <td>
				${inv.getName()}          
			</td>
        </c:forEach>
            
           
        </tr>
        </table>
    </div>
    </form>
</body>
</html>