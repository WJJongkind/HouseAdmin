package houseadmin.login;

import common.JSONStatusResponse;
import commstandards.JSON;
import houseadmin.BaseServlet;
import houseadmin.data.User;
import houseadmin.data.tables.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.QueryReader;

/**
 * This servlet is used to activate accounts for the application.
 * @author Wessel
 * @version 28-08-2017
 */
@WebServlet(name = "ActivationServlet", urlPatterns = {"/ActivationServlet"})
public class ActivationServlet extends BaseServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out = null;
        try{
            //Decode the query string
            Map<String, String> query = QueryReader.readQuery(request.getQueryString());
            User user = new User(query.get(Users.ACTIVATION_ID), false);
            user.setActivationID("activated");
            user.update();
            
            out = response.getWriter();
            out.print(JSONStatusResponse.success());
        }catch(Exception e){
            out = response.getWriter();
            out.print(JSONStatusResponse.failure());
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    }
}
