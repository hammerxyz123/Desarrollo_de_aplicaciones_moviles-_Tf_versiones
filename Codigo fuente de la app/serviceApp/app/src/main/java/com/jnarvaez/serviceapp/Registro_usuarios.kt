package com.jnarvaez.serviceapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Registro_usuarios : AppCompatActivity() {

    private lateinit var datosEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var direccionEditText: EditText
    private lateinit var celularEditText: EditText
    private lateinit var registrarButton: Button
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuarios)

        datosEditText = findViewById(R.id.datos)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        direccionEditText = findViewById(R.id.direccion)
        celularEditText = findViewById(R.id.celular)
        registrarButton = findViewById(R.id.btnregistrar)

        requestQueue = Volley.newRequestQueue(this)

        registrarButton.setOnClickListener {
            val datos = datosEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val direcion = direccionEditText.text.toString()
            val celular = celularEditText.text.toString()

            if (TextUtils.isEmpty(datos) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(direcion) || TextUtils.isEmpty(celular)
            ) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            try {
                val jsonBody = JSONObject().apply {
                    put("datos", datos)
                    put("email", email)
                    put("password", password)
                    put("direcion", direcion)
                    put("celular", celular)
                }

                Log.d("Registro_usuarios", "Request Body: $jsonBody")

                val url = "http://192.168.1.33:3000/register"

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, jsonBody,
                    { response ->
                        Log.d("Registro_usuarios", "Response: $response")
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login_Main::class.java)
                        startActivity(intent)
                        finish()  // Finaliza la actividad actual
                    },
                    { error ->
                        Log.e("Registro_usuarios", "Error: ${error.message}")
                        Toast.makeText(
                            this,
                            "Error en el registro: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                requestQueue.add(jsonObjectRequest)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}
