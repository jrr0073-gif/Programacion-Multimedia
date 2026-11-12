package com.example.loginjesus;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String PREFS = "sesion";
    private static final String CLAVE_CORREO = "correo";

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

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        //al abrir: si hay correo guardado, escribirlo y activar el interruptor
        String correoGuardado = prefs.getString(CLAVE_CORREO, null);
        if (correoGuardado != null) {
            etCorreo.setText(correoGuardado);
            swRecordar.setChecked(true);
            etPass.requestFocus();
        }

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

            //datos correctos: guardar o borrar el correo y abrir el menú
            if (correo.equals(CORREO_VALIDO) && pass.equals(PASS_VALIDA)) {
                intentos = 0;

                if (swRecordar.isChecked()) {
                    prefs.edit().putString(CLAVE_CORREO, correo).apply();
                } else {
                    prefs.edit().remove(CLAVE_CORREO).apply();
                }

                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CORREO, correo);
                startActivity(intent);
                finish();
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