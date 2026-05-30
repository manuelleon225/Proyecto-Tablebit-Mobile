package com.tablebit.mobile.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tablebit.mobile.MainActivity;
import com.tablebit.mobile.R;
import com.tablebit.mobile.ui.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword;
    private MaterialButton btnRegister, btnLogin;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initViews();
        observeViewModel();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnGoToLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        btnLogin.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        authViewModel.getLoading().observe(this, loading -> {
            btnRegister.setEnabled(!loading);
            btnRegister.setText(loading ? R.string.cargando : R.string.btn_register);
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        authViewModel.getLoginResult().observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, R.string.register_exitoso, Toast.LENGTH_SHORT).show();
                goToHome();
            }
        });
    }

    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.campo_requerido, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, R.string.password_min, Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.register(name, email, password);
    }

    private void goToHome() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
