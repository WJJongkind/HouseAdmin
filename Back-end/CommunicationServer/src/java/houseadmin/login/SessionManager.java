package houseadmin.login;

import commstandards.RequestKeywords;
import houseadmin.data.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Timer;
import util.RandomIDGenerator;

/**
 * This class manages all Session-related requests and decides whether or not
 * they should be timed-out.
 * @author Wessel Jongkind
 * @version 27-06-2017
 */
public class SessionManager implements ActionListener
{
    /**
     * A HashMap that contains the sessionID as a key which refers to the
     * Session object corresponding to a client.
     */
    protected static final HashMap<String, Session> SESSIONS = new HashMap<>();
    
    /**
     * The amount of time that a client is allowed to not make a server-side
     * request. If too much time passes, the client's Session will be removed
     * from the server and the client can not navigate to new pages without logging in.
     */
    protected static final long TIMEOUT_TRESHOLD = 300000;
    
    /**
     * The interval at which all the sessions are checked for timeouts.
     */
    protected static final int TIMER_INTERVAL = 1000;
    
    /**
     * The timer used to check all the sessions for timeouts.
     */
    protected static Timer timer;
    
    /**
     * Creates a new SessionManager.
     */
    public SessionManager()
    {
        if(timer == null) {
            timer = new Timer(TIMER_INTERVAL, this);
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    /**
     * Creates a new session for a client and stores it in the SessionManager.
     * @param request The HTTP request that the client has made.
     * @param response The response used to return a new page to the client.
     * @param user The User-object related to the client that made the request.
     */
    public String addSession(HttpServletRequest request, HttpServletResponse response, User user)
    {
        String id = RandomIDGenerator.generateUniqueID(new ArrayList<>(SESSIONS.keySet()));
        Session session = new Session(user, id, request.getRemoteAddr());
        response.addCookie(new Cookie(RequestKeywords.SESSION_ID, id));
        SESSIONS.put(id, session);
        
        timer.restart();
        
        return id;
    }
    
    /**
     * Removes a session from the SessionManager.
     * @param session The iD of the session that should be removed from the SessionManager.
     */
    public void removeSession(String session) {
        SESSIONS.remove(session);
        timer.restart();
    }
    
    /**
     * Returns a session that was found with the given ID or null if no session has been found.
     * @param ID The ID of the session that you are trying to find
     * @return The session that was found with the given ID or null if none were found.
     */
    public Session getSession(String ID) {
        return SESSIONS.get(ID);
    }

    /**
     * Continuously checks whether or not a user should be timed-out.
     * @param e The ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Checking... " + System.currentTimeMillis() / 1000);
        try {
            Iterator<Map.Entry<String, Session>> it = SESSIONS.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<String, Session> entry = it.next();
                Session session = entry.getValue();
                if(session.getLastActiveTime() + TIMEOUT_TRESHOLD < System.currentTimeMillis())
                    SESSIONS.remove(entry.getKey());
            }

            //If there are no more sessions, the manager should stop checking.
            if(!SESSIONS.isEmpty()) {
                timer.restart();
            }
        }catch(Exception f) {
            f.printStackTrace();
        }
    }
}
