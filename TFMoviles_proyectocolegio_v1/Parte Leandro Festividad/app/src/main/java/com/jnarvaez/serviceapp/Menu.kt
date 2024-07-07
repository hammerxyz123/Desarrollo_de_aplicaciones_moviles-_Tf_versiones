package com.jnarvaez.serviceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val gestionAlumnosButton: Button = findViewById(R.id.button_gestion_alumnos)
        val gestionPagosButton: Button = findViewById(R.id.button_gestion_pagos)
        val gestionFestividadesButton: Button = findViewById(R.id.button_gestion_festividades)
        val elaborarInformesButton: Button = findViewById(R.id.button_elaborar_informes)

        gestionAlumnosButton.setOnClickListener {
            Toast.makeText(this, "Gestión de Alumnos", Toast.LENGTH_SHORT).show()
            // Aquí puedes agregar la lógica para abrir la actividad correspondiente
        }

        gestionPagosButton.setOnClickListener {

        }

        gestionFestividadesButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        elaborarInformesButton.setOnClickListener {
            val intent = Intent(this, MainActivityInforme::class.java)
            startActivity(intent)
        }
    }
}