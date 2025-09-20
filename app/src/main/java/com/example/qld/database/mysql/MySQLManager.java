package com.example.qld.database.mysql;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.models.User;
import com.example.qld.network.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySQLManager {
    private static final String TAG = "MySQLManager";
    private Context context;
    private ApiClient apiClient;

    public MySQLManager(Context context) {
        this.context = context;
        this.apiClient = ApiClient.getInstance(context);
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
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePostRequest("login.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
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

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getUserByUsername(String username, UserCallback callback) {
        apiClient.makeGetRequest("users.php?username=" + username, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            JSONObject data = dataArray.getJSONObject(0);
                            User user = new User();
                            user.setId(data.getInt("id"));
                            user.setUsername(data.getString("username"));
                            user.setPassword(data.getString("password"));
                            user.setRole(data.getInt("role"));
                            user.setFullName(data.getString("full_name"));
                            user.setCreatedDate(data.getString("created_date"));
                            callback.onSuccess(user);
                        } else {
                            callback.onError("Không tìm thấy người dùng");
                        }
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void addUser(User user, UserCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", user.getUsername());
            jsonBody.put("password", user.getPassword());
            jsonBody.put("role", user.getRole());
            jsonBody.put("full_name", user.getFullName());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePostRequest("users.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về user vì chỉ cần biết thêm thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAllUsers(UsersCallback callback) {
        apiClient.makeGetRequest("users.php", new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
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

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getUserById(int userId, UserCallback callback) {
        apiClient.makeGetRequest("users.php?id=" + userId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
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

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateUser(User user, UserCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", user.getId());
            jsonBody.put("username", user.getUsername());
            jsonBody.put("password", user.getPassword());
            jsonBody.put("role", user.getRole());
            jsonBody.put("full_name", user.getFullName());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePutRequest("users.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về user vì chỉ cần biết cập nhật thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void deleteUser(int userId, UserCallback callback) {
        apiClient.makeDeleteRequest("users.php?id=" + userId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về gì vì chỉ cần biết xóa thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Các thao tác với học sinh
    public interface StudentCallback {
        void onSuccess(Student student);
        void onError(String error);
    }

    public interface StudentsCallback {
        void onSuccess(List<Student> students);
        void onError(String error);
    }

    public void addStudent(Student student, StudentCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", student.getUserId());
            jsonBody.put("student_code", student.getStudentCode());
            jsonBody.put("class_name", student.getClassName());
            jsonBody.put("birth_date", student.getBirthDate());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePostRequest("students.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về student vì chỉ cần biết thêm thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAllStudents(StudentsCallback callback) {
        apiClient.makeGetRequest("students.php", new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        List<Student> students = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject studentData = dataArray.getJSONObject(i);
                            Student student = new Student();
                            student.setId(studentData.getInt("id"));
                            student.setUserId(studentData.getInt("user_id"));
                            student.setStudentCode(studentData.getString("student_code"));
                            student.setClassName(studentData.getString("class_name"));
                            student.setBirthDate(studentData.getString("birth_date"));
                            students.add(student);
                        }

                        callback.onSuccess(students);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getStudentById(int studentId, StudentCallback callback) {
        apiClient.makeGetRequest("students.php?id=" + studentId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONObject data = response.getJSONObject("data");
                        Student student = new Student();
                        student.setId(data.getInt("id"));
                        student.setUserId(data.getInt("user_id"));
                        student.setStudentCode(data.getString("student_code"));
                        student.setClassName(data.getString("class_name"));
                        student.setBirthDate(data.getString("birth_date"));
                        callback.onSuccess(student);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getStudentByUserId(int userId, StudentCallback callback) {
        apiClient.makeGetRequest("students.php?user_id=" + userId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            JSONObject data = dataArray.getJSONObject(0);
                            Student student = new Student();
                            student.setId(data.getInt("id"));
                            student.setUserId(data.getInt("user_id"));
                            student.setStudentCode(data.getString("student_code"));
                            student.setClassName(data.getString("class_name"));
                            student.setBirthDate(data.getString("birth_date"));
                            callback.onSuccess(student);
                        } else {
                            callback.onError("Không tìm thấy học sinh");
                        }
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateStudent(Student student, StudentCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", student.getId());
            jsonBody.put("user_id", student.getUserId());
            jsonBody.put("student_code", student.getStudentCode());
            jsonBody.put("class_name", student.getClassName());
            jsonBody.put("birth_date", student.getBirthDate());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePutRequest("students.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về student vì chỉ cần biết cập nhật thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void deleteStudent(int studentId, StudentCallback callback) {
        apiClient.makeDeleteRequest("students.php?id=" + studentId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về gì vì chỉ cần biết xóa thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Các thao tác với môn học
    public interface SubjectCallback {
        void onSuccess(Subject subject);
        void onError(String error);
    }

    public interface SubjectsCallback {
        void onSuccess(List<Subject> subjects);
        void onError(String error);
    }

    public void addSubject(Subject subject, SubjectCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("subject_name", subject.getSubjectName());
            jsonBody.put("subject_code", subject.getSubjectCode());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePostRequest("subjects.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về subject vì chỉ cần biết thêm thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAllSubjects(SubjectsCallback callback) {
        apiClient.makeGetRequest("subjects.php", new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        List<Subject> subjects = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject subjectData = dataArray.getJSONObject(i);
                            Subject subject = new Subject();
                            subject.setId(subjectData.getInt("id"));
                            subject.setSubjectName(subjectData.getString("subject_name"));
                            subject.setSubjectCode(subjectData.getString("subject_code"));
                            subjects.add(subject);
                        }

                        callback.onSuccess(subjects);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getSubjectById(int subjectId, SubjectCallback callback) {
        apiClient.makeGetRequest("subjects.php?id=" + subjectId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONObject data = response.getJSONObject("data");
                        Subject subject = new Subject();
                        subject.setId(data.getInt("id"));
                        subject.setSubjectName(data.getString("subject_name"));
                        subject.setSubjectCode(data.getString("subject_code"));
                        callback.onSuccess(subject);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Các thao tác với điểm số
    public interface ScoreCallback {
        void onSuccess(Score score);
        void onError(String error);
    }

    public interface ScoresCallback {
        void onSuccess(List<Score> scores);
        void onError(String error);
    }

    public void addScore(Score score, ScoreCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("student_id", score.getStudentId());
            jsonBody.put("subject_id", score.getSubjectId());
            jsonBody.put("score_type", score.getScoreType());
            jsonBody.put("score", score.getScore());
            jsonBody.put("teacher_id", score.getTeacherId());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePostRequest("scores.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về score vì chỉ cần biết thêm thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAllScores(ScoresCallback callback) {
        apiClient.makeGetRequest("scores.php", new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        List<Score> scores = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject scoreData = dataArray.getJSONObject(i);
                            Score score = new Score();
                            score.setId(scoreData.getInt("id"));
                            score.setStudentId(scoreData.getInt("student_id"));
                            score.setSubjectId(scoreData.getInt("subject_id"));
                            score.setScoreType(scoreData.getString("score_type"));
                            score.setScore(scoreData.getDouble("score"));
                            score.setDateCreated(scoreData.getString("date_created"));
                            score.setTeacherId(scoreData.getInt("teacher_id"));
                            scores.add(score);
                        }

                        callback.onSuccess(scores);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getScoresByStudentId(int studentId, ScoresCallback callback) {
        apiClient.makeGetRequest("scores.php?student_id=" + studentId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        List<Score> scores = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject scoreData = dataArray.getJSONObject(i);
                            Score score = new Score();
                            score.setId(scoreData.getInt("id"));
                            score.setStudentId(scoreData.getInt("student_id"));
                            score.setSubjectId(scoreData.getInt("subject_id"));
                            score.setScoreType(scoreData.getString("score_type"));
                            score.setScore(scoreData.getDouble("score"));
                            score.setDateCreated(scoreData.getString("date_created"));
                            score.setTeacherId(scoreData.getInt("teacher_id"));
                            scores.add(score);
                        }

                        callback.onSuccess(scores);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getScoreById(int scoreId, ScoreCallback callback) {
        apiClient.makeGetRequest("scores.php?id=" + scoreId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONObject data = response.getJSONObject("data");
                        Score score = new Score();
                        score.setId(data.getInt("id"));
                        score.setStudentId(data.getInt("student_id"));
                        score.setSubjectId(data.getInt("subject_id"));
                        score.setScoreType(data.getString("score_type"));
                        score.setScore(data.getDouble("score"));
                        score.setDateCreated(data.getString("date_created"));
                        score.setTeacherId(data.getInt("teacher_id"));
                        callback.onSuccess(score);
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateScore(Score score, ScoreCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", score.getId());
            jsonBody.put("student_id", score.getStudentId());
            jsonBody.put("subject_id", score.getSubjectId());
            jsonBody.put("score_type", score.getScoreType());
            jsonBody.put("score", score.getScore());
            jsonBody.put("teacher_id", score.getTeacherId());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            callback.onError("Lỗi khi tạo yêu cầu");
            return;
        }

        apiClient.makePutRequest("scores.php", jsonBody, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về score vì chỉ cần biết cập nhật thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void deleteScore(int scoreId, ScoreCallback callback) {
        apiClient.makeDeleteRequest("scores.php?id=" + scoreId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        callback.onSuccess(null); // Không cần trả về gì vì chỉ cần biết xóa thành công
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Các phương thức tiện ích
    public interface AverageScoreCallback {
        void onSuccess(double average);
        void onError(String error);
    }

    public void calculateAverageScore(int studentId, AverageScoreCallback callback) {
        apiClient.makeGetRequest("scores.php?student_id=" + studentId, new ApiClient.ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        double total = 0.0;
                        int count = dataArray.length();

                        if (count > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject scoreData = dataArray.getJSONObject(i);
                                total += scoreData.getDouble("score");
                            }
                            double average = total / count;
                            callback.onSuccess(average);
                        } else {
                            callback.onSuccess(0.0);
                        }
                    } else {
                        String message = response.getString("message");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response", e);
                    callback.onError("Lỗi khi xử lý phản hồi");
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}