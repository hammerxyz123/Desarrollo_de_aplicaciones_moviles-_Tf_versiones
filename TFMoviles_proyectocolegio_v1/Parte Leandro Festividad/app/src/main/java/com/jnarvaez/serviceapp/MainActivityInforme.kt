package com.jnarvaez.serviceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivityInforme : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_informe)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvPayments = findViewById<TextView>(R.id.tvPayments)

        // Configurar el progreso inicial del ProgressBar
        val totalPagos = 1453
        val progresoActual = (totalPagos / 2000.0 * 100).toInt() // Asumiendo que 2000 es el total de pagos posibles
        progressBar.progress = progresoActual

        // Configurar los botones
        findViewById<Button>(R.id.btnAlumnosNotas).setOnClickListener {
            // Acción para Alumnos + Notas
        }

        findViewById<Button>(R.id.btnPagosAlumnos).setOnClickListener {
            // Acción para Pagos de Alumnos
        }

        findViewById<Button>(R.id.btnHistorialFestividades).setOnClickListener {
            // Acción para Historial festividades
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
           finish()
        }
    }
}