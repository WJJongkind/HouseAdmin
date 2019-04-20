/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import org.json.JSONObject;

/**
 *
 * @author Wessel
 */
public class JSONStatusResponse {
    public static String success() {
        return "{\"success\": true}";
    }
    
    public static String success(JSONObject data) {
        return "{\"success\": true, \"data\": " + data.toString() + "}";
    }
    
    public static String success(String key, String value) {
        return "{\"success\": true, \"data\":{\"" + key + "\": \"" + value + "\"}}";
    }
    
    public static String failure() {
        return "{\"success\": false}";
    }
    
    public static String failure(JSONObject data) {
        return "{\"success\": false, \"data\": " + data.toString() + "}";
    }
    
    public static String failure(String key, String value) {
        return "{\"success\": false, \"data\":{\"" + key + "\": \"" + value + "\"}}";
    }
}
