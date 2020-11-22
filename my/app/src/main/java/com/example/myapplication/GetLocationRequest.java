package com.example.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetLocationRequest extends StringRequest {
    final static private String URL = "https://rlatmd0829.cafe24.com/GetLocation.php";
    private Map<String, String> parameters;

    public GetLocationRequest(Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
