package houseadmin.data;

import common.DBAO;
import commstandards.JSON;
import houseadmin.data.tables.Groups;
import houseadmin.data.tables.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.RandomIDGenerator;

/**
 * Objects of this class represent the different users of the application. This
 * class is used for authentication and validation of users/clients.
 *
 * @author Wessel Jongkind
 * @version 02-09-2017
 */
public final class User extends DBAO implements JSON {

    /**
     * The activationID of the user.
     */
    private String activationID;

    /**
     * The email of the user.
     */
    private String email;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The hash of the password associated with the user/account
     */
    private String hash;

    /**
     * The salt used to generate the hash associated with the user/account
     */
    private String salt;

    /**
     * The groups of which the user is part.
     */
    private List<String> groups;

    /**
     * SQL statement used to obtain the basic info about a user. As a parameter
     * it requires the name of the user.
     */
    private static final String GET = "select * from Users where " + Users.EMAIL + "=?";

    private static final String GET_BY_ID = "select * from Users where " + Users.ACTIVATION_ID + "=?";

    private static final String UPDATE = "update Users set " + Users.HASH + "=?, "
            + Users.SALT + "=?, "
            + Users.NAME + "=?, "
            + Users.ACTIVATION_ID + "=? "
            + "where " + Users.EMAIL + "=?";
    private static final String INSERT = "insert into Users(" + Users.HASH + ", "
            + Users.SALT + ", "
            + Users.NAME + ", "
            + Users.EMAIL + ", "
            + Users.ACTIVATION_ID + ") values(?,?,?,?,?)";

    private static final String GET_ALL_ID = "select " + Users.ACTIVATION_ID + " from Users";

    /**
     * SQL statement used to remove a user's details from the database. As a
     * parameter it requires the email of the user that should be removed.
     */
    private static final String DELETE = "delete from Users where " + Users.EMAIL + "=?";

    private static final String GET_GROUPS = "select ID from Users_Groups where " + Users.EMAIL + "=?";

    private static final String DELETE_GROUPS = "delete from Users_Groups where " + Users.EMAIL + "=?";

    /**
     * Instantiates an user object. An activation ID is automatically generated
     * for the user.
     */
    public User() throws Exception {
        Connection conn = DBManager.getConnection(true);

        PreparedStatement get = null;
        ResultSet rs = null;
        try {
            get = conn.prepareStatement(GET_ALL_ID);
            rs = get.executeQuery();

            List<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getString(Users.ACTIVATION_ID));
            }

            this.activationID = RandomIDGenerator.generateUniqueID(ids);
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }
    }

    public User(String identifier, boolean isMail) throws Exception {
        Connection conn = DBManager.getConnection(true);
        this.groups = new ArrayList<>();

        PreparedStatement get = null;
        ResultSet rs = null;
        PreparedStatement getGroups = null;
        ResultSet rsGroups = null;

        try {
            if (isMail) {
                get = conn.prepareStatement(GET);
            } else {
                get = conn.prepareStatement(GET_BY_ID);
            }

            get.setString(1, identifier);
            rs = get.executeQuery();

            if (!rs.next()) {
                throw new Exception("No user with given name found.");
            }

            this.name = rs.getString(Users.NAME);
            this.email = rs.getString(Users.EMAIL);
            this.hash = rs.getString(Users.HASH);
            this.salt = rs.getString(Users.SALT);
            this.activationID = rs.getString(Users.ACTIVATION_ID);

            getGroups = conn.prepareStatement(GET_GROUPS);
            getGroups.setString(1, email);
            rsGroups = getGroups.executeQuery();

            while (rsGroups.next()) {
                groups.add(rsGroups.getString(Groups.ID));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs, getGroups, rsGroups);
        }
    }

    @Override
    public void update() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement update = null;

        try {
            update = conn.prepareStatement(UPDATE);
            update.setString(1, hash);
            update.setString(2, salt);
            update.setString(3, name);
            update.setString(4, activationID);
            update.setString(5, email);
            update.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(update);
        }
    }

    @Override
    public void delete() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement delete = null;
        PreparedStatement deleteGroups = null;

        try {
            deleteGroups = conn.prepareStatement(DELETE_GROUPS);
            deleteGroups.setString(1, email);
            deleteGroups.executeUpdate();

            delete = conn.prepareStatement(DELETE);
            delete.setString(1, email);
            delete.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(delete, deleteGroups);
        }
    }

    @Override
    public void create() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement insert = null;

        try {
            insert = conn.prepareStatement(INSERT);
            insert.setString(1, hash);
            insert.setString(2, salt);
            insert.setString(3, name);
            insert.setString(4, email);
            insert.setString(5, activationID);

            insert.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(insert);
        }
    }

    /**
     * Sets the hash of the password associated with the user's account.
     *
     * @param hash The hash of the password associated with the user's account.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Sets the salt used to hash the user's password.
     *
     * @param salt The salt used to hash the user's password.
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setActivationID(String id) {
        this.activationID = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Returns the hash generated from the user's password.
     *
     * @return The hash generated from the user's password.
     */
    public String getHash() {
        return hash;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    /**
     * Returns the salt used to hash the user's password.
     *
     * @return The salt used to hash the user's password.
     */
    public String getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return "activated".equals(activationID);
    }

    public String getActivationID() {
        return activationID;
    }

    public static boolean exists(String email) {
        try {
            User user = new User(email, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String constructJSONMessage() {
        //TODO implement
        return null;
    }
}
