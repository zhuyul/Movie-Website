package edu.uci.ics.fabflixmobile;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KyleMa on 3/7/17.
 */

public class SearchRequest extends StringRequest {
    private static String url = "http://ec2-54-218-36-106.us-west-2.compute.amazonaws.com:8080/Fabflix/servlet/MobileSearch";
    private Map<String, String> params;

    public SearchRequest(String text, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("title", text);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
