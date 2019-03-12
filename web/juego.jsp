<%-- 
    Document   : juego
    Created on : 11/03/2019, 07:43:39 PM
    Author     : PLANZAGOM
--%>

<%@page import="multicast.MulticastReceivingPeer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>





        <div>
            <center>
                <img src="images/logo.jpg" alt="Guacamole" width="300" height="300"/>
                <p>
                    <%
                        String data = "";
                        Thread t;
                        MulticastReceivingPeer receptor;
                        //do {
                        receptor = new MulticastReceivingPeer();
                        t = new Thread(receptor);
                        t.start();
                        t.join();
                        data = receptor.getData();
                        out.println(data);
                        //Thread.sleep(5000);
                        //} while (data != "hola");
                        //response.sendRedirect("juego.jsp");
                        //RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/juego.jsp");
                        //dispatcher.forward(request, response);
%>
                </p>
                <table >
                    <tr >
                        <td><input type="checkbox" name="hole1" >hole1<br></td>
                        <td><input type="checkbox" name="hole4" >hole4<br></td>
                        <td><input type="checkbox" name="hole7" >hole7<br></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="hole2" >hole2<br></td>
                        <td><input type="checkbox" name="hole5" >hole5<br></td>
                        <td><input type="checkbox" name="hole8" >hole8<br></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="hole3" >hole3<br></td>
                        <td><input type="checkbox" name="hole6" >hole6<br></td>
                        <td><input type="checkbox" name="hole9" >hole9<br></td>
                    </tr>
                </table>
            </center>

        </div>
    </body>
</html>
