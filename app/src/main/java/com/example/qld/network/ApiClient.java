package com.example.qld.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String BASE_URL = "http://10.0.2.2/api/"; // 10.0.2.2 là localhost của máy host trong emulator
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private Context context;

    private ApiClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public interface ApiResponseCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public void makeGetRequest(String endpoint, ApiResponseCallback callback) {
        String url = BASE_URL + endpoint;
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error making GET request to " + url, error);
                        callback.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    public void makePostRequest(String endpoint, JSONObject requestBody, ApiResponseCallback callback) {
        String url = BASE_URL + endpoint;
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error making POST request to " + url, error);
                        callback.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    public void makePutRequest(String endpoint, JSONObject requestBody, ApiResponseCallback callback) {
        String url = BASE_URL + endpoint;
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error making PUT request to " + url, error);
                        callback.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    public void makeDeleteRequest(String endpoint, ApiResponseCallback callback) {
        String url = BASE_URL + endpoint;
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error making DELETE request to " + url, error);
                        callback.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }
}