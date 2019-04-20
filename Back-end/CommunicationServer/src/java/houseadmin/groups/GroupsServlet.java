/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.groups;

import common.JSONStatusResponse;
import commstandards.JSON;
import commstandards.RequestKeywords;
import houseadmin.BaseServlet;
import houseadmin.DefaultResponseFormat;
import houseadmin.data.Group;
import houseadmin.data.User;
import houseadmin.data.tables.Groups;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import util.QueryReader;

/**
 *
 * @author Wessel
 */
@WebServlet(name = "groups", urlPatterns = {"/HouseAdmin/groups.json"})
public class GroupsServlet extends BaseServlet {
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
                
                String response;
                User user = servlet.getUser(request);
                if(RequestKeywords.OPTION_ALL.equals(params.get(RequestKeywords.OPTIONS))) {
                    if(user.getGroups().size() > 0) {
                        response = "[";
                        for(String group : user.getGroups()) {
                            response += new Group(group).constructJSONMessage() + ",";
                        }
                        response = response.substring(0, response.length() - 1) + "]";
                    } else {
                        response = "[]";
                    }
                } else {
                    System.out.println(params.get(Groups.ID));
                    if(user.getGroups().contains(params.get(Groups.ID))) {
                        response = new Group(params.get(Groups.ID)).constructJSONMessage();
                    } else {
                        this.response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
                out.println(response);
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
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                
                JSONObject obj = JSON.constructJSON(data);
                User user = servlet.getUser(request);
                
                Group group = new Group();
                group.setName(obj.getString(Groups.NAME));
                group.setDescription(obj.getString(Groups.DESCRIPTION));
                group.setAdmin(user.getEmail());
                group.create();
                
                user.getGroups().add(group.getId());
                user.update();
                
                out.println(JSONStatusResponse.success("ID", group.getId()));
            }
        }.respond();
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                
                User user = servlet.getUser(request);
                String groupID = QueryReader.readQuery(
                                    request.getQueryString())
                                    .get(Groups.ID);
                Group group = new Group(groupID);
                if(group.getAdmin().equals(user.getEmail())) {
                    new Group(groupID).delete();
                    user.getGroups().remove(groupID);
                    out.println(JSONStatusResponse.success());
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    out.println(JSONStatusResponse.failure());
                }
                
            }
        }.respond();
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                
                JSONObject obj = JSON.constructJSON(data);
                User user = servlet.getUser(request);
                
                Group group = new Group(obj.getString(Groups.ID));
                
                if(user.getEmail().equals(group.getAdmin())) {
                    group.setName(obj.getString(Groups.NAME));
                    group.setDescription(obj.getString(Groups.DESCRIPTION));
                    group.setAdmin(obj.getString(Groups.ADMIN));
                    group.update();
                    out.println(JSONStatusResponse.success());
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    out.println(JSONStatusResponse.failure());
                }
                
            }
        }.respond();
    }
}
