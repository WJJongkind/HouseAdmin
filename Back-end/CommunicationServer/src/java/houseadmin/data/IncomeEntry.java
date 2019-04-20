/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.data;

import common.DBAO;
import commstandards.JSON;
import houseadmin.data.tables.Income;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import util.Date;
import util.RandomIDGenerator;

/**
 *
 * @author Wessel
 */
public class IncomeEntry extends DBAO implements JSON {

    private Date date;
    private double amount;
    private String category;
    private String comments;
    private String person;
    private String id;
    private String groupID;

    private static final String GET = "select * from Income where " + Income.ID + " = ?";
    private static final String GET_ALL_BY_GROUP = "select * from Income where " + Income.GROUP + "=?";
    private static final String UPDATE = "update Income set " + Income.DATE + "=?, "
            + Income.CATEGORY + "=?, "
            + Income.AMOUNT + "=?, "
            + Income.PERSON + "=?, "
            + Income.COMMENTS + "=? "
            + Income.GROUP + "=? "
            + "where " + Income.ID + "=?";
    private static final String INSERT = "insert into Income(" + Income.DATE + ", "
            + Income.CATEGORY + ", "
            + Income.AMOUNT + ", "
            + Income.PERSON + ", "
            + Income.COMMENTS + ", "
            + Income.ID + ", "
            + Income.GROUP + ") values(?,?,?,?,?,?,?)";

    private static final String DELETE = "delete from  Income where " + Income.ID + "=?";

    private static final String GET_ALL_ID = "select " + Income.ID + " from Income";

    public IncomeEntry() {
        // Instantiate blanco object
    }

    public IncomeEntry(JSONObject obj, User user) throws JSONException, Exception {
        this.amount = obj.getDouble("amount");
        this.category = obj.getString("category");
        this.date = new Date(obj.getString("date"));
        this.comments = obj.getString("comments");
        this.person = user.getEmail();
        this.groupID = obj.getString("groupID");

        try {
            this.id = obj.getString("ID");
        } catch (Exception e) {
        }
    }

    public IncomeEntry(JSONObject obj, User user, Group group) throws JSONException, Exception {
        this.amount = obj.getDouble("amount");
        this.category = obj.getString("category");
        this.date = new Date(obj.getString("date"));
        this.comments = obj.getString("comments");
        this.person = user.getEmail();
        this.groupID = group.getId();
        this.id = obj.getString("id");
    }

    public IncomeEntry(String id) throws Exception {
        Connection conn = DBManager.getConnection(true);

        PreparedStatement get = null;
        ResultSet rs = null;
        try {
            get = conn.prepareStatement(GET);
            get.setString(1, id);
            rs = get.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Income entry with ID " + id + " is not found!");
            }

            initialiseFromResultSet(rs);
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }
    }

    private IncomeEntry(ResultSet rs) throws Exception {
        initialiseFromResultSet(rs);
    }

    private void initialiseFromResultSet(ResultSet rs) throws Exception {
        this.id = rs.getString(Income.ID);
        this.date = new Date(rs.getString(Income.DATE));
        this.amount = Double.parseDouble(rs.getString(Income.AMOUNT));
        this.category = rs.getString(Income.CATEGORY);
        this.comments = rs.getString(Income.COMMENTS);
        if (this.comments == null) {
            this.comments = "";
        }
        this.person = rs.getString(Income.PERSON);
        this.groupID = rs.getString(Income.GROUP);
    }

    public static List<IncomeEntry> getAllEntriesByGroup(String groupID) throws Exception {
        Connection conn = DBManager.getConnection(true);

        List<IncomeEntry> income = new ArrayList<>();

        PreparedStatement get = null;
        ResultSet rs = null;
        try {
            get = conn.prepareStatement(GET_ALL_BY_GROUP);
            get.setString(1, groupID);
            rs = get.executeQuery();

            while (rs.next()) {
                income.add(new IncomeEntry(rs));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }

        return income;
    }

    @Override
    public void update() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement update = null;
        ResultSet rs = null;
        try {
            update = conn.prepareStatement(UPDATE);

            update.setString(1, date.toString());
            update.setString(2, category);
            update.setDouble(4, amount);
            update.setString(5, person);
            update.setString(6, comments);
            update.setString(7, id);

            update.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(update, rs);
        }
    }

    @Override
    public void delete() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement delete = null;
        try {
            delete = conn.prepareStatement(DELETE);
            delete.setString(1, id);

            delete.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(delete);
        }
    }

    @Override
    public void create() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement insert = null;
        PreparedStatement getID = null;
        ResultSet allID = null;
        try {
            insert = conn.prepareStatement(INSERT);

            ArrayList<String> ids = new ArrayList<>();
            getID = conn.prepareStatement(GET_ALL_ID);
            allID = getID.executeQuery();
            while (allID.next()) {
                ids.add(allID.getString(Income.ID));
            }
            this.id = RandomIDGenerator.generateUniqueID(ids);

            insert.setString(1, date.toString());
            insert.setString(2, category);
            insert.setDouble(3, amount);
            insert.setString(4, person);
            insert.setString(5, comments);
            insert.setString(6, id);
            insert.setString(7, groupID);

            insert.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(insert, getID, allID);
        }
    }

    @Override
    public String constructJSONMessage() {
        return "{"
                + "     \"date\": \"" + date.toString() + "\","
                + "     \"amount\": " + amount + ","
                + "     \"category\": \"" + category + "\","
                + "     \"comments\": \"" + comments + "\","
                + "     \"id\": \"" + id + "\","
                + "     \"person\": \"" + person + "\","
                + "     \"groupID\": \"" + groupID + "\""
                + "}";
    }

    public static String constructJSONMessage(List<IncomeEntry> entries) {
        if (entries.isEmpty()) {
            return "[]";
        }

        String message = "[";

        for (IncomeEntry entry : entries) {
            message += entry.constructJSONMessage() + ",";
        }

        message = message.substring(0, message.length() - 1);

        message += "]";

        return message;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getComments() {
        return comments;
    }

    public String getPerson() {
        return person;
    }

    public String getId() {
        return id;
    }

    public String getGroupID() {
        return groupID;
    }
}
