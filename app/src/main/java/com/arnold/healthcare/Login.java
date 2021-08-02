package com.arnold.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.canteen.Canteen;

public class Login extends AppCompatActivity {

    private EditText Username, Password,PasswordVisible;
    private Button Login,Reset;
    private ProgressBar progressBar;
    private ImageView hidden,visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.purple_500));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.login);

        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        PasswordVisible = findViewById(R.id.Visible);
        PasswordVisible.setEnabled(false);
        Login = findViewById(R.id.log_in_button);
        Reset = findViewById(R.id.reset_button);
        progressBar = findViewById(R.id.progress_circular);
        hidden = findViewById(R.id.hidden);
        visible = findViewById(R.id.visible);
        Username.setSelection(Username.getText().length());
        Username.requestFocus();
        Username.getShowSoftInputOnFocus();

        hidden.setOnClickListener(v -> {
            PasswordVisible.setVisibility(View.VISIBLE);
            hidden.setVisibility(View.GONE);
            visible.setVisibility(View.VISIBLE);
            Password.setSelection(Password.getText().length());
            Password.requestFocus();
        });

        visible.setOnClickListener(v -> {
            PasswordVisible.setVisibility(View.GONE);
            hidden.setVisibility(View.VISIBLE);
            visible.setVisibility(View.GONE);
            Password.setSelection(Password.getText().length());
            Password.requestFocus();
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username.setText("");
                Password.setText("");
                PasswordVisible.setText("");
            }
        });

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PasswordVisible.setText(Password.getText().toString());
            }
        });

        Login.setOnClickListener(v -> {
            if (TextUtils.isEmpty(Username.getText().toString()) || TextUtils.isEmpty(Password.getText().toString())) {
                showToast("Please enter value in both the fields");
            }else{
                String username = Username.getText().toString();
                String password = Password.getText().toString();
                if(username.equals("healthcare@admin.com") && password.equals("28072021Admin")){
                    final SharedPreferences fieldsVisibility1 = getSharedPreferences("login", MODE_PRIVATE);
                    final SharedPreferences.Editor fieldsVisibility2 = fieldsVisibility1.edit();
                    fieldsVisibility2.putString("username", username);
                    fieldsVisibility2.putString("password", password);
                    fieldsVisibility2.apply();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                }
                else{
                    showToast("Please enter valid login details");
                }
            }
        });
    }

    void showToast(String message) {
        Toast toast = new Toast(Login.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Login.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
}