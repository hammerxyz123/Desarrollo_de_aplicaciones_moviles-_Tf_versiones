package com.toledo.proyectocolegio

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toledo.proyectocolegio.entidades.Alumno
import com.toledo.proyectocolegio.entidades.Calificacion
import com.toledo.proyectocolegio.entidades.Pago
import com.toledo.proyectocolegio.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GestionAlumno : AppCompatActivity() {

    private lateinit var rvAlumnos:RecyclerView
    private lateinit var btnAddAlumno:FloatingActionButton
    private var adaptador:AdaptadorPersonalizadoAlumnos=AdaptadorPersonalizadoAlumnos()
    private var listaAlumnos:ArrayList<Alumno> = ArrayList()
    private var listaCalficacion:ArrayList<Calificacion> = ArrayList()
    private var listaPago:ArrayList<Pago> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestionalumno)

        asignarReferencias()
        cargarDatos()
    }

    private fun asignarReferencias(){
        rvAlumnos=findViewById(R.id.rvalumnos)
        btnAddAlumno=findViewById(R.id.btnAddalumno)
        rvAlumnos.layoutManager= LinearLayoutManager(this)
        btnAddAlumno.setOnClickListener {
            val intent= Intent(this,AlumnoAdd::class.java)
            startActivity(intent)
        }
        adaptador.contexto(this)//editar

        adaptador.setEliminarItemAlumno {//eliminar
            eliminar(it.alu_id,it.alu_nombre)
        }
    }

    fun eliminar(id:Int,nombre:String){
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Confimación")
        ventana.setMessage("¿Desea eliminar el alumno: "+nombre+"?")
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            CoroutineScope(Dispatchers.IO).launch{
                val rpta2=RetrofitClient.webService.eliminarCalificacion(id)
                val rpta1=RetrofitClient.webService.eliminarPago(id)
                val rpta=RetrofitClient.webService.eliminarAlumno(id)
                runOnUiThread {
                    if(rpta2.isSuccessful&&rpta1.isSuccessful)
                        mostrarMensaje(rpta.body().toString())
                }
            }
        })
        ventana.setNegativeButton("Cancelar",null)
        ventana.create().show()
    }
    private  fun mostrarMensaje(mensaje:String){
        val ventana=AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent= Intent(this,GestionAlumno::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }

    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.obtenerAlumnos()
            val rpta1=RetrofitClient.webService.obtenerCalificaciones()
            val rpta2=RetrofitClient.webService.obtenerPagos()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaAlumnos=rpta.body()!!.listaAlumnos
                    listaCalficacion=rpta1.body()!!.listaCalificaciones
                    listaPago=rpta2.body()!!.listaPagos
                    adaptador.agregarAlumno(listaAlumnos,listaCalficacion,listaPago)
                    mostrarDatos()
                }else{
                    Log.d("===","Error en servicio")
                }
            }
        }
    }
    private fun mostrarDatos(){
        rvAlumnos.adapter=adaptador
    }
}