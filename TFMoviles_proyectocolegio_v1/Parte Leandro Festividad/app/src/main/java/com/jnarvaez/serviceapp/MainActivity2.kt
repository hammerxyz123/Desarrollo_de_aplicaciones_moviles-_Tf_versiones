package com.jnarvaez.serviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    private lateinit var rvFestividad:RecyclerView
    private lateinit var btnNuevo:FloatingActionButton


    private var adaptador:AdaptadorPersonalizado = AdaptadorPersonalizado()
    private var listaFestividad:ArrayList<Festividad> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        asignarRefencias()
        cargarDatos();
    }

    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerFestividades()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaFestividad = rpta.body()!!.listaFestividad
                    adaptador.agregarDatos(listaFestividad)
                    mostrarDatos();
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
            val intent = Intent(this,FormActivity::class.java)
            startActivity(intent)
        }

    }
}