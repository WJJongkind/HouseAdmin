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
import houseadmin.data.Group;
import houseadmin.data.IncomeEntry;
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
@WebServlet(name = "IncomeServlet", urlPatterns = {"/HouseAdmin/Income.json"})
public class IncomeServlet extends BaseServlet {
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
                        String jsonResult = IncomeEntry.constructJSONMessage(IncomeEntry.getAllEntriesByGroup(params.get(Groups.ID)));
                        out.println(jsonResult);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else if(id != null) {
                    IncomeEntry entry = new IncomeEntry(id);
                    
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
        handlePostOrPutEvent(request, response);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handlePostOrPutEvent(request, response);
    }
    
    private void handlePostOrPutEvent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                
                JSONObject obj = JSON.constructJSON(data);
                User user = servlet.getUser(request);
                IncomeEntry entry = new IncomeEntry(obj, user);
                
                Group group = new Group(entry.getGroupID());
                
                if(group.getMembers().contains(user.getEmail())) {
                    if(entry.getId() == null) {
                        entry.create();
                    } else {
                        entry.update();
                    }
                    out.print(entry.constructJSONMessage());
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(JSONStatusResponse.failure());
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
                IncomeEntry entry = new IncomeEntry(params.get(Expenses.ID));
                Group group = new Group(entry.getGroupID());
                
                if(group.getMembers().contains(user.getEmail())) {
                    entry.delete();
                    out.println("{}");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(JSONStatusResponse.failure());
                }
            }
        }.respond();
    }
}
