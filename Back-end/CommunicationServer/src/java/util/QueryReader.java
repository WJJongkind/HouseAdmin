/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Objects of this class can be used to decode queries into a map in which
 * the key is a variable name in the query and the value is a value of the given variable name.
 * @author Wessel Jongkind
 * @version 16-06-2017
 */
public class QueryReader
{   
    /**
     * Decodes a query into a map in which the key is a variable name in the query
     * and the value is the value of a given variable name.
     * @param queryString The query-string that has to be decoded.
     * @return The decoded query-string.
     */
    public static Map<String, String> readQuery(String queryString)
    {
        try{
            HashMap<String, String> converted = new HashMap<>();
            String[] params = queryString.split("&");
            
            for(String param : params) {
                int idx = param.indexOf("=");
                converted.put(URLDecoder.decode(param.substring(0, idx), "UTF-8"), URLDecoder.decode(param.substring(idx + 1), "UTF-8"));
            }
            
            return converted;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
