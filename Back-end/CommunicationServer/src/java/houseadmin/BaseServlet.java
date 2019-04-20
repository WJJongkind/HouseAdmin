package houseadmin;

import commstandards.RequestKeywords;
import houseadmin.data.User;
import houseadmin.login.Session;
import houseadmin.login.SessionManager;
import java.io.IOException;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents the base-servlet which should be extended by all other
 * servlet classes. It provides basic functionality such as the sending of error
 * messages, requesting SQL-statement executions and sending replies.
 *
 * @author Wessel Jongkind
 * @version 02-09-2017
 */
@WebServlet(name = "BaseServlet", urlPatterns = {"/BaseServlet"})
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    @Override
    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Obtains the value of a cookie of a HTTP request.
     *
     * @param key The name of the cookie/the key referring to the cookie.
     * @param request The associated HTTP request.
     * @return The value of the cookie. If no cookie with such a name/key has
     * been found then null will be returned.
     */
    protected String getCookieValue(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        String value = null;

        //Searching for a cookie with the specified key/name.
        for (Cookie c : cookies) {
            if (c.getName().equals(key)) {
                value = c.getValue();
                break;
            }
        }

        return value;
    }

    /**
     * Retrieves the base-url from which the request has been made. For example;
     * if the request has been made from
     * http://www.something.com/map1/index.html then this method will return
     * http://www.something.com.
     *
     * @param request The request from which the base-url has to be obtained.
     * @return The base-url.
     */
    protected String getBaseURL(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        if (request.getServletPath() != null) {
            url = url.replace(request.getServletPath(), "");
        }
        if (request.getPathInfo() != null) {
            url = url.replace(request.getPathInfo(), "");
        }
        return url;
    }

    /**
     * Verifies whether or not a session is still valid and should be trusted.
     *
     * @param request The HTTP request which has to be verified
     * @return True if the session is valid and can be trusted, else false.
     */
    protected boolean verifySession(HttpServletRequest request) {
        try {
            //No cookie has been set with a SessionID
            Session session = getSession(request);

            return verifySession(request, session);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean verifySession(HttpServletRequest request, JSONObject object) {
        try {
            //No cookie has been set with a SessionID
            Session session = getSession(object);
            return verifySession(request, session);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean verifySession(HttpServletRequest request, Session session) {
        /*
            Either the session can't be found (expired) or the IP of the client changed
         */
        if (session == null || (!session.getIP().equals(request.getRemoteAddr()))) {
            return false;
        } else {
            session.updateActiveTime();
            return true;
        }
    }

    /**
     * Obtains the Session object that is associated with the HTTP request.
     *
     * @param request The HTTP request of which the associated Session-object
     * should be retrieved.
     * @return The Session-object associated with the HTTP request.
     */
    protected Session getSession(HttpServletRequest request) {
        String sessionID = getCookieValue(RequestKeywords.SESSION_ID, request);
        SessionManager manager = new SessionManager();

        return manager.getSession(sessionID);
    }

    public User getUser(HttpServletRequest request) throws Exception {
        Session session = getSession(request);

        if (session != null) {
            return session.getUser();
        } else {
            return null;
        }
    }

    protected Session getSession(JSONObject object) throws JSONException {
        String sessionID = object.getString("sessionID");
        SessionManager manager = new SessionManager();

        return manager.getSession(sessionID);
    }

    public User getUser(JSONObject object) throws JSONException {
        return getSession(object).getUser();
    }
    
}
