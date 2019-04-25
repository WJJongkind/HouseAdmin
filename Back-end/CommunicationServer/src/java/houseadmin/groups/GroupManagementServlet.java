/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.groups;

import common.JSONStatusResponse;
import commstandards.JSON;
import houseadmin.BaseServlet;
import houseadmin.DefaultResponseFormat;
import houseadmin.data.Group;
import houseadmin.data.User;
import houseadmin.data.tables.Groups;
import houseadmin.data.tables.Users;
import java.io.IOException;
import java.util.Map;
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
@WebServlet(name = "groupmanagement", urlPatterns = {"/HouseAdmin/groupmanagement.json"})
public class GroupManagementServlet extends BaseServlet {
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
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
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
                
                Group group = new Group(obj.getString(Groups.ID));
                String newMember = obj.getString(Users.EMAIL);
                
                if(group.getMembers().contains(newMember)) {
                    response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                    out.println(JSONStatusResponse.failure());
                } else if(user.getEmail().equals(group.getAdmin())) {
                    group.addMember(newMember);
                    group.update();
                    //TODO finish me.
                    if(!User.exists(newMember)) {
                        sendInvite(newMember);
                    }
                }
                
                out.println("{}");
            }
        }.respond();
    }
    
    private void sendInvite(String email) {
        
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new DefaultResponseFormat(this, request, response) {
            @Override
            public void doSomething() throws Exception {
                out = response.getWriter();
                
                User user = servlet.getUser(request);
                Map<String, String> queryParams = QueryReader.readQuery(request.getQueryString());
                String groupID = queryParams.get(Groups.ID);
                
                Group group = new Group(groupID);
                if(group.getAdmin().equals(user.getEmail())) {
                    group.removeMember(queryParams.get(Users.EMAIL));
                    group.update();
                    user.getGroups().remove(groupID);
                    user.update();
                    out.println("{}");
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    out.println(JSONStatusResponse.failure());
                }
                
            }
        }.respond();
    }
}
