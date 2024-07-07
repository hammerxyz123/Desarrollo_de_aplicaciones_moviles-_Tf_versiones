package com.jnarvaez.serviceapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Menu : AppCompatActivity() {

    private lateinit var gestionAlumnosButton:CardView
    private lateinit var gestionPagoButton:CardView
    private lateinit var gestionFestividadesButton:CardView
    private lateinit var gestionInformesButton:CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        asignaReferencia()
    }

    private fun asignaReferencia(){
        gestionAlumnosButton = findViewById(R.id.button_gestion_alumnos)
        gestionPagoButton = findViewById(R.id.button_gestion_pagos)
        gestionFestividadesButton = findViewById(R.id.button_gestion_festividad)
        gestionInformesButton = findViewById(R.id.button_gestion_informes)

        gestionAlumnosButton.setOnClickListener {
            val intent = Intent(this, GestionAlumno::class.java)
            startActivity(intent)
        }
        gestionPagoButton.setOnClickListener{
            val intent = Intent(this, GestionPago::class.java)
            startActivity(intent)
        }
        gestionFestividadesButton.setOnClickListener{
            val intent = Intent(this, Gestion_festividades::class.java)
            startActivity(intent)
        }
        gestionInformesButton.setOnClickListener{
            val intent = Intent(this, MainActivityInforme::class.java)
            startActivity(intent)
        }
    }
}