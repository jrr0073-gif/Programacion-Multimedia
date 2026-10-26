package com.example.loginjesus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    private static final String CORREO_VALIDO = "jesus@correo.com";
    private static final String PASS_VALIDA = "1234";

    //contador de intentos fallidos
    private int intentos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etPass = findViewById(R.id.etPass);
        MaterialSwitch swRecordar = findViewById(R.id.swRecordar);
        Button btnContinuar = findViewById(R.id.btnContinuar);
        TextView tvMensaje = findViewById(R.id.tvMensaje);

        btnContinuar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String pass = etPass.getText().toString();

            //campos vacíos
            boolean hayVacios = false;
            if (correo.isEmpty()) {
                etCorreo.setError(getString(R.string.error_correo));
                hayVacios = true;
            }
            if (pass.isEmpty()) {
                etPass.setError(getString(R.string.error_pass));
                hayVacios = true;
            }
            if (hayVacios) {
                return;
            }

            //extra
            if (correo.equals(CORREO_VALIDO) && pass.equals(PASS_VALIDA)) {
                intentos = 0;
                if (swRecordar.isChecked()) {
                    tvMensaje.setText(R.string.ok_recordar);
                } else {
                    tvMensaje.setText(R.string.ok);
                }
                tvMensaje.setTextColor(getColor(R.color.verde_ok));
            } else {
                intentos++;
                tvMensaje.setTextColor(getColor(R.color.rojo_error));
                if (intentos >= 3) {
                    btnContinuar.setEnabled(false);
                    tvMensaje.setText(R.string.demasiados);
                    Toast.makeText(this, R.string.demasiados, Toast.LENGTH_SHORT).show();
                } else {
                    tvMensaje.setText(R.string.fallo);
                }
            }
        });
    }
}