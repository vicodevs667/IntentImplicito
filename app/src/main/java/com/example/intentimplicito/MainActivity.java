package com.example.intentimplicito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etTelefono;
    private ImageButton btnCall, btnCamera;

    String phoneNumber;
    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVistas();
        btnCall.setOnClickListener(view -> {
            recibirTelefono();
        });
    }

    private void recibirTelefono() {
        //uri como referencia de url
        phoneNumber = etTelefono.getText().toString();
        if(!phoneNumber.isEmpty()) {
            //accion, uri
            //Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
            //startActivity(intentCall);
            //Comprobar version actual de android que estamos corriendo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
            } else {
                olderVersions(phoneNumber);
            }
        } else {
            Toast.makeText(this, "Inserte un numero", Toast.LENGTH_LONG).show();
        }
    }

    private void olderVersions(String phoneNumber) {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
        if(checkPermission(Manifest.permission.CALL_PHONE)) {
            startActivity(intentCall);
        } else {
            Toast.makeText(this, "Declinaste el acceso", Toast.LENGTH_LONG).show();
        }
    }



    private void inicializarVistas() {
        etTelefono = findViewById(R.id.etTelefono);
        btnCall = findViewById(R.id.btnCall);
        btnCamera = findViewById(R.id.btnCamera);
    }

    private boolean checkPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //estamos en el caso del telefono
        switch (requestCode) {
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];
                if(permission.equals(Manifest.permission.CALL_PHONE)) {
                    //Comprobar si ha sido aceptado o denegado la peticion de permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        // Concedio su permiso
                        phoneNumber = etTelefono.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phoneNumber));
                        startActivity(intentCall);
                    } else {
                        //no concedio su permiso
                        Toast.makeText(this, "Declinaste el acceso", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}