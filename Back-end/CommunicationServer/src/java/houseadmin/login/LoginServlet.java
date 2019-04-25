package houseadmin.login;

import common.JSONStatusResponse;
import commstandards.JSON;
import houseadmin.BaseServlet;
import houseadmin.data.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import util.PasswordHasher;

/**
 * This class handles all the logins towards the application.
 *
 * @author Wessel Jongkind
 * @version 02-09-2017
 */
@WebServlet(name = "loginservlet", urlPatterns = {"/HouseAdmin/loginservlet", "/HouseAdmin/Loginservlet", "/HouseAdmin/LoginServlet", "/HouseAdmin/Login.json"})
public class LoginServlet extends BaseServlet {

    /**
     * Error messages that can be shown to the user when the wrong password is
     * given or the account is inactive.
     */
    private static final String INACTIVE_ACCOUNT = "Your account has not yet been activated.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("http://www.cowlite.nl");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            JSONObject json = JSON.extractJSON(request);
            User user = new User(json.getString("username"), true);
            PasswordHasher hasher = new PasswordHasher();
            hasher.setPassword((String) json.get("password"));
            hasher.hash(user.getSalt());

            // Verify whether or not user credentials are correct
            out = response.getWriter();
            if (checkPassword(json.getString("password"), user)) {
                SessionManager manager = new SessionManager();
                String sessionID = manager.addSession(request, response, user);

                out.println("{\"id\": \"" + sessionID + "\"}");
            } else {
                out.println(JSONStatusResponse.failure());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (out == null) {
                out = response.getWriter();
            }

            out.println(JSONStatusResponse.failure());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private boolean checkPassword(String password, User user) throws Exception {
        PasswordHasher hasher = new PasswordHasher();
        hasher.setPassword(password);
        hasher.hash(user.getSalt());

        return hasher.getHash().equals(user.getHash());
    }
}
