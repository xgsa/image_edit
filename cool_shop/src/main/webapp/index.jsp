<%@ page import="java.text.*" %>
<%@ page contentType="text/html; charset=utf-8" %>
<html>
<head>
    <title>Login page</title>
    <style>
        #outer {
            display: table;
            position: absolute;
            height: 100%;
            width: 100%;
        }

        #middle {
            display: table-cell;
            vertical-align: middle;
        }

        #inner {
            margin-left: auto;
            margin-right: auto;
            width: 300px;
            background-color: #EEEEEE;
            text-align: center;
            border-radius: 10px;
            padding: 5px;
        }
        .wide {
            width: 100%;
        }
    </style>
</head>
<body>

<div id="outer">
    <div id="middle">
        <div id="inner">
            <B>Welcome, today is <%= DateFormat.getDateInstance().format(new java.util.Date()) %></b>
            <HR>
            <FORM action="/login" name="login-form" method="post">
                <table class="wide">
                    <tr>
                        <td>Login:</td>
                        <td><INPUT type="text" name="login" class="wide"></td>
                    </tr>
                    <tr>
                        <td>Passord:</td>
                        <td><INPUT type="password" name="password" class="wide"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">
                            <INPUT type="submit" name="login" value="Go-go-go!">
                        </td>
                    </tr>
                </table>
            </FORM>
        </div>
    </div>
</div>

</body>
</html>