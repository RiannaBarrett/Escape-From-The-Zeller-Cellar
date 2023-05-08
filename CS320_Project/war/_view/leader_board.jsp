<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
  <head>
    <title>Escape from the Zeller Cellar</title>
    <link href="./_view/css/style.css" rel="stylesheet" type="text/css" />
    <meta charset="utf-8">
  		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
  		<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  		<link rel="stylesheet" href="./css/all.min.css">
  		<link rel="stylesheet" href="https://kit.fontawesome.com/a839866b20.css" crossorigin="anonymous">
    </head>
<body>
<h1 class="leaderBoard">Leader Board</h1>
<table>
<tr>
<td class="tableTitle">Username</td>
<td class="tableTitle">Time Remaining (seconds)</td>
</tr>

<c:forEach items="${pairs}" var="pair">
<tr>
        <td>
         ${pair.getLeft()}
        </td>
         <td>
         ${pair.getRight()}
        </td>
        </tr>
      </c:forEach>


</table>
</body>
</html>