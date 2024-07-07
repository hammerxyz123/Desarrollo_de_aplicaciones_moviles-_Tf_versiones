package com.jnarvaez.serviceapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import android.widget.Toast
import com.jnarvaez.serviceapp.databinding.ActivityLoginMainBinding
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Login_Main : AppCompatActivity() {

    lateinit var binding : ActivityLoginMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //iniciando animacion

        var fade_in = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.fade_in)
        var bottom_down = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.bottom_down)
        //--------------------------------------------------------

        //establecer la animación de abajo hacia abajo en el diseño superior
        binding.topLinearLayout.animation=bottom_down

        //creemos un controlador para otros diseños
        var handler = Handler()
        var runnable = Runnable{

            //establezcamos desvanecimiento en la animación en otros diseños
            binding.cardView.animation = fade_in
            binding.cardView2.animation = fade_in
            binding.cardView3.animation = fade_in
            binding.cardView4.animation = fade_in
            binding.textView10.animation = fade_in
            binding.textView11.animation = fade_in
            binding.registerLayout.animation = fade_in
        }

        handler.postDelayed(runnable, 1000)

        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginbotton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val queue = Volley.newRequestQueue(this)
            val url = "http://10.0.2.2:3000/login"  // 10.0.2.2 es el localhost para el emulador de Android

            val stringRequest = object : StringRequest(Request.Method.POST, url, { response ->
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getString("status")

                if (status == "success") {
                    Toast.makeText(this, "Acceso autorizado", Toast.LENGTH_LONG).show()

                    val intent = Intent(this,Menu::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                }
            }, { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }

            queue.add(stringRequest)
        }

    }

}