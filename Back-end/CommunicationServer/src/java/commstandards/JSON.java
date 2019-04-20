/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commstandards;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wessel
 */
public interface JSON {

    public String constructJSONMessage();

    public static JSONObject extractJSON(HttpServletRequest request) throws IOException, JSONException {
        BufferedReader reader = request.getReader();

        String msg = "";
        String l = null;
        while ((l = reader.readLine()) != null) {
            msg += l;
        }

        return new JSONObject(msg);
    }

    public static JSONObject constructJSON(String json) throws JSONException {
        return new JSONObject(json);
    }
}
