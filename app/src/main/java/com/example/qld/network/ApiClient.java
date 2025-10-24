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
     * 
     * Cách thức hoạt động:
     * 1. Lưu context của ứng dụng để sử dụng trong các thao tác mạng
     * 2. Tạo một RequestQueue mới bằng Volley để quản lý các yêu cầu mạng
     * 3. Đảm bảo rằng chỉ có một thể hiện của lớp này tồn tại trong suốt vòng đời ứng dụng
     * 
     * @param context Context của ứng dụng (Activity, Service, Application, v.v.)
     *                Được sử dụng để tạo RequestQueue và thực hiện các yêu cầu mạng
     */
    private ApiClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Lấy thể hiện duy nhất của ApiClient (Singleton pattern)
     * 
     * Cách thức hoạt động:
     * 1. Kiểm tra xem thể hiện đã được tạo chưa
     * 2. Nếu chưa, tạo mới một thể hiện ApiClient
     * 3. Trả về thể hiện duy nhất
     * 
     * @param context Context của ứng dụng (Activity, Service, Application, v.v.)
     *                Được sử dụng để tạo ApiClient nếu cần
     * @return Thể hiện duy nhất của ApiClient, đảm bảo chỉ có một thể hiện trong toàn bộ ứng dụng
     * 
     * Ví dụ: ApiClient apiClient = ApiClient.getInstance(context);
     */
    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    /**
     * Giao diện callback để xử lý kết quả từ các yêu cầu API
     * 
     * Cách thức hoạt động:
     * 1. Khi yêu cầu API thành công, phương thức onSuccess sẽ được gọi
     * 2. Khi có lỗi xảy ra, phương thức onError sẽ được gọi
     * 3. Cung cấp hai phương thức để xử lý cả hai trường hợp thành công và lỗi
     */
    public interface ApiResponseCallback {
        /**
         * Được gọi khi yêu cầu API thành công
         * 
         * Cách thức hoạt động:
         * 1. Nhận dữ liệu phản hồi từ máy chủ dưới dạng JSONObject
         * 2. Xử lý dữ liệu phản hồi theo logic nghiệp vụ của ứng dụng
         * 
         * @param response Đối tượng JSON phản hồi từ máy chủ (không được null)
         *                 Chứa dữ liệu trả về từ API, có thể là dữ liệu thành công hoặc thông báo
         * 
         * Ví dụ: Nếu API trả về {"status": "success", "data": {...}}, phương thức sẽ nhận được đối tượng JSON này
         */
        void onSuccess(JSONObject response);
        
        /**
         * Được gọi khi có lỗi xảy ra trong quá trình thực hiện yêu cầu API
         * 
         * Cách thức hoạt động:
         * 1. Nhận chuỗi mô tả lỗi từ hệ thống xử lý lỗi
         * 2. Hiển thị hoặc xử lý lỗi theo logic nghiệp vụ của ứng dụng
         * 
         * @param error Mô tả lỗi dưới dạng chuỗi (không được null)
         *              Có thể là lỗi kết nối, lỗi máy chủ, lỗi timeout, v.v.
         * 
         * Ví dụ: Nếu không có kết nối mạng, error có thể là "Kết nối hết thời gian. Vui lòng kiểm tra kết nối mạng."
         */
        void onError(String error);
    }

    /**
     * Thực hiện yêu cầu GET đến API
     * 
     * Cách thức hoạt động:
     * 1. Ghép nối BASE_URL với endpoint để tạo URL hoàn chỉnh
     * 2. Tạo một JsonObjectRequest kiểu GET với URL
     * 3. Thiết lập các callback xử lý phản hồi thành công và lỗi
     * 4. Thiết lập chính sách thử lại và timeout
     * 5. Thêm yêu cầu vào hàng đợi để thực hiện
     * 
     * @param endpoint Đường dẫn cụ thể của API (chuỗi không được null hoặc rỗng)
     *                 Ví dụ: "users/123", "students", "scores?subjectId=1"
     * @param callback Callback để xử lý kết quả (không được null)
     *                 Gồm hai phương thức onSuccess và onError để xử lý phản hồi
     * 
     * Ví dụ: makeGetRequest("users/123", callback) sẽ thực hiện GET request đến http://10.0.2.2/api/users/123
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
     * 
     * Cách thức hoạt động:
     * 1. Ghép nối BASE_URL với endpoint để tạo URL hoàn chỉnh
     * 2. Tạo một JsonObjectRequest kiểu POST với URL và dữ liệu body
     * 3. Thiết lập các callback xử lý phản hồi thành công và lỗi
     * 4. Thiết lập chính sách thử lại và timeout
     * 5. Thêm yêu cầu vào hàng đợi để thực hiện
     * 
     * @param endpoint Đường dẫn cụ thể của API (chuỗi không được null hoặc rỗng)
     *                 Ví dụ: "users", "students", "scores"
     * @param requestBody Dữ liệu JSON cần gửi trong body (có thể là null nếu không cần gửi dữ liệu)
     *                    Thường chứa thông tin để tạo mới hoặc cập nhật tài nguyên
     * @param callback Callback để xử lý kết quả (không được null)
     *                 Gồm hai phương thức onSuccess và onError để xử lý phản hồi
     * 
     * Ví dụ: makePostRequest("users", {"username": "john", "password": "123456"}, callback) 
     * sẽ thực hiện POST request đến http://10.0.2.2/api/users với dữ liệu body
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
     * 
     * Cách thức hoạt động:
     * 1. Ghép nối BASE_URL với endpoint để tạo URL hoàn chỉnh
     * 2. Tạo một JsonObjectRequest kiểu PUT với URL và dữ liệu body
     * 3. Thiết lập các callback xử lý phản hồi thành công và lỗi
     * 4. Thiết lập chính sách thử lại và timeout
     * 5. Thêm yêu cầu vào hàng đợi để thực hiện
     * 
     * @param endpoint Đường dẫn cụ thể của API (chuỗi không được null hoặc rỗng)
     *                 Thường bao gồm ID của tài nguyên cần cập nhật, ví dụ: "users/123", "students/456"
     * @param requestBody Dữ liệu JSON cần gửi trong body (có thể là null nếu không cần gửi dữ liệu)
     *                    Thường chứa thông tin cần cập nhật cho tài nguyên
     * @param callback Callback để xử lý kết quả (không được null)
     *                 Gồm hai phương thức onSuccess và onError để xử lý phản hồi
     * 
     * Ví dụ: makePutRequest("users/123", {"fullName": "John Doe"}, callback) 
     * sẽ thực hiện PUT request đến http://10.0.2.2/api/users/123 với dữ liệu body
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
     * 
     * Cách thức hoạt động:
     * 1. Ghép nối BASE_URL với endpoint để tạo URL hoàn chỉnh
     * 2. Tạo một JsonObjectRequest kiểu DELETE với URL
     * 3. Thiết lập các callback xử lý phản hồi thành công và lỗi
     * 4. Thiết lập chính sách thử lại và timeout
     * 5. Thêm yêu cầu vào hàng đợi để thực hiện
     * 
     * @param endpoint Đường dẫn cụ thể của API (chuỗi không được null hoặc rỗng)
     *                 Thường bao gồm ID của tài nguyên cần xóa, ví dụ: "users/123", "students/456"
     * @param callback Callback để xử lý kết quả (không được null)
     *                 Gồm hai phương thức onSuccess và onError để xử lý phản hồi
     * 
     * Ví dụ: makeDeleteRequest("users/123", callback) 
     * sẽ thực hiện DELETE request đến http://10.0.2.2/api/users/123
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
     * 
     * Cách thức hoạt động:
     * 1. Ghi log lỗi để debug (sử dụng Log.e)
     * 2. Phân tích loại lỗi và mã lỗi từ phản hồi mạng
     * 3. Tạo thông báo lỗi phù hợp dựa trên loại lỗi
     * 4. Gọi callback.onError với thông báo lỗi đã tạo
     * 
     * @param error Lỗi Volley xảy ra trong quá trình thực hiện yêu cầu (không được null)
     *              Có thể là TimeoutError, NetworkError, ServerError, v.v.
     * @param callback Callback để xử lý lỗi (không được null)
     *                 Sử dụng phương thức onError để truyền thông báo lỗi
     * 
     * Ví dụ: Nếu error là TimeoutError, phương thức sẽ gọi callback.onError với thông báo "Kết nối hết thời gian. Vui lòng kiểm tra kết nối mạng."
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