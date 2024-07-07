package com.jnarvaez.serviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.entidad.Pago
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GestionPago : AppCompatActivity() {

    private lateinit var rvPago: RecyclerView
    private var adaptador: AdaptadorPersonalizadoPago = AdaptadorPersonalizadoPago()
    private var listaAlumnos:ArrayList<Alumno> = ArrayList()
    private var listaPagos:ArrayList<Pago> = ArrayList()
    private lateinit var buttonRegresar: Button
    private var listaCalificacion:ArrayList<Calificacion> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_pago)

        asignarReferencias()
        cargarDatos()
    }

    private fun asignarReferencias(){
        rvPago=findViewById(R.id.rvpago)
        buttonRegresar = findViewById(R.id.buttonRegresar)
        rvPago.layoutManager= LinearLayoutManager(this)
        adaptador.contexto(this)//editar

        buttonRegresar.setOnClickListener {
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)
        }
    }
    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta= RetrofitClient.webService.obtenerAlumnos()
            val rpta1= RetrofitClient.webService.obtenerPagos()
            val rpta2= RetrofitClient.webService.obtenerCalificaciones()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaAlumnos=rpta.body()!!.listaAlumnos
                    listaPagos=rpta1.body()!!.listaPagos
                    listaCalificacion=rpta2.body()!!.listaCalificaciones
                    adaptador.agregarAlumno(listaAlumnos,listaPagos,listaCalificacion)
                    mostrarDatos()
                }else{
                    Log.d("===","Error en servicio")
                }
            }
        }
    }
    private fun mostrarDatos(){
        rvPago.adapter=adaptador
    }
}