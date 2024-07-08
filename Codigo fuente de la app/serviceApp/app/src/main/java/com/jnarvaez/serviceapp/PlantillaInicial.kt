package com.jnarvaez.serviceapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class PlantillaInicial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plantilla_inicial)

        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        val gifImageView1 = findViewById<ImageView>(R.id.gifImageView1)
        val gifImageView2 = findViewById<ImageView>(R.id.gifImageView2)

        // Cargar el primer GIF en gifImageView
        Glide.with(this)
            .load(R.drawable.g3gif)
            .into(gifImageView)

        // Cargar el segundo GIF en gifImageView1
        Glide.with(this)
            .load(R.drawable.patito)
            .into(gifImageView1)

        Glide.with(this)
            .load(R.drawable.carga)
            .into(gifImageView2)

        // Retraso antes de iniciar otra actividad
        Handler().postDelayed({
            val intent = Intent(this@PlantillaInicial, Login_Main::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}