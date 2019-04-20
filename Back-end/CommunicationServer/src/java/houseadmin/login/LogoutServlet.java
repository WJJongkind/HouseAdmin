package houseadmin.login;

import houseadmin.BaseServlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles all the logouts from the application.
 * @author Wessel Jongkind
 * @version 11-10-2017
 */
@WebServlet(name = "logoutservlet", urlPatterns = {"/HouseAdmin/logoutservlet", "/HouseAdmin/Logoutservlet", "/HouseAdmin/LogoutServlet"})
public class LogoutServlet extends BaseServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        handleRequest(request, response);
    }
    
    /**
     * This method is used to log out the user by removing the session associated with his/her account.
     * @param request The HTTP request used to log out
     * @param response The HTTP response used to communicate back to the client
     * @throws ServletException -
     * @throws IOException -
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        try{
            Session session = getSession(request);
            SessionManager manager = new SessionManager();
            manager.removeSession(session.getSessionID());
            out.println("{\"logout\": true}");
        }catch(Exception e){
            out = response.getWriter();
            
            out.println("{\"logout\": false}");
        }finally{
            out.close();
        }
    }
}
