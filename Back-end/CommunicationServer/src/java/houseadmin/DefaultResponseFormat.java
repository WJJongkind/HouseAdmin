/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin;

import commstandards.JSON;
import commstandards.RequestKeywords;
import houseadmin.data.ExpenseEntry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.QueryReader;

/**
 *
 * @author Wessel
 */
public abstract class DefaultResponseFormat {
    public HttpServletRequest request;
    public String data;
    public HttpServletResponse response;
    public BaseServlet servlet;
    public PrintWriter out;
    public Map<String, String> params;
    
    public DefaultResponseFormat(BaseServlet servlet, HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.servlet = servlet;        
        
        try {
            BufferedReader reader = request.getReader();
            data = "";
            String l = null;
            while((l = reader.readLine()) != null) {
                data += l;
            }
        }catch(Exception e) {
          
        }
        if(request.getQueryString() != null) {
            this.params = QueryReader.readQuery(request.getQueryString());
        }
    }
    
    public void respond() throws IOException {
        PrintWriter out = null;
        
        try {
            if(servlet.verifySession(request)) {
                doSomething();
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }
    
    public abstract void doSomething() throws Exception;
}
