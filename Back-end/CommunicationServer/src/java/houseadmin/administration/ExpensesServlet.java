/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.administration;

import common.JSONStatusResponse;
import commstandards.JSON;
import commstandards.RequestKeywords;
import houseadmin.BaseServlet;
import houseadmin.DefaultResponseFormat;
import houseadmin.data.ExpenseEntry;
import houseadmin.data.Group;
import houseadmin.data.User;
import houseadmin.data.tables.Expenses;
import houseadmin.data.tables.Groups;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Wessel
 */
@WebServlet(name = "ExpensesServlet", urlPatterns = {"/HouseAdmin/Expenses.json"})
public class ExpensesServlet extends BaseServlet {
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                User user = servlet.getUser(request);
                String id = params.get(Groups.ID);
                
                if(RequestKeywords.OPTION_ALL.equals(params.get(RequestKeywords.OPTIONS)) && id != null) {
                    if(user.getGroups().contains(id)) {
                        String jsonResult = ExpenseEntry.constructJSONMessage(ExpenseEntry.getAllEntriesByGroup(params.get(Groups.ID)));
                        out.println(jsonResult);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else if(id != null) {
                    ExpenseEntry entry = new ExpenseEntry(id);
                    
                    if(user.getGroups().contains(entry.getGroupID())) {
                        out.write(entry.constructJSONMessage());
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                }
            }
        }.respond();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handlePutOrPostEvent(request, response);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handlePutOrPostEvent(request, response);
    }
    
    private void handlePutOrPostEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                System.out.println(data);
                JSONObject obj = JSON.constructJSON(data);
                User user = servlet.getUser(request);
                ExpenseEntry entry = new ExpenseEntry(obj, user);
                
                Group group = new Group(entry.getGroupID());
                
                if(group.getMembers().contains(user.getEmail())) {
                    if(entry.getId() == null) {
                        entry.create();
                    } else {
                        entry.update();
                    }
                    out.print(entry.constructJSONMessage());
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }.respond();
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                
                User user = servlet.getUser(request);
                ExpenseEntry entry = new ExpenseEntry(params.get(Expenses.ID));
                Group group = new Group(entry.getGroupID());
                
                if(group.getMembers().contains(user.getEmail())) {
                    entry.delete();
                    out.println("{}");
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(JSONStatusResponse.failure());
                }
                
            }
        }.respond();
    }
}
