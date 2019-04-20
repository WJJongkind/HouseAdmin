package houseadmin.login;

import houseadmin.data.User;


/**
 * This class is used to track users. When a client logs in to the application, a
 * Session for that person will be created. Due to security reasons this
 * person's IP address will also be monitored.
 *
 * @author Wessel Jongkind
 * @version 01-06-2017
 */
public class Session 
{

    /**
     * The IP of the client associated with this Session.
     */
    private final String ip;

    /**
     * A randomly generated ID, corresponding with the ID that has been stored
     * on a cookie for the client.
     */
    private final String sessionID;

    /**
     * Either a researcher or a patient. Contains all that person's basic
     * information.
     */
    private final User user;

    /**
     * The last timestamp (measured in Milliseconds, Unix) that this client has
     * made a server request.
     */
    private long lastActiveTime;

    /**
     * Creates a session which can be used to handle the different clients.
     *
     * @param user The user associated with this session.
     * @param ID The ID corresponding to the ID that has been attached to the
     * client's cookies.
     * @param ip The IP address from which the client is contacting the server.
     */
    public Session(User user, String ID, String ip) {
        this.user = user;
        this.sessionID = ID;
        this.ip = ip;

        updateActiveTime();
    }

    /**
     * Updates the timestamp to the time that the client made a new request.
     */
    public void updateActiveTime() {
        lastActiveTime = System.currentTimeMillis();
    }

    /**
     * Returns the User-object associated with this session.
     * @return The User associated with this session.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the IP address associated with this session.
     * @return The IP address associated with this session.
     */
    public String getIP() {
        return ip;
    }

    /**
     * The unique ID associated with this session.
     * @return The Session ID associated with this session.
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * The last time the user has made a request. This is in Unix milliseconds.
     * @return Timestamp of the last time when a user has made a request in Unix milliseconds.
     */
    public long getLastActiveTime() {
        return lastActiveTime;
    }
}