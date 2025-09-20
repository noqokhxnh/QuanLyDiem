package com.example.qld.database.mysql;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySQLManager {
    private static final String TAG = "MySQLManager";
    private static final String BASE_URL = "http://10.0.2.2/api/"; // 10.0.2.2 là localhost của máy host trong emulator
    private Context context;
    private RequestQueue requestQueue;

    public MySQLManager(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Các thao tác với người dùng
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public interface UsersCallback {
        void onSuccess(List<User> users);
        void onError(String error);
    }

    public void authenticateUser(String username, String password, UserCallback callback) {
        String url = BASE_URL + "login.php";
        
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject data = response.getJSONObject("data");
                                User user = new User();
                                user.setId(data.getInt("id"));
                                user.setUsername(data.getString("username"));
                                user.setPassword(data.getString("password"));
                                user.setRole(data.getInt("role"));
                                user.setFullName(data.getString("full_name"));
                                user.setCreatedDate(data.getString("created_date"));
                                callback.onSuccess(user);
                            } else {
                                String message = response.getString("message");
                                callback.onError(message);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                            callback.onError("Lỗi khi xử lý phản hồi");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error authenticating user", error);
                        callback.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    public void getAllUsers(UsersCallback callback) {
        String url = BASE_URL + "users.php";
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray dataArray = response.getJSONArray("data");
                                List<User> users = new ArrayList<>();
                                
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject userData = dataArray.getJSONObject(i);
                                    User user = new User();
                                    user.setId(userData.getInt("id"));
                                    user.setUsername(userData.getString("username"));
                                    user.setPassword(userData.getString("password"));
                                    user.setRole(userData.getInt("role"));
                                    user.setFullName(userData.getString("full_name"));
                                    user.setCreatedDate(userData.getString("created_date"));
                                    users.add(user);
                                }
                                
                                callback.onSuccess(users);
                            } else {
                                String message = response.getString("message");
                                callback.onError(message);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                            callback.onError("Lỗi khi xử lý phản hồi");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error getting users", error);
                        callback.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    // Các phương thức khác cho Student, Subject, Score sẽ được implement tương tự
    // Ví dụ cho một số phương thức cơ bản:

    public interface StudentCallback {
        void onSuccess(Student student);
        void onError(String error);
    }

    public interface StudentsCallback {
        void onSuccess(List<Student> students);
        void onError(String error);
    }

    public void getAllStudents(StudentsCallback callback) {
        String url = BASE_URL + "students.php";
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Implement tương tự như getAllUsers
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Implement tương tự như getAllUsers
                    }
                }
        );

        requestQueue.add(request);
    }

    public interface SubjectCallback {
        void onSuccess(Subject subject);
        void onError(String error);
    }

    public interface SubjectsCallback {
        void onSuccess(List<Subject> subjects);
        void onError(String error);
    }

    public void getAllSubjects(SubjectsCallback callback) {
        String url = BASE_URL + "subjects.php";
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Implement tương tự như getAllUsers
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Implement tương tự như getAllUsers
                    }
                }
        );

        requestQueue.add(request);
    }

    public interface ScoreCallback {
        void onSuccess(Score score);
        void onError(String error);
    }

    public interface ScoresCallback {
        void onSuccess(List<Score> scores);
        void onError(String error);
    }

    public void getAllScores(ScoresCallback callback) {
        String url = BASE_URL + "scores.php";
        
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Implement tương tự như getAllUsers
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Implement tương tự như getAllUsers
                    }
                }
        );

        requestQueue.add(request);
    }
}