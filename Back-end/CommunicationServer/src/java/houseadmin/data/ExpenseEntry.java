/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.data;

import common.DBAO;
import commstandards.JSON;
import houseadmin.data.tables.Expenses;
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
public class ExpenseEntry extends DBAO implements JSON {

    private Date date;
    private double amount;
    private String type;
    private String category;
    private String comments;
    private String person;
    private String id;
    private String groupID;

    private static final String GET = "select * from Expenses where " + Expenses.ID + " = ?";
    private static final String GET_ALL_BY_GROUP = "select * from Expenses where " + Expenses.GROUP + "=?";
    private static final String UPDATE = "update Expenses set " + Expenses.DATE + "=?, "
            + Expenses.CATEGORY + "=?, "
            + Expenses.TYPE + "=?, "
            + Expenses.AMOUNT + "=?, "
            + Expenses.PERSON + "=?, "
            + Expenses.COMMENTS + "=?, "
            + Expenses.GROUP + "=? "
            + "where " + Expenses.ID + "=?";
    private static final String INSERT = "insert into Expenses(" + Expenses.DATE + ", "
            + Expenses.CATEGORY + ", "
            + Expenses.TYPE + ", "
            + Expenses.AMOUNT + ", "
            + Expenses.PERSON + ", "
            + Expenses.COMMENTS + ", "
            + Expenses.ID + ", "
            + Expenses.GROUP + ") values(?,?,?,?,?,?,?,?)";
    private static final String DELETE = "delete from  Expenses where " + Expenses.ID + "=?";
    private static final String GET_ALL_ID = "select " + Expenses.ID + " from Expenses";

    public ExpenseEntry() {
        // Instantiate blanco object
    }

    public ExpenseEntry(JSONObject obj, User user) throws JSONException, Exception {
        this.amount = obj.getDouble("amount");
        this.category = obj.getString("category");
        this.type = obj.getString("type");
        this.date = new Date(obj.getString("date"));
        this.comments = obj.getString("comments");
        this.person = user.getEmail();
        this.groupID = obj.getString("groupID");

        try {
            this.id = obj.getString("id");
        } catch (Exception e) {
        }
    }

    public ExpenseEntry(JSONObject obj, User user, Group group) throws JSONException, Exception {
        this.amount = obj.getDouble("amount");
        this.category = obj.getString("category");
        this.type = obj.getString("type");
        this.date = new Date(obj.getString("date"));
        this.comments = obj.getString("comments");
        this.person = user.getEmail();
        this.groupID = group.getId();
        this.id = obj.getString("id");
    }

    public ExpenseEntry(String id) throws Exception {
        Connection conn = DBManager.getConnection(true);

        PreparedStatement get = null;
        ResultSet rs = null;
        try {
            get = conn.prepareStatement(GET);
            get.setString(1, id);
            rs = get.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Expense with ID " + id + " is not found!");
            }

            instantiateFromResultSet(rs);
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }
    }

    private ExpenseEntry(ResultSet rs) throws Exception {
        this.instantiateFromResultSet(rs);
    }

    private void instantiateFromResultSet(ResultSet rs) throws Exception {
        this.id = rs.getString(Expenses.ID);
        this.date = new Date(rs.getString(Expenses.DATE));
        this.amount = Double.parseDouble(rs.getString(Expenses.AMOUNT));
        this.type = rs.getString(Expenses.TYPE);
        this.category = rs.getString(Expenses.CATEGORY);
        this.comments = rs.getString(Expenses.COMMENTS);
        if (this.comments == null) {
            this.comments = "";
        }
        this.person = rs.getString(Expenses.PERSON);
        this.groupID = rs.getString(Expenses.GROUP);
    }

    public static List<ExpenseEntry> getAllEntriesByGroup(String groupID) throws Exception {
        Connection conn = DBManager.getConnection(true);

        List<ExpenseEntry> expenses = new ArrayList<>();

        PreparedStatement get = null;
        ResultSet rs = null;
        try {
            get = conn.prepareStatement(GET_ALL_BY_GROUP);
            get.setString(1, groupID);
            rs = get.executeQuery();

            while (rs.next()) {
                expenses.add(new ExpenseEntry(rs));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }

        return expenses;
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
            update.setString(3, type);
            update.setDouble(4, amount);
            update.setString(5, person);
            update.setString(6, comments);
            update.setString(7, groupID);
            update.setString(8, id);

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
                ids.add(allID.getString(Expenses.ID));
            }
            this.id = RandomIDGenerator.generateUniqueID(ids);

            insert.setString(1, date.toString());
            insert.setString(2, category);
            insert.setString(3, type);
            insert.setDouble(4, amount);
            insert.setString(5, person);
            insert.setString(6, comments);
            insert.setString(7, id);
            insert.setString(8, groupID);

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
                + "     \"type\": \"" + type + "\","
                + "     \"comments\": \"" + comments + "\","
                + "     \"id\": \"" + id + "\","
                + "     \"person\": \"" + person + "\","
                + "     \"groupID\": \"" + groupID + "\""
                + "}";
    }

    public static String constructJSONMessage(List<ExpenseEntry> entries) {
        if (entries.isEmpty()) {
            return "[]";
        }

        String message = "[";

        for (ExpenseEntry entry : entries) {
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

    public String getType() {
        return type;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

}
