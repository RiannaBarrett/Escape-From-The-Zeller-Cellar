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
                <img class = "roomImg" src ="./Images/RoomPlaceholder.jpg" alt = "Placeholder Room Image">
                <button class="button left"><</button>
                <button class="button right"><</button>
                <button class="button up"><</button>
              	<button class="button down"><</button>
              	</div>
                <c:forEach items="${items}" var="item">
              		<div class = "clickableHover">
              		<input type="hidden" name = "${item.getName()}" style="top:${item.getYPosition()}px; left:${item.getXPosition()}px; width:20%; ">
                	<input type="image" name="${item.getName()}"class = "clickable" src ="./Images/${item.getName()}.png" alt = "${item.getName()}" 
                	style="top:${item.getYPosition()}px; left:${item.getXPosition()}px; width:20%; ">
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
            <th>
                <input type="hidden" name = "${inv1}" style= width:10%; ">
                	<input type="image" name="${inv1}" class = "clickable" src ="./Images/${inv1}.png" alt = "${item.getName()}" 
                	style="width:5%; ">
                
            </th>
            <th>
                <input type="hidden" name = "${inv2}" style= width:10%; ">
                	<input type="image" name="${inv2}" class = "clickable" src ="./Images/${inv2}.png" alt = "${item.getName()}" 
                	style="width:5%; ">
            </th>
            <th>
                <input type="hidden" name = "${inv3}" style= width:10%; ">
                	<input type="image" name="${inv3}" class = "clickable" src ="./Images/${inv3}.png" alt = "${item.getName()}" 
                	style="width:5%; ">
            </th>
            <th>
                <input type="hidden" name = "${inv4}" style= width:10%; ">
                	<input type="image" name="${inv4}" class = "clickable" src ="./Images/${inv4}.png" alt = "${item.getName()}" 
                	style="width:5%; ">
            </th>
            <th>
                <input type="hidden" name = "${inv5}" style= width:10%; ">
                	<input type="image" name="${inv5}" class = "clickable" src ="./Images/${inv5}.png" alt = "${item.getName()}" 
                	style="width:10%; ">
            </th>
        </tr>
        <tr>
            <td>
				ITEM          
			</td>
            <td>
            	ITEM
            </td>
            <td>
                ITEM
            </td>
            <td>
                ITEM
            </td>
            <td>
                ITEM
            </td>
        </tr>
        </table>
    </div>
    </form>
</body>
</html>