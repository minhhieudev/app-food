// MainActivity.java
package com.example.appfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import android.widget.Toast;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private boolean isLoginMode = true; // true: Đăng nhập, false: Đăng ký
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Retrofit và ApiService
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Khởi tạo Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("YOUR_CLIENT_ID") // Thay YOUR_CLIENT_ID bằng client ID thực tế của bạn
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Khởi tạo các view
        Button loginButton = findViewById(R.id.loginButton);
        LinearLayout googleLoginButtonContainer = findViewById(R.id.googleLoginButtonContainer);
        TextView loginTitleTextView = findViewById(R.id.loginTitleTextView);
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        TextView registerLinkTextView = findViewById(R.id.registerLinkTextView);

        // Sự kiện khi nhấn vào "Tiếp tục với Google"
        googleLoginButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        // Sự kiện khi nhấn vào nút "Đăng nhập/Đăng ký"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên người dùng và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isLoginMode) {
                    login(username, password);
                } else {
                    register(username, password);
                }
            }
        });

        // Sự kiện khi nhấn vào tiêu đề để chuyển đổi giữa đăng nhập và đăng ký
        loginTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLoginMode();
            }
        });

        // Sự kiện khi nhấn vào "Đăng ký"
        registerLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLoginMode();
            }
        });
    }

    private void toggleLoginMode() {
        isLoginMode = !isLoginMode;
        TextView loginTitleTextView = findViewById(R.id.loginTitleTextView);
        TextView registerLinkTextView = findViewById(R.id.registerLinkTextView);
        Button loginButton = findViewById(R.id.loginButton);
        loginTitleTextView.setText(isLoginMode ? "Đăng nhập" : "Đăng ký");
        registerLinkTextView.setText(!isLoginMode ? "Đăng nhập" : "Đăng ký");
        loginButton.setText(isLoginMode ? "Đăng nhập" : "Đăng ký");
    }

    private void login(String username, String password) {
        // Gọi hàm xử lý đăng nhập
        Toast.makeText(this, "Đăng nhập với: " + username, Toast.LENGTH_SHORT).show();
        // Thực hiện đăng nhập tại đây
        // Không cần chuyển trang, chỉ cần hiển thị dữ liệu
    }

    private void register(String username, String password) {
        // Hiển thị thông báo đăng ký
        Toast.makeText(this, "Đăng ký với: " + username, Toast.LENGTH_SHORT).show();

        // Tạo đối tượng RegisterRequest
        RegisterRequest registerRequest = new RegisterRequest(username, password);

        // Gọi API đăng ký
        Call<RegisterResponse> call = apiService.register(registerRequest);

        // In ra URL thực sự được gọi
        String apiUrl = call.request().url().toString(); // Lấy URL từ request
        Log.d("API_CALL", "Đang gọi API: " + apiUrl); // In ra URL

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        Toast.makeText(MainActivity.this, "Đăng ký thành công: " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                        toggleLoginMode(); // Chuyển sang chế độ đăng nhập
                    } else {
                        Toast.makeText(MainActivity.this, "Phản hồi từ server không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý các mã lỗi HTTP khác
                    Toast.makeText(MainActivity.this, "Đăng ký thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Toast.makeText(MainActivity.this, "Gọi API thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Đăng nhập thành công
            Toast.makeText(this, "Đăng nhập Google thành công với: " + account.getEmail(), Toast.LENGTH_SHORT).show();
            // Xử lý tiếp theo như lưu thông tin người dùng, chuyển sang Activity khác, v.v.
        } catch (ApiException e) {
            // Xử lý lỗi đăng nhập
            e.printStackTrace();
            Toast.makeText(this, "Đăng nhập thất bại: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }
}
