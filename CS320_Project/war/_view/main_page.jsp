<!DOCTYPE html>
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
                game goes here
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