package com.tablebit.mobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.admin.AdminDashboardActivity;
import com.tablebit.mobile.ui.auth.LoginActivity;
import com.tablebit.mobile.ui.cliente.HomeClienteActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            sessionManager.forceLogout(this);
            return;
        }

        Intent intent;
        if (sessionManager.isAdmin()) {
            intent = new Intent(this, AdminDashboardActivity.class);
        } else {
            intent = new Intent(this, HomeClienteActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
