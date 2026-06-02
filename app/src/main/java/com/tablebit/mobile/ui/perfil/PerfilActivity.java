package com.tablebit.mobile.ui.perfil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tablebit.mobile.R;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.viewmodel.PerfilViewModel;

public class PerfilActivity extends BaseActivity {

    private PerfilViewModel viewModel;
    private TextInputEditText etName, etTelefono;
    private MaterialButton btnGuardar;
    private View rootView;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        rootView = findViewById(android.R.id.content);
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        initViews();
        observeViewModel();
        viewModel.loadPerfil();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etTelefono = findViewById(R.id.etTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);

        setupToolbarWithBack(getString(R.string.titulo_perfil));

        findViewById(R.id.tvAvatar).setOnClickListener(v -> seleccionarImagen());

        btnGuardar.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            viewModel.updatePerfil(name, telefono);
        });

        CardView cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(v -> confirmLogout());
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar avatar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Snackbar.make(rootView, "Imagen seleccionada, subiendo...", Snackbar.LENGTH_SHORT).show();
            viewModel.uploadAvatar(imageUri);
        }
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            btnGuardar.setEnabled(!Boolean.TRUE.equals(loading));
            btnGuardar.setText(Boolean.TRUE.equals(loading) ? "Guardando..." : "Guardar cambios");
        });

        viewModel.getAvatarUploading().observe(this, uploading -> {
            if (Boolean.TRUE.equals(uploading)) {
                Snackbar.make(rootView, "Subiendo avatar...", Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.getSaveSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Snackbar.make(rootView, "Perfil actualizado", Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Snackbar.make(rootView, error, Snackbar.LENGTH_LONG).show();
        });

        viewModel.getUsuario().observe(this, user -> {
            if (user != null) {
                String inicial = user.getName() != null && user.getName().length() > 0
                        ? String.valueOf(user.getName().charAt(0)).toUpperCase() : "?";
                ((TextView) findViewById(R.id.tvAvatar)).setText(inicial);
                ((TextView) findViewById(R.id.tvName)).setText(user.getName());
                ((TextView) findViewById(R.id.tvEmail)).setText(user.getEmail());
                ((TextView) findViewById(R.id.tvRol)).setText(formatearRol(user.getRole()));
                etName.setText(user.getName());
                etTelefono.setText(user.getTelefono() != null ? user.getTelefono() : "");
            }
        });

        viewModel.getLogoutDone().observe(this, done -> {
            if (Boolean.TRUE.equals(done)) finish();
        });
    }

    private String formatearRol(String role) {
        if (role == null) return "";
        switch (role) {
            case "admin_restaurante": return "Admin. Restaurante";
            case "superadmin": return "Super Admin";
            default: return role.substring(0, 1).toUpperCase() + role.substring(1);
        }
    }

    private void confirmLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton("S\u00ed", (dialog, which) -> viewModel.logout())
                .setNegativeButton("No", null)
                .show();
    }
}
