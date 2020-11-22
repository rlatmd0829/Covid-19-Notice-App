package com.example.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LocationRequest extends StringRequest {
    final static private String URL = "https://rlatmd0829.cafe24.com/UserLocation.php";
    private Map<String, String> parameters;

    public LocationRequest(String lat, String lot, String userName, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("lat",lat);
        parameters.put("lot",lot);
        parameters.put("userName",userName);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
