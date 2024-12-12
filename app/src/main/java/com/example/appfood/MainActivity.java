// MainActivity.java
package com.example.appfood;

import android.content.Intent;
import android.content.SharedPreferences;
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
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Khởi tạo Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("458973661783-mtt3pldt4g152ng9cb2d07ge1srprrv8.apps.googleusercontent.com") // Thay YOUR_CLIENT_ID bằng client ID thực tế của bạn
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
        // Tạo đối tượng LoginRequest
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Gọi API đăng nhập
        Call<LoginResponse> call = apiService.login(loginRequest);

        // In ra URL thực sự được gọi
        String apiUrl = call.request().url().toString(); // Lấy URL từ request
        Log.d("API_CALL", "Đang gọi API: " + apiUrl); // In ra URL

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getData() != null) {
                        // Đăng nhập thành công
                        String userEmail = loginResponse.getData().getCustomer().getEmail();
                        String id = loginResponse.getData().getCustomer().getId();

                        String accessToken = loginResponse.getData().getAccessToken();
                        String refreshToken = loginResponse.getData().getRefreshToken();

                        String currency = loginResponse.getData().getCustomer().getCurrency();


                        // Lưu token vào SharedPreferences hoặc xử lý thêm
                        saveUserData(userEmail, accessToken, refreshToken,id, currency);


                        // Chuyển sang trang Home
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.putExtra("username", userEmail); // Truyền email qua Home
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý lỗi khi đăng nhập thất bại
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Toast.makeText(MainActivity.this, "Gọi API thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(String email, String accessToken, String refreshToken,String id,String currency) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("id", id);
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.putString("currency", currency);

        Log.d("OrderInfo", "Afternoon Delivery Time: " + refreshToken);
        editor.apply();
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
        Log.d("signInWithGoogle", "signInWithGooglesignInWithGoogle: " ); // In ra URL

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleSignIn", "Đăng nhập thành công: " + account.getEmail());
            } catch (ApiException e) {
                // Log chi tiết lỗi
                Log.e("GoogleSignIn", "Lỗi đăng nhập Google:");
                Log.e("GoogleSignIn", "Status Code: " + e.getStatusCode());
                Log.e("GoogleSignIn", "Message: " + e.getMessage());
                Log.e("GoogleSignIn", "Localized Message: " + e.getLocalizedMessage());

                // Hiển thị lỗi chi tiết cho người dùng nếu cần
                Toast.makeText(this, "Lỗi Google Sign-In: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }



    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            Log.e("KKKKKK", "LLLLLLLLLLLLLLLLLLL: ");
           // completedTask.
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("HHHHHHHHHH", "HHHHHHHHHH: ");
            if (account != null) {
                String googleToken = account.getIdToken();
                Log.d("GoogleSignIn", "Google Token: " + googleToken);
                // Gửi Google Token tới server
                sendGoogleTokenToServer(googleToken);
                Log.e("AAAAAAAAAAA", "AAAAAAAAAAA: ");

            }
        } catch (ApiException e) {
            Log.e("GoogleSignIn", "Lỗi đăng nhập: " + e.getMessage() + ", mã lỗi: " + e.getStatusCode());
        }

    }

    private void sendGoogleTokenToServer(String googleToken) {
        if (googleToken == null || googleToken.isEmpty()) {
            Log.e("GoogleSignIn", "Token Google trống");
            Toast.makeText(this, "Token Google không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request gửi lên server
        GoogleLoginRequest request = new GoogleLoginRequest();
        request.setToken(googleToken);

        // Gọi API giống logic từ bản web
        Call<LoginResponse> call = apiService.googleLogin(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String userEmail = loginResponse.getData().getCustomer().getEmail();

                    // Lưu thông tin vào SharedPreferences hoặc xử lý theo ý định
                    saveUserData(userEmail, loginResponse.getData().getAccessToken(),
                            loginResponse.getData().getRefreshToken(),
                            loginResponse.getData().getCustomer().getId(),
                            loginResponse.getData().getCustomer().getCurrency());

                    // Chuyển sang trang Home
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.putExtra("username", userEmail);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Đăng nhập Google thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("GoogleLogin", "Đăng nhập Google thất bại: " + response.message());
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("GoogleLogin", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
