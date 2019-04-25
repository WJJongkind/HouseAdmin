package houseadmin.login;

import common.JSONStatusResponse;
import commstandards.JSON;
import houseadmin.BaseServlet;
import houseadmin.data.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mail.MailManager;
import org.json.JSONObject;
import util.PasswordHasher;

/**
 * This servlet handles all registration requests for the application.
 *
 * @author Wessel Jongkind
 * @version 02-09-2017
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/HouseAdmin/Register.json"})
public class RegistrationServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println(JSONStatusResponse.failure());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = null;

        try {
            User user = createUser(JSON.extractJSON(request));

            out = response.getWriter();
            if (User.exists(user.getEmail())) {
                out.print(JSONStatusResponse.failure());
            } else {
                user.create();
                sendConfirmationMail(user, request);
                out.print("{}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Send a confirmation mail to verify that the client has access to the
     * given email.
     *
     * @param user The user that has to confirm his/her email-address.
     * @param request The HTTP request used to activate the servlet.
     * @throws Exception
     */
    private void sendConfirmationMail(User user, HttpServletRequest request) throws Exception {
        ActivationMail mail = new ActivationMail(user.getEmail(), user.getActivationID(), getBaseURL(request));
        MailManager manager = new MailManager();
        manager.sendMail(mail);
    }

    /**
     * Create the user-object with the data in the HTTP request.
     *
     * @param request The HTTP request used to register a new account.
     * @return The newly created User-object (which hasn't been saved yet)
     * @throws Exception When the password could not be hashed.
     */
    private User createUser(JSONObject object) throws Exception {
        User user = new User();
        user.setEmail(object.getString("email"));
        user.setName(object.getString("username"));

        //Hash the password and store it in the user-object.
        PasswordHasher hasher = new PasswordHasher();
        hasher.setPassword(object.getString("password"));
        hasher.hash();

        user.setHash(hasher.getHash());
        user.setSalt(hasher.getSalt());

        return user;
    }
}
