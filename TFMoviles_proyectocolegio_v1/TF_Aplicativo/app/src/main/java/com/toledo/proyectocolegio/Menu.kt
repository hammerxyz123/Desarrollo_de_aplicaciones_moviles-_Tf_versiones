package com.toledo.proyectocolegio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class Menu : AppCompatActivity() {

    private lateinit var gestionAlumnosButton:Button
    private lateinit var gestionPagosButton: Button
    private lateinit var gestionFestividadesButton: Button
    private lateinit var elaborarInformesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        asignaReferencia()
    }

    private fun asignaReferencia(){
        gestionAlumnosButton = findViewById(R.id.button_gestion_alumnos)
        gestionPagosButton = findViewById(R.id.button_gestion_pagos)
        gestionFestividadesButton = findViewById(R.id.button_gestion_festividades)
        elaborarInformesButton = findViewById(R.id.button_elaborar_informes)

        gestionAlumnosButton.setOnClickListener {
            val intent = Intent(this, GestionAlumno::class.java)
            startActivity(intent)
        }
        gestionPagosButton.setOnClickListener {
            val intent = Intent(this, GestionPago::class.java)
            startActivity(intent)
        }
    }
}