package com.jnarvaez.serviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Gestion_festividades : AppCompatActivity() {

    private lateinit var rvFestividad:RecyclerView
    private lateinit var btnNuevo:FloatingActionButton


    private var adaptador:AdaptadorPersonalizadoFestividades = AdaptadorPersonalizadoFestividades()
    private var listaFestividad:ArrayList<Festividad> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_festividades)
        asignarRefencias()
        cargarDatos()
    }

    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerFestividades()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaFestividad = rpta.body()!!.listaFestividad
                    adaptador.agregarDatos(listaFestividad)
                    mostrarDatos()
                }else{
                    Log.d("===", "Error en servicio")
                }
            }
        }
    }

    private fun mostrarDatos(){
        rvFestividad.adapter = adaptador
    }

    private fun asignarRefencias(){
        rvFestividad = findViewById(R.id.rvFestividad)
        btnNuevo = findViewById(R.id.btnNuevo)
        rvFestividad.layoutManager = LinearLayoutManager(this)
        btnNuevo.setOnClickListener {
            val intent = Intent(this,add_festividades::class.java)
            startActivity(intent)
        }

    }
}