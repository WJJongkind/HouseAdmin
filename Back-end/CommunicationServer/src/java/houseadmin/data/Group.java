package houseadmin.data;

import common.DBAO;
import commstandards.JSON;
import houseadmin.data.tables.Expenses;
import houseadmin.data.tables.Groups;
import houseadmin.data.tables.Income;
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
public final class Group extends DBAO implements JSON {

    private String id;
    private String name;
    private String description;
    private String admin;
    private List<String> members;
    private List<String> newMembers;
    private List<String> deletedMembers;

    private static final String GET = "select * from Groups where " + Groups.ID + "=?";

    private static final String UPDATE = "update Groups set " + Groups.NAME + "=?,"
            + Groups.DESCRIPTION + "=?,"
            + Groups.ADMIN + "=? where " + Groups.ID + "=?";
    private static final String INSERT = "insert into Groups(" + Groups.ID + ", "
            + Groups.NAME + ", "
            + Groups.DESCRIPTION + ", "
            + Groups.ADMIN + ") values(?,?,?,?)";

    /**
     * SQL statement used to remove a user's details from the database. As a
     * parameter it requires the email of the user that should be removed.
     */
    private static final String DELETE_REFERENCES = "delete from Users_Groups where " + Groups.ID + "=?";

    private static final String DELETE = "delete from Groups where " + Groups.ID + "=?";

    private static final String GET_ALL_ID = "select " + Groups.ID + " from Groups";

    private static final String GET_USERS = "select * from Users_Groups where " + Groups.ID + "=?";

    private static final String ADD_MEMBER = "insert into Users_Groups values(?,?)";

    private static final String DELETE_MEMBER = "delete from Users_Groups where " + Users.ID + "=?";

    private static final String GET_EXPENSES = "select " + Expenses.ID + " from Expenses where Group_ID = ?";

    private static final String GET_INCOME = "select " + Income.ID + " from Income where Group_ID = ?";

    public Group() throws Exception {
        Connection conn = DBManager.getConnection(true);

        PreparedStatement get = null;
        ResultSet rs = null;
        try {
            get = conn.prepareStatement(GET_ALL_ID);
            rs = get.executeQuery();

            List<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getString(Groups.ID));
            }

            this.id = RandomIDGenerator.generateUniqueID(ids);
            this.members = new ArrayList<>();
            deletedMembers = new ArrayList<>();
            newMembers = new ArrayList<>();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }
    }

    public Group(String id) throws Exception {
        Connection conn = DBManager.getConnection(true);

        PreparedStatement get = null;
        ResultSet rs = null;

        members = new ArrayList<>();
        deletedMembers = new ArrayList<>();
        newMembers = new ArrayList<>();

        try {
            get = conn.prepareStatement(GET);
            get.setString(1, id);
            rs = get.executeQuery();

            if (!rs.next()) {
                throw new Exception("No group with given name found.");
            }

            initialiseFromResultSet(rs, conn);
        } catch (SQLException e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(get, rs);
        }
    }

    private void initialiseFromResultSet(ResultSet rs, Connection conn) throws Exception {
        PreparedStatement getUsers = null;
        ResultSet users = null;

        try {
            this.id = rs.getString(Groups.ID);
            this.name = rs.getString(Groups.NAME);
            this.description = rs.getString(Groups.DESCRIPTION);
            this.admin = rs.getString(Groups.ADMIN);

            getUsers = conn.prepareStatement(GET_USERS);
            getUsers.setString(1, id);
            users = getUsers.executeQuery();

            while (users.next()) {
                members.add(users.getString(Users.ID));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(getUsers, users);
        }
    }

    @Override
    public void update() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement update = null;

        try {
            update = conn.prepareStatement(UPDATE);

            update.setString(1, name);
            update.setString(2, description);
            update.setString(3, admin);
            update.setString(4, id);

            update.executeUpdate();
            conn.commit();

            updateMembers();
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

        PreparedStatement deleteReferences = null;
        PreparedStatement delete = null;

        PreparedStatement getExpenses = null;
        ResultSet expenses = null;

        PreparedStatement getIncome = null;
        ResultSet income = null;

        try {
            getExpenses = conn.prepareStatement(GET_EXPENSES);
            getExpenses.setString(1, id);
            expenses = getExpenses.executeQuery();

            while (expenses.next()) {
                new ExpenseEntry(expenses.getString(Expenses.ID)).delete();
            }

            getIncome = conn.prepareStatement(GET_INCOME);
            getIncome.setString(1, id);
            income = getIncome.executeQuery();

            while (income.next()) {
                new IncomeEntry(income.getString(Income.ID)).delete();
            }

            deleteReferences = conn.prepareStatement(DELETE_REFERENCES);
            deleteReferences.setString(1, id);
            deleteReferences.executeUpdate();

            delete = conn.prepareStatement(DELETE);
            delete.setString(1, id);
            delete.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(delete, deleteReferences, getExpenses, expenses, getIncome, income);
        }
    }

    @Override
    public void create() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement insert = null;

        try {
            insert = conn.prepareStatement(INSERT);

            insert.setString(1, id);
            insert.setString(2, name);
            insert.setString(3, description);
            insert.setString(4, admin);

            insert.executeUpdate();
            conn.commit();

            addMember(admin);
            updateMembers();
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(insert);
        }
    }

    private void updateMembers() throws Exception {
        deleteMembers();
        insertNewMembers();
    }

    private void deleteMembers() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement delete = null;

        try {
            delete = conn.prepareStatement(DELETE_MEMBER);

            for (String member : deletedMembers) {
                delete.setString(1, member);
                delete.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(delete);
        }
    }

    private void insertNewMembers() throws Exception {
        Connection conn = DBManager.getConnection(false);

        PreparedStatement addMember = null;

        try {
            addMember = conn.prepareStatement(ADD_MEMBER);

            for (String member : newMembers) {
                addMember.setString(1, id);
                addMember.setString(2, member);
                addMember.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            DBManager.releaseConnection(conn);
            closeQuietly(addMember);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAdmin() {
        return admin;
    }

    public void addMember(String email) {
        if (deletedMembers.contains(email)) {
            deletedMembers.remove(email);
            members.add(email);
        } else if (!newMembers.contains(email)) {
            newMembers.add(email);
        }
    }

    public void removeMember(String email) {
        if (members.contains(email)) {
            members.remove(email);
            deletedMembers.add(email);
        } else if (newMembers.contains(email)) {
            newMembers.remove(email);
        }
    }

    public List<String> getMembers() {
        return members;
    }

    @Override
    public String constructJSONMessage() {
        String memberString;

        if (members.isEmpty()) {
            memberString = "[]";
        } else {
            memberString = "[";

            for (String member : members) {
                memberString += "\"" + member + "\",";
            }

            memberString = memberString.substring(0, memberString.length() - 1) + "]";
        }
        return "{"
                + "\"id\": \"" + id + "\","
                + "\"name\": \"" + name + "\","
                + "\"description\": \"" + description + "\","
                + "\"admin\": \"" + admin + "\","
                + "\"members\": " + memberString
                + "}";
    }
}
