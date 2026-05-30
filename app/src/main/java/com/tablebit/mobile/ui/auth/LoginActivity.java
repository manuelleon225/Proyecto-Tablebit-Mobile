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
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnRegister;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            goToHome();
            return;
        }

        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initViews();
        observeViewModel();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    private void observeViewModel() {
        authViewModel.getLoading().observe(this, loading -> {
            btnLogin.setEnabled(!loading);
            btnLogin.setText(loading ? R.string.cargando : R.string.btn_login);
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        authViewModel.getLoginResult().observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, R.string.login_exitoso, Toast.LENGTH_SHORT).show();
                goToHome();
            }
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.campo_requerido, Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.login(email, password);
    }

    private void goToHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
