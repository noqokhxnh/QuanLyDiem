package com.example.qld.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Lớp tiện ích để thực hiện các yêu cầu mạng đến API
 * Sử dụng Volley để quản lý các yêu cầu mạng HTTP
 */
public class ApiClient {
    private static final String TAG = "ApiClient";
    // Sử dụng 10.0.2.2 để truy cập localhost của máy host từ Android Emulator
    // Nếu chạy trên thiết bị thật, hãy thay bằng IP của máy tính
    private static final String BASE_URL = "http://10.0.2.2/api/"; 
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private Context context;

    /**
     * Constructor riêng tư để đảm bảo chỉ có một thể hiện duy nhất (Singleton pattern)
     * @param context Context của ứng dụng
     */
    private ApiClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Lấy thể hiện duy nhất của ApiClient (Singleton pattern)
     * @param context Context của ứng dụng
     * @return Thể hiện duy nhất của ApiClient
     */
    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    /**
     * Giao diện callback để xử lý kết quả từ các yêu cầu API
     */
    public interface ApiResponseCallback {
        /**
         * Được gọi khi yêu cầu thành công
         * @param response Đối tượng JSON phản hồi từ máy chủ
         */
        void onSuccess(JSONObject response);
        
        /**
         * Được gọi khi có lỗi xảy ra trong quá trình thực hiện yêu cầu
         * @param error Mô tả lỗi
         */
        void onError(String error);
    }

    /**
     * Thực hiện yêu cầu GET đến API
     * @param endpoint Đường dẫn cụ thể của API
     * @param callback Callback để xử lý kết quả
     */
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
                        handleError(error, callback);
                    }
                }
        );

        // Thiết lập timeout và retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 giây timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    /**
     * Thực hiện yêu cầu POST đến API
     * @param endpoint Đường dẫn cụ thể của API
     * @param requestBody Dữ liệu JSON cần gửi trong body
     * @param callback Callback để xử lý kết quả
     */
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
                        handleError(error, callback);
                    }
                }
        );

        // Thiết lập timeout và retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 giây timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    /**
     * Thực hiện yêu cầu PUT đến API
     * @param endpoint Đường dẫn cụ thể của API
     * @param requestBody Dữ liệu JSON cần gửi trong body
     * @param callback Callback để xử lý kết quả
     */
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
                        handleError(error, callback);
                    }
                }
        );

        // Thiết lập timeout và retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 giây timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    /**
     * Thực hiện yêu cầu DELETE đến API
     * @param endpoint Đường dẫn cụ thể của API
     * @param callback Callback để xử lý kết quả
     */
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
                        handleError(error, callback);
                    }
                }
        );

        // Thiết lập timeout và retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 giây timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    /**
     * Xử lý các lỗi Volley một cách thống nhất
     * @param error Lỗi Volley xảy ra
     * @param callback Callback để xử lý lỗi
     */
    private void handleError(VolleyError error, ApiResponseCallback callback) {
        Log.e(TAG, "Network error: ", error);
        
        String errorMessage = "Lỗi kết nối không xác định";
        
        if (error instanceof TimeoutError) {
            errorMessage = "Kết nối hết thời gian. Vui lòng kiểm tra kết nối mạng.";
        } else if (error.networkResponse != null) {
            NetworkResponse response = error.networkResponse;
            switch (response.statusCode) {
                case 404:
                    errorMessage = "Không tìm thấy tài nguyên. Vui lòng kiểm tra URL.";
                    break;
                case 500:
                    errorMessage = "Lỗi máy chủ. Vui lòng thử lại sau.";
                    break;
                case 400:
                    errorMessage = "Yêu cầu không hợp lệ.";
                    break;
                default:
                    errorMessage = "Lỗi mạng (Mã: " + response.statusCode + "). Vui lòng kiểm tra kết nối.";
                    break;
            }
        } else if (error.getMessage() != null && error.getMessage().contains("UnknownHostException")) {
            errorMessage = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng và URL.";
        } else if (error.getMessage() != null) {
            errorMessage = "Lỗi mạng: " + error.getMessage();
        }
        
        callback.onError(errorMessage);
    }
}