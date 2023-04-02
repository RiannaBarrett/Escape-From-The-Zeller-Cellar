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
                Key Descriptions
            </td>
            <td class="gameGraphics">
                <img class = "roomImg" src ="./Images/RoomPlaceholder.jpg" alt = "Placeholder Room Image">
                <c:forEach items="${items}" var="item">
              		<div class = "clickableHover">
              		<input type="hidden" name = "${item.getName()}" style="top:${item.getYPosition()}px; left:${item.getXPosition()}px; width:20%; ">
                	<input type="image" name="${item.getName()}"class = "clickable" src ="./Images/${item.getName()}.png" alt = "${item.getName()}" 
                	style="top:${item.getYPosition()}px; left:${item.getXPosition()}px; width:20%; ">
                	</div>
                </c:forEach>
               <p name = "textOutput">${textOutput}</p>
            </td>
            <td class="objectives">
                Objectives
            </td>
        </table>
    </div>

    <div class="inventory">
        <table>
            <tr>
                item
            </tr>
            <tr>
                item 2
            </tr>
            <tr>
                item 3
            </tr>
        </table>
    </div>
    </form>
</body>
</html>